package network5;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

public class signUp extends JFrame{

	private JTextField IDInput;
	private JPasswordField PWDInput;
	private JButton signUpbtn;
	private JTextArea signUP;
	private JTextField nickNameInput;
	private JTextField emailInput;
	private JTextField phoneInput;
	static Connection con;
	private String[] yearContent = {"Year", "down", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", 
			"2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "up"};
	private String[] monthContent = {"Month", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	private String[] dayContent = {"Day", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30","31"};
	private JTextField nameInput;
	private JComboBox<String> yearBox = new JComboBox<>(yearContent);
	private JComboBox<String> monthBox = new JComboBox<>(monthContent);
	private JComboBox<String> dayBox = new JComboBox<>(dayContent);

	public signUp() {
		initialize();
	}

	//Initialize the contents of the frame.
	private void initialize() {
		setBounds(100, 100, 469, 867);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 0, 455, 830);
		panel.setLayout(null);
		panel.setVisible(true);
		setContentPane(panel);
		
		JTextArea txtSignUp = new JTextArea();
		txtSignUp.setFont(new Font("HY以묎퀬�뵓", Font.BOLD, 30));
		txtSignUp.setText("SIGN UP");
		txtSignUp.setBounds(12, 22, 242, 58);
		txtSignUp.setEditable(false);
		panel.add(txtSignUp);
		
		JTextArea txtId = new JTextArea();
		txtId.setText("ID");
		txtId.setFont(new Font("HY以묎퀬�뵓", Font.PLAIN, 25));
		txtId.setBounds(62, 108, 141, 27);
		txtId.setEditable(false);
		panel.add(txtId);
		
		IDInput = new JTextField();
		IDInput.setBounds(62, 139, 305, 44);
		panel.add(IDInput);
		IDInput.setColumns(10);
		
		JTextArea txtPassword = new JTextArea();
		txtPassword.setText("PASSWORD");
		txtPassword.setFont(new Font("HY以묎퀬�뵓", Font.PLAIN, 25));
		txtPassword.setBounds(61, 193, 162, 27);
		txtPassword.setEditable(false);
		panel.add(txtPassword);
		
		PWDInput = new JPasswordField();
		PWDInput.setEchoChar('*');
		PWDInput.setColumns(10);
		PWDInput.setBounds(62, 225, 305, 44);
		panel.add(PWDInput);
		
		JTextArea txtNickName = new JTextArea();
		txtNickName.setText("NICKNAME");
		txtNickName.setFont(new Font("HY以묎퀬�뵓", Font.PLAIN, 25));
		txtNickName.setBounds(62, 279, 141, 27);
		txtNickName.setEditable(false);
		panel.add(txtNickName);
		
		JTextArea txtEmail = new JTextArea();
		txtEmail.setText("E-MAIL");
		txtEmail.setFont(new Font("HY以묎퀬�뵓", Font.PLAIN, 25));
		txtEmail.setBounds(62, 455, 141, 27);
		txtEmail.setEditable(false);
		panel.add(txtEmail);
		
		JTextArea txtBirthday = new JTextArea();
		txtBirthday.setText("BIRTHDAY");
		txtBirthday.setFont(new Font("HY以묎퀬�뵓", Font.PLAIN, 25));
		txtBirthday.setBounds(62, 546, 141, 27);
		txtBirthday.setEditable(false);
		panel.add(txtBirthday);
		
		JTextArea txtPhone = new JTextArea();
		txtPhone.setText("PHONE NUMBER");
		txtPhone.setFont(new Font("HY以묎퀬�뵓", Font.PLAIN, 25));
		txtPhone.setBounds(62, 637, 227, 27);
		txtPhone.setEditable(false);
		panel.add(txtPhone);
		
		nickNameInput = new JTextField();
		nickNameInput.setColumns(10);
		nickNameInput.setBounds(62, 310, 305, 44);
		panel.add(nickNameInput);
		
		emailInput = new JTextField();
		emailInput.setColumns(10);
		emailInput.setBounds(62, 492, 305, 44);
		panel.add(emailInput);
		
		phoneInput = new JTextField();
		phoneInput.setColumns(10);
		phoneInput.setBounds(61, 674, 305, 44);
		panel.add(phoneInput);
		
		yearBox.setBackground(Color.WHITE);
		yearBox.setBounds(62, 583, 92, 34);
		panel.add(yearBox);
		
		monthBox.setBackground(Color.WHITE);
		monthBox.setBounds(171, 583, 92, 34);
		panel.add(monthBox);
		
		dayBox.setBackground(Color.WHITE);
		dayBox.setBounds(275, 583, 92, 34);
		panel.add(dayBox);
		
		signUpbtn = new JButton("Sign up");
		signUpbtn.setFont(new Font("Dialog", Font.PLAIN, 19));
		signUpbtn.setBackground(new Color(67, 173, 192));
		signUpbtn.setForeground(Color.WHITE);
		signUpbtn.setBounds(130, 758, 157, 44);
		signUpbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IDInput.getText().trim().length()==0) {
					JOptionPane.showMessageDialog(null, "Please enter ID", "sign up", JOptionPane.WARNING_MESSAGE);
					IDInput.grabFocus();
				}
				else if(PWDInput.getText().trim().length()==0) {
					JOptionPane.showMessageDialog(null, "Please enter password", "sign up", JOptionPane.WARNING_MESSAGE);
					PWDInput.grabFocus();
				}
				else if(nickNameInput.getText().trim().length()==0) {
					JOptionPane.showMessageDialog(null, "Please enter nickname", "sign up", JOptionPane.WARNING_MESSAGE);
					nickNameInput.grabFocus();
				}
				else if(nameInput.getText().trim().length()==0) {
					JOptionPane.showMessageDialog(null, "Please enter name", "sign up", JOptionPane.WARNING_MESSAGE);
					nickNameInput.grabFocus();
				}
				else if(emailInput.getText().trim().length()==0) {
					JOptionPane.showMessageDialog(null, "Please enter email", "sign up", JOptionPane.WARNING_MESSAGE);
					nickNameInput.grabFocus();
				}
				else if(yearBox.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please enter year of birthday", "sign up", JOptionPane.WARNING_MESSAGE);
					yearBox.grabFocus();
				}
								
				else if(monthBox.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please enter month of birthday", "sign up", JOptionPane.WARNING_MESSAGE);
					monthBox.grabFocus();
				}
				
				else if(dayBox.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please enter day of birthday", "sign up", JOptionPane.WARNING_MESSAGE);
					dayBox.grabFocus();
				}
				
				else {
					//password encryption
					String tmp = null;
					try {
						tmp = sha256(PWDInput.getText().trim());
					} catch (NoSuchAlgorithmException e1) {
						e1.printStackTrace();
					}
					String birth = yearBox.getSelectedItem().toString() +"-"+monthBox.getSelectedItem().toString() + "-" + dayBox.getSelectedItem().toString();
					int result = 0;
					Connection con = Method.GetCon();
					//if phone number not entered
					if(phoneInput.getText().trim().length()==0)
						result = Method.Signin(con, IDInput.getText().trim(), tmp, nickNameInput.getText().trim(), nameInput.getText().trim(),emailInput.getText().trim(), birth, null);
					//if phone number entered 
					else
						result = Method.Signin(con, IDInput.getText().trim(), tmp, nickNameInput.getText().trim(), nameInput.getText().trim(),emailInput.getText().trim(), birth, phoneInput.getText().trim());
					if(result == 0)
						JOptionPane.showMessageDialog(null, "sign up success", "sign up", JOptionPane.WARNING_MESSAGE);
					else if(result == 1) {
						JOptionPane.showMessageDialog(null, "sign up failed", "sign up", JOptionPane.WARNING_MESSAGE);
						try {
							//insert user to database
							String q1 = "insert into user_info values(\"" + IDInput.getText().trim() + "\", " + false + ",\"" + "0000-00-00 00:00:00" + "\",\"" + 0 + "\",\"" + "null" + "\");";
							System.out.println(q1);
							Statement stmt = con.createStatement();
							stmt.executeUpdate(q1);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						dispose();
					}
				}
			}
		});
		panel.add(signUpbtn);
		
		JTextArea txtName = new JTextArea();
		txtName.setText("NAME");
		txtName.setFont(new Font("Dialog", Font.PLAIN, 25));
		txtName.setEditable(false);
		txtName.setBounds(62, 364, 141, 27);
		panel.add(txtName);
		
		nameInput = new JTextField();
		nameInput.setColumns(10);
		nameInput.setBounds(62, 401, 305, 44);
		panel.add(nameInput);
		
		setVisible(true);
	}
	
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
