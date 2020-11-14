import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class SecurityGame extends JPanel {
    public static SQLInjection sqlInjection;
    public static DictionaryAttackPasswordCracker dictionaryAttackPasswordCracker;
    public static BruteForcePasswordCracker bruteForcePasswordCracker;

    public enum Game {
        SQL, Password, PhysicalAspects, StrongPasswords, End
    }

    public static Game currentGame;
    public static JPanel panel_game;
    public static JPanel resultsPanel;
    public static JPanel utilityPanel;
    public static JButton mainButton;
    public static JButton secondaryButton;
    public static JTextArea searchTextArea;
    public static JTextArea resultsTextArea;
    public static JTextArea utilityTextArea;
    public static JTextArea helpTextArea;

    public SecurityGame() {
        currentGame = Game.SQL;
        initializeGame();
    }

    private void initializeGame() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(700, 200));

        searchTextArea = new JTextArea();

        // Set the contents of the JTextArea.
        String output = "";
        searchTextArea.setText(output);
        searchTextArea.setLineWrap(true);
        searchTextArea.setWrapStyleWord(true);

        JScrollPane pane = new JScrollPane(searchTextArea);
        pane.setPreferredSize(new Dimension(700, 55));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainButton = new JButton("Search");
        mainButton.addActionListener(e -> {
            String search = searchTextArea.getText();
            searchTextArea.setText("");

            switch(currentGame) {
                case SQL:
                    if (search.equals("next")) {
                        sqlInjection.setProgress(SQLInjection.Progress.Done);
                        break;
                    }
                    String sqlStatement = "SQL Statement Executed From Your Search, " + search + " : \n" +
                            "SELECT item \n" +
                            "FROM inventory \n" +
                            "WHERE item ILIKE '%" + search + "%' AND available = TRUE";
                    try {
                        String result = sqlInjection.selectItem(search);
                        if (sqlInjection.getProgress().equals(SQLInjection.Progress.DatabaseType) && result.contains("PostgreSQL 12.4")) {
                            sqlInjection.setProgress(SQLInjection.Progress.TableNames);

                            String help = "Now that you know its a PostgreSQL database, you now want to find the names of the tables in this database \n" +
                                    "Use the SELECT statement to return the column table_name FROM the table information_schema.tables \n" +
                                    "\n" +
                                    "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.TableNames) && result.contains("pg_publication_tables")) {
                            sqlInjection.setProgress(SQLInjection.Progress.PublicTableNames);

                            String help = "Next, you need to narrow down the list of tables in the database to just tables that are public \n" +
                                    "Use the SELECT statement to return the column table_name FROM the table information_schema.tables, but \n" +
                                    "now use a WHERE clause to specify that the table_schema has to be public \n" +
                                    "\n" +
                                    "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.PublicTableNames) && result.contains("users")) {
                            sqlInjection.setProgress(SQLInjection.Progress.ColumnNames);

                            String help = "You now see there are two public tables – inventory and users. \n" +
                                    "The users tables likely holds interesting information we want to extract. \n" +
                                    "Try to find the names of the columns in the users table. \n" +
                                    "Hint: use a SELECT statement to return the column column_name FROM information_schema.columns \n" +
                                    "Don't forget to use a WHERE clause to specify that the table_name needs to be equal to users \n" +
                                    "\n" +
                                    "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.ColumnNames) && result.contains("password")) {
                            sqlInjection.setProgress(SQLInjection.Progress.UsernamesAndPasswords);

                            String help = "You now know the users table has two columns – username and password \n" +
                                    "Since we want both but a UNION can only return the same number of items in the prior SELECT statement, \n" +
                                    "you have to use string concatenation to get both column values to return \n" +
                                    "Example: SELECT field1 || ' ' || field2 FROM table \n" +
                                    "This returns the value in field1, a space, and then the value in field2, all on one line. \n" +
                                    "\n" +
                                    "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.UsernamesAndPasswords) && result.contains("johnsmith") && result.contains("dc4b56ff4967374b261a29cd4a90580d")) {
                            sqlInjection.setProgress(SQLInjection.Progress.Done);

                            String help = "The seemingly random strings of 32 characters is a 128-bit hash of a user's real password";
                            helpTextArea.setText(help);
                        }

                        resultsTextArea.setText(result);
                    } catch (SQLException sqlException) {
                        String result = "Error Selecting Item(s)";
                        resultsTextArea.setText(result);
                    }
                    utilityTextArea.setText(sqlStatement);
                    break;
                case Password:
                    String result = dictionaryAttackPasswordCracker.findPasswordGivenHash(search);
                    resultsTextArea.setText(result);
                    break;
                case StrongPasswords:
                    byte[] passwordHash;
                    StringBuilder output1 = new StringBuilder();
                    resultsTextArea.setText("");
                    try {
                        passwordHash = bruteForcePasswordCracker.getHash(search);
                        StringBuilder sb = new StringBuilder();
                        sb.setLength(search.length());
                        long start = System.currentTimeMillis();
                        bruteForcePasswordCracker.findPasswordGivenHash(0, search.length(), sb, bruteForcePasswordCracker.LOWER_AND_UPPER_CASE_LETTERS_AND_DIGITS, passwordHash, output1, start);
                        resultsTextArea.setText(output1.toString());
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        output1.append("Could not find password");
                    }
                    resultsTextArea.setText(output1.toString());
                    break;
                case PhysicalAspects:
                    break;
                case End:
                    break;
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        secondaryButton = new JButton();
        secondaryButton.addActionListener(e -> {
            String search = searchTextArea.getText();
            searchTextArea.setText("");

            switch(currentGame) {
                case StrongPasswords:
                    resultsTextArea.setText("");
                    String passwordHash;
                    String result;
                    try {
                        passwordHash = dictionaryAttackPasswordCracker.bytestoHexString(dictionaryAttackPasswordCracker.getHash(search));
                        result = dictionaryAttackPasswordCracker.findPasswordGivenHash(passwordHash);
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        result = "Could not find password";
                    }
                    resultsTextArea.setText(result);
                    break;
                default:
                    break;
            }
        });
        secondaryButton.setVisible(false);

        this.add(mainButton);
        this.add(Box.createHorizontalGlue());
        this.add(secondaryButton);

        this.add(pane, BorderLayout.CENTER);
    }

    public static void showFrame() {
        // Main Game Component
        SecurityGame game = new SecurityGame();
        game.setOpaque(true);

        /* ***** GAME CONTENT FRAME COMPONENTS ***** */
        // layers: frame -> panel_bckgrnd -> panel_game + panel_buttons

        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        // panel on top of frame
        JPanel panel_bckgrnd;

        panel_game = new JPanel();
        panel_game.setBackground(Color.GRAY);

        GUIproperties gui = new GUIproperties();

        JTextArea ta = new JTextArea();
        JScrollPane sp = new JScrollPane(ta);

        // Add the scroll pane into the content pane
        JFrame f = new JFrame();
        f.getContentPane().add(sp);

        /* ***** RESULTS COMPONENT ***** */
        resultsPanel = new JPanel();
        resultsTextArea = new JTextArea();
        JScrollPane resultsPane = new JScrollPane(resultsTextArea);
        resultsPane.setPreferredSize(new Dimension(680, 100));
        resultsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** RESULTS PANEL PROPERTIES ***** */
        resultsPanel.setVisible(true);
        resultsPanel.add(resultsPane);

        /* ***** UTILITY COMPONENT ***** */
        utilityPanel = new JPanel();
        utilityTextArea = new JTextArea();
        JScrollPane utilityPane = new JScrollPane(utilityTextArea);
        utilityPane.setPreferredSize(new Dimension(680, 100));
        utilityPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** UTILITY PANEL PROPERTIES ***** */
        utilityPanel.setVisible(true);
        utilityPanel.add(utilityPane);

        /* ***** HELP COMPONENTS ***** */
        JFrame helpFrame = new JFrame();
        helpFrame.setTitle("Help");
        helpTextArea = new JTextArea();
        JScrollPane helpPane = new JScrollPane(helpTextArea);
        helpPane.setPreferredSize(new Dimension(700, 500));
        helpPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** HELP FRAME PROPERTIES ***** */
        helpFrame.setSize(500, 200);
        helpFrame.setVisible(true);
        helpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        helpFrame.getContentPane().add(helpPane);

        String help = "First you need to find what kind of database this application is using \n" +
                "Try the following SELECT statements one at a time: \n" +
                "SELECT @@version \n" +
                "SELECT * FROM v$version \n" +
                "SELECT version() \n\n" +
                "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
        helpTextArea.setText(help);

        // add panel to add button
        JPanel panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.DARK_GRAY);
        JButton cont_button = gui.continue_button(panel_buttons);

        cont_button.addActionListener(e -> {
            switch(currentGame) {
                case SQL:
                    if (sqlInjection.getProgress().equals(SQLInjection.Progress.Done)) {
                        currentGame = Game.Password;
                        mainButton.setText("Crack Password");
                        utilityTextArea.setText(resultsTextArea.getText());
                        searchTextArea.setText("");
                        resultsTextArea.setText("");
                        dictionaryAttackPasswordCracker = new DictionaryAttackPasswordCracker();
                    }
                    break;
                case Password:
                    currentGame = Game.StrongPasswords;
                    panel_game.remove(utilityPanel);
                    utilityPanel.setVisible(false);
                    mainButton.setText("Brute Force Attack");
                    secondaryButton.setText("Dictionary Attack");
                    secondaryButton.setVisible(true);
                    bruteForcePasswordCracker = new BruteForcePasswordCracker();
                    break;
                case StrongPasswords:
                    break;
                case PhysicalAspects:
                    break;
                case End:
                    break;
            }
        });

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

        panel_game.setPreferredSize(new Dimension(700, 680));
        panel_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxes[0].add(panel_game);
        panel_buttons.setPreferredSize(new Dimension(700, 120));
        boxes[1].add(panel_buttons);
        panel_game.add(game);
        panel_game.add(resultsPanel);
        panel_game.add(utilityPanel);

        // game content displayed
        panel_bckgrnd.add(panel_game);

        // buttons panel
        panel_bckgrnd.add(panel_buttons);   // add panel with buttons
        panel_buttons.add(exit_button);     // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(690, 0))); // spacing between buttons
        panel_buttons.add(cont_button);     // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(0, 100))); // spacing between buttons


        /* ***** MAIN FRAME PROPERTIES ***** */
        frame.getContentPane().add(panel_bckgrnd);
        frame.setSize(1000, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        sqlInjection = new SQLInjection();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                sqlInjection.closeConnection();
                System.out.println("DB Connection Closed");
            } catch (SQLException sqlException) {
                System.out.println("Error Closing DB Connection");
            }
        }));

        // Start Game
        SwingUtilities.invokeLater(SecurityGame::showFrame);
    }
}