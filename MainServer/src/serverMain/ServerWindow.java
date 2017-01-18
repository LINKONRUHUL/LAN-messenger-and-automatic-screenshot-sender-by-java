package serverMain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.activation.MailcapCommandMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;

public class ServerWindow extends JFrame {
	protected JLabel label;;
	protected JButton start;
	private int port;
	private JScrollPane scrollPane;
	public MainServer server;
	public JTextArea displayArea;
	public ServerWindow()
	{
		super("Main Server");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ServerWindow.class.getResource("/serverMain/images.png")));
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

		label=new JLabel("Server Connections");
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		label.setBounds(0, 4, 600, 10);
		start=new JButton("Start Server");
		start.setFont(new Font("Arial", Font.BOLD, 20));
		start.setBounds(100, 24, 200, 30);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					server=new MainServer(ServerWindow.this, port);
				} catch (Exception e){
					e.printStackTrace();
				}			
			}
		});	
		
		scrollPane = new JScrollPane();
		
		JButton btnNewButton = new JButton("Stop Server");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					server.stop();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 20));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(173)
					.addComponent(start)
					.addPreferredGap(ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(167))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(206)
					.addComponent(label)
					.addContainerGap(402, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(start, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton))
					.addContainerGap())
		);
			
		displayArea = new JTextArea();
		displayArea.setFont(new Font("Arial", Font.BOLD, 18));
		displayArea.setEditable(false);
		scrollPane.setViewportView(displayArea);
		getContentPane().setLayout(groupLayout);
		setSize(600,400);
		setVisible(true);
		pack();
	}
	public void ServerWindow()
	{
	}
	public static void main(String[] args)
	{
		ServerWindow display=new ServerWindow();
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
