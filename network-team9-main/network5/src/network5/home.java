package network5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;

public class home extends JFrame{
	double temp = 0;
    double humid = 0;
    double rainAmount = 0;
    double wind = 0;
    int hour;
    int min;
    JTextArea weather = new JTextArea();
	/*public home(String id, List<String> frList) {
		initialize(id, frList);
	}*/
    
    //run class home
	public home(String id, List<String> frList, Socket socket) {
		ClientInfo.Logined_id = id;
		initialize(id, frList, socket);
	}
	private void initialize(String id, List<String> frList, Socket socket) {

		int numOfFriend=frList.size();
		
		//set Frame
		setBounds(100, 100, 469, 800);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel appBar = new JPanel();
		appBar.setBounds(0, 0, 455, 158);
		getContentPane().add(appBar);
		appBar.setBackground(new Color(67, 173, 192));
		appBar.setLayout(null);
		
		ImageIcon logoImage = new ImageIcon("logo.jpg");
		Image img = logoImage.getImage();
		Image changeImage = img.getScaledInstance(118, 102, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImage);
		
		JLabel logo = new JLabel(changeIcon);
		logo.setBounds(154, 0, 118, 102);
		appBar.add(logo);
		
		ImageIcon searchImage = new ImageIcon("search.png");
		Image sImg = searchImage.getImage();
		Image scImg = sImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon cSearchIcon = new ImageIcon(scImg);
		
		JLabel lblNewLabel = new JLabel(cSearchIcon);
		lblNewLabel.setBounds(29, 113, 35, 35);
		appBar.add(lblNewLabel);
		
		ImageIcon menuImage = new ImageIcon("menu.png");
		Image mImg = menuImage.getImage();
		Image mcImg = mImg.getScaledInstance(52, 50, Image.SCALE_SMOOTH);
		ImageIcon cmenuIcon = new ImageIcon(mcImg);
		
		//if click menu, go to setting
		JLabel lblNewLabel_1 = new JLabel(cmenuIcon);
		lblNewLabel_1.setBounds(369, 24, 52, 50);
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("menu");
				new Setting(id);
			}
		});
		appBar.add(lblNewLabel_1);
		
		ImageIcon logoutImage = new ImageIcon("logout.png");
		Image lImg = logoutImage.getImage();
		Image lcImg = lImg.getScaledInstance(52, 44, Image.SCALE_SMOOTH);
		ImageIcon clogoutIcon = new ImageIcon(lcImg);
		
		JLabel lblNewLabel_3 = new JLabel(clogoutIcon);
		lblNewLabel_3.setBounds(12, 24, 52, 44);
		appBar.add(lblNewLabel_3);
		
		Connection con = Method.GetCon();

		String q = "update user_info set status = \""+0+"\"where id= \"" + id + "\";";
		//if click logout, close frame
		lblNewLabel_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("logout");
				try{
					//update status to false
		            Statement stmt = con.createStatement();
		    		int result = stmt.executeUpdate(q);
		            if(result == 1)
		            	dispose();
		        } catch (SQLException e1) {
		            e1.printStackTrace();
		        }
			}
		});
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setBounds(62, 112, 356, 36);
		appBar.add(btnNewButton);
		//if click search, go to search
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new search(socket, id);
				//dispose();
			}
		});
		weather.setBackground(new Color(67, 173, 192));
		
		//public data
		weather.setEditable(false);
		api();
		weather.append("현재시간: " + hour + ":" + min + "\n");
		weather.append("현재기온: " + temp + "℃\n");
		weather.append("습도: " + humid + "%\n");
		weather.append("강수량: " + rainAmount + "mm\n");
		weather.append("풍속: " + wind + "m/s\n");
		weather.setBounds(337, 20, 106, 78);
		appBar.add(weather);
		
		//for friends list
		JLayeredPane layeredpanel = new JLayeredPane();
		layeredpanel.setBounds(0, 156, 455, 607);
		getContentPane().add(layeredpanel);
		layeredpanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 156, 455, 607);
		panel.setBackground(new Color(255, 255, 255));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollpane=new JScrollPane(panel);
		scrollpane.setSize(470, 700);
		scrollpane.setBackground(new Color(255, 255, 255));
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Add as many friend panels as the number of friends
		for(int i = 0; i < numOfFriend; i++) {
			System.out.println("friend");
			Friend f = new Friend(frList.get(i));
			FriendPanel p1 = new FriendPanel(f, socket);
			p1.setBackground(new Color(255,255,255));
			panel.add(p1);

			
		}
		
		//user information (logged in user)
		layeredpanel.add(scrollpane);
		String nickname = null;
		String intro = null;
		try {
			String q1 = "select nickname from user where id = \"" + id +"\";";
			ResultSet rs = Method.ExecuteQuery(con, q1);
			if(rs.next())
				nickname = rs.getString(1);
			
			String q2 = "select intro from user_info where id = \"" + id +"\";";
			rs =Method.ExecuteQuery(con, q2);
			if(rs.next())
				intro = rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageIcon fIcon = Method.getStateImage(1);
		System.out.println(fIcon);
		JPanel infoPanel = new JPanel();
		infoPanel.setBounds(80, 15, 158, 40);
		infoPanel.setBackground(new Color(255, 255, 255));
		panel.add(infoPanel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(0, 159, 455, 101);
		getContentPane().add(panel_1);
		setLocationRelativeTo(null);
		setVisible(true);
		panel_1.setLayout(null);
		
		JLabel userStatus = new JLabel(fIcon, JLabel.CENTER);
		userStatus.setBounds(29, 20, 20, 20);
		userStatus.setVerticalTextPosition(JLabel.CENTER);
		userStatus.setHorizontalTextPosition(JLabel.RIGHT);
		panel_1.add(userStatus);
		
		JLabel userName = new JLabel(nickname);
		userName.setForeground(Color.BLACK);
		userName.setBounds(54, 10, 389, 42);
		panel_1.add(userName);
		
		JLabel ID = new JLabel(id);
		ID.setBounds(54, 45, 389, 22);
		ID.setVerticalAlignment(SwingConstants.BOTTOM);
		ID.setForeground(Color.GRAY);
		panel_1.add(ID);
		
		JLabel lblNewLabel_2 = new JLabel(intro);
		lblNewLabel_2.setBounds(54, 70, 389, 33);
		panel_1.add(lblNewLabel_2);
		
		JButton btnInfo = new JButton("Info");
		panel_1.add(btnInfo);
		btnInfo.setBackground(new Color(67, 173, 192));
		btnInfo.setBounds(361, 47, 82, 31);
		btnInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Info(id);
			}
		});
	}
	
	public void api()
	{		
    	try
    	{
    		LocalDateTime now = LocalDateTime.now();
        	String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            hour = now.getHour();
            min = now.getMinute();
            if(min <= 30) { // 해당 시각 발표 전에는 자료가 없음 - 이전시각을 기준으로 해야함
                hour -= 1;
            }
            String hourStr = hour + "00"; // 정시 기준
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=fXAOWIO2gYzP9w7USuRh4lKzRv8e7ClA5kh7CkPOTSXzCWg8O%2BI4%2BCDv7Bd0whCQqLvolyT2nRm%2BsN4AqKne%2BA%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("8", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*‘21년 6월 28일 발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hourStr, "UTF-8")); /*06시 발표(정시단위) */
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("56", "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*///경기도기준
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            String data = sb.toString();
            System.out.println(data);
            JSONObject jObject = new JSONObject(data);
            JSONObject response = jObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray jArray = items.getJSONArray("item");
            
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                String category = obj.getString("category");
                double obsrValue = obj.getDouble("obsrValue");

                switch (category) {
                    case "T1H":
                        temp = obsrValue;
                        break;
                    case "RN1":
                        rainAmount = obsrValue;
                        break;
                    case "REH":
                        humid = obsrValue;
                        break;
                    case "WSD":
                    	wind = obsrValue;
                        break;
                }
            }
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
	
}
