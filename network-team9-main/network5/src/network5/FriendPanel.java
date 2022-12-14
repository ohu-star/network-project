package network5;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class FriendPanel extends JPanel implements ActionListener{
	JPanel panel;
	String _id = null;
	public Dimension getMaximumSize() {
		Dimension d = getPreferredSize();
		d.width = Integer.MAX_VALUE;
		
		return d;
	}
	
	//create Friend Panel with friend, socket, friend id
	public FriendPanel(Friend friend, Socket socket, String id) {
		JPanel panel;
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setPreferredSize(new Dimension(455, 65));
		setBounds(0,0,455,75);
		setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0,0,455,65);
		panel.setPreferredSize(new Dimension(455,50));
		panel.setLayout(null);
		add(panel);
		
		//get friend's status(active, inactive) 
		int status = friend.status;
		System.out.println(status);
		ImageIcon fIcon = Method.getStateImage(status);
		System.out.println(fIcon);
		JPanel infoPanel = new JPanel();
		infoPanel.setBounds(80, 15, 158, 40);
		infoPanel.setBackground(new Color(255, 255, 255));
		panel.add(infoPanel);
		FlowLayout fl_infoPanel = new FlowLayout(FlowLayout.LEFT, 5, 5);
		infoPanel.setLayout(fl_infoPanel);
		
		//and add on panel friend's status
		JLabel userStatus = new JLabel(fIcon, JLabel.CENTER);
		userStatus.setBounds(29, 20, 20, 20);
		userStatus.setVerticalTextPosition(JLabel.CENTER);
		userStatus.setHorizontalTextPosition(JLabel.RIGHT);
		panel.add(userStatus);
		
		JLabel userName = new JLabel(friend.nickName);
		userName.setFont(new Font("Thoma", Font.PLAIN, 15));
		userName.setForeground(Color.BLACK);
		infoPanel.add(userName);
		
		String fId = friend.id;
		JLabel ID = new JLabel(fId);
		ID.setVerticalAlignment(SwingConstants.BOTTOM);
		ID.setFont(new Font("Thoma", Font.PLAIN, 10));
		ID.setForeground(Color.GRAY);
		infoPanel.add(ID);
		
		//if add button pushed, register the user as a friend of the logged in user
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Method.registerFriend(fId);
				BufferedReader in = null;
				BufferedWriter out = null;
				String tmp = null;
				try {
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					//flag to add
					out.write("add\n");
					//send the ID of the user to be registered as a friend and the user's ID to the server 
					out.write(fId+"\n");
					out.write(id+"\n");
					out.flush();
					//Upon response from the server
					if((tmp = in.readLine()) != null) {
						int addResult = Integer.parseInt(tmp);
						//If the server sends 1 : Friend registration complete
						if(addResult == 1)
							JOptionPane.showMessageDialog(null, "Friend addition completed", "friend registration", JOptionPane.WARNING_MESSAGE);
						//If the server sends 0 or other : Friend registration failed
						else
							JOptionPane.showMessageDialog(null, "Friend addition failed", "friend registration", JOptionPane.WARNING_MESSAGE);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBackground(new Color(67, 173, 192));
		btnNewButton.setBounds(274, 24, 82, 31);
		panel.add(btnNewButton);
		
		//if click information
		JButton btnInfo = new JButton("Info");
		btnInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnInfo.setBackground(new Color(67, 173, 192));
		btnInfo.setBounds(361, 24, 82, 31);
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Info(friend.id);
			}
		});
		panel.add(btnInfo);
		
		//pop up menu
		//items : chatting, see information
		PopupMenu pm = new PopupMenu("Menu");
		MenuItem item1 = new MenuItem("Chatting");
		item1.addActionListener(this);
		item1.setActionCommand("chatting");
		
		MenuItem item2 = new MenuItem("Information");
		item2.addActionListener(this);
		item2.setActionCommand("information");
		
		pm.add(item1);
		pm.addSeparator();
		pm.add(item2);

		panel.add(pm);
		
		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			//mouse Event listener
			@Override
			public void mousePressed(MouseEvent e) {
				//If you right-click the mouse
				if (e.getButton() == MouseEvent.BUTTON3) {
					 pm.show(panel, e.getX(), e.getY());
				 }
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		switch(command) {
		case "chatting":
			new chatting(_id);
			new chatting(ClientInfo.Logined_id, _id);
			System.out.println("chatting");
			break;
		case "information":
			new Info(_id);
			System.out.println("information");
			break;
		}
	}
	
	//create Friend Panel with friend, socket
	public FriendPanel(Friend friend, Socket socket) {
		JPanel panel;
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setPreferredSize(new Dimension(455, 65));
		setBounds(0,0,455,75);
		setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0,0,455,65);
		panel.setPreferredSize(new Dimension(455,50));
		panel.setLayout(null);
		add(panel);
		
		//get friend's status(active, inactive) 
		int status = friend.status;
		System.out.println(status);
		ImageIcon fIcon = Method.getStateImage(status);
		System.out.println(fIcon);
		JPanel infoPanel = new JPanel();
		infoPanel.setBounds(80, 15, 158, 40);
		infoPanel.setBackground(new Color(255, 255, 255));
		panel.add(infoPanel);
		FlowLayout fl_infoPanel = new FlowLayout(FlowLayout.LEFT, 5, 5);
		infoPanel.setLayout(fl_infoPanel);
		
		//and add on panel friend's status
		JLabel userStatus = new JLabel(fIcon, JLabel.CENTER);
		userStatus.setBounds(29, 20, 20, 20);
		userStatus.setVerticalTextPosition(JLabel.CENTER);
		userStatus.setHorizontalTextPosition(JLabel.RIGHT);
		panel.add(userStatus);
		
		JLabel userName = new JLabel(friend.nickName);
		userName.setFont(new Font("Thoma", Font.PLAIN, 15));
		userName.setForeground(Color.BLACK);
		infoPanel.add(userName);
		
		_id = friend.id;
		JLabel ID = new JLabel(_id);
		ID.setVerticalAlignment(SwingConstants.BOTTOM);
		ID.setFont(new Font("Thoma", Font.PLAIN, 10));
		ID.setForeground(Color.GRAY);
		infoPanel.add(ID);

		//if click information
		JButton btnInfo = new JButton("Info");
		btnInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnInfo.setBackground(new Color(67, 173, 192));
		btnInfo.setBounds(361, 24, 82, 31);
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Info(friend.id);
			}
		});
		panel.add(btnInfo);
		
		//pop up menu
		//items : chatting, see information
		PopupMenu pm = new PopupMenu("Menu");
		MenuItem item1 = new MenuItem("Chatting");
		item1.addActionListener(this);
		item1.setActionCommand("chatting");
		
		MenuItem item2 = new MenuItem("Information");
		item2.addActionListener(this);
		item2.setActionCommand("information");
		
		pm.add(item1);
		pm.addSeparator();
		pm.add(item2);
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				 
			}
		});
		
		panel.add(pm);
		
		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == MouseEvent.BUTTON3) {
					 pm.show(panel, e.getX(), e.getY());
				 }
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		setVisible(true);
	}
	
}
