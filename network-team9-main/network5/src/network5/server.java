package network5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mysql.cj.xdevapi.Statement;

public class server {
	static int UserNum = 0;
	static Connection con = null;
	static String[] info = new String[2];
	static String id = null;
	static String pwd = null;
	public static void main(String[] args) throws IOException {
		//connection with database
		Method.init("jdbc:mysql://localhost/test2");
		ServerSocket listener = null;
		Socket sock = null;
		try  {
			//create server socket
			listener = new ServerSocket(8421);
			System.out.println("start server ...");
			System.out.println("waiting connection...");
			ExecutorService pool = Executors.newFixedThreadPool(20);
			while(true) {
				//Serve multiple clients (up to 20 in this project)
				sock=listener.accept();
				System.out.println("sock : " + sock);
				pool.execute(new executeClient(sock));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		//new logIn();
		//new signUp();
	}
		
	private static class executeClient implements Runnable {
		
		private Socket socket;
		executeClient(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			BufferedReader in = null;
			BufferedWriter out = null;
			try {
				in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream()));
				while(true) {
					//receive id, pwd from client
					String id = in.readLine();
					String pwd = in.readLine();
					System.out.println("id :"+ id + "pwd : "+ pwd);
					con = Method.GetCon();
					//try login, save result
					String result = Method.Login(con,id,pwd);
					//if result is null
					if(result == "") {
						//log in failed
						out.write("no match");
						out.flush();
						return;
					}
					//if result is not null
					else {
						//log in success
						System.out.println("result" +result);
						//send login(flag)
						out.write("login\n");
						out.flush();
						//send id
						out.write(result+"\n");
						out.flush();
						ClientInfo.Logined_id = result;
						
						//Get login time
						LocalDateTime now = LocalDateTime.now();
						int year = now.getYear();
					    int month = now.getMonthValue();
					    int day = now.getDayOfMonth();
					    int hour = now.getHour();
					    int minute = now.getMinute();
					    int second = now.getSecond();
					    
					    String time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
						
					    //update user state, login time, number of login.
					    try {
							java.sql.Statement stmt = con.createStatement();
							String q1 = "update user_info set status = " + 1 + " where id = \"" + result + "\";";
							String q2 = "update user_info set time = \"" + time + "\" where id = \"" + result + "\";";
							String q3 = "update user_info set numOfLogin = numOfLogin + 1 where id = \"" + result + "\";";
							stmt.executeUpdate(q1);
							stmt.executeUpdate(q2);
							stmt.executeUpdate(q3);
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					    
					    //get friend list
						List<String> res = getFriend(id);
						Iterator iter = res.iterator();
						//send friend list to client
						while(iter.hasNext())
							out.write(iter.next()+"\n");
						//! : to signal the end
						out.write("!");
						out.flush();
						String f = null;
						String u = null;
						while(true) {
							//receive the client to do
							String op = in.readLine();
							//if cliend want to add friend
							if(op.equals("add") && op.trim().length() != 0) {
								//receive friend id
								f = in.readLine();
								//receive user id
								u = in.readLine();
								//result : Whether or not the friend addition was successful
								int addResult = Method.addFriend(con, f, u);
								//send result to client
								out.write(addResult+"\n");
								out.flush();
							}
						}
					}
					
				}
			
			}catch(IOException e) {
				System.out.println("error" + socket);
			}finally {
				try { socket.close(); } catch(IOException e) {}
				System.out.println("Closed: "+ socket);
			}
		}
	}
	
	//Get friend list
	public static List<String> getFriend(String id) {
		Connection con;
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		List<String> frList = new ArrayList<String> ();
		try {
			con = Method.GetCon();
			String q1 = "select fr_id from friend where id = \"" + id + "\";";
			stmt = con.createStatement();
			rs = stmt.executeQuery(q1);
			while(rs.next()) {
				frList.add(rs.getString(1));
			}
		}catch (SQLException e){
            e.printStackTrace();
        }
		return frList;
	}
}