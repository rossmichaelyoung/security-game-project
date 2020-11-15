import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

class CyberAdventure extends JPanel {

    public static PhysicalAspects physicalAspects;
    
    public static void main(String[] args) {

        GUIproperties gui = new GUIproperties();
        physicalAspects = new PhysicalAspects();


        /* ***** PLAYER STATUS COMPONENTS ***** */
        JFrame status_frame = new JFrame();
        status_frame.setTitle("Player Status");

        JPanel status_bckgrnd = new JPanel();
        status_bckgrnd.setBackground(Color.WHITE);

        /* ***** ANSWER OPTIONS COMPONENTS ***** */
        JFrame options_frame = new JFrame();
        options_frame.setTitle("Make a Decision");

        JPanel options_bckgrnd = new JPanel();
        options_bckgrnd.setBackground(Color.WHITE);

        /* ***** GANE CONTENT FRAME COMPONENTS ***** */
        /* layers: frame -> panel_bckgrnd -> panel_game + panel_buttons */
        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        /* panel on top of frame */
        JPanel panel_bckgrnd = new JPanel();

        JPanel panel_game = new JPanel();
        panel_game.setBackground(Color.GRAY);

        /* call function to ask user's name */
        String name = gui.ask_name();
        gui.get_player_name(name);

        /* add panel to add button */
        JPanel panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.DARK_GRAY);
        String b_label = "HIDE HELP";
        JButton help_button = gui.help_button(panel_buttons, b_label);
        JButton continue_button = gui.continue_button(panel_buttons);
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
        String click_to_cont = "Click \"CONTINUE\" to continue >>> ";
        JLabel proceed = gui.standard_label(click_to_cont);
        proceed.setText(click_to_cont);
        panel_game.add(proceed);

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

        /* ***** PLAYER STATUS FRAME PROPERTIES ***** */
        LayoutManager status_layout = new BoxLayout(status_bckgrnd, BoxLayout.X_AXIS);
        status_bckgrnd.setLayout(status_layout);

        JLabel question_label = gui.question_label(status_bckgrnd);
        status_bckgrnd.setBorder(new EmptyBorder(10, 10, 10, 10)); // spacing between border and text
        status_bckgrnd.add(question_label);

        status_frame.add(status_bckgrnd);

        status_frame.setSize(400, 500);
        final Color LIGHT_BLUE = new Color(51, 153, 255);
        status_frame.getContentPane().setBackground(LIGHT_BLUE);
        status_frame.setVisible(false);
        status_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /* ***** ANSWER OPTIONS FRAME PROPERTIES ***** */
        options_frame.add(options_bckgrnd);

        options_frame.setSize(400, 500);
        final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
        options_frame.getContentPane().setBackground(VERY_LIGHT_BLUE);
        options_frame.setVisible(false);
        options_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /* ***** ACTIONLISTENER FOR CONTINUE AND EXIT BUTTONS ***** */
        help_button.addActionListener(new ActionListener() {
            boolean hasBeenClicked = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hasBeenClicked) {
                    proceed.setText(null);

                    String b_label = "HIDE HELP";
                    help_button.setText(b_label);

                    status_frame.setVisible(true);
                    options_frame.setVisible(true);

                    status_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    options_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } else if (hasBeenClicked) {
                    String b_label = "SHOW HELP";
                    help_button.setText(b_label);
                    status_frame.dispose();
                    options_frame.dispose();
                    status_frame.setVisible(false);
                    options_frame.setVisible(false);
                }
                hasBeenClicked = !hasBeenClicked;

            } // end void actionPerformed()
        }); // end actionListener()

        exit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                status_frame.dispose();
                options_frame.dispose();
                frame.dispose();
            } // end void actionPerformed()
        }); // end actionListener()

    } // end main
} // end class