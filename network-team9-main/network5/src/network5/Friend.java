package network5;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Friend {
	public String id;
	public String pwd;
	public String nickName;
	public String name;
	public String email;
	public String birthday;
	public String phone;
	public int status;
	public String time;
	public int numOfLogin;
	public String intro;
	
	//Friend information without friend ID
	Friend() {
		id = "";
		pwd = "";
		nickName = "";
		name = "";
		email = "";
		birthday = "";
		phone = "";
		status = 0;
		time = "";
		numOfLogin = 0;
		intro = "";
	}
	
	//Friend information with friend ID
	Friend(String f_id) {
		id = "";
		pwd = "";
		nickName = "";
		name = "";
		email = "";
		birthday = "";
		phone = "";
		status = 0;
		time = "";
		numOfLogin = 0;
		intro = "";
		
		//SQL Query
		//select all from user and user info
		String q1 = "select * from user where id = \"" + f_id + "\";";
		String q2 = "select * from user_info where id = \"" + f_id + "\";";
		
		ResultSet rs = Method.ExecuteQuery(Method.GetCon(), q1);
		ResultSet rs2 = Method.ExecuteQuery(Method.GetCon(), q2);
		
		try {
			//if ResultSet have result, save to friend info
			if(rs.next()) {
				id = rs.getString(1);
				pwd = rs.getString(2);
				nickName = rs.getString(3);
				name = rs.getString(4);
				email = rs.getString(5);
				birthday = rs.getString(6);
				phone = rs.getString(7);
			}
			if(rs2.next()) {
				status = rs2.getInt(2);
				time = rs2.getString(3);
				numOfLogin = rs2.getInt(4);
				intro = rs2.getString(5);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}