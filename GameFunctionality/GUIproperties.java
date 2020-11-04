
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* All button properties and other related content placed in here */

public class GUIproperties extends JFrame {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

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








    /**
     *
     */
    private static final long serialVersionUID = 1L;

}   // end class