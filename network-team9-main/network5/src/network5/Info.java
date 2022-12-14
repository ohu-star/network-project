package network5;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JTextField;

public class Info extends JFrame{
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField txt;

	//information pop up
	public Info(String f_id) {
		//save friend information
		Friend f = new Friend(f_id);
		String id = f.id;
		String nickname = f.nickName;
		String name = f.name;
		String email = f.email;
		String birthday = f.birthday;
		String intro = f.intro;
		
		//If there is no introduction base is "HI :)"
		if(intro.equals("null")) intro = "Hi :)";
		setBounds(100, 100, 469, 800);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 455, 763);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel appBar = new JPanel();
		appBar.setBounds(0, 0, 455, 158);
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
		
		txt = new JTextField(intro);
		txt.setForeground(Color.WHITE);
		txt.setFont(new Font("Dialog", Font.BOLD, 20));
		txt.setBackground(new Color(67, 173, 192));
		txt.setBounds(12, 108, 417, 40);
		appBar.add(txt);
		txt.setHorizontalAlignment(JLabel.CENTER);
		txt.setColumns(10);
		
		JTextArea txtrId = new JTextArea();
		txtrId.setText("ID");
		txtrId.setFont(new Font("Dialog", Font.PLAIN, 18));
		txtrId.setBounds(51, 180, 181, 24);
		panel.add(txtrId);
		
		JTextArea txtrId_1 = new JTextArea();
		txtrId_1.setText("nickname");
		txtrId_1.setFont(new Font("Dialog", Font.PLAIN, 18));
		txtrId_1.setBounds(51, 280, 181, 24);
		panel.add(txtrId_1);
		
		JTextArea txtrId_2 = new JTextArea();
		txtrId_2.setText("name");
		txtrId_2.setFont(new Font("Dialog", Font.PLAIN, 18));
		txtrId_2.setBounds(51, 380, 181, 24);
		panel.add(txtrId_2);
		
		JTextArea txtrId_3 = new JTextArea();
		txtrId_3.setText("email");
		txtrId_3.setFont(new Font("Dialog", Font.PLAIN, 18));
		txtrId_3.setBounds(51, 480, 181, 24);
		panel.add(txtrId_3);
		
		JTextArea txtrId_4 = new JTextArea();
		txtrId_4.setText("birthday");
		txtrId_4.setFont(new Font("Dialog", Font.PLAIN, 18));
		txtrId_4.setBounds(51, 580, 181, 24);
		panel.add(txtrId_4);
		
		textField = new JTextField(id);
		textField.setBounds(51, 210, 315, 43);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField(nickname);
		textField_1.setColumns(10);
		textField_1.setBounds(51, 310, 315, 43);
		panel.add(textField_1);
		
		textField_2 = new JTextField(name);
		textField_2.setColumns(10);
		textField_2.setBounds(51, 410, 315, 43);
		panel.add(textField_2);
		
		textField_3 = new JTextField(email);
		textField_3.setColumns(10);
		textField_3.setBounds(51, 510, 315, 43);
		panel.add(textField_3);
		
		textField_4 = new JTextField(birthday);
		textField_4.setColumns(10);
		textField_4.setBounds(51, 610, 315, 43);
		panel.add(textField_4);
		
		textField.setEditable(false);	
		textField_1.setEditable(false);	
		textField_2.setEditable(false);	
		textField_3.setEditable(false);	
		textField_4.setEditable(false);	
		
		setVisible(true);
		
	}
}