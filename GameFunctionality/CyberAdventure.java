import java.math.BigInteger;
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
    public static JFrame helpFrame;
    public static JPanel panel_game;
    public static JPanel resultsPanel;
    public static JButton mainButton;
    public static JTextArea searchTextArea;
    public static JTextArea resultsTextArea;
    public static JTextArea helpTextArea;
    public static JComboBox<String> characterSpaceOptions;
    public static JComboBox<String> timeOptions;


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

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setResultsPanel() {
        /* ***** RESULTS COMPONENT ***** */
        resultsPanel = new JPanel();
        resultsTextArea = new JTextArea();
        JScrollPane resultsPane = new JScrollPane(resultsTextArea);
        resultsPane.setPreferredSize(new Dimension(680, 240));
        resultsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** RESULTS PANEL PROPERTIES ***** */
        resultsPanel.setVisible(true);
        resultsPanel.add(resultsPane);
        resultsPanel.add(Box.createRigidArea(new Dimension(95, 0)));
    }

    public static void setupInteractiveGames() {
        setResultsPanel();

        /* ***** HELP COMPONENTS ***** */
        helpFrame = new JFrame();
        helpFrame.setTitle("Help");
        helpTextArea = new JTextArea();
        JScrollPane helpPane = new JScrollPane(helpTextArea);
        helpPane.setPreferredSize(new Dimension(700, 500));
        helpPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** HELP FRAME PROPERTIES ***** */
        helpFrame.setSize(500, 200);
        helpFrame.setVisible(true);
        helpFrame.getContentPane().add(helpPane);

        String help = "First you need to find what kind of database this application is using \n" +
                "Try the following SELECT statements one at a time: \n" +
                "SELECT @@version \n" +
                "SELECT * FROM v$version \n" +
                "SELECT version() \n\n" +
                "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
        helpTextArea.setText(help);

        JPanel searchPanel = new JPanel();
        searchTextArea = new JTextArea();

        // Set the contents of the JTextArea.
        searchTextArea.setLineWrap(true);
        searchTextArea.setWrapStyleWord(true);

        JScrollPane searchPane = new JScrollPane(searchTextArea);
        searchPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        searchPane.setPreferredSize(new Dimension(680, 50));
        searchPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        searchPanel.setVisible(true);
        searchPanel.add(searchPane);
        searchPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        mainButton = new JButton("Search");
        mainButton.setPreferredSize(new Dimension(138, 20));
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

                            String helpAndHints = "<html>Now that you know its a PostgreSQL database, you now want to find the names of the tables in this database <br>" +
                                    "Use the SELECT statement to return the column table_name FROM the table information_schema.tables <br><br>" +
                                    "Remember to start your SQL command with a single quote (') and a <br>UNION statement and to end your SQL statement with a comment command, which is -- <br></html>";
                            helpTextArea.setText(helpAndHints);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.TableNames) && result.contains("pg_publication_tables")) {
                            sqlInjection.setProgress(SQLInjection.Progress.PublicTableNames);

                            String helpAndHints = "Next, you need to narrow down the list of tables in the database to just tables that are public \n" +
                                    "Use the SELECT statement to return the column table_name FROM the table information_schema.tables, but \n" +
                                    "now use a WHERE clause to specify that the table_schema has to be public \n" +
                                    "\n" +
                                    "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(helpAndHints);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.PublicTableNames) && result.contains("users")) {
                            sqlInjection.setProgress(SQLInjection.Progress.ColumnNames);

                            String helpAndHints = "You now see there are two public tables – inventory and users. \n" +
                                    "The users tables likely holds interesting information we want to extract. \n" +
                                    "Try to find the names of the columns in the users table. \n" +
                                    "Hint: use a SELECT statement to return the column column_name FROM information_schema.columns \n" +
                                    "Don't forget to use a WHERE clause to specify that the table_name needs to be equal to users \n" +
                                    "\n" +
                                    "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(helpAndHints);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.ColumnNames) && result.contains("password")) {
                            sqlInjection.setProgress(SQLInjection.Progress.UsernamesAndPasswords);

                            String helpAndHints = "You now know the users table has two columns – username and password \n" +
                                    "Since we want both but a UNION can only return the same number of items in the prior SELECT statement, \n" +
                                    "you have to use string concatenation to get both column values to return \n" +
                                    "Example: SELECT field1 || ' ' || field2 FROM table \n" +
                                    "This returns the value in field1, a space, and then the value in field2, all on one line. \n" +
                                    "\n" +
                                    "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n";
                            helpTextArea.setText(helpAndHints);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.UsernamesAndPasswords) && result.contains("johnsmith") && result.contains("dc4b56ff4967374b261a29cd4a90580d")) {
                            sqlInjection.setProgress(SQLInjection.Progress.Done);

                            String helpAndHints = "The seemingly random strings of 32 characters is a 128-bit hash of a user's real password";
                            helpTextArea.setText(helpAndHints);
                        }

                        resultsTextArea.setText(result);
                    } catch (SQLException sqlException) {
                        String result = "Error Selecting Item(s)";
                        resultsTextArea.setText(result);
                    }

                    resultsTextArea.append("\n\n" + sqlStatement);
                    break;
                case Password:
                    resultsTextArea.setText("");
                    String result = dictionaryAttackPasswordCracker.findPasswordGivenHash(search);
                    resultsTextArea.setText(result);
                    break;
                case StrongPasswords:
                    resultsTextArea.setText("");
                    String passwordHash;
                    mainButton.setPreferredSize(new Dimension(175, 5));

                    if(mainButton.getText().equals("Brute Force Attack")) {
                        String selectedCharacterSpace = String.valueOf(characterSpaceOptions.getSelectedItem());
                        if (bruteForcePasswordCracker.checkCharacterSpace(search, selectedCharacterSpace)) {
                            bruteForcePasswordCracker.setFound(false);
                            resultsTextArea.setText("");
                            try {
                                passwordHash = bruteForcePasswordCracker.bytestoHexString(bruteForcePasswordCracker.getHash(search));
                                BigInteger n = new BigInteger(Integer.toString(bruteForcePasswordCracker.getCharacterSpace(selectedCharacterSpace).length()));
                                BigInteger numToCheck = n.pow(search.length());
                                String possibilities = numToCheck.toString();
                                String explainToUser = "Your password " + search + " has the hash " + passwordHash + "\n" +
                                        "The brute force attack is trying every password permutation with your given password's length and your chosen character space. \n" +
                                        "The brute force attack then converts each password permutation to its hash, and then compares" +
                                        " this hash value to your password's hash value. \n" +
                                        "If these hash values match, your password has been cracked \n\n" +
                                        "The brute force attack will have to check at most " + possibilities + " passwords to crack your password \n\n";
                                resultsTextArea.setText(explainToUser);

                                int timeAllotted = Integer.parseInt(timeOptions.getSelectedItem().toString());
                                result = bruteForcePasswordCracker.findPasswordGivenHash(passwordHash, selectedCharacterSpace, search.length(), timeAllotted);
                            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                result = "Could not crack password";
                            }
                        } else {
                            result = "Characters found in password that are not in character space \n" +
                                    "Please adjust the character space to match your password";
                        }
                    } else {
                        try {
                            passwordHash = dictionaryAttackPasswordCracker.bytestoHexString(dictionaryAttackPasswordCracker.getHash(search));
                            String explainToUser = "Your password " + search + " has the hash " + passwordHash + "\n" +
                                    "The dictionary attack is scanning through a large file of known passwords and converting each password into its hash \n" +
                                    "If a password's hash from the file of passwords matches your password's hash, then your password has been cracked \n\n";
                            resultsTextArea.setText(explainToUser);
                            result = dictionaryAttackPasswordCracker.findPasswordGivenHash(passwordHash);
                        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            result = "Could not find password";
                        }
                    }
                    resultsTextArea.append(result);
                    break;
                case PhysicalAspects:
                    break;
                case End:
                    break;
            }
        });

        searchPanel.add(mainButton);

        panel_game.add(searchPanel);
        panel_game.add(resultsPanel);
    }
    
    public static void main(String[] args) {

        GUIproperties gui = new GUIproperties();
        PhysicalAspects phys = new PhysicalAspects();

        physicalAspects = new PhysicalAspects();
        sqlInjection = new SQLInjection();
        dictionaryAttackPasswordCracker = new DictionaryAttackPasswordCracker();
        bruteForcePasswordCracker = new BruteForcePasswordCracker();
        setCurrentGame(Game.PhysicalAspects);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                sqlInjection.closeConnection();
                System.out.println("DB Connection Closed");
            } catch (SQLException sqlException) {
                System.out.println("Error Closing DB Connection");
            }
        }));

        // Start Game
//        SwingUtilities.invokeLater(SecurityGame::showFrame);

        /* ***** PLAYER STATUS COMPONENTS ***** */
        // JFrame status_frame = new JFrame();
        // status_frame.setTitle("Player Status");

        // JPanel status_bckgrnd = new JPanel();
        // status_bckgrnd.setBackground(Color.WHITE);

        // /* ***** ANSWER OPTIONS COMPONENTS ***** */
        // JFrame options_frame = new JFrame();
        // options_frame.setTitle("Make a Decision");

        // JPanel options_bckgrnd = new JPanel();
        // options_bckgrnd.setBackground(Color.WHITE);

        /* ***** GAME CONTENT FRAME COMPONENTS ***** */
        /* layers: frame -> panel_bckgrnd -> panel_game + panel_buttons */
        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        /* panel on top of frame */
        JPanel panel_bckgrnd = new JPanel();

        panel_game = new JPanel();
        // panel_game.setBackground(Color.GRAY);

        /* ***** PHYSICAL ASPECTS GAME COMPONENTS ***** */
        // JFrame physaspects_frame = new JFrame(); // actual frame
        // JPanel panel_physaspects = new JPanel(); // add panel to create layout
        JPanel panel_question = new JPanel(); // where question displayed
        JPanel panel_answer = new JPanel(); // where text and submit is displayed
        JPanel panel_choices = new JPanel(); // where choices are displayed

        /* call function to ask user's name */
        String name = gui.ask_name();
        gui.get_player_name(name);

        /* add panel to add button */
        JPanel panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.GRAY);
        String b_label = "HIDE HELP";
        JButton help_button = gui.help_button(panel_buttons, b_label);
        // JButton imposter = gui.continue_button(panel_buttons);
        JButton continue_button = gui.continue_button(panel_buttons);

        

        // JButton continue_button = new JButton();
        continue_button.addActionListener(e -> {
            help_button.setText("HIDE HELP");

            JLabel scene = phys.scene_;
            scene.setText(null);

            panel_bckgrnd.remove(panel_question);
            panel_bckgrnd.remove(panel_answer);
            panel_bckgrnd.remove(panel_choices);

            switch(currentGame) {
                case SQL:
                    if (sqlInjection.getProgress().equals(SQLInjection.Progress.Done)) {
                        setCurrentGame(Game.Password);
                        mainButton.setText("Crack Password");
                        searchTextArea.setText("");
                        resultsTextArea.setText("");
                    }
                    break;
                case Password:
                    setCurrentGame(Game.StrongPasswords);
                    searchTextArea.setText("");
                    resultsTextArea.setText("");

                    mainButton.setText("Brute Force Attack");

                    JLabel characterSpaceLabel = new JLabel("Select Password's Character Space:");
                    String[] csChoices = {"Lowercase Characters Only","Lowercase and Uppercase Characters", "Lowercase Characters and Numbers","Lowercase and Uppercase Characters and Numbers"};
                    characterSpaceOptions = new JComboBox<String>(csChoices);
                    characterSpaceOptions.setVisible(true);

                    JPanel characterSpacePanel = new JPanel();
                    characterSpacePanel.add(characterSpaceLabel);
                    characterSpacePanel.add(characterSpaceOptions);

                    panel_game.remove(resultsPanel);
                    panel_game.add(characterSpacePanel);

                    JPanel timeoutPanel = new JPanel();
                    timeoutPanel.add(Box.createRigidArea(new Dimension(180, 0)));
                    JLabel timeoutLabel = new JLabel("Select Length of Time in Seconds to Crack Password:");
                    timeoutPanel.add(timeoutLabel);
                    String[] timeChoices = {"30","45","60","90","120","180"};
                    timeOptions = new JComboBox<String>(timeChoices);
                    timeOptions.setVisible(true);
                    timeoutPanel.add(timeOptions);
                    panel_game.add(timeoutPanel);
                    panel_game.add(resultsPanel);

                    JPanel togglePanel = new JPanel();
                    JButton toggle = new JButton("Switch to Dictionary Attack");

                    mainButton.setPreferredSize(new Dimension(145, 20));

                    toggle.addActionListener(event -> {
                        if(mainButton.getText().equals("Brute Force Attack")) {
                            mainButton.setPreferredSize(new Dimension(145, 20));
                            mainButton.setText("Dictionary Attack");
                            toggle.setText("Switch to Brute Force Attack");
                            characterSpacePanel.setVisible(false);
                            timeoutPanel.setVisible(false);
                        } else {
                            mainButton.setPreferredSize(new Dimension(145, 20));
                            mainButton.setText("Brute Force Attack");
                            toggle.setText("Switch to Dictionary Attack");
                            characterSpacePanel.setVisible(true);
                            timeoutPanel.setVisible(true);
                        }
                    });
                    togglePanel.add(Box.createRigidArea(new Dimension(440, 0)));
                    togglePanel.add(toggle);
                    togglePanel.add(Box.createRigidArea(new Dimension(50, 0)));

                    panel_game.add(togglePanel);

                    break;
                case StrongPasswords:
                    break;
                case PhysicalAspects:
                    setCurrentGame(Game.SQL);
                    setupInteractiveGames();
                    break;
                case End:
                    break;
            }
        });
        JButton exit_button = gui.exit_button(panel_buttons);

        /* ***** MAIN PANEL COMPONENTS ***** */
        /* call function to set up button layout and main window design */
        gui.interface_layout(panel_bckgrnd, panel_game, panel_buttons);

        /* ***** ADD GAME CONTENT HERE ***** */
        /* spacing between border and content */
        panel_game.setBorder(new EmptyBorder(50, 20, 20, 20));

        /* add player name */
        JLabel player_name = gui.greeting_label(name);
        JOptionPane.showMessageDialog(null, player_name);
        /* update frame to have player name displayed */
        frame.setTitle("Cyber Adventure - Currently Playing: " + name);
        // physical.info();
        /* show message to tell user what to do */
        String html = "<html>** Occupation: ethical hacker <br><br>"
                + "Your Task: You are hired by a private company that specializes in selling rare antiques to high-end customers. <br>"
                + "Recently, there has been a rise in crime targeting websites selling high-end products, and the company is <br>"
                + "worried that they might be a target. So, they decided to hire you to test the security of their site. ** <br><br> </html>";

        JLabel proceed = gui.task_label(html);
        proceed.setText(html);
        panel_buttons.add(proceed);
        panel_game.add(proceed);

        /* physical aspects section */
        JButton next_game = new JButton("NEXT >>>");
        JButton next_alt = new JButton("NEXT >>>"); // when switching roles, to sql
        JButton next_alt2 = new JButton("NEXT >>>"); // when switching roles, to password

        phys.info();
        phys.intro_lesson();
        phys.interface_layout(frame, panel_game, panel_bckgrnd, panel_question, panel_answer, panel_choices);
        JTextField answer = phys.answer_input();
        JButton submit_b = phys.submit();

        phys.add_panels(panel_game, answer, submit_b);
        /* QUESTION 1 */
        phys.scene_one(answer, submit_b, panel_game, proceed);
        /* QUESTION 2 */
        JButton submit_b2 = phys.submit();
        phys.scene_two(answer, submit_b2, next_alt, next_alt2);
        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK */
        phys.transition_game(next_game);
        phys.scene_two_alt(answer, submit_b2, next_alt, next_game);
        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK, DIFFERENT TEXT DISPLAYED */
        phys.scene_two_alt2(answer, submit_b2, next_alt2, next_game);
        /* QUESTION 3 */
        phys.ethical_hacker_route(answer, continue_button);
        /* physical aspects section */

        /* ***** DISPLAY PANEL WHERE GAME CONTENT WILL BE PLACED ***** */
        panel_bckgrnd.add(panel_game);

        /* ***** BUTTONS PANEL COMPONENTS ***** */
        panel_bckgrnd.add(panel_buttons); // add panel with buttons
        panel_buttons.add(exit_button); // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(500, 0))); // spacing between buttons
        panel_buttons.add(help_button); // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(0, 20))); // spacing between buttons
        panel_buttons.add(continue_button);
        panel_buttons.add(Box.createRigidArea(new Dimension(0, 120))); // spacing between buttons

        /* ***** MAIN FRAME PROPERTIES ***** */
        frame.getContentPane().add(panel_bckgrnd);
        frame.setSize(1100, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* ***** ACTIONLISTENER FOR CONTINUE AND EXIT BUTTONS ***** */
        help_button.addActionListener(new ActionListener() {
            boolean hasBeenClicked = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hasBeenClicked) {
                    // proceed.setText(null);

                    String b_label = "HIDE HELP";
                    help_button.setText(b_label);

                    helpFrame.setVisible(true);

                } else if (hasBeenClicked) {
                    String b_label = "SHOW HELP";
                    help_button.setText(b_label);
                    helpFrame.dispose();
                    helpFrame.setVisible(false);
                }
                hasBeenClicked = !hasBeenClicked;
            } // end void actionPerformed()
        }); // end actionListener()

        exit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                helpFrame.dispose();
                frame.dispose();
            } // end void actionPerformed()
        }); // end actionListener()

    } // end main
} // end class