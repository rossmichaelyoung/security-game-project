
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* All button properties and other related content placed in here */

public class GUIproperties extends JFrame {
    
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    /* ***** SETTING UP INTERFACE LAYOUT ***** */
    public String player_greeting() {   // ask for user's name
        String name = JOptionPane.showInputDialog("Hey there, what's your name?");
        return name;
    }   // end playerGreeting()

    public String get_player_name(String name) {    // return player name
        return name;
    }   // end getPlayerName()

    public JLabel greeting_label(JPanel panel, String name) {   // display player name
        JLabel player_label = new JLabel("Welcome, " + name + " !");
        player_label.setFont(new Font("Arial", Font.PLAIN, 25));

        return player_label;
    }   // end greeting_label()

    public JButton continue_button(JPanel panel) {
        JButton button = new JButton("CONTINUE"); 
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setPreferredSize(new Dimension(140, 60));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return button;
    }   // end intro_button()

    public JButton exit_button(JPanel panel) {
        JButton button = new JButton("EXIT");
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setPreferredSize(new Dimension(140, 60));
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        return button;
    } // end intro_button()


    /* ***** SETTING UP PLAYER STATUS LAYOUT ***** */
    public JLabel question_label(JPanel panel) { // display player name
        JLabel question_label = new JLabel("Write your question here.");
        question_label.setFont(new Font("Arial", Font.PLAIN, 15));

        return question_label;
    } // end greeting_label()




    /**
     *
     */
    private static final long serialVersionUID = 1L;

}   // end class