package network5;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Dialog extends JFrame {

	private JPanel contentPane;
	
	//Pop up Dialog
	public Dialog(String dialogTitle,String content) {
		int x = 100;
		int y = 100;
		int w = 300;
		int h = 180;
	
		setBounds(x, y, w, h);
		setLocationRelativeTo(null);
		setTitle(dialogTitle);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(255,255,255));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//if click OK button, pop up dispose
		JButton btnNewButton = new JButton("OK");
		btnNewButton.setBounds((int)(w / 2) - 55, (int)(h * 0.5) + 10, 90, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnNewButton);
		
		JLabel ContentLabel = new JLabel(content);
		ContentLabel.setBounds(10, 10, w - 35, (int)(h * 0.4));
		contentPane.add(ContentLabel);
		
		setVisible(true);
		
	}
}
