package com.main;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.window.*;
import com.sound.Sound;
import com.sound.ChatingTime;

public class SocketClient implements Runnable {

	public int port;
	public String serverAddr;
	public Socket socket;
	public ClientWindow ui;
	public ObjectInputStream In;
	public ObjectOutputStream Out;
	public Sound aleart=new Sound();
	public ChatingTime c=new ChatingTime();
	String currentDate="< "+c.date()+" >";
	String currentTime=c.time();

	public SocketClient(ClientWindow frame) throws IOException {
		ui = frame;
		this.serverAddr = ui.serverAddr;
		this.port = ui.port;
		socket = new Socket(InetAddress.getByName(serverAddr), port);

		Out = new ObjectOutputStream(socket.getOutputStream());
		Out.flush();
		In = new ObjectInputStream(socket.getInputStream());
	}
	@Override
	public void run() {
		boolean keepRunning = true;
		while (keepRunning) {
			try {
				ConnectionMessage msg = (ConnectionMessage) In.readObject();
				System.out.println("Incoming : " + msg.toString());

				if (msg.type.equals("message")) {
					if (msg.recipient.equals(ui.username)) {
						ui.textArea.append("[" + msg.sender + "  >>>>   Me] : "
								+ msg.content + "\n");
						aleart=new Sound();
						aleart.start();
						System.out.println("Sound is Running");
            		}else if(msg.recipient.equals("All")){
            			ui.textArea.append("[" + msg.sender + " >>>>  "
								+ msg.recipient + "] : " + msg.content + "\n");
						aleart=new Sound();
						aleart.start();
						System.out.println("Sound is Running");
            			
            		}
					else {
						ui.textArea.append("[" + msg.sender + " >==>  "
								+ msg.recipient + "] : " + msg.content + "\n");						
					}
				} else if (msg.type.equals("login")) {
					if (msg.content.equals("TRUE")) {
						ui.textArea.append(currentDate+"\n");
						ui.textArea
								.append("");
						ui.textArea
						.append(""+msg.sender);
					} else {
						ui.textArea.append("[Server > Me] : Login Failed !!!\n");
					}
				} else if (msg.type.equals("test")) {
				} else if(msg.type.equals("upload_res")){
					File f = new File(ClientWindow.lastSnpName);
					Upload u = new Upload(msg.content, Integer.parseInt(msg.sender), f);
					new Thread(u).start();					
				}else if (msg.type.equals("newuser")) {
					if (!msg.content.equals(ui.username)) {
						boolean exists = false;
						for (int i = 0; i < ui.model.getSize(); i++) {
							if (ui.model.getElementAt(i).equals(msg.content)) {
								exists = true;
								break;
							}
						}
						if (!exists) {
							ui.model.addElement(msg.content);
						}
					}
				} else if (msg.type.equals("signup")) {
					if (msg.content.equals("TRUE")) {
						ui.textArea
								.append("[SERVER >>> Me] : Singup Successful\n");
					} else {
						ui.textArea.append("[SERVER >>> Me] : Signup Failed\n");
					}
				} else if (msg.type.equals("signout")) {
					if (msg.content.equals(ui.username)) {
						ui.textArea.append("[" + msg.sender
								+ " >>>  Me] : \n");
						for (int i = 1; i < ui.model.size(); i++) {
							ui.model.removeElementAt(i);
						}

						ui.clientThread.stop();
					} else {
						ui.model.removeElement(msg.content);
						ui.textArea.append("[" + msg.sender + " > All] : "
								+ msg.content + " Has Signed Out\n");
					}
				} else {
					ui.textArea
							.append("[SERVER > Me] : Unknown message type\n");
				}
			} catch (Exception ex) {
				keepRunning = false;
				ui.textArea
						.append("[Application > Me] : Sorry Connection Failed\n");
				for (int i = 1; i < ui.model.size(); i++) {
					ui.model.removeElementAt(i);
				}

				ui.clientThread.stop();

				System.out.println("Exception SocketClient run()");
				ex.printStackTrace();
			}
		}
	}
	
	public void send(ConnectionMessage msg) {
		try {
			Out.writeObject(msg);
			Out.flush();
			System.out.println("Outgoing : " + msg.toString());

			if (msg.type.equals("message") && !msg.content.equals(".bye")) {
				String msgTime = (new Date()).toString();
			}
		} catch (Exception e) {
			System.out.println("Problem in SocketClient class send Method.");
			e.getMessage();
		}
	}

	public void closeThread(Thread t) {
		t = null;
	}
	
}
