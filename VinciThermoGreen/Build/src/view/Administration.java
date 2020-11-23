package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import control.Controller;
import database.BDD;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class Administration extends JFrame {
	
	private JPanel contentPane;
	private JTable tableUsers;
	private Controller control;	
	private BDD Bdd;
	private JTextField userNameAdd;
	private JPasswordField passwordFieldAdd;
	private String[][] tab;
	DefaultTableModel model;
	
	/**
	 * Create the frame.
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	public Administration() throws ParseException, SQLException {
		
		control = new Controller();
		Bdd = new BDD();
		
		setTitle("VTG - Administration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 762, 617);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(128, 128, 128)));
		panel.setBounds(24, 23, 298, 503);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Utilisateurs");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(107, 11, 76, 14);
		panel.add(lblNewLabel); 
		
		model = new DefaultTableModel();
		tableUsers = new JTable(model);
		model.addColumn("ID");
		model.addColumn("NOM");
		
		//adding the users from the database to the JTable
		getUsersFromDatabase();
		tableUsers.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableUsers.setBorder(new LineBorder(new Color(128, 128, 128)));
		tableUsers.setBackground(UIManager.getColor("Button.background"));
		tableUsers.setBounds(10, 38, 278, 423);
		panel.add(tableUsers);
		
		JButton btnSupprimer = new JButton("Supprimer");
		btnSupprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int column = 0;
				int row = tableUsers.getSelectedRow();
				String value = tableUsers.getModel().getValueAt(row, column).toString();
				Bdd.removeUserfromDatabase(value);
				model.setRowCount(0);
				try {
					getUsersFromDatabase();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSupprimer.setForeground(new Color(128, 0, 0));
		btnSupprimer.setBounds(92, 472, 111, 23);
		panel.add(btnSupprimer);
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				control.closeAdministration();
			}
		});
		btnQuitter.setForeground(new Color(128, 0, 0));
		btnQuitter.setBounds(647, 11, 89, 23);
		contentPane.add(btnQuitter);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(128, 128, 128)));
		panel_1.setBounds(360, 23, 250, 225);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JComboBox<String> comboBoxRole = new JComboBox<String>();
		comboBoxRole.setBounds(28, 145, 192, 23);
		panel_1.add(comboBoxRole);
		comboBoxRole.addItem("user");
		comboBoxRole.addItem("Admin");
		
		JButton btnAddUsers = new JButton("Ajouter");
		btnAddUsers.setBounds(62, 191, 136, 23);
		panel_1.add(btnAddUsers);
		btnAddUsers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				boolean verif = Bdd.addUserToDataBase(userNameAdd.getText(), passwordFieldAdd.getText().toString(), comboBoxRole.getSelectedItem().toString());
				if (verif) {
					
//					//JPP : DU COUP FAUT FINIR : TU DOIS FOUND L'ID DE LA DERNIERE LIGNE POUR AFFICHER DANS LE TABLEAU
//					int tabLength = tab.length;
//					String lastID = tab[tabLength-1][0];
//					System.out.println(lastID);
//					model.addRow(new Object[] {lastID, userNameAdd.getText() });
					try {
						model.setRowCount(0);
						userNameAdd.setText("");
						passwordFieldAdd.setText("");
						getUsersFromDatabase();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}else {
					JOptionPane.showMessageDialog(contentPane,
						    "L'insertion des données n'a pas été effectués");
				}
			}
		});
		btnAddUsers.setForeground(new Color(0, 128, 0));
		
		JLabel lblAjouterUnUtilisateur = new JLabel("Ajouter un utilisateur");
		lblAjouterUnUtilisateur.setHorizontalAlignment(SwingConstants.CENTER);
		lblAjouterUnUtilisateur.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAjouterUnUtilisateur.setBounds(56, 11, 142, 14);
		panel_1.add(lblAjouterUnUtilisateur);
		
		userNameAdd = new JTextField();
		userNameAdd.setBounds(28, 56, 192, 23);
		panel_1.add(userNameAdd);
		userNameAdd.setColumns(10);
		
		passwordFieldAdd = new JPasswordField();
		passwordFieldAdd.setBounds(28, 111, 193, 23);
		panel_1.add(passwordFieldAdd);
		
		JLabel lblNom = new JLabel("Nom :");
		lblNom.setBounds(29, 36, 46, 14);
		panel_1.add(lblNom);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe :");
		lblMotDePasse.setBounds(28, 90, 113, 14);
		panel_1.add(lblMotDePasse);
		
		JLabel lblSelectionnez = new JLabel("Remarque : Selectionnez un utilisateur dans la table et ensuite cliquez sur le button supprimer");
		lblSelectionnez.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblSelectionnez.setBounds(24, 553, 423, 14);
		contentPane.add(lblSelectionnez);
	}
	
	public void getUsersFromDatabase() throws SQLException {
		
		tab = Bdd.getAllUsers();
		
		//Parcours lignes et colonnes pour les deux for, respectivement
		for(int i=0; i<tab.length; i++)
		{
			String tab0 = tab[i][0];
			String tab1 = tab[i][1];
			model.addRow(new Object[] { tab0, tab1});

		}
		
	}
}
