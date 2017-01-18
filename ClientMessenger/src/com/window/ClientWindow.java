package com.window;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.main.ConnectionMessage;
import com.main.SocketClient;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;

public class ClientWindow extends JFrame {
	public SocketClient client;
	public Thread clientThread;
	public String serverAddr, username="hello", password;
	public int port;
	public DefaultListModel model;
	public static String lastSnpName;
	long  timeOut = 4000;
	public Background background = new Background();

	public ClientWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClientWindow.class.getResource("/com/window/images png.png")));
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		setContentPane(new Background());
		setResizable(false);
		getContentPane().setBackground(new Color(238, 232, 170));
		setSize(new Dimension(1000,1000));
		setTitle("Client Connected Window");
		setFont(new Font("Arial black", Font.PLAIN, 60));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);

		scrollPane_1 = new JScrollPane();

		lblServerAddress = new JLabel("Server IP :");
		lblServerAddress.setForeground(Color.WHITE);
		lblServerAddress.setBackground(Color.ORANGE);
		lblServerAddress.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 19));

		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 22));
		textField.setColumns(20);
		textField.setText("");

		lblPort = new JLabel("Port Number :");
		lblPort.setForeground(Color.WHITE);
		lblPort.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 19));

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Arial", Font.PLAIN, 22));
		textField_1.setColumns(10);
		textField_1.setText("");

		btnConnect = new JButton("Connect");
		btnConnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnConnect.setFont(new Font("Arial", Font.BOLD, 18));
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ConnectButton_actionPerformed(event);
			}
		});

		lblUserName = new JLabel("Username :");
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 19));

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 22));
		textField_2.setEnabled(false);
		textField_2.setColumns(10);

		lblPassword = new JLabel("Password :");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 19));

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		passwordField.setEnabled(false);

		btnLogIn = new JButton("Login\r\n");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LogIn_ActionPerformed(arg0);
			}
		});
		btnLogIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogIn.setFont(new Font("Arial", Font.BOLD, 18));

		btnLogOut = new JButton("Logout");
		btnLogOut.setEnabled(false);
		btnLogOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		 btnLogOut.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent ev) {
			 Logout_actionPerformed(ev);
		 }
		 });
		btnLogOut.setFont(new Font("Felix Titling", Font.PLAIN, 12));
		
				list = new JList();
				list.setEnabled(false);
				list.setModel((model = new DefaultListModel()));
				list.setSelectedIndex(0);
		
		JButton btnNewButton = new JButton("Logout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 18));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(8)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(lblUserName)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE))
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(lblPort, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE))
												.addGroup(groupLayout.createSequentialGroup()
													.addComponent(lblServerAddress)
													.addPreferredGap(ComponentPlacement.UNRELATED)
													.addComponent(textField, 0, 0, Short.MAX_VALUE)))
											.addPreferredGap(ComponentPlacement.RELATED))))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnLogIn, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(18)
											.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE))
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(115)
											.addComponent(btnNewButton)))
									.addGap(29)))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(58)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 384, GroupLayout.PREFERRED_SIZE)
									.addGap(94)
									.addComponent(list, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
									.addGap(85)
									.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(44)
									.addComponent(btnConnect, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)))
							.addGap(789))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnLogOut)
							.addGap(331)))
					.addGap(314))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnConnect, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblServerAddress, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
							.addGap(18)))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addGap(14)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(275)
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(123)
							.addComponent(btnLogOut))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(22)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUserName))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
							.addGap(73)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnLogIn, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))))
					.addContainerGap(73, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(131)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(list, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE))
					.addGap(100))
		);
		model.addElement("All Clients Name: ");
		textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setEditable(false);
		scrollPane.setColumnHeaderView(textArea);
		getContentPane().setLayout(groupLayout);
		pack();
	}

	public void ConnectButton_actionPerformed(ActionEvent event) {
		serverAddr = textField.getText();
		port = Integer.parseInt(textField_1.getText());
		textField_2.setEnabled(true);
		passwordField.setEnabled(true);
		btnLogIn.setEnabled(true);
		textArea.setEnabled(true);
		list.setEnabled(true);
		btnLogOut.setEnabled(true);
		
		if (!serverAddr.isEmpty() && !textField_1.getText().isEmpty()) {
			try {
				client = new SocketClient(this);
				clientThread = new Thread(client);
				clientThread.start();
				client.send(new ConnectionMessage("signup",username, "Sineup",
						"SERVER"));
				JOptionPane.showMessageDialog(null, "Connected Successfully.");
				
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(true){
							try {
								Thread.sleep(timeOut);
								Date date= new Date();
								try {
									lastSnpName =username+ date.getTime()+".jpg";
									takeSnapShoot(lastSnpName);
						
									client.send(new ConnectionMessage("upload_req",username,lastSnpName,"Server"));
								} catch (AWTException | IOException e) {
									e.printStackTrace();
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
					}
				});
				t.start();
				
				
			} catch (Exception ex) {
				textArea.append("[Application > Me] : Sorry, Server not found\n");
			}
		}

	}
	public void LogIn_ActionPerformed(ActionEvent e)
	{
		username = textField_2.getText();
		password = passwordField.getText();

		if (!username.isEmpty() && !password.isEmpty()) {
			client.send(new ConnectionMessage("signup", username, password, "SERVER"));
		}
	}
	public void Logout_actionPerformed(ActionEvent ev)
	{
		client.send(new ConnectionMessage("signout", username, "bye", "SERVER"));
	}

	public static void main(String args[]) {

		try {
			new ClientWindow().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void takeSnapShoot(String fname) throws AWTException, IOException{
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension d=tk.getScreenSize();
		Rectangle rec=new Rectangle(0,0,d.width,d.height);
		Robot ro=new Robot();
		BufferedImage img=ro.createScreenCapture(rec);   			
		File f;

		f=new File(fname);
		ImageIO.write(img,"jpg",f);
	}
	private JLabel lblServerAddress;
	private JLabel lblPort;
	private JLabel lblUserName;
	private JLabel lblPassword;
	public JTextField textField;
	public JTextField textField_1;
	public JTextField textField_2;
	public JPasswordField passwordField;
	public JTextArea textArea;
	public JButton btnConnect;
	public JButton btnLogIn;
	public JButton btnLogOut;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	public JList list;
}

class Background extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage((new ImageIcon(ClientWindow.class.getResource("img0.jpg")))
				.getImage(), 0, 0, this.getSize().width, this.getSize().height,
				this);
	}
}
