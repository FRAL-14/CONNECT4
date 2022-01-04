package game;

import java.sql.*;

public class Leaderboard {

	public static Connection getConnection() {
		//		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4 Database";
		String jdbc = "jdbc:postgresql://localhost:5432/Connect4";
		String username = "postgres";
		//		String password = "Student_1234";
		String password = "anubis512";

		try {
			return DriverManager.getConnection(jdbc, username, password);

		} catch (SQLException e) {
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
					name VARCHAR(20) NOT NULL,
					moves INT,
					game_duration INT
					)
					""";
			stmt.executeUpdate(CreateSql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error in connection to PostgreSQL server");
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
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

	/**
	 Prints top 5 Leaderboard scores
	 */
	public static void printTop5Scores() {
		Connection connection = getConnection();
		Statement stmt;
		String query = "SELECT LPAD(name,20,'.'),moves,game_duration FROM int_leaderboard ORDER BY 2,3";

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
			System.out.println("Error in connection to PostgreSQL server");
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
		String query = " SELECT LPAD(name,20,'.'),moves,game_duration " +
				"FROM int_leaderboard " +
				"WHERE UPPER(name) = '" + playerName.toUpperCase() + "' " +
				"ORDER BY 2,3";

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
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

}
