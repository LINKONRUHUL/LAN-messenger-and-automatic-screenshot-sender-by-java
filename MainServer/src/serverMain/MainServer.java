package serverMain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.main.ConnectionMessage;

public class MainServer implements Runnable {

	public ClientClass clients[];
	public ServerSocket server = null;
	public Thread thread = null;
	public int clientCount = 0;
	public int port;
	public ServerWindow ui;

	public MainServer(ServerWindow frame, int Port) {

		clients = new ClientClass[100];
		ui = frame;
		port = Port;

		try {
			server = new ServerSocket(port);
			ui.displayArea.append("Server Started & IP Address : "
					+ InetAddress.getLocalHost().getHostAddress()
					+ " And Port Number : " + server.getLocalPort());

			thread = new Thread(this);
			thread.start();

		} catch (IOException ioe) {
			ui.displayArea.append("\nSorry,Can Not Connect to the Port " + port
					+ ": " + ioe.getMessage());
		}
	}

	public void run() {
		while (true) {
			try {
				ui.displayArea.append("\nWaiting For a Client to Connect...");
				addThread(server.accept());
			} catch (Exception ioe) {
				ui.displayArea.append("\nServer Found an Error: \n");
			}
		}
	}
	@SuppressWarnings("deprecation")
	public void stop() throws IOException {
		if (thread != null) {
			thread.stop();
			server.close();
			server=null;
			thread = null;			
			ui.displayArea.append("\nServer has been Stopped.");			
		}
	}

	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].getID() == ID) {
				return i;
			}
		}
		return -1;
	}

	public synchronized void handle(int ID, ConnectionMessage msg) {
		if (msg.content.equals("bye")) {
			Announce("signout", "SERVER", msg.sender);
			remove(ID);
		} else {

			if (msg.type.equals("login")) {
				if (findUserThread(msg.sender) == null) {
					clients[findClient(ID)].username = msg.sender;
					clients[findClient(ID)].send(new ConnectionMessage("login",
							"SERVER", "TRUE", msg.sender));
					Announce("newuser", "SERVER", msg.sender);
					SendUserList(msg.sender);

				} else {
					clients[findClient(ID)].send(new ConnectionMessage("login",
							"SERVER", "FALSE", msg.sender));
				}
			} else if (msg.type.equals("message")) {
				if (msg.recipient.equals("All")) {
					Announce("message", msg.sender, msg.content);
				} else {

					findUserThread(msg.recipient).send(
							new ConnectionMessage(msg.type, msg.sender,
									msg.content, msg.recipient));
					clients[findClient(ID)].send(new ConnectionMessage(
							msg.type, msg.sender, msg.content, msg.recipient));
				}
			} else if (msg.type.equals("test")) {
				clients[findClient(ID)].send(new ConnectionMessage("test",
						"SERVER", "OK", msg.sender));
			} else if (msg.type.equals("signup")) {
				if (findUserThread(msg.sender) == null) {

					clients[findClient(ID)].username = msg.sender;
					clients[findClient(ID)].send(new ConnectionMessage("login",
							"SERVER", "TRUE", msg.sender));
					Announce("newuser", "SERVER", msg.sender);
					SendUserList(msg.sender);

				} else {
					clients[findClient(ID)].send(new ConnectionMessage("login",
							"SERVER", "FALSE", msg.sender));
				}

			} else if (msg.type.equals("upload_req")) {

				try {
					System.out.println("Request accepted" + msg);
					System.out.println("User name: " + msg.sender + " "
							+ findUserThread(msg.sender).username);
					Download d = new Download(msg.content);
					System.out.println("Download port: " + d.port
							+ " \nDownload Ip: "
							+ InetAddress.getLocalHost().getHostName() + "");
					findUserThread(msg.sender).send(
							new ConnectionMessage("upload_res", d.port + "",
									InetAddress.getLocalHost().getHostName()
											+ "", ""));
					new Thread(d).start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void Announce(String type, String sender, String content) {
		ConnectionMessage msg = new ConnectionMessage(type, sender, content,
				"All");
		for (int i = 0; i < clientCount; i++) {
			clients[i].send(msg);
		}
	}

	public void SendUserList(String toWhom) {
		for (int i = 0; i < clientCount; i++) {
			findUserThread(toWhom).send(
					new ConnectionMessage("newuser", "SERVER",
							clients[i].username, toWhom));
		}
	}

	public ClientClass findUserThread(String usr) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].username.equals(usr)) {
				return clients[i];
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		if (pos >= 0) {
			ClientClass toTerminate = clients[pos];
			ui.displayArea.append("\nRemoved Client Thread " + ID + " at "
					+ pos);
			if (pos < clientCount - 1) {
				for (int i = pos + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
			}
			clientCount--;
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				ui.displayArea.append("\nError Closing Thread: " + ioe);
			}
			toTerminate.stop();
		}
	}

	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			ui.displayArea.append("\nClient Accepted: " + socket);
			clients[clientCount] = new ClientClass(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe) {
				ui.displayArea.append("\nError Opening Thread: " + ioe);
			}
		} else {
			ui.displayArea.append("\nERROR: Maximum Number Of Client "
					+ clients.length + " has Reached.");
		}
	}

	class ClientClass extends Thread {

		public MainServer server = null;
		public Socket socket = null;
		public int ID = -1;
		public String username = "";
		public ObjectInputStream streamIn = null;
		public ObjectOutputStream streamOut = null;
		public ServerWindow ui;

		public ClientClass(MainServer _server, Socket _socket) {
			super();
			server = _server;
			socket = _socket;
			ID = socket.getPort();
			ui = _server.ui;
		}

		public void send(ConnectionMessage msg) {
			try {
				streamOut.writeObject(msg);
				streamOut.flush();
			} catch (IOException ex) {
				System.out.println("Exception [SocketClient : send(...)]");
			}
		}

		public int getID() {
			return ID;
		}

		@SuppressWarnings("deprecation")
		public void run() {
			ui.displayArea.append("\nServer Thread " + ID + " is Running Now");
			while (true) {
				try {
					ConnectionMessage msg = (ConnectionMessage) streamIn
							.readObject();
					server.handle(ID, msg);
				} catch (Exception ioe) {
					ioe.printStackTrace();
					System.out.println(ID + " ERROR reading: "
							+ ioe.getMessage());
					System.out.println("problem in runServer");
					server.remove(ID);
					stop();
				}
			}
		}

		public void open() throws IOException {
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.flush();
			streamIn = new ObjectInputStream(socket.getInputStream());
		}

		public void close() throws IOException {
			if (socket != null)
				socket.close();
			if (streamIn != null)
				streamIn.close();
			if (streamOut != null)
				streamOut.close();
		}
	}
}
