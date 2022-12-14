package network5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class chattings extends JFrame{
	
	// Server Frame
	private JPanel contentPane;
	private JTextField textField_port;
	private JTextArea textArea = new JTextArea();

	// Network Source
	private ServerSocket serverSocket;
	private Socket socket;
	private int port = 7777;
	private Vector vector_user = new Vector();
	private Vector vector_room = new Vector();
	// ETC Source
	private StringTokenizer stringTokenizer;
	
	private void server_start(){
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "이미 사용중인 포트", "알림", JOptionPane.ERROR_MESSAGE);
		}
		if(serverSocket != null){ // socket ok!
			connection();
		}
			
	}
	public class Socket_thread implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				try { //wait client
					textArea.append("사용자 접속 대기중\n");
					socket = serverSocket.accept();
					UserInfo userInfo = new UserInfo(socket);
					userInfo.start();
				} catch (IOException e) {
					textArea.append("서버가 중지 되었습니다\n");
					break;
				}
			}
		}
	}
	
	private void connection(){
		Thread thread = new Thread(new Socket_thread());
		thread.start();
	
	}
	public chattings(){
		server_start();
	}
	private void init(){ // Server GUI
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 412, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 372, 257);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new chattings();
	}
	private class UserInfo extends Thread{
		private InputStream inputStream;
		private OutputStream outputStream;
		private DataOutputStream dataOutputStream;
		private DataInputStream dataInputStream;
		private Socket socket_user;
		private String Nickname = "";
		private String CurrentRoom = null;
		private boolean RoomCheck = true; 
		public UserInfo(Socket socket){
			this.socket_user = socket;
			userNetwork();
		}
		public String getNicname(){
			return Nickname;
		}
		public void userNetwork(){
			try {
				inputStream = socket_user.getInputStream();
				dataInputStream = new DataInputStream(inputStream);
				outputStream = socket_user.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);	
				Nickname = dataInputStream.readUTF();
				textArea.append(Nickname + " : 사용자 접속\n");
				
				//when a new user is connected
				
				BroadCast("NewUser/" + Nickname);
				for(int i = 0; i< vector_user.size(); i++){ // server alert at existing user and send the new user's id
					UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
					this.send_Message("OldUser/" +  userInfo.getNicname());
				}
				vector_user.add(this);
				BroadCast("user_list_update/ ");
				SetOldRoom();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Stream설정 에러", "알림", JOptionPane.ERROR_MESSAGE);
			}
		}
		private void BroadCast(String str){
			for(int i = 0; i< vector_user.size(); i++){ // server alert at existing user and send the new user's id
				UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
				userInfo.send_Message(str);
			}
		}
		private void SetOldRoom(){
			for(int i = 0; i< vector_room.size(); i++){
				RoomInfo roomInfo = (RoomInfo) vector_room.elementAt(i);
				this.send_Message("OldRoom/" + roomInfo.getRoomName());
			}
			this.send_Message("room_list_update/ ");
		}
		private void ExitRoom(UserInfo userInfo){
			for(int i = 0; i< vector_room.size(); i++){
				RoomInfo roomInfo = (RoomInfo) vector_room.elementAt(i);
				if(roomInfo.getRoomName().equals(CurrentRoom)){
					int size = roomInfo.Remove_User(userInfo);
					if(size ==0){
						BroadCast("ExitRoom/"+CurrentRoom);
						vector_room.remove(i);
						BroadCast("room_list_update/ ");
					}
					else{
						roomInfo.BroadCast_Room("Chatting/알림/******    "+Nickname+"님이 퇴장하셨습니다     *****");
					}
					CurrentRoom = null;
					break;
				}
			}
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(true){
				try {
					String msg = dataInputStream.readUTF();//클라로부터받는부분
					textArea.append(Nickname + " : " + msg + "\n");
					InMessage(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					textArea.append(Nickname + " : 사용자 접속 끊어짐\n");
					ExitRoom(this);
					try {
						dataInputStream.close();
						dataOutputStream.close();
						socket_user.close();
						vector_user.remove(this);
						BroadCast("UserOut/"+Nickname);
						BroadCast("user_list_update/ ");
					} catch (IOException e1) {}
				
					break;
				}
			}
		}
		private void InMessage(String str){ //handle the message from client
			System.out.println(str);
			System.out.println(socket);
			stringTokenizer = new StringTokenizer(str, "/");
			String protocol = stringTokenizer.nextToken();
			String message = stringTokenizer.nextToken();
			System.out.println("protocol : " + protocol);
			if(protocol.equals("invite")){//invite
				//protocol = Note
				//message = user, msg = room
				System.out.println("받는 사람 : " + message);
				String msg = stringTokenizer.nextToken();
				for(int i = 0; i< vector_user.size(); i++){
					UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
					if(userInfo.Nickname.equals(message)){
						userInfo.send_Message("invite/"+ Nickname+"/"+msg);
					}
				}
			}
			else if(protocol.equals("inviteok")){
				System.out.println("받는 사람 : " + message);
				for(int i = 0; i< vector_user.size(); i++){
					UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
					if(userInfo.Nickname.equals(message)){
						userInfo.send_Message("inviteok/"+ Nickname);
					}
				}
			}
			else if(protocol.equals("inviterefuse")){
				System.out.println("받는 사람 : " + message);
				for(int i = 0; i< vector_user.size(); i++){
					UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
					if(userInfo.Nickname.equals(message)){
						userInfo.send_Message("inviterefuse/"+ Nickname);
					}
				}
			}
			else if(protocol.equals("FileStart")){//message는 파일명
				String msg = stringTokenizer.nextToken();//msg = 받는, sid = 보낸
				String sid = stringTokenizer.nextToken();
				for(int i = 0; i< vector_user.size(); i++){
					UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
					if(userInfo.Nickname.equals(msg)){
						userInfo.send_Message("FileStart/"+message+"/"+sid+"/"+msg);
					}
				}
			}
			else if(protocol.equals("FileStartok")){//message는 이름
				for(int i = 0; i< vector_user.size(); i++){
					UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
					if(userInfo.Nickname.equals(message)){
						userInfo.send_Message("FileStartok/"+message);
					}
				}
			}
			else if(protocol.equals("FileStartrefuse")){//message는 이름
				for(int i = 0; i< vector_user.size(); i++){
					UserInfo userInfo = (UserInfo) vector_user.elementAt(i);
					if(userInfo.Nickname.equals(message)){
						userInfo.send_Message("FileStartrefuse/"+message);
					}
				}
			}
			else if(protocol.equals("CreateRoom")){
				for(int i = 0; i< vector_room.size(); i++){
					RoomInfo roomInfo = (RoomInfo) vector_room.elementAt(i);
					if(roomInfo.getRoomName().equals(message)){ //방이 이미  존재
						send_Message("CreateRoomFail/ok");
						RoomCheck = false;
						break;
					}
				}
				if(RoomCheck){
					RoomInfo roomInfo_new_room = new RoomInfo(message, this);
					vector_room.add(roomInfo_new_room);
					CurrentRoom = message;
					send_Message("CreateRoom/" + message);
					send_Message("Chatting/알림/******    "+Nickname+"님이 입장하셨습니다     *****");
					BroadCast("NewRoom/"+message);
				}
				else
				{
					RoomCheck = true;	
				}	
			}
			else if(protocol.equals("Chatting")){
				String msg = stringTokenizer.nextToken();//msg는채팅내용, message는 마이룸
				for(int i =0; i <vector_room.size(); i++){
					RoomInfo roomInfo = (RoomInfo) vector_room.elementAt(i);
					if(roomInfo.getRoomName().equals(message)){ // 해당 방을 찾으면
						roomInfo.BroadCast_Room("Chatting/"+Nickname+"/"+msg);
					}
				}
			}
			else if(protocol.equals("JoinRoom")){
				for(int i =0; i <vector_room.size(); i++){
					RoomInfo roomInfo = (RoomInfo) vector_room.elementAt(i);
					if(roomInfo.getRoomName().equals(message)){
						CurrentRoom = message;
						//새로운 사용자를 알린다
						roomInfo.Add_User(this);
						roomInfo.BroadCast_Room("Chatting/알림/******    "+Nickname+"님이 입장하셨습니다     *****");
						send_Message("JoinRoom/" + message);
					}
				}
			}
			else if(protocol.equals("ExitRoom")){
				ExitRoom(this);
			}
		}
		private void send_Message(String message){
			try {
				dataOutputStream.writeUTF(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				textArea.append("메세지 전송 실패");
			}
		}
		
	}
	
	private class RoomInfo{

		private String Room_name;
		private Vector vector_room_user = new Vector();
		
		RoomInfo(String str, UserInfo userInfo){
			this.Room_name = str;
			this.vector_room_user.add(userInfo);
		}
		public String getRoomName(){
			return Room_name;
		}
		public Vector getRoom_user(){
			return vector_room_user;
		}
		private void Add_User(UserInfo userInfo){
			this.vector_room_user.add(userInfo);
		}
		public int Remove_User(UserInfo userInfo){
			this.vector_room_user.remove(userInfo);
			return vector_room_user.size();

		}
		public void BroadCast_Room(String str){ //현재 방의 모든 사람에게 알린다.
			for(int i = 0; i<vector_room_user.size(); i++){
				UserInfo userInfo = (UserInfo) vector_room_user.elementAt(i);
				userInfo.send_Message(str);
			}
		}
	}
	
}