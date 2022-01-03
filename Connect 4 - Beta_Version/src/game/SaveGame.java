package game;

import java.sql.*;
import java.util.Scanner;


public class SaveGame {






	public static void createALLTABLES(){
        /*
          These are for executing queries through jdbc
         */
		Statement stmt = null;
		String CreateSql = null;

        /*
          This is the login info for the Database
         */
		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4 Database";
		String username = "postgres";
		String password = "Student_1234";

		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);
			stmt = connection.createStatement();

			CreateSql = """
                                          
                        CREATE SEQUENCE IF NOT EXISTS player_id_seq AS INT
                        increment by 1
                        Start with 1
                        NO MAXVALUE;
                        
                        CREATE SEQUENCE IF NOT EXISTS game_id_seq AS INT
                        Start with 1
                        increment by 1
                        NO MAXVALUE;
                        
                        CREATE SEQUENCE IF NOT EXISTS score_id_seq AS INT
                        increment by 1
                        Start with 1
                        NO MAXVALUE;
                
                        
                        CREATE TABLE IF NOT EXISTS INT_gameSession(
                            game_id INT primary key,
                            last_played_at timestamp default now()
                        );
                        CREATE TABLE IF NOT EXISTS INT_score(
                            score_id INT primary key,
                            moves INT,
                            game_duration INT
                        );
                        CREATE TABLE IF NOT EXISTS INT_player(
                            game_id INT references INT_gameSession(game_id) ON DELETE CASCADE,
                            player_id INT primary key,
                            name VARCHAR(20),
                            score_id INT references INT_score(score_id) ON DELETE CASCADE
                        );
                        
                        CREATE TABLE IF NOT EXISTS INT_spot(
                            x INT,
                            y INT,
                            sign char(1) default null,
                            game_id INT references INT_gameSession(game_id) ON DELETE CASCADE,
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

	public static void saveGame(String playerName, int moves, int duration){
		createALLTABLES();

		PreparedStatement pstmt=null;
		PreparedStatement pstmt2=null;
		Statement stmt=null;

		String insertSql=null;

		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4 Database";
		String username = "postgres";
		String password = "Student_1234";
		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);

			checkAndDeletePlayerIfExist(playerName);

			pstmt = connection.prepareStatement("""
                        INSERT INTO int_gamesession (game_id)
                        VALUES (nextval('game_id_seq'));
                        
                        INSERT INTO int_score (score_id, moves, game_duration)
                        VALUES (nextval('score_id_seq'),?,?);
                        
                        INSERT INTO int_player (player_id,name,game_id,score_id)
                        VALUES (nextval('player_id_seq'),?,currval('game_id_seq'),currval('score_id_seq'));
                        
                        
                        """);
			pstmt.setInt(1,moves);
			pstmt.setInt(2,duration);
			pstmt.setString(3,playerName);
			pstmt.executeUpdate();





			insertSql= """
                       INSERT INTO int_spot (x,y,sign,game_id)
                       VALUES (?,?,?,currval(game_id_seq))
                       """;
			pstmt2 = connection.prepareStatement(insertSql);
			for(int row = 0; row < Grid.ROWS_AMOUNT; row++){
				pstmt2.setInt(2, row);
				for (int col = 0; col < Grid.COLUMNS_AMOUNT; col++){
					pstmt2.setInt(1,col);
					pstmt2.setString(3,String.valueOf(grid.getSpot(row,col).getCoin().getSign()));
					pstmt2.executeUpdate();
				}}


			connection.close();
			pstmt.close();
			pstmt2.close();
			System.out.println("Game Saved!");

		} catch (SQLException e) {
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

	public static void checkAndDeletePlayerIfExist(String playerName){
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		Statement stmt=null;
		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4 Database";
		String username = "postgres";
		String password = "Student_1234";
		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);

			stmt = connection.createStatement();

			pstmt2 = connection.prepareStatement("SELECT * FROM int_player WHERE name = ?");
			pstmt2.setString(1,playerName);

			ResultSet rs = stmt.executeQuery(String.valueOf(pstmt2));
			if (rs.next()){

				pstmt1 = connection.prepareStatement("""
                                                            DELETE FROM int_gamesession
                                                            WHERE game_id in (SELECT game_id
                                                            FROM int_player
                                                            WHERE name = ? );
                                                            DELETE FROM int_spot
                                                            WHERE game_id in (SELECT game_id
                                                                              FROM int_player
                                                                              WHERE name= ? );
                                                            DELETE FROM int_score
                                                            WHERE score_id in (SELECT score_id
                                                            FROM int_player WHERE name = ? );
                                                            """);
				pstmt1.setString(1,playerName);
				pstmt1.setString(2,playerName);
				pstmt1.setString(3,playerName);

			}

		} catch (SQLException e) {
			System.out.println("Error in connection to PostgreSQL server");
			e.printStackTrace();
		}
	}

	public static void dropEverything(){
		Statement stmt = null;
		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4 Database";
		String username = "postgres";
		String password = "Student_1234";
		String insertSql=null;
		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);

			stmt = connection.createStatement();

			insertSql= """
                drop table if exists int_player;
                                
                drop table if exists int_score;
                                
                drop table if exists int_spot;
                                
                drop table if exists int_gamesession;
                                
                drop sequence if exists player_id_seq;
                                
                drop sequence if exists game_id_seq;
                                
                drop sequence if exists score_id_seq;
                                
                """;
			stmt.executeUpdate(insertSql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void loadGame(){
		ResultSet playerQuery = null;
		PreparedStatement pstmt;
		String jdbc = "jdbc:postgresql://localhost:5432/Connect 4 Database";
		String username = "postgres";
		String password = "Student_1234";
		String query=null;
		try {
			Connection connection = DriverManager.getConnection(jdbc, username, password);
			query= """
                SELECT * FROM int_gamesession,INT_PLAYER,int_score
                WHERE name=?
                                
                """;
			pstmt = connection.prepareStatement(query);


			playerQuery = pstmt.executeQuery(query);

			if (!playerQuery.next()) {
				System.out.println("No saved progress");
			}else{
				Grid grid = new Grid();
				PlayerHuman playerHuman = new PlayerHuman(rs.getString("name"),grid,new Score(rs.getString("name"),rs.getTimestamp("")));
				for (int row = 0; row < ROWAMOUNT; row++){
					for (int column = 0; column < COLUMNAMOUNT; column++){
						grid.getSpot(row,column).setSpot(rs.getString("spot"));
					}
				}
			}

			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

