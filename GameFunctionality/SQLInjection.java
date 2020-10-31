// connect to database
// take input from GUI and run that in SQL select statement
// output to GUI what you get from SQL select statement

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class SQLInjection {

    public static Connection connect() throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://ec2-54-152-40-168.compute-1.amazonaws.com:5432/ddtc8vf18pmans";
        String user = "swxxbcpnhycxwi";
        String password = "870fe50b865f47620205e79b722404cc13344c7ad690ce8fd752c90f3e6a9634";
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    public static void selectItem(String item, Connection conn) throws SQLException {
        String sql = "SELECT name " +
                "FROM inventory " +
                "WHERE name ILIKE '"+item+"' AND available = TRUE";

        System.out.println("\nSQL statement about to be executed: \n" + sql + "\n");

        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery(sql);
        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String row = "";
            for(int i = 1; i <= columnCount; i++) {
                row += rs.getString(i);
            }
            System.out.println(row);
        }
        rs.close();
        s.close();
    }

    public static void main(String[] args) {
        Connection conn;
        try {
            conn = connect();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Search for item");
                String item = reader.readLine();
                selectItem(item, conn);
                reader.close();
            } catch (SQLException e) {
                System.out.println("Error selecting item");
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error connecting to database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
