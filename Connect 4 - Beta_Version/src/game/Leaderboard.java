package game;

import java.sql.*;
import java.util.Scanner;

public class Leaderboard {

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

		Banners.printNewScreen();
		System.out.print("Please enter your postgres password (Default = Student_1234): ");

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
	 Creates the leaderboard table
	 */
	public static void createLeaderboardTable() {
		Connection connection = getConnection();
		Statement stmt;
		String CreateSql;
		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();
			CreateSql = """
					CREATE TABLE IF NOT EXISTS int_leaderboard(
					    player_id      BIGSERIAL
					        CONSTRAINT int_leaderboard_pkey
					            PRIMARY KEY,
					    player_name    VARCHAR(20) NOT NULL,
					    last_played_at TIMESTAMP DEFAULT NOW( ),
					    moves          INTEGER,
					    game_duration  INTEGER     NOT NULL);
					""";
			stmt.executeUpdate(CreateSql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while creating Leaderboard table.");
			e.printStackTrace();
		}
	}

	/**
	 Inserts into the Leaderboard table

	 @param playerName   <code>String</code>
	 @param moves        <code>int</code>
	 @param gameDuration <code>int</code>
	 */
	public static void insertToLeaderboard(String playerName, int moves, int gameDuration) {
		Connection connection = getConnection();

		try {
			assert connection != null : "Connection is null";
			String sql = """
					INSERT INTO int_leaderboard (player_name,moves,game_duration)
					VALUES (?,?,?)
					""";

			PreparedStatement pstmt = connection.prepareStatement(sql);

			pstmt.setString(1, playerName);
			pstmt.setInt(2, moves);
			pstmt.setInt(3, gameDuration);

			pstmt.executeUpdate();
			pstmt.close();
			connection.close();

		} catch (SQLException e) {
			System.out.println("Error while inserting into leaderboard table.");
			e.printStackTrace();
		}
	}

	/**
	 Prints top 5 Leaderboard scores
	 */
	public static void printTop5Scores() {
		Connection connection = getConnection();
		Statement stmt;
		String query = "SELECT LPAD(player_name,20,'.'),moves,game_duration FROM int_leaderboard ORDER BY 2,3";

		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(query);

			int rank = 1;
			while (rs.next() && rank <= 5) {

				if (rs.isFirst()) {
					System.out.printf("  \t\t%s  \t\t\t%s      %s\n", "Name", "Moves", "Time(seconds)");
					System.out.println("-".repeat(45));
				}
				System.out.printf("%d%s \t  %d \t\t\t%d \n", rank, rs.getString(1), rs.getInt("moves"), rs.getInt("game_duration"));
				rank++;
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while displaying top 5 scores.");
			e.printStackTrace();
		}
	}

	/**
	 Search for a player with a name in the Leaderboard table

	 @param playerName <code>String</code>
	 */
	public static void searchPlayer(String playerName) {
		Connection connection = getConnection();
		Statement stmt;
		String query = " SELECT LPAD(player_name,20,'.'),moves,game_duration " +
				"FROM int_leaderboard " +
				"ORDER BY 2,3";
		//TODO weird double nextLine() ???
		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int rank = 1;
			while (rs.next()) {
				if (rs.isFirst()) {

					//FIXME: no use of tabs (different sizes in different terminals
					System.out.printf("  \t\t%s  \t\t\t%s      %s\n", "Name", "Moves", "Time(seconds)");
					System.out.println("-".repeat(45));
				}
				if (rs.getString(1).contains(playerName))
					System.out.printf("%d%s \t  %d \t\t\t%d \n", rank, rs.getString(1), rs.getInt("moves"), rs.getInt("game_duration"));
				rank++;
			}

			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while searching for a name in the leaderboard table.");
			e.printStackTrace();
		}
	}

}
