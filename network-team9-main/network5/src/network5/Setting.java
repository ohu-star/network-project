package network5;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JTextField;

public class Setting extends JFrame{
	private JTextField textField;
	private JTextField textField_1;
	
	//pop up setting
	public Setting(String id) {
		getContentPane().setLayout(null);
		
		setBounds(0, 0, 436, 425);
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 436, 425);
		getContentPane().add(panel);
		panel.setLayout(null);
		JPanel appBar = new JPanel();
		appBar.setBounds(0, 0, 436, 103);
		panel.add(appBar);
		appBar.setBackground(new Color(67, 173, 192));
		appBar.setLayout(null);
		
		ImageIcon logoImage = new ImageIcon("logo.jpg");
		Image img = logoImage.getImage();
		Image changeImage = img.getScaledInstance(118, 102, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImage);
		
		JLabel logo = new JLabel(changeIcon);
		logo.setBounds(154, 0, 118, 102);
		appBar.add(logo);
		
		JTextArea txtrSetting = new JTextArea();
		txtrSetting.setText("Setting");
		txtrSetting.setFont(new Font("Dialog", Font.BOLD, 27));
		txtrSetting.setBounds(12, 113, 177, 49);
		panel.add(txtrSetting);
		

		Connection con = Method.GetCon();

		String nickname = null;
		String intro = null;
		try {
			//get nickname
			String q1 = "select nickname from user where id = \"" + id +"\";";
			ResultSet rs = Method.ExecuteQuery(con, q1);
			if(rs.next())
				nickname = rs.getString(1);
			
			//get introduce
			String q2 = "select intro from user_info where id = \"" + id +"\";";
			rs =Method.ExecuteQuery(con, q2);
			if(rs.next())
				intro = rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JTextArea txtrNicknameChange = new JTextArea();
		txtrNicknameChange.setFont(new Font("Dialog", Font.PLAIN, 18));
		txtrNicknameChange.setText("nickname change");
		txtrNicknameChange.setBounds(55, 171, 165, 26);
		txtrNicknameChange.setEditable(false);
		panel.add(txtrNicknameChange);
		
		String tmpNickname = nickname;
		textField = new JTextField(nickname);
		textField.setBounds(55, 197, 326, 39);
		panel.add(textField);
		//if change nickname button clicked
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String change = textField.getText();
				//update in database
				String p1 = "update user set nickname = \""+change+"\"where id= \"" + id + "\";";
				//If the current nickname and the nickname you want to change are the same
				if(tmpNickname.equals(change)) {
					JOptionPane.showMessageDialog(null, "You cannot change with the same nickname", "Change nickname", JOptionPane.WARNING_MESSAGE);
				}
				//if there are different
				else if(Method.ExecuteUpdate(Method.GetCon(), p1) != 0) {
					JOptionPane.showMessageDialog(null, "Nickname change completed","Change nickname", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		textField.setColumns(10);
		
		JTextArea txtrIntroChange = new JTextArea();
		txtrIntroChange.setText("introduce change");
		txtrIntroChange.setFont(new Font("HY以묎퀬�뵓", Font.PLAIN, 18));
		txtrIntroChange.setBounds(55, 273, 165, 26);
		panel.add(txtrIntroChange);
		
		if(intro.equals("null")) intro = "";
		String tmpIntro = intro;
		textField_1 = new JTextField(intro);
		textField_1.setColumns(10);
		textField_1.setBounds(55, 299, 326, 39);
		//if change introduce button clicked
		textField_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String change = textField_1.getText();
				//update in database
				String p1 = "update user_info set intro = \""+change+"\"where id= \"" + id + "\";";
				//If the current introduction and the introduction to be replaced are the same
				if(tmpIntro.equals(change)) {
					JOptionPane.showMessageDialog(null, "You cannot change with the same introduce", "Change introduce", JOptionPane.WARNING_MESSAGE);
				}
				//if there are different
				else if(Method.ExecuteUpdate(Method.GetCon(), p1) != 0) {
					JOptionPane.showMessageDialog(null, "introduce change completed", "Change introduce", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		panel.add(textField_1);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

}