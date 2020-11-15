import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

class CyberAdventure extends JPanel {

    public static PhysicalAspects physicalAspects;
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
    public static JComboBox<String> characterSpaceOptions;

    public CyberAdventure() {
        currentGame = Game.SQL;
        initializeGame();
    }

    private void initializeGame() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(700, 200));

        searchTextArea = new JTextArea();

        /* Set the contents of the JTextArea */
        searchTextArea.setLineWrap(true);
        searchTextArea.setWrapStyleWord(true);

        JScrollPane pane = new JScrollPane(searchTextArea);
        pane.setPreferredSize(new Dimension(700, 55));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainButton = new JButton("Search");
        mainButton.addActionListener(e -> {
            String search = searchTextArea.getText();
            searchTextArea.setText("");

            switch (currentGame) {
                case SQL:
                    if (search.equals("next")) {
                        sqlInjection.setProgress(SQLInjection.Progress.Done);
                        break;
                    }
                    String sqlStatement = "SQL Statement Executed From Your Search, " + search + " : \n"
                            + "SELECT item \n" + "FROM inventory \n" + "WHERE item ILIKE '%" + search
                            + "%' AND available = TRUE";
                    try {
                        String result = sqlInjection.selectItem(search);
                        if (sqlInjection.getProgress().equals(SQLInjection.Progress.DatabaseType)
                                && result.contains("PostgreSQL 12.4")) {
                            sqlInjection.setProgress(SQLInjection.Progress.TableNames);

                            String help = "Now that you know its a PostgreSQL database, you now want to find the names of the tables in this database \n"
                                    + "Use the SELECT statement to return the column table_name FROM the table information_schema.tables \n"
                                    + "\n"
                                    + "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.TableNames)
                                && result.contains("pg_publication_tables")) {
                            sqlInjection.setProgress(SQLInjection.Progress.PublicTableNames);

                            String help = "Next, you need to narrow down the list of tables in the database to just tables that are public \n"
                                    + "Use the SELECT statement to return the column table_name FROM the table information_schema.tables, but \n"
                                    + "now use a WHERE clause to specify that the table_schema has to be public \n"
                                    + "\n"
                                    + "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.PublicTableNames)
                                && result.contains("users")) {
                            sqlInjection.setProgress(SQLInjection.Progress.ColumnNames);

                            String help = "You now see there are two public tables – inventory and users. \n"
                                    + "The users tables likely holds interesting information we want to extract. \n"
                                    + "Try to find the names of the columns in the users table. \n"
                                    + "Hint: use a SELECT statement to return the column column_name FROM information_schema.columns \n"
                                    + "Don't forget to use a WHERE clause to specify that the table_name needs to be equal to users \n"
                                    + "\n"
                                    + "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.ColumnNames)
                                && result.contains("password")) {
                            sqlInjection.setProgress(SQLInjection.Progress.UsernamesAndPasswords);

                            String help = "You now know the users table has two columns – username and password \n"
                                    + "Since we want both but a UNION can only return the same number of items in the prior SELECT statement, \n"
                                    + "you have to use string concatenation to get both column values to return \n"
                                    + "Example: SELECT field1 || ' ' || field2 FROM table \n"
                                    + "This returns the value in field1, a space, and then the value in field2, all on one line. \n"
                                    + "\n"
                                    + "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(help);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.UsernamesAndPasswords)
                                && result.contains("johnsmith")
                                && result.contains("dc4b56ff4967374b261a29cd4a90580d")) {
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
                    resultsTextArea.setText("");
                    String result = dictionaryAttackPasswordCracker.findPasswordGivenHash(search);
                    resultsTextArea.setText(result);
                    break;
                case StrongPasswords:
                    resultsTextArea.setText("");
                    bruteForcePasswordCracker.setFound(false);
                    String selectedCharacterSpace = String.valueOf(characterSpaceOptions.getSelectedItem());
                    result = bruteForcePasswordCracker.findPasswordGivenHash(search, selectedCharacterSpace);
                    resultsTextArea.setText(result);
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

            switch (currentGame) {
                case StrongPasswords:
                    resultsTextArea.setText("");
                    String passwordHash;
                    String result;
                    try {
                        passwordHash = dictionaryAttackPasswordCracker
                                .bytestoHexString(dictionaryAttackPasswordCracker.getHash(search));
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
    }   // end initializeGame()

    public static void ShowFrame() {
        CyberAdventure game = new CyberAdventure();
        game.setOpaque(true);

        GUIproperties gui = new GUIproperties();

        /* ***** GAME CONTENT FRAME COMPONENTS ***** */
        /* layers: frame -> panel_bckgrnd -> panel_game + panel_buttons */

        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        /* panel on top of frame */
        JPanel panel_bckgrnd = new JPanel();

        JPanel panel_game = new JPanel();
        panel_game.setBackground(Color.GRAY);

        JTextArea ta = new JTextArea();
        JScrollPane sp = new JScrollPane(ta);

        /* Add the scroll pane into the content pane */
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

        String help = "First you need to find what kind of database this application is using \n"
                + "Try the following SELECT statements one at a time: \n" + "SELECT @@version \n"
                + "SELECT * FROM v$version \n" + "SELECT version() \n\n"
                + "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
        helpTextArea.setText(help);

        /* add panel to add button */
        JPanel panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.DARK_GRAY);
        String b_label = "HIDE HELP";
        JButton help_button = gui.help_button(panel_buttons, b_label);
        JButton cont_button = gui.continue_button(panel_buttons);
        JButton exit_button = gui.exit_button(panel_buttons);

        cont_button.addActionListener(e -> {
            switch (currentGame) {
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
                    searchTextArea.setText("");
                    resultsTextArea.setText("");
                    utilityTextArea.setText("");

                    mainButton.setText("Brute Force Attack");
                    secondaryButton.setText("Dictionary Attack");
                    secondaryButton.setVisible(true);
                    bruteForcePasswordCracker = new BruteForcePasswordCracker();

                    String[] choices = { "Lowercase Characters Only", "Lowercase and Uppercase Characters",
                            "Lowercase Characters and Numbers", "Lowercase and Uppercase Characters and Numbers" };
                    characterSpaceOptions = new JComboBox<String>(choices);
                    characterSpaceOptions.setVisible(true);
                    panel_game.add(characterSpaceOptions);
                    break;
                case StrongPasswords:
                    break;
                case PhysicalAspects:
                    break;
                case End:
                    break;
            }
        });

        gui.interface_layout(panel_bckgrnd, panel_game, panel_buttons, game, resultsPanel, utilityPanel);
        /* spacing between border and content */
        panel_game.setBorder(new EmptyBorder(50, 20, 20, 20));

        exit_game(frame, exit_button);
        
        /* ***** DISPLAY PANEL WHERE GAME CONTENT WILL BE PLACED ***** */
        panel_bckgrnd.add(panel_game);

        /* ***** BUTTONS PANEL COMPONENTS ***** */
        panel_bckgrnd.add(panel_buttons); // add panel with buttons
        panel_buttons.add(exit_button); // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(500, 0))); // spacing between buttons
        panel_buttons.add(help_button); // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(0, 20))); // spacing between buttons
        panel_buttons.add(cont_button);
        panel_buttons.add(Box.createRigidArea(new Dimension(0, 120))); // spacing between buttons

        /* ***** MAIN FRAME PROPERTIES ***** */
        frame.getContentPane().add(panel_bckgrnd);
        frame.setSize(1100, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }   // end ShowFrame()

    public static void exit_game(JFrame frame, JButton exit_button) {
        exit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            } // end void actionPerformed()
        }); // end actionListener()
    }   // end exit_game()

    public static void show_hide_help(JFrame frame, JButton help_button, JLabel label) {
        help_button.addActionListener(new ActionListener() {
            boolean hasBeenClicked = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hasBeenClicked) {
                    label.setText(null);

                    String b_label = "HIDE HELP";
                    help_button.setText(b_label);
                    
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } else if (hasBeenClicked) {
                    String b_label = "SHOW HELP";
                    help_button.setText(b_label);
                    frame.dispose();
                    frame.setVisible(false);
                }
                hasBeenClicked = !hasBeenClicked;

            } // end void actionPerformed()
        }); // end actionListener()

    }
    
    public static void main(String[] args) {

        GUIproperties gui = new GUIproperties();
        physicalAspects = new PhysicalAspects();
        
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

    } // end main
} // end class