import java.sql.*;

public class Leaderboard {


	public static void main(String[] args) throws SQLException {

	}


	public static void createLeaderboardTable() {

		Statement stmt = null;
		String CreateSql = null;
		String Insertsql = null;

		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4";
		String username = "postgres";
		String password = "anubis512";
		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);
			//           System.out.println("Connected to PostgreSQL server");
			stmt = connection.createStatement();

			CreateSql = "CREATE TABLE int_leaderboard(" +
					"player_id BIGSERIAL PRIMARY KEY , " +
					"player_name VARCHAR(20) NOT NULL," +
					" last_played_at TIMESTAMP DEFAULT NOW(), " +
					"moves INT, " +
					"game_duration INT NOT NULL) ";
			stmt.executeUpdate(CreateSql);

			stmt.close();

			connection.close();
		} catch (SQLException e) {
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

	public static void insertToLeaderboard(String playerName, int moves, int gameDuration) {
		Statement stmt = null;
		String insertSql = null;

		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4";
		String username = "postgres";
		String password = "Student_1234";
		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);
			stmt = connection.createStatement();
			//            System.out.println("Connected to PostgreSQL server");

			insertSql = "INSERT INTO int_leaderboard as il (player_name,moves,game_duration) " +
					"VALUES('" + playerName + "','" + moves + "','" + gameDuration + "'";

			stmt.executeUpdate(insertSql);

			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

	public static void printTop5Scores() {
		Statement stmt = null;
		String query = null;
		query = "SELECT LPAD(player_name,20,'.'),moves,game_duration FROM int_leaderboard ORDER BY 2,3";

		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4";
		String username = "postgres";
		String password = "Student_1234";
		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);
			//            System.out.println("Connected to PostgreSQL server");
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
