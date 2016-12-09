import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import javafx.collections.*;

// NOTE ResultTable is deprecated; can now be dropped from DB.

/**
 * Created by Robert Russell
 *
 */
public class Connect {

	/** IPv4 Address of the Server. */
	private static final String Server = "68.0.192.250";

	/** Port for the Database Server. */
	private static final String port = "53978";

	/** Username for the Database. */
	private static final String user = "bahama";

	/** Password for the Database. */
	private static final String password = "recipro";

	/** Name of the Database. */
	private static final String database = "ReciProDB";

	/** Establishes a Connection with the Database. */
	private static Connection connection;

	/** Prepares queries to the Database. */
	private static Statement statement;

	/** Results of a query to the Database. */
	private static ResultSet set;

	/** URL for connecting to the Database. */
	private static String jdbcurl = "jdbc:sqlserver://" + Server + ":" + port + ";databaseName=" + database + ";user=" + user
			+ ";password=" + password;

	/**
	 * Returns an ObservableList containing all Recipes in the Database.
	 * @throws SQLException
	 */
	public static ObservableList<Recipe> getAll() throws SQLException {
		ObservableList<Recipe> results = FXCollections.observableArrayList();

		try {
			connector();

			final String SQL = "SELECT * FROM MasterTable";
			statement = connection.createStatement();
			set = statement.executeQuery(SQL);

			while (set.next()) {
				// NOTE How do we retrieve also the record's index in the DB?
				Recipe r = new Recipe(set.getString(1), set.getString(2), set.getString(3));
				results.addAll(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return results;
	}

	/**
	 * Returns an ObservableList containing Recipes from the Database
	 * that are like given Keywords in a String.
	 * @param s
	 * @throws SQLException
	 */
	public static ObservableList<Recipe> getByKeyword(String s) throws SQLException {
		ObservableList<Recipe> results = FXCollections.observableArrayList();

		final ArrayList<String> keywords = new ArrayList<>();
        for (String keyword : s.split(" ")) {
        	keywords.add(keyword);
        }

		try {
			connector();

			String SQL = "SELECT DISTINCT * FROM MasterTable WHERE ";

			for (int i = 0; i < keywords.size(); i++) {
				if (i == 0) {
					SQL += "(dishName LIKE '%" + keywords.get(i) + keywords.get(i) + "%' OR recipe LIKE '%" + keywords.get(i) + "%')";
				} else {
					SQL += "OR (dishName LIKE '%" + keywords.get(i) + keywords.get(i) + "%' OR recipe LIKE '%" + keywords.get(i) + "%')";
				}
			}

			statement = connection.createStatement();
			set = statement.executeQuery(SQL);

			while (set.next() != false) {
				Recipe r = new Recipe(set.getString(1), set.getString(2), set.getString(3));
				results.addAll(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return results;
	}

	/**
	 * Loads the SQL Server Driver and establishes a Connection.
	 */
	private static void connector() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(jdbcurl);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Calls connector() and then takes Parameters to make a new Entry
	 * in the Database for a new Recipe.
	 * @param name
	 * @param ingredient
	 * @param recipe
	 * @throws SQLException
	 */
	public static void add(String name, String recipe) throws SQLException {
		try {
			connector();

			final String SQL = "INSERT INTO MasterTable (dishName, recipe) VALUES('"+name+"', '"+recipe+"')";
			statement = connection.createStatement();
			statement.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates an existing Recipe by its Index.
	 * @param index
	 * @param name
	 * @param ingredient
	 * @param recipe
	 * @throws SQLException
	 */
	public static void update(String index, String name, String recipe) throws SQLException {
		try {
			connector();

			final String SQL = "UPDATE MasterTable SET dishName='"+name+"', recipe='"+recipe+"' WHERE dishName='"+name+"';";
			statement = connection.createStatement();
			statement.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	* Deletes an existing Recipe by its Index.
	* @param index
	*/
	public static void delete(String index) throws SQLException {
		try {
			connector();

			final String SQL = "DELETE from MasterTable WHERE pId='" + index + "';";
			statement = connection.createStatement();
			statement.executeUpdate(SQL);
		} catch (SQLException e) {
			System.out.println("The record could not be deleted.");
		}
	}
}
