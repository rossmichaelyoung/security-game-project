
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class CyberAdventure {


    public static String player_name() {
        String name = "";
        return name;
    } // end player_prompt()

    public static void main(String[] args) {

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
        // layers: frame -> panel_bckgrnd -> panel_game + panel_buttons

        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        // panel on top of frame
        JPanel panel_bckgrnd = new JPanel();  

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* ***** PLAYER STATUS FRAME PROPERTIES ***** */
        status_frame.add(status_bckgrnd);

        status_frame.setSize(400, 500);
        final Color LIGHT_BLUE = new Color(51, 153, 255);
        status_frame.getContentPane().setBackground(LIGHT_BLUE);
        status_frame.setVisible(true);
        status_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* ***** ANSWER OPTIONS FRAME PROPERTIES ***** */
        options_frame.add(options_bckgrnd);

        options_frame.setSize(400, 500);
        final Color VERY_LIGHT_BLUE = new Color(51, 204, 255);
        options_frame.getContentPane().setBackground(VERY_LIGHT_BLUE);
        options_frame.setVisible(true);
        options_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }   // end main
}   // end class