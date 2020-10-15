
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

        GameEffects gamefx = new GameEffects();
        GUIproperties gui = new GUIproperties();

        JFrame frame = new JFrame();
        frame.setTitle("Cyber Adventure");

        Scanner in = new Scanner(System.in);
        String name = player_name();

        gamefx.greeting();

        JPanel intro = new JPanel();
        intro = gui.intro_panel();
        // intro.setSize(new Dimension(1300, 720));
        // JButton intro_button = new JButton();
        JButton intro_button = gui.intro_button(frame);
        

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
        

        /* ***** FRAME COMPONENTS ***** */

        // frame.add(intro_button);

        frame.setSize(1400, 800);
        // frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setLayout(new GridLayout(2, 1));
        
        frame.add(intro);
        frame.add(intro_button);

        final Color LIGHT_BLUE = new Color(51, 153, 255);
        frame.getContentPane().setBackground(LIGHT_BLUE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }   // end main
}   // end class