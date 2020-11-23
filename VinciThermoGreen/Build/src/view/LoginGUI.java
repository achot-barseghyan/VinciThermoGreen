package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.mindrot.jbcrypt.BCrypt;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import control.Controller;
import database.BDD;
import sun.invoke.empty.Empty;

import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Label;
import javax.swing.JTextArea;
import java.awt.Button;


public class LoginGUI extends JFrame {
	
	private static BDD Bdd;
	private Controller control;
	private JPanel contentPane;
	private JTextField txtNomDutilisateur;
	private JPasswordField passwordField;
	public static String userName;
	public  String password;
	String strpass;
	ResultSet reqUser = null;
	boolean reqExecuted = false;
	private JTextField info_connection;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	
	/**
	 * Create the frame.
	 * @throws ParseException 
	 */
	public LoginGUI() throws ParseException {
		control = new Controller();
		Bdd = new BDD();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1020, 720);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(30, 144, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton submitbtn = new JButton("Se connecter");
		submitbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				 userName = txtNomDutilisateur.getText();
				 password = passwordField.getText();
				 strpass = password.toString();
				 try {
					control.callPasswordChecker();
				} catch (SQLException e1) {
					e1.printStackTrace();
					info_connection.setVisible(true);
				}
				 reqExecuted = true;
			}
		});
		submitbtn.setToolTipText("Se connecter");
		submitbtn.setForeground(SystemColor.textHighlight);
		submitbtn.setBackground(UIManager.getColor("FormattedTextField.selectionBackground"));
		submitbtn.setBounds(424, 487, 147, 54);
		contentPane.add(submitbtn);
		
		
		txtNomDutilisateur = new JTextField();
		txtNomDutilisateur.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtNomDutilisateur.setHorizontalAlignment(SwingConstants.LEFT);
		txtNomDutilisateur.setForeground(new Color(51, 153, 255));
		txtNomDutilisateur.setToolTipText("Nom D'utilisateur");
		txtNomDutilisateur.setBounds(315, 268, 372, 63);
		contentPane.add(txtNomDutilisateur);
		txtNomDutilisateur.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
		        	if (key == KeyEvent.VK_ENTER) {	             
						 userName = txtNomDutilisateur.getText();
						 password = passwordField.getText();
						 try {
								control.callPasswordChecker();
							} catch (SQLException e1) {
								e1.printStackTrace();
								info_connection.setVisible(true);
							}
		        	}
			}
		});
		passwordField.setForeground(SystemColor.textHighlight);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setToolTipText("Mot de Passe");
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setBounds(315, 371, 372, 63);
		contentPane.add(passwordField);
		
		JLabel lblNewLabel = new JLabel("Nom D'utilisateur");
		lblNewLabel.setForeground(SystemColor.textHighlight);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(315, 243, 108, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Mot de Passe");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1.setForeground(SystemColor.textHighlight);
		lblNewLabel_1.setBounds(315, 346, 108, 14);
		contentPane.add(lblNewLabel_1);
		
		info_connection = new JTextField();
		info_connection.setHorizontalAlignment(SwingConstants.CENTER);
		info_connection.setForeground(new Color(128, 0, 0));
		info_connection.setText("Nom d'utilisateur ou MDP incorrect");
		info_connection.setEditable(false);
		info_connection.setBackground(UIManager.getColor("Button.background"));
		info_connection.setBounds(315, 445, 372, 20);
		contentPane.add(info_connection);
		info_connection.setColumns(10);
		info_connection.setBorder(emptyBorder);
		info_connection.setVisible(false);
		
	}
	
	public String getUsername() {
		return userName;
	}
	
	public String getPassword() {
		return strpass;
	}
	
	public boolean getCorrectPassword() throws SQLException {
		
		String correcPass = Bdd.getPasswordfrom(userName);

		if (BCrypt.checkpw(password, correcPass)) {
			txtNomDutilisateur.setText(null);
			passwordField.setText(null);
			info_connection.setVisible(false);
			return true;
		}else {
			info_connection.setVisible(true);
			return false;
		}	
			
	}
}
