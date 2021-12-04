import java.sql.*;

public class Leaderboard {


    public static void main(String[] args) throws SQLException {

        Statement stmt = null;
        String CreateSql = null;
        String Insertsql = null;

        String jdbc = "jdbc:postgresql://localhost:5432/Leaderboard - Connect 4";
        String username = "postgres";
        String password = "Student_1234";
        try {
            Connection connection = DriverManager.getConnection(jdbc, username, password);
            System.out.println("Connected to PostgreSQL server");
            stmt = connection.createStatement();
////
////
//            CreateSql = "Create Table INT_Leaderboard(player_id BIGSERIAL primary key , player_name varchar(20) NOT NULL, last_played_at TIMESTAMP default now(), moves INT, game_duration INT not null) ";
//////            Insertsql = "INSERT INTO int_leaderboard as il (player_name,moves,game_duration) VALUES('PeterTheEliminator', 20, 23), ('Tea', 56, 16), ('Lais', 24, 48), ('SharpXboss', 13, 2),('Batman',26,89)";
//            stmt.executeUpdate(CreateSql); //stmt.executeQuery(Insertsql);
////            printTop5Scores();
////            stmt.close();
////
////            connection.close();
        } catch (SQLException e) {
            System.out.println("Error in connection to PostgreSQL server");
            e.printStackTrace();
        }
        printTop5Scores();
    }

    public static void printTop5Scores() {
        Statement stmt = null;
        String CreateSql = null;
        String query = null;
        query = "SELECT LPAD(player_name,20,'.'),moves,game_duration FROM int_leaderboard ORDER BY 2,3";
//        query = "SELECT * from int_leaderboard";

        String jdbc = "jdbc:postgresql://localhost:5432/Leaderboard - Connect 4";
        String username = "postgres";
        String password = "Student_1234";
        try {
            Connection connection = DriverManager.getConnection(jdbc, username, password);
            System.out.println("Connected to PostgreSQL server");
            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);


//            for (int i = 0; i < 6 && rs.next(); i++) {
//
//                if (rs.isFirst()) {
//                    System.out.printf("  %s  \t%s      %s\n", "playerName", "Moves", "Time(seconds)");
//                    System.out.println("-".repeat(35));
//                }
//
//                System.out.printf("%d %s %d \t\t%d \t\t\n", i, rs.getString("player_name"), rs.getInt("moves"), rs.getInt("game_duration"));
//
//            }
            int rank=1;
            while(rs.next()){

                if (rs.isFirst()){
                    System.out.printf("  \t\t%s  \t\t\t%s      %s\n", "Name", "Moves", "Time(seconds)");
                    System.out.println("-".repeat(45));
                }
                System.out.printf("%d%s \t  %d \t\t\t%d \n",rank , rs.getString(1), rs.getInt("moves"), rs.getInt("game_duration"));
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


