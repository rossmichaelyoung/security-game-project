import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* All button properties and other related content placed in here */

public class GUIproperties extends JFrame {

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    /* ***** SETTING UP INTERFACE LAYOUT ***** */
    public void interface_layout(JPanel p1, JPanel p2, JPanel p3) {
        LayoutManager box_layout = new BoxLayout(p1, BoxLayout.Y_AXIS);
        Box boxes[] = new Box[2];
        boxes[0] = Box.createVerticalBox(); // where game content will be displayed
        boxes[1] = Box.createVerticalBox(); // where buttons are displayed
        p1.setBackground(Color.WHITE);
        p1.setLayout(box_layout);
        p1.add(boxes[0]);
        p1.add(boxes[1]);

        p2.setPreferredSize(new Dimension(1050, 510));
        p2.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxes[0].add(p2);
        p3.setAlignmentX(Component.CENTER_ALIGNMENT);
        p3.setPreferredSize(new Dimension(1050, 120));
        boxes[1].add(p3);
    } // end interface_layout()

    // public void player_status_interface() {

    // }

    public String ask_name() { // ask for user's name
        JLabel label = new JLabel("Hey there, what's your name?");
        String name = JOptionPane.showInputDialog(label);

        return name;
    } // end playerGreeting()

    public String get_player_name(String name) { // return player name
        return name;
    } // end getPlayerName()

    public JLabel greeting_label(String name) { // display player name
        JLabel player_label = new JLabel("Welcome, " + name + "!");
        player_label.setFont(new Font("Arial", Font.PLAIN, 25));
        player_label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        return player_label;
    } // end greeting_label()

    public JLabel standard_label(String str) { // standard font sizing and position for all labels
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.PLAIN, 25));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        return label;
    } // end greeting_label()

    public JButton help_button(JPanel panel, String b_label) {
        JButton button = new JButton(b_label);
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setPreferredSize(new Dimension(200, 60));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return button;
    } // end intro_button()

    public JButton continue_button(JPanel panel) {
        JButton button = new JButton("CONTINUE");
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setPreferredSize(new Dimension(200, 60));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return button;
    } // end intro_button()

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

} // end class