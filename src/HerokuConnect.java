import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import javafx.collections.*;

/**
 * Created by Dillon Fagan
 * Alternate Connect Class that connects to a PostgreSQL Database
 * hosted by Heroku.
 */
public class HerokuConnect {

	/** Address of the Database Server. */
	private static final String server = "jdbc:postgresql://ec2-107-21-212-175.compute-1.amazonaws.com:5432/d43kddmodnlmhv?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

	/** Database User. */
	private static final String user = "xukqlogkmzbazo";

	/** Database User Password. */
	private static final String password = "e942f135cb3072e6b243c1a12f840d50a0e0eb77986768164dedc95b28dc7d74";

	/** Establishes a Connection with the Database. */
	private static Connection connection;

	/** Prepares queries to the Database. */
	private static Statement statement;

	/** Results of a query to the Database. */
	private static ResultSet set;

	/**
	 * Returns a Recipe given its Index.
	 * @throws SQLException
	 */
	public static Recipe getRecipe(String index) throws SQLException {
		Recipe r = new Recipe();

		try {
			connector();

			final String SQL = "select * from recipes where recipe_index='" + index + "';";
			statement = connection.createStatement();
			set = statement.executeQuery(SQL);

			set.next();
			r = new Recipe(set.getString(1), set.getString(2), set.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * Returns an ObservableList containing all Recipes in the Database.
	 * @throws SQLException
	 */
	public static ObservableList<Recipe> getAll() throws SQLException {
		ObservableList<Recipe> results = FXCollections.observableArrayList();

		try {
			connector();

			final String SQL = "select * from recipes";
			statement = connection.createStatement();
			set = statement.executeQuery(SQL);

			while (set.next()) {
				Recipe r = new Recipe(set.getString("recipe_index"), set.getString("recipe_text"), set.getString("recipe_text"));
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

			String SQL = "select distinct * from recipes where ";

			for (int i = 0; i < keywords.size(); i++) {
				if (i == 0) {
					SQL += "(UPPER(recipe_title) like UPPER('%" + keywords.get(i) + "%') or UPPER(recipe_text) like UPPER( '%" + keywords.get(i) + "%'))";
				} else {
					SQL += "OR (UPPER(recipe_title) like UPPER('%" + keywords.get(i) + "%') or UPPER(recipe_text) like UPPER('%" + keywords.get(i) + "%'))";
				}
			}

			statement = connection.createStatement();
			set = statement.executeQuery(SQL);

			while (set.next()) {
				Recipe r = new Recipe(set.getString("recipe_index"), set.getString("recipe_title"), set.getString("recipe_text"));
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
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(server, user, password);
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
	public static void add(String title, String text) throws SQLException {
		try {
			connector();

			final String SQL = "insert into recipes (recipe_title, recipe_text) values ('" + title + "', '" + text + "')";
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
	public static void update(String index, String title, String text) throws SQLException {
		try {
			connector();

			final String SQL = "update recipes set recipe_title='" + title + "', recipe_text='" + text + "' where recipe_index='" + index + "';";
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

			final String SQL = "delete from recipes where recipe_index='" + index + "';";
			statement = connection.createStatement();
			statement.executeUpdate(SQL);
		} catch (SQLException e) {
			System.out.println("The record could not be deleted.");
		}
	}
}
