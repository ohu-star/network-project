package network5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImagingOpException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JPasswordField;
public class chatting extends JFrame implements ActionListener, KeyListener{
	
	// Main Frame
	private JPanel contentPane;
	private JTextField textField_message;
	private JButton btnInvite = new JButton("invite");
	private JButton game_button = new JButton("game");
	private JButton button_create_room = new JButton("");
	private JButton btnSend = new JButton("send");
	private JButton btnFile = new JButton("file");
	private JList list_user = new JList();
	private JTextArea textArea_chat = new JTextArea();
	private JTextArea weather = new JTextArea();
	
	// Network Source
	private Socket socket = null;
	private String id ="noname";
	private String uid = null;
	double temp = 0;
    double humid = 0;
    double rainAmount = 0;
    double wind = 0;
    int hour;
    int min;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private FileInputStream fileInputStream;

	//etc valuable
	private Vector vector_user_list = new Vector();
	private Vector vector_room_list = new Vector();	
	private StringTokenizer stringTokenizer;
	private String my_room = null; 
	JFileChooser jFileChooser;
	File path;
	
	public chatting(String _id, String _uid){
		id = _id;
		uid = _uid;
		network();
		Main_init();  //Main GUI
		start1();	  //ACTION
	}
	
	public chatting(String _uid){
		id = _uid;;
		network();
		Main_init();  //Main GUI
		start2();	  //ACTION
	}
	private void start1(){ //ACTION
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
		}
		btnInvite.addActionListener(this);
		game_button.addActionListener(this);
		button_create_room.addActionListener(this);
		btnSend.addActionListener(this);
		btnFile.addActionListener(this);
		textField_message.addKeyListener(this);
		System.out.println("send_invite");
		String roomname = null;
		if(my_room == null){
			roomname = JOptionPane.showInputDialog("�� �̸�");
		}
		if(roomname != null){//�濡�ִµ���
			send_message("CreateRoom/"+roomname);
			button_create_room.setEnabled(true);
			button_create_room.setText("�泪����");
		}
		System.out.println("create_room");
		String user = uid;
		if(user != null){
			send_message("invite/" + user + "/" + roomname);
		}
		System.out.println("�޴»�� : " + user);
	}
	private void start2(){ //ACTION
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
		}
		btnInvite.addActionListener(this);
		game_button.addActionListener(this);
		button_create_room.addActionListener(this);
		btnSend.addActionListener(this);
		btnFile.addActionListener(this);
		textField_message.addKeyListener(this);
	}
	private void Main_init(){ //Main GUI
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 674, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User List");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 10, 100, 15);
		contentPane.add(lblNewLabel);
		
		
		list_user.setBounds(12, 33, 100, 125);
		contentPane.add(list_user);
		list_user.setListData(vector_user_list);
		
		btnInvite.setBounds(12, 168, 100, 23);
		contentPane.add(btnInvite);
		
		
		game_button.setBounds(12, 379, 100, 23);
		contentPane.add(game_button);
		
		button_create_room.setBounds(12, 412, 100, 23);
		contentPane.add(button_create_room);
		button_create_room.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(124, 6, 522, 396);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(textArea_chat);
		textArea_chat.setEditable(false);
		textField_message = new JTextField();
		textField_message.setBounds(124, 413, 300, 21);
		contentPane.add(textField_message);
		textField_message.setColumns(10);
		textField_message.setEnabled(false);
		
		btnSend.setBounds(446, 412, 95, 23);
		btnSend.setEnabled(false);
		contentPane.add(btnSend);
		
		btnFile.setBounds(551, 412, 95, 23);
		
		contentPane.add(btnFile);
		
		this.setVisible(true);
	}
	private void network(){
		try {
			Scanner filein = null;
			filein = new Scanner(new File("server_info.txt"));//
			socket = new Socket(filein.nextLine(), 7777);
			if(socket != null){ //socket ok!!
				connection();
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "���� ����", "�˸�", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "���� ����", "�˸�", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void connection(){
		try {
			inputStream = socket.getInputStream();
			dataInputStream = new DataInputStream(inputStream);
			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "���� ����", "�˸�", JOptionPane.ERROR_MESSAGE);
		}
		this.setVisible(true);
		send_message(id); //first connect -> send id
		vector_user_list.add(id); //add my id in user_list
		Thread thread = new Thread(new Socket_thread());
		thread.start();
		
	}
	public class Socket_thread implements Runnable{
		public void run() {
			// TODO Auto-generated method stub			 
			while(true){
				try {
					
					InMessage(dataInputStream.readUTF());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					try{
						outputStream.close();
						inputStream.close();
						dataInputStream.close();
						dataOutputStream.close();
						socket.close();
						
						JOptionPane.showMessageDialog(null, "������ ���� ������", "�˸�", JOptionPane.ERROR_MESSAGE);
					}catch(IOException e1){}
					break;
				}
			}
		}
	}
	private void InMessage(String str){ //all message from server
		stringTokenizer = new StringTokenizer(str, "/");
		String protocol = stringTokenizer.nextToken();
		String message = stringTokenizer.nextToken();
		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + message);
		if(protocol.equals("NewUser")){
			vector_user_list.add(message);
		}
		else if(protocol.equals("OldUser")){
			vector_user_list.add(message);
		}
		else if(protocol.equals("invite")){
			int x = JOptionPane.showConfirmDialog(null, "�ʴ븦 �����Ͻðڽ��ϱ�?", message + "������ ���� �� �ʴ�", JOptionPane.YES_NO_OPTION); //basic support dialog
			if(x == 0)
			{
				String room = stringTokenizer.nextToken();
				send_message("inviteok/" + message);
				btnSend.setEnabled(true);
				btnFile.setEnabled(true);
				textField_message.setEnabled(true);
				button_create_room.setEnabled(true);
				
				String JoinRoom = room;
				if(my_room!= null){
					if(my_room.equals(JoinRoom)){
						JOptionPane.showMessageDialog(null, "���� ä�ù��Դϴ�.", "�˸�", JOptionPane.ERROR_MESSAGE);
						return;
					}
					send_message("ExitRoom/"+my_room);
					textArea_chat.setText("");
				}
				send_message("JoinRoom/" + JoinRoom);
				System.out.println("join_room");
				String roomname = null;
				if(my_room == null){
					roomname = JoinRoom;
				}
				if(roomname != null){//�濡�ִµ���
					button_create_room.setText("�泪����");
				}
			}
			else send_message("inviterefuse/" + message);
		}
		else if(protocol.equals("FileStart")){//message = ���ϸ�
			String sid = stringTokenizer.nextToken();//����, �޴�
			String msg = stringTokenizer.nextToken();
			if(!sid.equals(id))
			{
				int x = JOptionPane.showConfirmDialog(null, "������ �����ðڽ��ϱ�?", sid + "������ ���� �� ����", JOptionPane.YES_NO_OPTION); //basic support dialog
				if(x == 0)
				{
					System.out.println(socket+"?");
					send_message("FileStartok/"+sid);
				}
				else send_message("FileStartrefuse/"+sid);
			}
		}
		else if(protocol.equals("user_list_update")){
			list_user.setListData(vector_user_list);
		}
		else if(protocol.equals("CreateRoom")){
			my_room = message;			
		}
		else if(protocol.equals("CreateRoomFail")){
			JOptionPane.showMessageDialog(null, "�� ����� ����", "�˸�", JOptionPane.ERROR_MESSAGE);
		}
		else if(protocol.equals("NewRoom")){
			vector_room_list.add(message);
		}
		else if(protocol.equals("OldRoom")){
			vector_room_list.add(message);
		}
		else if(protocol.equals("room_list_update")){
		}
		else if(protocol.equals("JoinRoom")){
			my_room = message;
			btnSend.setEnabled(true);
			btnFile.setEnabled(true);
			textField_message.setEnabled(true);
		}
		else if(protocol.equals("ExitRoom")){
			vector_room_list.remove(message);
		}
		else if(protocol.equals("Chatting")){
			String msg = stringTokenizer.nextToken();
			textArea_chat.append(message + " : " + msg + "\n");
		}
		else if(protocol.equals("UserOut")){
			vector_user_list.remove(message);
		}
		else if(protocol.equals("inviteok"))
		{
			btnSend.setEnabled(true);
			btnFile.setEnabled(true);
			textField_message.setEnabled(true);
		}
		else if(protocol.equals("inviterefuse"))
		{
			send_message("ExitRoom/"+my_room);//�泪������
			btnSend.setEnabled(false);
			my_room = null;
			button_create_room.setEnabled(false);
			textArea_chat.setText("");
			JOptionPane.showMessageDialog(null, "ä�ù濡�� �����߽��ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(protocol.equals("FileStartok"))
		{
			try 
			{
				fileInputStream = new FileInputStream(path);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				int len;
		        int size = 4096;
		        byte[] data = new byte[size];
		        System.out.println(socket+"?");
		        while ((len = bufferedInputStream.read(data)) != -1) //�о ������
		        {
		        	dataOutputStream.write(data, 0, len);
		        }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(protocol.equals("FileStartrefuse"))
		{
			JOptionPane.showMessageDialog(null, "���� ������ �����߽��ϴ�.", "�˸�", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void send_message(String message){ //button
		try {
			dataOutputStream.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnInvite){
			System.out.println("send_invite");
			String roomname = null;
			if(my_room == null){
				roomname = JOptionPane.showInputDialog("�� �̸�");
			}
			if(roomname != null){//�濡�ִµ���
				send_message("CreateRoom/"+roomname);
				button_create_room.setEnabled(true);
				button_create_room.setText("�泪����");
			}
			System.out.println("create_room");
			String user = (String) list_user.getSelectedValue();
			if(user != null){
				send_message("invite/" + user + "/" + roomname); //ex) Note/User2/hi
			}
			System.out.println("�޴»�� : " + user);
		}
		else if(e.getSource() == game_button){
			new arrowgame();
		}
		else if(e.getSource() == button_create_room){
			String roomname = null;
			if(my_room == null){
				roomname = id;
			}
			if(roomname != null){//�濡�ִµ���
				send_message("CreateRoom/"+roomname);
				button_create_room.setText("�泪����");
			}else{
				send_message("ExitRoom/"+my_room);//�泪������
				btnSend.setEnabled(false);
				my_room = null;
				button_create_room.setEnabled(false);
				textArea_chat.setText("");
				JOptionPane.showMessageDialog(null, "ä�ù濡�� �����߽��ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
			System.out.println("create_room");
		}
		else if(e.getSource() == btnSend){
			System.out.println("send_message");
			if(my_room == null){
				JOptionPane.showMessageDialog(null, "ä�ù濡 �������ּ���", "�˸�", JOptionPane.ERROR_MESSAGE);
			}
			else{
				send_message("Chatting/"+my_room+"/" + textField_message.getText().trim());
				textField_message.setText("");
				textField_message.requestFocus();
			}
		}
		else if(e.getSource() == btnFile){
			
			System.out.println("send_file");
			jFileChooser = new JFileChooser("C://");
			jFileChooser.setDialogTitle("���� ����");
			jFileChooser.setMultiSelectionEnabled(true);
			jFileChooser.setApproveButtonToolTipText("������ ������ �����ϼ���");
			jFileChooser.showDialog(this, "����");
			path = jFileChooser.getSelectedFile();
			if (path != null) {
				send_message("FileStart/"+path.getName()+"/"+uid+"/"+id);//���ϸ�,�޴�,����
			}
			textField_message.requestFocus();
			
		}
	}
	public void keyPressed(KeyEvent arg0) { // ������ ��
		// TODO Auto-generated method stub
		
	}
	public void keyReleased(KeyEvent e) { //������ ���� ��
		// TODO Auto-generated method stub
		if(e.getKeyCode() == 10){ //enter
			send_message("Chatting/"+my_room+"/" + textField_message.getText().trim());
			textField_message.setText("");
			textField_message.requestFocus();
		}
	}
	public void keyTyped(KeyEvent arg0) { //Ÿ����
		// TODO Auto-generated method stub
		
	}
	
}
