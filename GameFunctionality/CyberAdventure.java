
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

        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");
        // panel on top of frame
        JPanel panel_bckgrnd = new JPanel();    

        GUIproperties gui = new GUIproperties();

        JPanel panel_game = new JPanel();
        panel_game.setBackground(Color.GRAY);

        // add panel to add button
        JPanel panel_buttons = new JPanel();
        panel_buttons.setBackground(Color.DARK_GRAY);
        JButton cont_button = gui.continue_button(panel_buttons);
        JButton exit_button = gui.exit_button(panel_buttons);

        // Scanner in = new Scanner(System.in);
        // String name = player_name();

        // layout = gui.panel();        

        // System.out.print("Player name: ");
        // name = in.nextLine();
        // System.out.println();

        // try {
        //     Thread.sleep(15);
        //     System.out.println("Welcome, " + name + "!");
        // } catch (Exception e) {
        //     System.err.println("Welcome error");
        // }

        // gamefx.loading_bar();
        

        /* ***** MAIN PANEL COMPONENTS ***** */ 
        panel_bckgrnd = new JPanel();
        // add layout to lower panel for button alignment
        LayoutManager box_layout = new BoxLayout(panel_bckgrnd, BoxLayout.Y_AXIS);
        Box boxes[] = new Box[2];
        boxes[0] = Box.createVerticalBox();
        boxes[1] = Box.createVerticalBox();
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
        panel_buttons.add(cont_button);     // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(1070, 0)));
        panel_buttons.add(exit_button);     // add buttons
        panel_buttons.add(Box.createRigidArea(new Dimension(0, 100)));

        /* ***** MAIN FRAME PROPERTIES ***** */
        frame.getContentPane().add(panel_bckgrnd);
        frame.setSize(1400, 800);
        final Color LIGHT_BLUE = new Color(51, 153, 255);
        frame.getContentPane().setBackground(LIGHT_BLUE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }   // end main
}   // end class