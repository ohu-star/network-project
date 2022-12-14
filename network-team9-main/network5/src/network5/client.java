package network5;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class client extends JFrame{

	private JFrame frame;
	private JTextField IDInput;
	private JPasswordField PWDInput;
	private static Connection con;
	static Socket socket;
	static String id = null;

	private  JPanel contentPane;

	JFileChooser jFileChooser;
	static File path;
	
	public static void main(String[] args) {
		
		//connect to Database
		Method.init("jdbc:mysql://localhost/test2");
		
		BufferedReader br;

		//server information
		String fileName = "server_info.txt";
		String ip = null;
		int port = 0;
		
		//friend list
		List<String> frList = new ArrayList<String>();
		
		//try to connect server
		System.out.println("server connecting ...");
		try {
			//read the server information file
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ip = reader.readLine();
			port = Integer.parseInt(reader.readLine());
			reader.close();
			System.out.println("IP address: " + ip + ", port number: " + port);

		} catch (FileNotFoundException e) {
			System.out.println("Problem opening the file " + fileName);
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			//connect with socket
			String tmp = null;
			socket = new Socket(ip, port);
			//pop up login frame
			new client(socket);
			
			//for sending to and receiving from the server
			BufferedReader in = null;
			BufferedWriter out = null;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while(true) {
				//Receive from the server whether the login was successful
				tmp = in.readLine();
				//login success
				if(tmp.equals("login") && tmp.trim().length() != 0) {
					//Receiver ID from server
					id=in.readLine();
					ClientInfo.Logined_id = id;
					StringBuffer buffer = new StringBuffer();
					//receiver friend list
					int ch = 0;
					while(ch >= 0 && ch != '!') {
						ch = in.read();
						if(ch >= 48) {
							while(ch != '\n') {
								buffer.append((char) ch);
								ch = in.read();
								System.out.println(ch);
							}
							buffer.append((char) ch);
							String receiveStr = buffer.toString();
							receiveStr = receiveStr.replaceAll("\n", " ");
							receiveStr = receiveStr.trim();
							System.out.println("String: "+ receiveStr);
							frList.add(receiveStr);
							buffer.setLength(0);
						}
					}
					break;
				}
				//login fail
				else if(tmp.equals("not match") && tmp.trim().length() != 0){
					System.out.println("try again");
				}
			}
			//pop up home
			new home(ClientInfo.Logined_id, frList, socket);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public client(Socket socket) {
		logIn(socket);
	}
	
	private void logIn(Socket socket){
		setBounds(100, 100, 469, 487);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 0, 455, 450);
		setContentPane(panel);
		panel.setLayout(null);
		
		JTextArea txtrOnlineMessenger = new JTextArea();
		txtrOnlineMessenger.setFont(new Font("Dialog", Font.BOLD, 30));
		txtrOnlineMessenger.setText("LOGIN");
		txtrOnlineMessenger.setEditable(false);
		txtrOnlineMessenger.setBounds(12, 22, 242, 58);
		panel.add(txtrOnlineMessenger);
		
		JTextArea txtrId = new JTextArea();
		txtrId.setText("ID");
		txtrId.setEditable(false);
		txtrId.setFont(new Font("Dialog", Font.PLAIN, 25));
		txtrId.setBounds(62, 108, 141, 34);
		panel.add(txtrId);
		
		IDInput = new JTextField();
		IDInput.setBounds(62, 151, 305, 44);
		panel.add(IDInput);
		IDInput.setColumns(10);
		
		JTextArea txtrPassword = new JTextArea();
		txtrPassword.setText("PASSWORD");
		txtrPassword.setEditable(false);
		txtrPassword.setFont(new Font("Dialog", Font.PLAIN, 25));
		txtrPassword.setBounds(62, 234, 162, 34);
		panel.add(txtrPassword);
		
		PWDInput = new JPasswordField();
		PWDInput.setEchoChar('*');
		PWDInput.setColumns(10);
		PWDInput.setBounds(62, 275, 305, 44);
		panel.add(PWDInput);
		
		//when try login
		JButton btnNewButton = new JButton("Log in");
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 19));
		btnNewButton.setBackground(new Color(67, 173, 192));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBounds(46, 374, 157, 44);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = IDInput.getText();
				String pw = PWDInput.getText();
				String tmp = null;
				//password encryption
				try {
					tmp = sha256(pw);
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
				try {
					BufferedReader in = null;
					BufferedWriter out = null;
					
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					
					//Send ID and password to the server (to check if can log in)
					out.write(id + "\n");
					out.write(tmp + "\n");
					
					out.flush();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				setVisible(false);
			}
		});
		panel.add(btnNewButton);
		
		//if click the Sign Up button
		JButton btnSignUp = new JButton("Sign up");
		btnSignUp.setFont(new Font("Dialog", Font.PLAIN, 19));
		btnSignUp.setForeground(Color.WHITE);
		btnSignUp.setBackground(new Color(67, 173, 192));
		btnSignUp.setBounds(234, 374, 157, 44);
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new signUp();
			}
		});
		panel.add(btnSignUp);
		
		setVisible(true);
	}
	
	//for password encryption
	public static String sha256(String msg)  throws NoSuchAlgorithmException {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(msg.getBytes());
	    return byteToHexString(md.digest());
	}

	public static String byteToHexString(byte[] data) {
	    StringBuilder sb = new StringBuilder();
	    for(byte b : data) {
	        sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
	    }
	    return sb.toString();
	}
}