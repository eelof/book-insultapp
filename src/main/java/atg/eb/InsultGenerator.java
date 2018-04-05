package atg.eb;

import java.sql.*;

public class InsultGenerator {
    private Connection connection;

    public String generateInsult() {

        String vowels = "AEIOU";
        String article = "an";
        String theInsult = "";

        connectToDatabase();

        try {

            String SQL = "select a.string AS first, b.string AS second, c.string AS noun from short_adjective a , long_adjective b, noun c ORDER BY random() limit 1";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) {
                    article = "a";
                }
                theInsult = String.format("Thou art %s %s %s %s!", article,
                        rs.getString("first"), rs.getString("second"), rs.getString("noun"));
                rs.close();
                connection.close();
            }

        } catch(SQLException e) {
            return e.getMessage();
        }

        return theInsult;

    }

    private void connectToDatabase() {
        try {
            String databaseURL = "jdbc:postgresql://";
            databaseURL += System.getenv("POSTGRESQL_SERVICE_HOST");
            databaseURL += "/" + System.getenv("POSTGRESQL_DATABASE");

            String username = System.getenv("POSTGRESQL_USER");
            String password = System.getenv("PGPASSWORD");

            connection = DriverManager.getConnection(databaseURL, username, password);
        } catch (Exception e) {
            System.out.println("Connection failure ");
            System.out.print(e.getMessage());
        }
    }

}
