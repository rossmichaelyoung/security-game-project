import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SecurityGame extends JPanel {
    public static SQLInjection sqlInjection;
    public static DictionaryAttackPasswordCracker dictionaryAttackPasswordCracker;

    public enum Game {
        SQL, Password, PhysicalAspects, End
    }

    public static Game currentGame;
    public static JButton button;
    public static JTextArea mainTextArea;
    public static JFrame resultsFrame;
    public static JTextArea resultsTextArea;

    public SecurityGame() {
        currentGame = Game.SQL;
        initializeGame();
    }

    private void initializeGame() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 500));

        mainTextArea = new JTextArea();

        // Set the contents of the JTextArea.
        String output = "";
        mainTextArea.setText(output);
        mainTextArea.setLineWrap(true);
        mainTextArea.setWrapStyleWord(true);

        JScrollPane pane = new JScrollPane(mainTextArea);
        pane.setPreferredSize(new Dimension(1000, 500));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        button = new JButton("Search");
        button.addActionListener(e -> {
            String search = mainTextArea.getText();
            mainTextArea.setText("");
            resultsTextArea.setText("");
            switch(currentGame) {
                case SQL:
                    try {
                        String result = sqlInjection.selectItem(search);
                        resultsTextArea.setText(result);
                    } catch (SQLException sqlException) {
                        mainTextArea.setText("Error selecting item\n");
                    }
                case Password:
                    String result = dictionaryAttackPasswordCracker.findPassword(search);
                    resultsTextArea.setText(result);
            }
        });

        this.add(pane, BorderLayout.CENTER);
        this.add(button, BorderLayout.SOUTH);
    }

    public static void showFrame() {
        /* ***** RESULTS COMPONENTS ***** */
        resultsFrame = new JFrame();
        resultsFrame.setTitle("Results");
        resultsTextArea = new JTextArea();
        JScrollPane resultsPane = new JScrollPane(resultsTextArea);
        resultsPane.setPreferredSize(new Dimension(1000, 500));
        resultsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* ***** RESULTS FRAME PROPERTIES ***** */
        resultsFrame.setSize(400, 500);
        resultsFrame.setVisible(true);
        resultsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultsFrame.add(resultsTextArea);

        // Main Game Component
        SecurityGame game = new SecurityGame();
        game.setOpaque(true);

        /* ***** GAME CONTENT FRAME COMPONENTS ***** */
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
        cont_button.addActionListener(e -> {
            switch(currentGame) {
                case SQL:
                    currentGame = Game.Password;
                    button.setText("Crack Password");
                    mainTextArea.setText("");
                    resultsTextArea.setText("");
                    dictionaryAttackPasswordCracker = new DictionaryAttackPasswordCracker();
                    break;
                case Password:
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

        panel_game.setPreferredSize(new Dimension(1400, 680));
        panel_game.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxes[0].add(panel_game);
        panel_buttons.setPreferredSize(new Dimension(1400, 120));
        boxes[1].add(panel_buttons);
        panel_game.add(game);

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