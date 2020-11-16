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
    public static GUIproperties gui;

    public enum Game {
        SQL, Password, PhysicalAspects, StrongPasswords, End
    }

    public static Game currentGame;
    public static JFrame helpFrame;
    public static JPanel panel_game;
    public static JPanel resultsPanel;
    public static JPanel utilityPanel;
    public static JPanel panel_buttons;
    public static JScrollPane resultsPane;
    public static JButton mainButton;
    public static JButton answerButton;
    public static JLabel searchLabel;
    public static JTextArea searchTextArea;
    public static JTextArea resultsTextArea;
    public static JTextArea helpTextArea;
    public static JTextArea utilityTextArea;
    public static JComboBox<String> characterSpaceOptions;
    public static JComboBox<String> timeOptions;
    public static int numAttempts;


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

    public static void setUpResultsPanel() {
        /* ***** RESULTS COMPONENT ***** */
        resultsPanel = new JPanel();
        resultsTextArea = new JTextArea();
        resultsPane = new JScrollPane(resultsTextArea);
        resultsPane.setPreferredSize(new Dimension(820, 200));
        resultsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** RESULTS PANEL PROPERTIES ***** */
        resultsPanel.setVisible(true);
        resultsPanel.add(resultsPane);
        resultsPanel.add(Box.createRigidArea(new Dimension(95, 0)));
    }

    public static void setUpUtilityPanel() {
        /* ***** UTILITY COMPONENT ***** */
        utilityPanel = new JPanel();
        utilityTextArea = new JTextArea();
        JScrollPane utilityPane = new JScrollPane(utilityTextArea);
        utilityPane.setPreferredSize(new Dimension(820, 100));
        utilityPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** UTILITY PANEL PROPERTIES ***** */
        utilityPanel.setVisible(true);
        utilityPanel.add(utilityPane);
    }

    public static void createAnswerButton() {
        answerButton = gui.answer_button(panel_buttons);
        answerButton.setVisible(false);
        answerButton.addActionListener(event -> {
            numAttempts = 0;
            JFrame answerFrame = new JFrame();
            answerFrame.setTitle("SQL Injection Answer");
            JTextArea answerTextArea = new JTextArea();
            JScrollPane answerPane = new JScrollPane(answerTextArea);
            answerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            answerFrame.setSize(1200, 300);
            answerFrame.setVisible(true);
            answerFrame.getContentPane().add(answerPane);

            SQLInjection.Progress currentProgress = sqlInjection.getProgress();
            String answer = "";
            switch (currentProgress) {
                case DatabaseType:
                    answer = "ANSWER: \n" +
                            "' UNION SELECT version()-- \n\n" +
                            "Explanation: \n" +
                            "You need to start your SQL injection with a single quote (') in order to escape out of the quotes in place \n" +
                            "If you look at the lowest portion of the game screen, you will see that whatever you search will appear between two single quote (ignore the % symbols for now) \n" +
                            "These quotes tell the database to treat whatever is between them as something to search for in the database and will not be executed as actual SQL code. \n" +
                            "We want the database to execute the commands we give it and thus we need to start with a single quote so the database will think we are searching for nothing in particular --> '%' is what the database will think we are searching for \n\n" +
                            "We end our SQL injection with -- because this is the command to let the SQL database know everything afterwards should be treated as a comment and not executed. \n" +
                            "Therefore, by adding the -- at the end, we tell the database to ignore this part of the pre-written command, %' AND available = TRUE \n\n" +
                            "Finally, we include the UNION command before the SELECT statement in order to return the database version with the results of the pre-written SQL command, which is SELECT item FROM inventory WHERE item ILIKE '%' \n" +
                            "That pre-written command means to return all items in the inventory table that anything (the % symbol is a wildcard meaning it can be anything)";
                    break;
                case PublicTableNames:
                    answer = "ANSWER: \n" +
                            "' UNION SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'-- \n\n" +
                            "Explanation: \n" +
                            "Once again, you need to start your SQL injection with a single quote (') in order to escape out of the quotes in place \n" +
                            "You also still need to include a UNION command before your SELECT statement to return the items from your injected SELECT statement \n" +
                            "Finally, you still need to include the comment command (--) at the end of your SQL command in order for the database to ignore the rest of the pre-written command \n\n" +
                            "Since we know its a PostgreSQL database now, we can query the information_schema.tables for all the public tables in the database \n" +
                            "In order to extract usernames and passwords, we need to know the name of the table that might contain them";
                    break;
                case ColumnNames:
                    answer = "ANSWER: \n" +
                            "' UNION SELECT column_name FROM information_schema.columns WHERE table_name = 'users'-- \n\n" +
                            "Explanation: \n" +
                            "Once again, you need to start your SQL injection with a single quote (') in order to escape out of the quotes in place \n" +
                            "You also still need to include a UNION command before your SELECT statement to return the items from your injected SELECT statement \n" +
                            "Finally, you still need to include the comment command (--) at the end of your SQL command in order for the database to ignore the rest of the pre-written command \n\n" +
                            "Since we know there is a table called users, which is likely where usernames and passwords are stored, we need the column names from this table \n" +
                            "Once we have the column names, we will be able to issue a SELECT statement on the users table with these column names \n" +
                            "Similar to the previous answer, we can SELECT the column names in users from the information_schema.columns \n";
                    break;
                case UsernamesAndPasswords:
                    answer = "ANSWER: \n" +
                            "' UNION SELECT username || ' ' || password FROM users-- \n\n" +
                            "Explanation: \n" +
                            "Once again, you need to start your SQL injection with a single quote (') in order to escape out of the quotes in place \n" +
                            "You also still need to include a UNION command before your SELECT statement to return the items from your injected SELECT statement \n" +
                            "Finally, you still need to include the comment command (--) at the end of your SQL command in order for the database to ignore the rest of the pre-written command \n\n" +
                            "This step requires a trick – string concatenation \n" +
                            "A UNION of two SELECT only works properly if both SELECT statements return the same number of items\n" +
                            "In our case, the pre-written, first SELECT statement only returns one column, the item from inventory \n" +
                            "Since we want both the username and its corresponding password, we need to concatenate them together using the string concatenation command ||\n" +
                            "username || ' ' || password means we want to concatenate the username, a space (this is the ' ' between the two sets of ||), and the password";
                    break;
                default:
                    break;
            }

            answerTextArea.setText(answer);
            answerButton.setVisible(false);
        });
        panel_buttons.add(answerButton);
    }

    public static void setupInteractiveGames() {
        setUpResultsPanel();
        setUpUtilityPanel();

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

        searchLabel = new JLabel("Search for an item in inventory:");
        searchPanel.add(searchLabel);
        searchPanel.add(searchPane);
        searchPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        mainButton = new JButton("Search");
        mainButton.setPreferredSize(new Dimension(138, 20));
        mainButton.addActionListener(e -> {
            String search = searchTextArea.getText();
            search = search.replaceAll("\\n", "");
            search = search.replaceAll("\\r", "");
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
                            sqlInjection.setProgress(SQLInjection.Progress.PublicTableNames);

                            String helpAndHints = "<html>Now that you know its a PostgreSQL database, you now want to find the names of the tables in this database <br>" +
                                    "Use the SELECT statement to return the column table_name FROM the table information_schema.tables <br><br>" +
                                    "Remember to start your SQL command with a single quote (') and a <br>UNION statement and to end your SQL statement with a comment command, which is -- <br></html>";
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

                    utilityTextArea.setText(sqlStatement);

                    if(numAttempts == 6) {
                        answerButton.setVisible(true);
                    }
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
        panel_game.add(utilityPanel);
    }
    
    public static void main(String[] args) {
        physicalAspects = new PhysicalAspects();
        sqlInjection = new SQLInjection();
        dictionaryAttackPasswordCracker = new DictionaryAttackPasswordCracker();
        bruteForcePasswordCracker = new BruteForcePasswordCracker();
        setCurrentGame(Game.PhysicalAspects);
        numAttempts = 0;

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
        gui = new GUIproperties();


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
        panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.GRAY);
        panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.DARK_GRAY);
        String b_label = "HIDE HELP";
        JButton help_button = gui.help_button(panel_buttons, b_label);
        // JButton imposter = gui.continue_button(panel_buttons);
        JButton continue_button = gui.continue_button(panel_buttons);



        // JButton continue_button = new JButton();
        continue_button.addActionListener(e -> {
            help_button.setText("HIDE HELP");

            JLabel scene = physicalAspects.scene_;
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
                        utilityTextArea.setText(resultsTextArea.getText());
                        resultsTextArea.setText("");
                        searchLabel.setText("Enter Password Here:");
                    }
                    break;
                case Password:
                    setCurrentGame(Game.StrongPasswords);
                    searchTextArea.setText("");
                    resultsTextArea.setText("");
                    utilityPanel.setVisible(false);

                    mainButton.setText("Brute Force Attack");

                    JLabel characterSpaceLabel = new JLabel("Select Password's Character Space:");
                    String[] csChoices = {"Lowercase Characters Only","Lowercase and Uppercase Characters", "Lowercase Characters and Numbers","Lowercase and Uppercase Characters and Numbers","Lowercase and Uppercase Characters, Number, and Special Characters"};
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
                    String[] timeChoices = {"30","45","60","90","120","180","240","300"};
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
                    resultsPane.setPreferredSize(new Dimension(900, 250));
                    break;
                case StrongPasswords:
                    break;
                case PhysicalAspects:
                    setCurrentGame(Game.SQL);
                    setupInteractiveGames();

                    JFrame sqlExplanationFrame = new JFrame();
                    sqlExplanationFrame.setTitle("SQL Injection");
                    JTextArea sqlExplanationTextArea = new JTextArea();
                    JScrollPane sqlExplanationPane = new JScrollPane(sqlExplanationTextArea);
                    sqlExplanationPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                    sqlExplanationFrame.setSize(new Dimension(1000, 150));
                    sqlExplanationFrame.setVisible(true);
                    sqlExplanationFrame.getContentPane().add(sqlExplanationPane);

                    String sqlExplanation = "You are to perform an SQL Injection attack to retrieve the usernames and passwords stored in this store's database \n" +
                            "An SQL Injection is essentially the act of typing in SQL code into the search bar instead of searching for items normally \n\n" +
                            "The pre-written SQL command being used by the backend software is SELECT item FROM inventory WHERE item ILIKE '% whatever you searched %' AND available = TRUE \n" +
                            "This command means the database is going to SELECT (and return) all the items FROM the inventory table that match whatever you searched for and are also available \n" +
                            "An SQL Injection circumvents the intended logic of this pre-written SQL statement by inserting SQL code into the 'whatever you search' part \n" +
                            "The current page has a search bar up top for a user to find current items in the store's inventory \n\n" +
                            "Below the search bar is the area where result from your search are returned \n" +
                            "The area lowest on the screen shows you the SQL statement executed by the store's backend software to find the item you searched for.\n" +
                            "Use this lowest area on the screen to help you with your SQL injection \n";

                    sqlExplanationTextArea.setText(sqlExplanation);
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
        phys.interface_layout(frame, panel_game, panel_bckgrnd, panel_buttons, panel_question, panel_answer, panel_choices);
        JTextField answer = phys.answer_input();
        JButton submit_b = phys.submit();

        phys.add_panels(panel_game, answer, submit_b, help_button, continue_button);
        /* QUESTION 1 */
        phys.scene_one(answer, submit_b, panel_game, proceed, help_button, continue_button);
        /* QUESTION 2 */
        JButton submit_b2 = phys.submit();
        phys.scene_two(answer, submit_b2, next_alt, next_alt2, help_button, continue_button);
        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK */
        phys.transition_game(next_game, help_button, continue_button);
        phys.scene_two_alt(answer, submit_b2, next_alt, next_game, help_button, continue_button);
        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK, DIFFERENT TEXT DISPLAYED */
        phys.scene_two_alt2(answer, submit_b2, next_alt2, next_game, help_button, continue_button);
        /* QUESTION 3 */
        phys.ethical_hacker_route(answer, help_button, continue_button);
        /* physical aspects section */

        /* ***** DISPLAY PANEL WHERE GAME CONTENT WILL BE PLACED ***** */
        panel_bckgrnd.add(panel_game);

        /* ***** BUTTONS PANEL COMPONENTS ***** */
        panel_bckgrnd.add(panel_buttons); // add panel with buttons
        panel_buttons.add(exit_button); // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(200, 0))); // spacing between buttons
        createAnswerButton();
        panel_buttons.add(help_button); // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(20, 20))); // spacing between buttons
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