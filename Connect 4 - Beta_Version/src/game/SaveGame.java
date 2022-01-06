package game;

import java.sql.*;

import static game.Grid.COLUMNS_AMOUNT;
import static game.Grid.ROWS_AMOUNT;
import static game.Leaderboard.getConnection;

/**
 @author Seifeldin Ismail
 @author Peter Buschenreiter */

public class SaveGame {

	/**
	 Creating all tables
	 */
	public static void createAllTables() {
		//          These are for executing queries through jdbc
		Connection connection = getConnection();
		Statement stmt;
		String CreateSql;

		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();

			CreateSql = """
					                  
					CREATE SEQUENCE IF NOT EXISTS player_id_seq AS INT
					INCREMENT BY 1
					START WITH 1
					NO MAXVALUE;
					                        
					CREATE SEQUENCE IF NOT EXISTS game_id_seq AS INT
					START WITH 1
					INCREMENT BY 1
					NO MAXVALUE;

					                        
					CREATE TABLE IF NOT EXISTS int_gamesession(
					    game_id INT PRIMARY KEY,
					    last_played_at TIMESTAMP DEFAULT NOW()
					);
					CREATE TABLE IF NOT EXISTS int_player(
					    game_id INT REFERENCES int_gamesession(game_id) ON DELETE CASCADE,
					    player_id INT PRIMARY KEY,
					    name VARCHAR(20) NOT NULL
					);
					CREATE TABLE IF NOT EXISTS int_score(
					    player_id INT PRIMARY KEY REFERENCES int_player(player_id) ON DELETE CASCADE ,
					    moves INT NOT NULL,
					    game_duration INT NOT NULL
					);
					CREATE TABLE IF NOT EXISTS int_spot(
					    x INT NOT NULL,
					    y INT NOT NULL,
					    sign CHAR(1) DEFAULT NULL,
					    game_id INT REFERENCES int_gamesession(game_id) ON DELETE CASCADE,
					    CONSTRAINT spot_pk PRIMARY KEY (x,y,game_id)
					);
					""";
			stmt.executeUpdate(CreateSql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while trying to create save & load tables.");
			e.printStackTrace();
		}
	}

	/**
	 Saves the game

	 @param playerName <code>String</code>
	 @param moves      <code>int</code>
	 @param duration   <code>long</code>
	 */
	public static void saveGame(String playerName, int moves, int duration, Grid grid) {
		Connection connection = getConnection();
		String insertSql;
		PreparedStatement pstmt;
		PreparedStatement pstmt2;

		try {
			assert connection != null : "Connection is null";

			//			Method checks for previous record of same player name and deletes it if so.
			checkAndDeletePlayerIfExists(playerName);

			pstmt = connection.prepareStatement("""
					INSERT INTO int_gamesession (game_id)
					VALUES (NEXTVAL('game_id_seq'));

					INSERT INTO int_player (player_id,name,game_id)
					VALUES (NEXTVAL('player_id_seq'),?,CURRVAL('game_id_seq'));

					INSERT INTO int_score (player_id, moves, game_duration)
					VALUES (CURRVAL('player_id_seq'),?,?);

					""");
			pstmt.setString(1, playerName);
			pstmt.setInt(2, moves);
			pstmt.setInt(3, duration);
			pstmt.executeUpdate();


			insertSql = """
					INSERT INTO int_spot (x,y,sign,game_id)
					VALUES (?,?,?,CURRVAL('game_id_seq'))
					""";
			pstmt2 = connection.prepareStatement(insertSql);

			for (int row = 0; row < ROWS_AMOUNT; row++) {
				pstmt2.setInt(2, row);
				for (int col = 0; col < COLUMNS_AMOUNT; col++) {
					pstmt2.setInt(1, col);
					if (grid.getSpot(row, col).getCoin() != null) {
						pstmt2.setString(3, String.valueOf(grid.getSpot(row, col).getCoin().getSign()));
					} else {
						pstmt2.setString(3, null);
					}
					pstmt2.executeUpdate();
				}
			}

			connection.close();
			pstmt.close();
			pstmt2.close();
		} catch (SQLException e) {
			System.out.println("Error while trying to save a game.");
			e.printStackTrace();
		}
	}

	/**
	 Checks if a player exists in the save&load db and deletes that player

	 @param playerName <code>String</code>
	 */
	public static void checkAndDeletePlayerIfExists(String playerName) {
		Connection connection = getConnection();
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		try {
			assert connection != null;
			pstmt1 = connection.prepareStatement("SELECT * FROM int_player WHERE name = ?");
			pstmt1.setString(1, playerName);

			ResultSet rs = pstmt1.executeQuery();
			if (rs.next()) {

				pstmt2 = connection.prepareStatement("""
						DELETE FROM int_gamesession
						USING int_player
						WHERE int_gamesession.game_id = int_player.game_id AND int_player.name = ?
						""");
				pstmt2.setString(1, playerName);
				pstmt2.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Error while trying to delete a saved game.");
			e.printStackTrace();
		}
	}

	/**
	 Displays names, moves and durations of available games to load.

	 @return <code>boolean</code>: <code>True</code> if at least 1 game available to load, otherwise <code>False</code>
	 */
	public static boolean printSavedGames() {
		Connection connection = getConnection();
		Statement stmt;
		String presentQuery = """
				         SELECT name, moves, game_duration
				         FROM int_player JOIN int_score USING (player_id)
				         ORDER BY 1;
				""";
		try {
			assert connection != null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(presentQuery);

			if (!rs.next()) {
				return false;
			}
			System.out.printf("%35s%10s%10s\n", "Name", "Moves", "Duration");
			System.out.println("-".repeat(55));
			do {
				System.out.printf("%35s%10d%10d\n", rs.getString(1), rs.getInt(2), rs.getInt(3));
			} while (rs.next());
			System.out.println("-".repeat(55));

		} catch (SQLException e) {
			System.out.println("Error while displaying saved games.");
			e.printStackTrace();
		}
		return true;
	}


	/**
	 Loads a saved game if it exists

	 @param name <code>String</code>: name of the player

	 @return <code>GameSession</code> or <code>null</code> if no game is available to load under that name
	 */
	public static GameSession loadGame(String name) {
		PlayerCPU playerCPU = null;
		PlayerHuman playerHuman = null;
		Grid grid = null;

		Connection connection = getConnection();
		ResultSet playerQuery;
		PreparedStatement pstmt;

		String searchQuery = """
				SELECT name,game_duration,x,y,sign,moves
				FROM int_player
				JOIN int_spot USING (game_id)
				JOIN int_score USING (player_id)
				WHERE name=?
				""";

		try {
			assert connection != null;
			pstmt = connection.prepareStatement(searchQuery);
			pstmt.setString(1, name);
			playerQuery = pstmt.executeQuery();

			if (!playerQuery.next()) {
				System.out.print("No saved progress");
				Banners.dotDotDot();
			} else {
				grid = new Grid();
				playerHuman = new PlayerHuman(playerQuery.getString("name"), grid, new Score(playerQuery.getInt("moves"), playerQuery.getInt("game_duration")));
				playerCPU = new PlayerCPU(grid, new Score());

				while (playerQuery.next()) {
					if (playerQuery.getString("sign") == null)                  // empty spot
						grid.getSpot(playerQuery.getInt("y"), playerQuery.getInt("x")).setCoin(null);

					else if (playerQuery.getString("sign").equals("O")) {       // Coin of human player
						grid.getSpot(playerQuery.getInt("y"), playerQuery.getInt("x")).setCoin(new Coin(playerHuman));
						grid.addCoin();     // to keep track of amount of coins in the grid
					} else {                                                                // Coin of CPU
						grid.getSpot(playerQuery.getInt("y"), playerQuery.getInt("x")).setCoin(new Coin(playerCPU));
						grid.addCoin();     // to keep track of amount of coins in the grid
					}

				}
			}
			pstmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while trying to load a game. ");
			e.printStackTrace();
		}
		if (grid != null && playerCPU != null && playerHuman != null)
			return new GameSession(grid, playerHuman, playerCPU);
		else return null;
	}

	/**
	 Drops all sequences and tables to reset the entire database and start from scratch
	 */
	public static void dropEverything() {
		Connection connection = getConnection();
		Statement stmt;
		String insertSql;
		try {
			assert connection != null;
			stmt = connection.createStatement();

			insertSql = """
					DROP TABLE IF EXISTS int_spot CASCADE;
										
					DROP TABLE IF EXISTS int_score CASCADE;
										
					DROP TABLE IF EXISTS int_player CASCADE;
					                
					DROP TABLE IF EXISTS int_gamesession CASCADE;
										
					DROP TABLE IF EXISTS int_leaderboard CASCADE;
					                
					DROP SEQUENCE IF EXISTS player_id_seq CASCADE ;
					                
					DROP SEQUENCE IF EXISTS game_id_seq CASCADE ;
										
					""";
			stmt.executeUpdate(insertSql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error while trying to drop all tables and sequences.");
			e.printStackTrace();
		}
	}
}
