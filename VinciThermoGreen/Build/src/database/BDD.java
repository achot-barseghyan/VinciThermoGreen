package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

public class BDD {

	public static Connection myConn = null;
	public static Statement myStmt = null;
	static String currentUserRole;

	public BDD() {

		// DATABASE
		try {

			myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/vincithermogreen?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC",
					"root", "");

			myStmt = myConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		} catch (SQLException e) {

		}

	}

	public String getPasswordfrom(String username) throws SQLException {

		ResultSet reqUser = null;
		reqUser = myStmt.executeQuery("select * from user where login='" + username + "'");
		reqUser.next();
		String password = reqUser.getString("password");
		currentUserRole = reqUser.getString("role");
		return (password);
	}

	public String[] getAllStades() throws SQLException {
		ResultSet reqStade = null;
		reqStade = myStmt.executeQuery("select * from Stade");
		reqStade.last();
		int row = reqStade.getRow();
		reqStade.first();
		String[] lesStades = new String[row];

		int i = 0;
		while (reqStade.next()) {
			lesStades[i] = reqStade.getString("nom_stade");
			i++;
		}

		return lesStades;

	}

	public String[][] getAllUsers() throws SQLException {
		ResultSet reqUsers = null;
		reqUsers = myStmt.executeQuery("select id_user,login from user");
		reqUsers.last();
		int row = reqUsers.getRow();
		reqUsers.first();

		String[][] lesUsers = new String[row][2];
		System.out.println(row);

		int i = 0;
		while (reqUsers.next()) {
			lesUsers[i][0] = reqUsers.getString("id_user");
			lesUsers[i][1] = reqUsers.getString("login");
			System.out.println(Arrays.toString(lesUsers[i]));
			i++;
		}

		return lesUsers;
	}

	public boolean addUserToDataBase(String userName, String passWord, String role) {
		String hashed = BCrypt.hashpw(passWord, BCrypt.gensalt());
		String query = "INSERT INTO user(login,password,role) VALUES('" + userName + "','" + hashed +"','"+ role +"')";

		try {
			myStmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean removeUserfromDatabase(String ID) {

		String query = "DELETE FROM user WHERE id_user =" + ID;

		try {
			myStmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	public static String getCurrentRole() {
		return currentUserRole;
		
	}

}
