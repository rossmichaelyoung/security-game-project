import java.math.BigInteger;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

class CyberAdventure extends JPanel {

    public static PhysicalAspects phys;
    public static SQLInjection sqlInjection;
    public static DictionaryAttackPasswordCracker dictionaryAttackPasswordCracker;
    public static BruteForcePasswordCracker bruteForcePasswordCracker;
    public static GUIproperties gui;

    public enum Game {
        SQL, Password, PhysicalAspects, StrongPasswords, End
    }

    public static Game currentGame;
    public static JFrame answerFrame;
    public static JFrame helpFrame;
    public static JFrame explanationFrame;
    public static JFrame sqlBasicsFrame;
    public static JPanel panel_game;
    public static JPanel resultsPanel;
    public static JPanel utilityPanel;
    public static JPanel panel_buttons;
    public static JPanel timeoutPanel;
    public static JPanel characterSpacePanel;
    public static JScrollPane resultsPane;
    public static JButton mainButton;
    public static JButton answerButton;
    public static JButton sqlBasicsButton;
    public static JButton continue_button;
    public static JLabel searchLabel;
    public static JTextArea searchTextArea;
    public static JTextArea resultsTextArea;
    public static JEditorPane helpTextArea;
    public static JTextArea utilityTextArea;
    public static JComboBox<String> characterSpaceOptions;
    public static JComboBox<String> timeOptions;
    public static int numAttempts;

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
        resultsTextArea.setEditable(false);
        resultsPane = new JScrollPane(resultsTextArea);
        resultsPane.setPreferredSize(new Dimension(680, 200));
        resultsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** RESULTS PANEL PROPERTIES ***** */
        resultsPanel.setVisible(true);
        resultsPanel.add(Box.createRigidArea(new Dimension(43, 0)));
        resultsPanel.add(resultsPane);
    }

    public static void setUpUtilityPanel() {
        /* ***** UTILITY COMPONENT ***** */
        utilityPanel = new JPanel();
        utilityTextArea = new JTextArea();
        utilityTextArea.setEditable(false);
        JScrollPane utilityPane = new JScrollPane(utilityTextArea);
        utilityPanel.add(Box.createRigidArea(new Dimension(46, 0)));

        utilityPane.setPreferredSize(new Dimension(680, 100));
        utilityPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** UTILITY PANEL PROPERTIES ***** */
        utilityPanel.setVisible(true);
        utilityPanel.add(utilityPane);

    }

    public static JButton createAnswerButton() {
        answerFrame = new JFrame();
        answerFrame.setTitle("Answer");
        JTextArea answerTextArea = new JTextArea();
        answerTextArea.setEditable(false);
        JScrollPane answerPane = new JScrollPane(answerTextArea);
        answerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        answerFrame.setSize(1200, 300);
        answerFrame.setVisible(false);
        answerFrame.getContentPane().add(answerPane);

        JButton button = gui.answer_button(panel_buttons);
        button.setVisible(false);
        button.addActionListener(event -> {
            if (currentGame == Game.SQL) {
                if(button.getText().equals("SHOW ANSWER")) {
                    button.setText("HIDE ANSWER");
                    SQLInjection.Progress currentProgress = sqlInjection.getProgress();
                    String answer = "";
                    switch (currentProgress) {
                        case DatabaseType:
                            answer = "ANSWER (EXECUTE THE BELOW COMMAND IN THE TOP SEARCH BAR TO MOVE ON): \n" +
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
                            answer = "ANSWER (EXECUTE THE BELOW COMMAND IN THE TOP SEARCH BAR TO MOVE ON): \n" +
                                    "' UNION SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'-- \n\n" +
                                    "Explanation: \n" +
                                    "Once again, you need to start your SQL injection with a single quote (') in order to escape out of the quotes in place \n" +
                                    "You also still need to include a UNION command before your SELECT statement to return the items from your injected SELECT statement \n" +
                                    "Finally, you still need to include the comment command (--) at the end of your SQL command in order for the database to ignore the rest of the pre-written command \n\n" +
                                    "Since we know its a PostgreSQL database now, we can query the information_schema.tables for all the public tables in the database \n" +
                                    "In order to extract usernames and passwords, we need to know the name of the table that might contain them";
                            break;
                        case ColumnNames:
                            answer = "ANSWER (EXECUTE THE BELOW COMMAND IN THE TOP SEARCH BAR TO MOVE ON): \n" +
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
                            answer = "ANSWER (EXECUTE THE BELOW COMMAND IN THE TOP SEARCH BAR TO MOVE ON): \n" +
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
                    answerFrame.setVisible(true);
                } else {
                    button.setText("SHOW ANSWER");
                    answerFrame.setVisible(false);
                }
            }
        });

        return button;
    }

    public static JButton createPasswordCrackerAttackButton() {
        JButton button = gui.password_cracker_attack_button(panel_buttons);
        button.setVisible(false);
        button.addActionListener(event -> {
            if (currentGame == Game.StrongPasswords) {
                if (mainButton.getText().equals("Brute Force Attack")) {
                    mainButton.setPreferredSize(new Dimension(145, 20));
                    mainButton.setText("Dictionary Attack");
                    button.setText("SWITCH TO BRUTE FORCE ATTACK");
                    characterSpacePanel.setVisible(false);
                    timeoutPanel.setVisible(false);
                } else {
                    mainButton.setPreferredSize(new Dimension(145, 20));
                    mainButton.setText("Brute Force Attack");
                    button.setText("SWITCH TO DICTIONARY ATTACK");
                    characterSpacePanel.setVisible(true);
                    timeoutPanel.setVisible(true);
                }
            }
        });

        return button;
    }

    public static void createSQLBasicsButton() {
        sqlBasicsFrame = new JFrame();
        sqlBasicsFrame.setTitle("SQL Basics");
        JEditorPane sqlBasicsTextArea = new JEditorPane();
        sqlBasicsTextArea.setContentType("text/html");
        sqlBasicsTextArea.setEditable(false);
        JScrollPane sqlBasicsPane = new JScrollPane(sqlBasicsTextArea);
        sqlBasicsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        sqlBasicsFrame.setSize(1200, 400);
        sqlBasicsFrame.setVisible(false);
        sqlBasicsFrame.getContentPane().add(sqlBasicsPane);

        sqlBasicsButton = gui.continue_button(panel_buttons);
        sqlBasicsButton.setVisible(false);
        sqlBasicsButton.setText("SHOW SQL BASICS");
        sqlBasicsButton.addActionListener(event -> {
            if(sqlBasicsButton.getText().equals("SHOW SQL BASICS")) {
                sqlBasicsButton.setText("HIDE SQL BASCIS");
                String sqlBasics = "<html><b>SQL Basics For SQL Injection </b> <br/>" +
                        "SQL (Structured Query Language) is a language used in programming to communicate with a database <br/>" +
                        "Below are some commands in SQL that might be useful <br/><br/>" +
                        "1. SELECT column_name FROM table_name <br/>" +
                        "   The above line tells a database to return all the values in the column_name column from the table table_name <br/><br/>" +
                        "   Example: say there is a table called inventory and a column in inventory called items. To get all the items from inventory, you would use the command: <br/>" +
                        "   SELECT items FROM inventory <br/><br/>" +
                        "2. SELECT column_name1 FROM table_name WHERE column_name2 = 'some value' <br/>" +
                        "   The above line does the same as #1 except the database will now only return the values in column_name1 where the value in column_name2 equals the value you specified <br/>" +
                        "   The value you specify in the WHERE clause needs to be between single quotes <br/><br/>" +
                        "   Example: using the example table from (1), say you want only the items that are called 'chair'. To do this, you would use the command: <br/>" +
                        "   SELECT items FROM inventory WHERE items = 'chair' <br/><br/>" +
                        "3. SELECT column_name1 FROM table_name1 UNION SELECT column_name2 FROM table_name2 <br/>" +
                        "   The above line uses the keyword UNION to return values from two separate tables (table_name1 and table_name2) <br/>" +
                        "   The UNION command is very important for an SQL injection because we want to return information from the table containing usernames and passwords and the information the search normally returns <br/><br/>" +
                        "4. SELECT column_name1 || ' ' || column_name2 FROM table_name <br/>" +
                        "   The above line concatenates the values in column_name1, a space, and column_name2 together in one line <br/>" +
                        "   column_name1 column_name2 </html>";

                sqlBasicsTextArea.setText(sqlBasics);
                sqlBasicsFrame.setVisible(true);
            } else {
                sqlBasicsButton.setText("SHOW SQL BASICS");
                sqlBasicsFrame.setVisible(false);
            }
        });
        panel_buttons.add(sqlBasicsButton);
    }

    public static void setAnswerButtonForNextStep() {
        numAttempts = 0;
        answerButton.setText("SHOW ANSWER");
        answerButton.setVisible(false);
        answerFrame.setVisible(false);
    }

    public static void setupInteractiveGames() {
        setUpResultsPanel();
        setUpUtilityPanel();

        /* ***** HELP COMPONENTS ***** */
        helpFrame = new JFrame();
        helpFrame.setTitle("Help");
        helpTextArea = new JEditorPane();
        helpTextArea.setContentType("text/html");
        helpTextArea.setEditable(false);
        JScrollPane helpPane = new JScrollPane(helpTextArea);
        helpPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** HELP FRAME PROPERTIES ***** */
        helpFrame.setSize(900, 375);
        helpFrame.setVisible(false);
        helpFrame.getContentPane().add(helpPane);

        String help = "<html>First you need to find what kind of database this application is using <br/>\n" +
                "Try the following SELECT statements one at a time: <br/>\n" +
                "SELECT @@version <br/>\n" +
                "SELECT * FROM v$version <br/>\n" +
                "SELECT version() <br/><br/>\n\n" +
                "Remember to start your SQL command with a single quote (') and a UNION statement and to end your SQL statement with a comment command, which is -- \n</html>";
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
//        searchPanel.add(Box.createRigidArea(new Dimension(400,0)));

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
                    if(!sqlInjection.getProgress().equals(SQLInjection.Progress.Done)) {
                        numAttempts++;
                    }

                    String sqlStatement = "SQL Statement Executed From Your Search, " + search + " : \n" +
                            "SELECT item \n" +
                            "FROM inventory \n" +
                            "WHERE item ILIKE '%" + search + "%' AND available = TRUE";
                    try {
                        String result = sqlInjection.selectItem(search);
                        if (sqlInjection.getProgress().equals(SQLInjection.Progress.DatabaseType) && result.contains("PostgreSQL 12.4")) {
                            sqlInjection.setProgress(SQLInjection.Progress.PublicTableNames);

                            setAnswerButtonForNextStep();

                            String helpAndHints = "<html>Now that you know its a PostgreSQL database, you now want to find the names of the public tables in this database <br>" +
                                    "Use the SELECT statement to return the column table_name FROM the table information_schema.tables <br>" +
                                    "Also, specify in the WHERE clause for the table_schema to be equal to 'public' <br><br>" +
                                    "Remember to start your SQL injection (your search) with a single quote (') and a <br>UNION statement and to end your SQL statement with a comment command, which is -- <br></html>";
                            helpTextArea.setText(helpAndHints);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.PublicTableNames) && result.contains("users")) {
                            sqlInjection.setProgress(SQLInjection.Progress.ColumnNames);

                            setAnswerButtonForNextStep();

                            String helpAndHints = "<html>You now see there are two public tables: inventory and users. <br/>\n" +
                                    "The users tables likely holds interesting information we want to extract. <br/>\n" +
                                    "Try to find the names of the columns in the users table. <br/><br/>\n" +
                                    "Hint: use a SELECT statement to return the column column_name FROM information_schema.columns <br/>\n" +
                                    "Don't forget to use a WHERE clause to specify that the table_name needs to be equal to 'users' <br/>\n" +
                                    "<br/>\n" +
                                    "Remember to start your SQL injection (your search) with a single quote (') and a <br>UNION statement and to end your SQL statement with a comment command, which is -- <br></html>";
                            helpTextArea.setText(helpAndHints);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.ColumnNames) && result.contains("password")) {
                            sqlInjection.setProgress(SQLInjection.Progress.UsernamesAndPasswords);

                            setAnswerButtonForNextStep();

                            String helpAndHints = "<html>You now know the users table has two columns – username and password <br/>\n" +
                                    "Since we want both but a UNION can only return the same number of items in the prior SELECT statement, <br/>\n" +
                                    "you have to use string concatenation to get both column values to return <br/>\n" +
                                    "Example: SELECT field1 || ' ' || field2 FROM table <br/>\n" +
                                    "This returns the value in field1, a space, and then the value in field2, all on one line. <br/><br/>\n" +
                                    "\n" +
                                    "Remember to start your SQL injection (your search) with a single quote (') and a <br>UNION statement and to end your SQL statement with a comment command, which is -- <br></html>";
                            helpTextArea.setText(helpAndHints);
                        } else if (sqlInjection.getProgress().equals(SQLInjection.Progress.UsernamesAndPasswords) && result.contains("johnsmith") && result.contains("dc4b56ff4967374b261a29cd4a90580d")) {
                            sqlInjection.setProgress(SQLInjection.Progress.Done);

                            setAnswerButtonForNextStep();
                            continue_button.setVisible(true);

                            String helpAndHints = "<html>The seemingly random strings of 32 characters is a 128-bit hash of a user's real password</html>";
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
                    search = search.trim();
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
                                        "The brute force attack is trying every password permutation with your given password's length and your chosen character set. \n" +
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
                            result = "Characters found in password that are not in character set \n" +
                                    "Please adjust the character set to match your password";
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

    public static void displayExplanation() {
        explanationFrame = new JFrame();
        JEditorPane explanationTextArea = new JEditorPane();
        explanationTextArea.setContentType("text/html");
        explanationTextArea.setEditable(false);
        JScrollPane explanationPane = new JScrollPane(explanationTextArea);
        explanationPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        explanationFrame.setSize(new Dimension(1000, 400));
        explanationFrame.setVisible(true);
        explanationFrame.getContentPane().add(explanationPane);

        String explanation = "";
        if(currentGame == Game.SQL) {
            explanationFrame.setTitle("SQL Injection");
            explanation = "<html>You are to perform an SQL Injection attack to retrieve the usernames and passwords stored in this store's database. <br/>\n" +
                    "An SQL Injection is essentially the act of typing in SQL code into the search bar instead of searching for items normally. <br/><br/>\n\n" +
                    "The pre-written SQL command being used by the backend software is: <br/> SELECT item FROM inventory WHERE item ILIKE '% whatever you searched %' AND available = TRUE <br/><br/>\n" +
                    "This command means the database is going to SELECT (and return) all the items FROM the inventory table that match whatever you searched for and are also available. <br/>\n" +
                    "An SQL Injection circumvents the intended logic of this pre-written SQL statement by inserting SQL code into the 'whatever you search' part. <br/><br/>\n" +
                    "The current page has a search bar up top for a user to find current items in the store's inventory. <br/>\n" +
                    "Below the search bar is the area where result from your search are returned. <br/>\n" +
                    "The area lowest on the screen shows you the SQL statement executed by the store's backend software to find the item you searched for.<br/>\n" +
                    "Use this lowest area on the screen to help you with your SQL injection. </html>\n";
        } else if (currentGame == Game.Password) {
            explanationFrame.setTitle("Password Cracker");
            explanation = "<html>" +
                    "<b>You are now to crack the passwords you just retrieved from your SQL injection</b> <br/>" +
                    "Copy and Paste the seemingly random 32 character strings into the box at the top on at a time and click 'Crack Password'. <br/>" +
                    "In the background, a password cracker is working to find out what password this seemingly random string corresponds to. <br/>" +
                    "This random string is a hash, which is what websites store in their database instead of an actual password. <br/><br/>" +
                    "Hashes are a one-way function, meaning you cannot reverse the process, that transforms a string into a seemingly random assortment of characters. <br/>" +
                    "Hashes are not random however because the same password will always produce the same hash value. <br/>" +
                    "Websites store a hash of your password instead of your actual password to make it harder for hackers to steal your information. <br/></br>" +
                    "As you will see, this does not prevent your information from being discovered. It just takes more effort on the attacker's part. <br/>" +
                    "This is why strong passwords are so important – the stronger the password, the harder it is for an attacker to get your password even if he or she hacked a database to get your credentials and information </html>";
        } else if (currentGame == Game.StrongPasswords) {
            explanationFrame.setTitle("What is a Strong Password");
            explanation = "<html>" +
                    "You now have the opportunity to try out various passwords of different lengths and character sets. <br/>" +
                    "The password you enter at the top will be converted to its hash similar to the passwords you extracted from the SQL injection. <br/>" +
                    "Whichever attack you choose to run on the password (brute force or dictionary) will try to crack the password you have entered using your entered password's hash, simulating a real scenario of cracking a password. <br/><br/>" +
                    "The character set is the number of different characters a password uses. <br/>" +
                    "Some passwords might only use lowercase letters, which means this password has a character set length of 26. <br/>" +
                    "The longer your password's character set length and the longer your password itself is, the harder it is to crack your password using a brute force attack. <br/><br/>" +
                    "A brute force attack attempts to crack a password by trying every possible combination of characters in a character set for a given length of password. <br/>" +
                    "The maximum number of possibilities a brute force attack has to try to crack a password is (the character set's length) ^ (the password's length) <br/><br/>" +
                    "After you have tried brute force attacks on various passwords, switch to the dictionary attack and see if passwords that could not be broken by brute force can be broken by the dictionary attack. <br/><br/>" +
                    "A dictionary attack scans through a file of known passwords, computes each password's hash, and compares the the hash value to the target hash value. If they match, the password as been cracked. <br/>" +
                    "A dictionary attack has the advantage of not wasting time computing passwords that no one would likely use, unlike the brute force attack, making a dictionary attack often faster than a brute force attack. <br/>" +
                    "The draw back of a dictionary attack is if a given password is not in the dictionary file, it cannot be cracked. <br/><br/>" +
                    "<b>Click on the Help button to get more information about how to analyze what makes a strong password</b>" +
                    "</html>";
        }
        explanationTextArea.setText(explanation);
    }

    public static void main(String[] args) {
        phys = new PhysicalAspects();
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

        gui = new GUIproperties();


        /* ***** GAME CONTENT FRAME COMPONENTS ***** */
        /* layers: frame -> panel_bckgrnd -> panel_game + panel_buttons */
        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        /* panel on top of frame */
        JPanel panel_bckgrnd = new JPanel();

        panel_game = new JPanel();

        /* ***** PHYSICAL ASPECTS GAME COMPONENTS ***** */
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

        JButton help_button = gui.help_button(panel_buttons);
        JButton directions_button = gui.directions_button(panel_buttons);
        JButton passwordCrackerAttackButton = createPasswordCrackerAttackButton();
        continue_button = gui.continue_button(panel_buttons);

        continue_button.addActionListener(e -> {
            switch(currentGame) {
                case SQL:
                    if (sqlInjection.getProgress().equals(SQLInjection.Progress.Done)) {
                        setCurrentGame(Game.Password);
                        mainButton.setText("Crack Password");
                        searchTextArea.setText("");
                        utilityTextArea.setText(resultsTextArea.getText());
                        resultsTextArea.setText("");
                        searchLabel.setText("Enter Password Here:");

                        explanationFrame.setVisible(false);
                        displayExplanation();
                        directions_button.setText("HIDE DIRECTIONS");
                        sqlBasicsButton.setVisible(false);
                        sqlBasicsFrame.setVisible(false);
                    }
                    break;
                case Password:
                    setCurrentGame(Game.StrongPasswords);
                    searchTextArea.setText("");
                    resultsTextArea.setText("");
                    utilityPanel.setVisible(false);

                    mainButton.setText("Brute Force Attack");

                    JLabel characterSpaceLabel = new JLabel("Select Password's Character Set:");
                    String[] csChoices = {"Lowercase Letters Only","Lowercase and Uppercase Letters", "Lowercase Letters and Numbers","Lowercase and Uppercase Letters and Numbers","Lowercase and Uppercase Letters, Numbers, and Special Characters"};
                    characterSpaceOptions = new JComboBox<String>(csChoices);
                    characterSpaceOptions.setVisible(true);

                    characterSpacePanel = new JPanel();
                    characterSpacePanel.add(characterSpaceLabel);
                    characterSpacePanel.add(characterSpaceOptions);

                    panel_game.remove(resultsPanel);
                    panel_game.add(characterSpacePanel);

                    timeoutPanel = new JPanel();
                    JLabel timeoutLabel = new JLabel("Select Time Limit in Seconds for Cracking Password:");
                    timeoutPanel.add(Box.createRigidArea(new Dimension(180, 0)));
                    timeoutPanel.add(timeoutLabel);
                    String[] timeChoices = {"30","45","60","90","120","180","240","300"};
                    timeOptions = new JComboBox<String>(timeChoices);
                    timeOptions.setVisible(true);
                    timeoutPanel.add(timeOptions);
                    panel_game.add(timeoutPanel);
                    panel_game.add(resultsPanel);

                    passwordCrackerAttackButton.setVisible(true);
                    passwordCrackerAttackButton.setText("SWITCH TO DICTIONARY ATTACK");
                    resultsPane.setPreferredSize(new Dimension(900, 250));
                    mainButton.setPreferredSize(new Dimension(145, 20));

                    explanationFrame.setVisible(false);
                    displayExplanation();
                    directions_button.setText("HIDE DIRECTIONS");

                    String help = "<html>" +
                            "<b>Directions</b><br><br>" +
                            "1. Start by entering a password that is less than 6 characters long and uses the 'Lowercase Letters Only' character set <br/>" +
                            "   Notice how long it takes to crack a password with these constraints <br/><br/>" +
                            "2. Now increase the length of your password to greater than 6 characters and still use the 'Lowercase Letters Only' character set <br/>" +
                            "   If the brute force attack times out, retry the attack with a longer allotted segment of time <br/><br/>" +
                            "3. Repeat the above two steps but now vary the character set <br/>" +
                            "   Notice as the character set grows larger, it takes longer to crack a password using a brute force attack <br/>" +
                            "   If a password cannot be creaked within the maximum amount of time of 5 minutes, you're on the right track, although this does not guarantee your password is ironclad <br/><br/>" +
                            "4. Now switch to the dictionary attack using the button at the bottom of the game screen <br/>" +
                            "   Try out various passwords with the dictionary attack, including passwords the brute force attack could not crack within the time constraints <br/>" +
                            "   You might notice some long passwords with a variety of characters could be cracked quickly <br/>" +
                            "   A long password with a variety of characters does not necessarily make it strong. A strong password also needs to be unique <br/>" +
                            "   Avoid using common words, phrases, and character combinations, as a dictionary attack can easily break these passwords, even though they may appear strong <br/><br/>" +
                            "   This is the last activity in the CyberAdventure Game <br/>" +
                            "<b>Please press 'Exit' when you are done with this activity</b>" +
                            "</html>";
                    helpTextArea.setText(help);

                    continue_button.setVisible(false);
                    break;
                case StrongPasswords:
                    break;
                case PhysicalAspects:
                    setCurrentGame(Game.SQL);
                    continue_button.setVisible(false);
                    setupInteractiveGames();
                    displayExplanation();
                    sqlBasicsButton.setVisible(true);

                    panel_bckgrnd.remove(panel_question);
                    panel_bckgrnd.remove(panel_answer);
                    panel_bckgrnd.remove(panel_choices);
                    help_button.setVisible(true);
                    directions_button.setVisible(true);

                    JLabel scene = phys.scene_;
                    scene.setText(null);

                    panel_bckgrnd.remove(panel_question);
                    panel_bckgrnd.remove(panel_answer);
                    panel_bckgrnd.remove(panel_choices);
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
        JButton next_alt2 = new JButton("NEXT >>>");

        phys.info();
        phys.intro_lesson();
        phys.interface_layout(frame, panel_game, panel_bckgrnd, panel_buttons, panel_question, panel_answer, panel_choices);
        JTextField answer = phys.answer_input();
        JButton submit_b = phys.submit();

        phys.add_panels(panel_game, answer, submit_b);
        /* QUESTION 1 */
        phys.scene_one(answer, submit_b, panel_game, proceed);
        /* QUESTION 2 */
        JButton submit_b2 = phys.submit();
        phys.scene_two(answer, submit_b2, next_alt, next_alt2);
        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK */
        phys.transition_game(next_game, continue_button);
        phys.scene_two_alt(answer, submit_b2, next_alt, next_game, continue_button);
        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK, DIFFERENT TEXT DISPLAYED */
        phys.scene_two_alt2(answer, submit_b2, next_alt2, next_game, continue_button);
        /* QUESTION 3 */
        phys.ethical_hacker_route(answer, continue_button);
        /* physical aspects section */

        /* ***** DISPLAY PANEL WHERE GAME CONTENT WILL BE PLACED ***** */
        panel_bckgrnd.add(panel_game);

        /* ***** BUTTONS PANEL COMPONENTS ***** */
        panel_bckgrnd.add(panel_buttons); // add panel with buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(10, 140))); // spacing between buttons
        panel_buttons.add(exit_button); // add buttons
        answerButton = createAnswerButton();
        panel_buttons.add(answerButton);
        panel_buttons.add(passwordCrackerAttackButton);
        panel_buttons.add(help_button); // add buttons
        panel_buttons.add(directions_button);
        createSQLBasicsButton();
        panel_buttons.add(continue_button);

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

        // end void actionPerformed()
        directions_button.addActionListener(e -> {
            if (directions_button.getText().equals("SHOW DIRECTIONS")) {
                String b_label = "HIDE DIRECTIONS";
                directions_button.setText(b_label);

                explanationFrame.setVisible(true);
            } else if (directions_button.getText().equals("HIDE DIRECTIONS")) {
                String b_label = "SHOW DIRECTIONS";
                directions_button.setText(b_label);
                explanationFrame.dispose();
                explanationFrame.setVisible(false);
            }
        }); // end actionListener()

        exit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                helpFrame.dispose();
            } // end void actionPerformed()
        }); // end actionListener()

    } // end main
} // end class