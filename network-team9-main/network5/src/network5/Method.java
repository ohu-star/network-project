package network5;

import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.mysql.cj.protocol.Resultset;

public class Method {
	public static Connection con;
	
	//connect to Database
	public static void init(String url) {
		Connection connection =  null;
		connection =  null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String user = "root", passwd = "12345";
            connection = DriverManager.getConnection(url, user, passwd);
            System.out.println("init con : " + connection);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
    
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        
        con = connection;
        
	}
	
	//Get connection
	public static Connection GetCon()
	{
		return con;
	}
	
	//try Log in, return id
	 public static String Login(Connection con, String id, String pwd) {
	        Statement stmt = null;
	        ResultSet rs = null;

	        try {
	        	String q1 = "select * from user where id = \"" + id + "\"";
	        	String q2 = "select * from user where id = \"" + id + "\" and password = \"" + pwd + "\"";
	        	System.out.println(id);
	        	stmt = con.createStatement();
	        	rs = stmt.executeQuery(q1);
	        	if(!rs.next()){
	        		System.out.println("ID does not exist");
	        		}
	        	else {
	        		rs = stmt.executeQuery(q2);
	        		if(rs.next()) {
	        			System.out.println("Login success");
		        		return rs.getString(1);
	        		}
	        		else {
	        			new Dialog("Login Failed!", "Wrong Password!");
	        		}
	        	}
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return "";
	    }
	 
	 //try sign up, return whether successful without phone number
	 public static int Signin(Connection connection, String id, String pwd, String nickName, String name, String email, String birth) {
	        Statement stmt = null;
	        ResultSet rs = null;

	        try {


	                String q1 = "select id from user where id = \"" + id + "\"";
	                stmt = connection.createStatement();
	                rs = stmt.executeQuery(q1);

	                if(rs.next())
	                    return 0;

	                q1 = "insert into user values(\"" + id + "\", \"" + pwd + "\", \"" + nickName + "\", \"" + name+ "\", \"" + email + "\", \"" + birth + "\", \"" + "null" + "\");";
	                System.out.println(q1);
	                stmt.executeUpdate(q1);
	                return 1;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return - 1;
	    }
	 
	 //try sign up, return whether successful with phone number
	 public static int Signin(Connection connection, String id, String pwd, String nickName, String name, String email, String birth, String phone) {
	        Statement stmt = null;
	        ResultSet rs = null;

	        try {


	                String q1 = "select id from user where id = \"" + id + "\"";
	                stmt = connection.createStatement();
	                rs = stmt.executeQuery(q1);

	                if(rs.next())
	                    return 0;

	                q1 = "insert into user values(\"" + id + "\", \"" + pwd + "\", \"" + nickName + "\", \"" + name+ "\", \"" + email + "\", \"" + birth + "\", \"" + phone + "\");";
	                System.out.println(q1);
	                stmt.executeUpdate(q1);
	                return 1;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return - 1;
	    }
	 
	 //execute Query in database
	 public static ResultSet ExecuteQuery(Connection con, String q1){
	        ResultSet result = null;
	        try{
	            Statement stmt = con.createStatement();
	            result = stmt.executeQuery(q1);
	            return result;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    return result;
	}
	 
	//execute update Query in database
	 public static int ExecuteUpdate(Connection con, String q1){
	        int result = 0;
	        try{
	            Statement stmt = con.createStatement();
	            result = stmt.executeUpdate(q1);
	            return result;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	
	 //get state image
	public static ImageIcon getStateImage(int state) {
		ImageIcon stateImage = null;
		if(state == 0)
			stateImage = new ImageIcon("stateOff.png");
		else
			stateImage = new ImageIcon("stateOn.png");
		Image img = stateImage.getImage();
		Image changeImage = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImage);
			
		return changeIcon;
	}
	
	//get all users
	public static Friend[] GetUsers(Connection connection){
        Statement stmt = null;
        ResultSet rs = null;
        
        List<Friend> list = new ArrayList<Friend>();
        try{
            stmt = connection.createStatement();
            String sql = "Select * from user";
            rs = stmt.executeQuery(sql);

            
           
            while(rs.next())
            {
            	
                String id = rs.getString(1);
                Friend u = new Friend(id);
                System.out.println("dfsfwfef" +id + u);
                list.add(u);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list.toArray(new Friend[0]);
    }
	
	//add friend in database
	public static int addFriend(Connection con, String f_id, String u_id) {
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;
		String q1 = "select * from friend where id = \"" + u_id +
				"\" and fr_id = \"" + f_id +"\"";
		String q2 = "insert into friend values(\"" + u_id + "\", \"" + f_id + "\");";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(q1);
			if(!rs.next()) {
				result = stmt.executeUpdate(q2);
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
}