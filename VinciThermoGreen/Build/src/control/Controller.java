/**
 * @author Jérôme Valenti 
 */
package control;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import database.BDD;
import model.Mesure;
import view.Administration;
import view.ConsoleGUI;
import view.LoginGUI;

/**
 * <p>
 * Le cont&ocirc;lleur :
 * </p>
 * <ol>
 * <li>lit les mesures de température dans un fichier texte</li>
 * <li>retourne la collection des mesures<br />
 * </li>
 * </ol>
 * 
 * @author Jérôme Valenti
 * @version 2.0.0
 *
 */
public class Controller {

	/**
	 * <p>
	 * 		Versino 2.0.0 : Les mesures lues dans le fichier des relevés de températures.
	 * </p>
	 * <p>
	 * 		Versino 3.0.0 : Les mesures lues dans la base de données.
	 * </p>
	 */
	private static ArrayList<Mesure> lesMesures;
	private static ConsoleGUI console;
	private static BDD Bdd;
	private static LoginGUI Login;
	private static Administration Adminis;
	
	public Controller() throws ParseException {

		//lireCSV("data\\mesures.csv");
		lireBDD();
	}
	
	public static void main(String[] args) throws ParseException, SQLException {
		lesMesures = new ArrayList<Mesure>();
		Bdd = new BDD();
		Adminis = new Administration();
		//Construit et affiche l'IHM
		console = new ConsoleGUI();
		console.setLocation(100,100);
		
		Login = new LoginGUI();
		Login.setLocation(100,100);
		//Instancie un contrôleur pour prendre en charge l'IHM
		//control = new Controller();
		Login.setVisible(true);
	
		//Demande l'acquisition des data
		//uneMesure = new Mesure();
		ConsoleGUI.lesMesures = getLesMesures();
				
		//Construit le tableau d'objet
		ConsoleGUI.laTable = ConsoleGUI.setTable(ConsoleGUI.lesMesures);
		
		//Definit le JScrollPane qui va recevoir la JTable
		ConsoleGUI.scrollPane.setViewportView(ConsoleGUI.laTable);
		
		System.out.println("Before set chart in main()");
		//affiche le graphique
		console.setChart();
		System.out.println("After set chart in main()");
		
	}

	/**
	 * <p>Lit un fichier de type CSV (Comma Separated Values)</p>
	 * <p>Le fichier contient les mesures de temp&eacute;rature de la pelouse.</p>
	 * 
	 * @author Jérôme Valenti
	 * @return
	 * @throws ParseException
	 * @since 2.0.0
	 */
	public void lireCSV(String filePath) throws ParseException {

		try {
			File f = new File(filePath);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			try {
				// Chaque ligne est un enregistrement de données
				String records = br.readLine();

				// Chaque enregistrement contient des champs
				String[] fields = null;
				String numZone = null;
				Date horoDate = null;
				float fahrenheit;

				while (records != null) {
					// Affecte les champs de l'enregistrement courant dans un
					// tableau de chaine
					fields = records.split(";");

					// Affecte les champs aux paramètre du constructeur de
					// mesure
					numZone = fields[0];
					horoDate = strToDate(fields[1]);
					fahrenheit = Float.parseFloat(fields[2]);

					// Instancie une Mesure
					Mesure laMesure = new Mesure(numZone, horoDate, fahrenheit);
					lesMesures.add(laMesure);

					// Enregistrement suivant
					records = br.readLine();
				}

				br.close();
				fr.close();
			} catch (IOException exception) {
				System.out.println("Erreur lors de la lecture : " + exception.getMessage());
			}
		} catch (FileNotFoundException exception) {
			System.out.println("Le fichier n'a pas été trouvé");
		}
	}
	
	
	/**
	 * <p>Lit la base de données</p>
	 * <p>Lit la table mesure de la base de données</p>
	 * 
	 * @author Achot Barseghyan
	 * @return
	 * @since 3.0.0
	 */
	public void lireBDD() {
		ResultSet reqAllMesures = null;
		
		try {
			reqAllMesures = BDD.myStmt.executeQuery("select * from mesure where id_stade = 1");
			
			while (reqAllMesures.next()) {
				float temperatures = reqAllMesures.getFloat("Temperature");
				String zone =  reqAllMesures.getString("zone");
				Date dateHeure =  reqAllMesures.getDate("dateHeure");

				// Instancie une Mesure
				Mesure laMesure = new Mesure(zone, dateHeure, temperatures);
				lesMesures.add(laMesure);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * <p>Lit la base de données</p>
	 * <p>Lit la table mesure de la base de données</p>
	 * 
	 * @author Achot Barseghyan
	 * @return
	 * @since 3.0.0
	 */
	public void updateData(Object selected_stade) {
		ResultSet reqAllMesures = null;
		
		//selected_stade = selected_stade.toString();
		
		try {
			reqAllMesures = Bdd.myStmt.executeQuery("select * from mesure"
					+ " inner join stade on stade.id_stade = mesure.id_stade"
					+ " where stade.nom_stade ='"+selected_stade+"'");
			
			lesMesures.clear();
			while (reqAllMesures.next()) {
				float temperatures = reqAllMesures.getFloat("temperature");
				String zone =  reqAllMesures.getString("zone");
				Date dateHeure =  reqAllMesures.getDate("dateHeure");

				// Instancie une Mesure
				Mesure laMesure = new Mesure(zone, dateHeure, temperatures);
				lesMesures.add(laMesure);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	

	/**
	 * <p>
	 * Filtre la collection des mesures en fonction des param&egrave;tres :
	 * </p>
	 * <ol>
	 * <li>la zone (null = toutes les zones)</li>
	 * <li>la date de d&eacute;but (null = &agrave; partir de l'origine)</li>
	 * <li>la date de fin (null = jusqu'&agrave; la fin)<br />
	 * </li>
	 * </ol>
	 */
	// public void filtrerLesMesure(String laZone, Date leDebut, Date lafin) {
	public ArrayList<Mesure> filtrerLesMesure(String laZone) {
		// Parcours de la collection
		// Ajout à laSelection des objets qui correspondent aux paramètres
		// Envoi de la collection
		ArrayList<Mesure> laSelection = new ArrayList<Mesure>();
		for (Mesure mesure : lesMesures) {
			if (laZone.compareTo("*") == 0) {
				laSelection.add(mesure);
			} else {
				if (laZone.compareTo(mesure.getNumZone()) == 0) {
					laSelection.add(mesure);
				}
			}
		}
		return laSelection;
	}

	/**
	 * <p>
	 * Retourne la collection des mesures
	 * </p>
	 * 
	 * @return ArrayList<Mesure>
	 */
	public static ArrayList<Mesure> getLesMesures() {

		return lesMesures;
	}

	/**
	 * <p>Convertion d'une String en Date</p>
	 * 
	 * @param strDate
	 * @return Date
	 * @throws ParseException
	 */
	private Date strToDate(String strDate) throws ParseException {

		SimpleDateFormat leFormat = null;
		Date laDate = new Date();
		leFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		laDate = leFormat.parse(strDate);
		return laDate;
	}
	
	public void callPasswordChecker() throws SQLException {
		
		if (Login.getCorrectPassword()) {
			Login.setVisible(false);
			Login.dispose();
			console.setVisible(true);
		}
		
	}
	
	public void disconnectUser() {
		console.setVisible(false);
		console.dispose();
		Login.setVisible(true);
	}
	
	public boolean goToAdministration() {
		if (BDD.getCurrentRole().equals("admin")) {
			Adminis.setVisible(true);
			return true;
		}else {
			return false;
		}
		
	}
	
	public void closeAdministration() {
		Adminis.dispose();
	}
	
}
