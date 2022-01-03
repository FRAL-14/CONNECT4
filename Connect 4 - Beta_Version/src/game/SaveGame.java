package game;

import java.sql.*;

import static game.Grid.COLUMNS_AMOUNT;
import static game.Grid.ROWS_AMOUNT;
import static game.Leaderboard.getConnection;

public class SaveGame {
	/**
	 Creating all tables
	 */
	public static void createALLTABLES() {
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
			System.out.println("Error in connection to PostgreSQL server");
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
		Statement stmt;
		String insertSql;
		PreparedStatement pstmt;
		PreparedStatement pstmt2;

		try {
			assert connection != null : "Connection is null";
			stmt = connection.createStatement();

			//			Method checks for previous record of same player name and deletes it if so.
			checkAndDeletePlayerIfExist(playerName);

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
			System.out.println("Game Saved!");

		} catch (SQLException e) {
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

	public static void checkAndDeletePlayerIfExist(String playerName) {
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
						WHERE int_player.name = ?
						""");
				pstmt2.setString(1, playerName);
				pstmt2.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

	//	public static void loadGame(String name) {
	//		Connection connection = getConnection();
	//		ResultSet playerQuery;
	//		PreparedStatement pstmt;
	//		String query = """
	//				SELECT name,duration,x,y,sign
	//				FROM int_player
	//				JOIN int_spot USING (game_id)
	//				JOIN int_score USING (score_id)
	//				WHERE name=?
	//				""";
	//
	//		try {
	//			assert connection != null;
	//			pstmt = connection.prepareStatement(query);
	//			pstmt.setString(1, name);
	//			playerQuery = pstmt.executeQuery(query);
	//
	//			if (!playerQuery.next()) {
	//				System.out.println("No saved progress");
	//			} else {
	//				Grid grid = new Grid();
	//				PlayerHuman playerHuman = new PlayerHuman(rs.getString("name"), grid, new Score(rs.getString("moves")));
	//				for (int row = 0; row < ROWS_AMOUNT; row++) {
	//					for (int column = 0; column < COLUMNS_AMOUNT; column++) {
	//						grid.getSpot(row, column).setSpot(rs.getString("spot"));
	//					}
	//				}
	//			}
	//
	//			pstmt.close();
	//			connection.close();
	//		} catch (SQLException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public static void dropEverything() {
		Connection connection = getConnection();
		Statement stmt;
		String insertSql;
		try {
			assert connection != null;
			stmt = connection.createStatement();

			insertSql = """
					DROP TABLE IF EXISTS int_spot;
										
					DROP TABLE IF EXISTS int_score;
										
					DROP TABLE IF EXISTS int_player;
					                
					DROP TABLE IF EXISTS int_gamesession;
					                
					DROP SEQUENCE IF EXISTS player_id_seq;
					                
					DROP SEQUENCE IF EXISTS game_id_seq;
										
					""";
			stmt.executeUpdate(insertSql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
