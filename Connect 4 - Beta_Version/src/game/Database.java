package game;

import java.sql.*;
import java.util.Scanner;

public class Database {
	private static final String jdbc = "jdbc:postgresql://localhost:5432/connect4database";
	private static final String username = "postgres";
	protected static String password = "Student_1234";


	/**
	 Asks for postgres password and saves it in class variable password
	 <br>
	 Creates the database in case it doesn't exist
	 */
	public static void createDatabase() {
		String jdbc = "jdbc:postgresql://localhost/";
		Scanner scanner = new Scanner(System.in);
		String pw;

		Utilities.printNewScreen();
		System.out.printf("Please enter your postgres password (Default = %s): ", password);

		pw = scanner.nextLine().trim();

		if (!pw.equals("")) {   // keep default password if no password was entered
			password = pw;
		}

		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);
			Statement statement = connection.createStatement();

			String sql = "SELECT datname FROM pg_database WHERE datname = 'connect4database';";

			ResultSet rs = statement.executeQuery(sql);

			if (!rs.next()) { // when resultSet is empty
				sql = "CREATE DATABASE connect4database";
				statement.executeUpdate(sql);
			}

		} catch (SQLException e) {
			System.out.println("Error while creating the database");
			System.exit(1);
		}
	}

	/**
	 Sets up postgres server connection

	 @return <code>Connection</code>
	 */
	public static Connection getConnection() {

		try {
			return DriverManager.getConnection(jdbc, username, password);

		} catch (SQLException e) {
			System.out.println("Error while creating the connection to the postgres");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 Drops all sequences and tables to reset the entire database and start from scratch
	 */
	public static void deleteData() {
		Connection connection = Database.getConnection();
		Statement stmt;
		try {
			assert connection != null;
			stmt = connection.createStatement();

			String truncSql = """
					TRUNCATE TABLE int_gamesession CASCADE;
					TRUNCATE TABLE int_leaderboard RESTART IDENTITY;
										
					ALTER SEQUENCE player_id_seq RESTART;
					ALTER SEQUENCE game_id_seq RESTART;
					""";

			stmt.executeUpdate(truncSql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while trying to drop all tables and sequences.");
		}
	}
}
