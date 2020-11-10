import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLGame extends JPanel {
    public static ArrayList<String> sqlTerms;
    public static Connection conn;

    public static void connect() throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://ec2-54-152-40-168.compute-1.amazonaws.com:5432/ddtc8vf18pmans";
        String user = "swxxbcpnhycxwi";
        String password = "870fe50b865f47620205e79b722404cc13344c7ad690ce8fd752c90f3e6a9634";
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(url, user, password);
    }

    public static String selectItem(String item, Connection conn) throws SQLException {
        String output = "";
        for(String sqlTerm : sqlTerms) {
            Pattern p = Pattern.compile(".*" + sqlTerm + ".*");
            Matcher m = p.matcher(item);
            if(m.find()) {
                output += "Found " + sqlTerm + " in " + item + "\n" +
                        "Use only SELECT statements\n";
                return output;
            }
        }

        String sql = "SELECT item " +
                "FROM inventory " +
                "WHERE item ILIKE '%"+item+"%' AND available = TRUE";

        output += "SQL Statement Executed: \n" + sql + "\n\n";

        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery(sql);
        int columnCount = rs.getMetaData().getColumnCount();
        output += "Results: \n";
        while (rs.next()) {
            StringBuilder row = new StringBuilder();
            for(int i = 1; i <= columnCount; i++) {
                row.append(rs.getString(i));
            }
            output += row.toString() + "\n";
        }

        rs.close();
        s.close();

        return output;
    }

    public SQLGame() {
        initializeUI();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 500));

        final JTextArea textArea = new JTextArea();

        // Set the contents of the JTextArea.
        String output = "";
        textArea.setText(output);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane pane = new JScrollPane(textArea);
        pane.setPreferredSize(new Dimension(1000, 500));
        pane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton button = new JButton("Execute Statement");
        button.addActionListener(e -> {
            // Execute search
            String search = textArea.getText();
            try {
                String output1 = selectItem(search, conn);
                textArea.setText(output1);
            } catch (SQLException s) {
                textArea.setText("Error selecting item\n");
            }
            System.out.println("contents = " + search);
        });

        this.add(pane, BorderLayout.CENTER);
        this.add(button, BorderLayout.SOUTH);
    }

    public static void showFrame() {
        JPanel panel = new SQLGame();
        panel.setOpaque(true);

        /* ***** GANE CONTENT FRAME COMPONENTS ***** */
        // layers: frame -> panel_bckgrnd -> panel_game + panel_buttons

        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        // panel on top of frame
        JPanel panel_bckgrnd;

        JPanel panel_game = new JPanel();
        panel_game.setBackground(Color.GRAY);

        GUIproperties gui = new GUIproperties();

        // add panel to add button
        JPanel panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.DARK_GRAY);
        JButton cont_button = gui.continue_button(panel_buttons);
        JButton exit_button = gui.exit_button(panel_buttons);


        /* ***** MAIN PANEL COMPONENTS ***** */
        // setting up button layout and main window design
        panel_bckgrnd = new JPanel();
        // add layout to lower panel for button alignment
        LayoutManager box_layout = new BoxLayout(panel_bckgrnd, BoxLayout.Y_AXIS);
        Box boxes[] = new Box[2];
        boxes[0] = Box.createVerticalBox(); // where game content will be displayed
        boxes[1] = Box.createVerticalBox(); // where buttons are displayed
        panel_bckgrnd.setBackground(Color.WHITE);
        panel_bckgrnd.setLayout(box_layout);
        panel_bckgrnd.add(boxes[0]);
        panel_bckgrnd.add(boxes[1]);

        panel_game.setPreferredSize(new Dimension(1400, 680));
        panel_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxes[0].add(panel_game);
        panel_buttons.setPreferredSize(new Dimension(1400, 120));
        boxes[1].add(panel_buttons);
        panel_game.add(panel);

        // game content displayed
        panel_bckgrnd.add(panel_game);

        // buttons panel
        panel_bckgrnd.add(panel_buttons);   // add panel with buttons
        panel_buttons.add(exit_button);     // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(1070, 0))); // spacing between buttons
        panel_buttons.add(cont_button);     // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(0, 100))); // spacing between buttons


        /* ***** MAIN FRAME PROPERTIES ***** */
        frame.getContentPane().add(panel_bckgrnd);
        frame.setSize(1400, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Ending SQL Injection Game");
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }));

        try {
            connect();
            sqlTerms = new ArrayList<>();
            sqlTerms.add("DROP");
            sqlTerms.add("DELETE");
            sqlTerms.add("UPDATE");
            sqlTerms.add("CREATE");
            sqlTerms.add("WITH");
            sqlTerms.add("ALTER");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error connecting to database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(SQLGame::showFrame);
    }
}