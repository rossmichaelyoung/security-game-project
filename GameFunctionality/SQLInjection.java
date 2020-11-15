import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLInjection {
    public static ArrayList<String> sqlTerms;
    public static Connection conn;
    public enum Progress {
        DatabaseType, TableNames, PublicTableNames, ColumnNames, UsernamesAndPasswords, Done
    }
    public static Progress progress;

    public static Connection connect() throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://ec2-54-152-40-168.compute-1.amazonaws.com:5432/ddtc8vf18pmans";
        String user = "swxxbcpnhycxwi";
        String password = "870fe50b865f47620205e79b722404cc13344c7ad690ce8fd752c90f3e6a9634";
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection() throws SQLException {
        conn.close();
    }

    public static String selectItem(String item) throws SQLException {
        StringBuilder output = new StringBuilder();
        for(String sqlTerm : sqlTerms) {
            Pattern p = Pattern.compile(".*" + sqlTerm + ".*");
            Matcher m = p.matcher(item);
            if(m.find()) {
                output.append("Found ").append(sqlTerm).append(" in ").append(item).append("\n").append("Use only SELECT statements\n");
                return output.toString();
            }
        }

        String sql = "SELECT item " +
                "FROM inventory " +
                "WHERE item ILIKE '%"+item+"%' AND available = TRUE";

        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery(sql);
        int columnCount = rs.getMetaData().getColumnCount();
        output.append("Results: \n");
        while (rs.next()) {
            StringBuilder row = new StringBuilder();
            for(int i = 1; i <= columnCount; i++) {
                row.append(rs.getString(i));
            }
            output.append(row.toString()).append("\n");
        }

        rs.close();
        s.close();

        return output.toString();
    }

    public static void setProgress(Progress p) {
        progress = p;
    }

    public static Progress getProgress() {
        return progress;
    }

    public SQLInjection() {
        try {
            conn = connect();
            sqlTerms = new ArrayList<>();
            sqlTerms.add("DROP");
            sqlTerms.add("DELETE");
            sqlTerms.add("UPDATE");
            sqlTerms.add("CREATE");
            sqlTerms.add("WITH");
            sqlTerms.add("ALTER");
            setProgress(Progress.DatabaseType);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error connecting to database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error finding JDBC PostgreSQL Driver");
        }
    }

    public static void main(String[] args) {
        new SQLInjection();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Search for item or enter q to quit");
            String search = reader.readLine();
            while (!search.equals("q") && !search.equals("quit")) {
                try {
                    String results = selectItem(search);
                    System.out.println(results);
                } catch (SQLException e) {
                    System.out.println("Error selecting item\n");
                }

                System.out.println("Search for item or enter q to quit");
                search = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
