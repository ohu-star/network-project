package network5;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

//pop up search
public class search extends JFrame {

	private JPanel contentPane;
	private JTextField txtTypeUserId;


	public search(Socket socket, String id) {
		setBounds(100, 100, 469, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 455, 763);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		panel_2.setBounds(116, 27, 315, 60);
		panel.add(panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
		
		
		ImageIcon searchImage = new ImageIcon("search.png");
		Image sImg = searchImage.getImage();
		Image scImg = sImg.getScaledInstance(46, 45, Image.SCALE_SMOOTH);
		ImageIcon cSearchIcon = new ImageIcon(scImg);
		
		JLabel lblNewLabel = new JLabel(cSearchIcon);
		lblNewLabel.setBounds(70, 37, 46, 45);
		panel.add(lblNewLabel);
		
		txtTypeUserId = new JTextField();
		txtTypeUserId.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtTypeUserId.setText("type user ID of nickname");
		txtTypeUserId.setBackground(new Color(247, 247, 247));
		txtTypeUserId.setPreferredSize(new Dimension(274, 40));
		panel_2.add(txtTypeUserId);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(panel_1);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 79, 464, 682);
		contentPane.add(scrollPane);
		
		Friend[] users = Method.GetUsers(Method.GetCon());
		
		//If you search (press enter)
		txtTypeUserId.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//get user's friends
				Friend[] users = Method.GetUsers(Method.GetCon());
				for(int i = 0; i < users.length; i++) {
					System.out.println(users[i].id);
				}
				panel_1.invalidate();
				panel_1.removeAll();
				panel_1.revalidate();
				
				//add friend panel
				for(int i =0;i < users.length; i++) {
					if(users[i].id.contains(txtTypeUserId.getText()) || users[i].nickName.contains(txtTypeUserId.getText())) {
						if(users[i].id.equals(id)) continue;
						FriendPanel up = new FriendPanel(users[i], socket, id);
						panel_1.add(up);
					}
				}
				panel_1.repaint();
			}
		});
		
		
		setVisible(true);
	}
}