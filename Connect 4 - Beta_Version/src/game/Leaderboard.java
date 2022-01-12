package game;

import java.sql.*;

public class Leaderboard {

	/**
	 Creates the leaderboard table
	 */
	public static void createLeaderboardTable() {
		Connection connection = Database.getConnection();
		Statement stmt;
		String CreateSql;
		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();
			CreateSql = """
					CREATE TABLE IF NOT EXISTS int_leaderboard(
					    player_id      INTEGER
					        GENERATED ALWAYS AS IDENTITY
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
		}
	}

	/**
	 Inserts into the Leaderboard table

	 @param playerName   <code>String</code>
	 @param moves        <code>int</code>
	 @param gameDuration <code>int</code>
	 */
	public static void insertToLeaderboard(String playerName, int moves, int gameDuration) {
		Connection connection = Database.getConnection();

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
		}
	}

	/**
	 Prints top 5 Leaderboard scores
	 */
	public static void printTop5Scores() {
		Connection connection = Database.getConnection();
		Statement stmt;
		String query = "SELECT LPAD(player_name,34,'.'),moves,game_duration FROM int_leaderboard ORDER BY 2,3";

		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(query);

			int rank = 1;
			while (rs.next() && rank <= 5) {

				if (rs.isFirst()) {
					System.out.printf("%35s%10s%10s\n", "Name", "Moves", "Duration");
					System.out.println("-".repeat(55));
				}
				System.out.printf("%1d%34s%10d%10d\n", rank, rs.getString(1), rs.getInt("moves"), rs.getInt("game_duration"));
				rank++;
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while displaying top 5 scores.");
		}
	}

	/**
	 Search for a player with a name in the Leaderboard table

	 @param playerName <code>String</code>
	 */
	public static void searchPlayer(String playerName) {
		Connection connection = Database.getConnection();
		Statement stmt;
		String query = " SELECT LPAD(player_name,32,'.'),moves,game_duration " +
				"FROM int_leaderboard " +
				"ORDER BY 2,3";
		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int rank = 1;
			while (rs.next()) {
				if (rs.isFirst()) {

					System.out.printf("%35s%10s%10s\n", "Name", "Moves", "Duration");
					System.out.println("-".repeat(55));
				}
				if (rs.getString(1).contains(playerName))
					System.out.printf("%3d%32s%10d%10d\n", rank, rs.getString(1), rs.getInt("moves"), rs.getInt("game_duration"));
				rank++;
			}

			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while searching for a name in the leaderboard table.");
		}
	}

}
