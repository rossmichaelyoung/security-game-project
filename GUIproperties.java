
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GUIproperties extends JFrame {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    public JButton continue_button(JPanel panel) {
        JButton button = new JButton("Continue"); 
        button.setPreferredSize(new Dimension(140, 60));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return button;
    }   // end intro_button()

    public JButton exit_button(JPanel panel) {
        JButton button = new JButton("Exit");
        button.setPreferredSize(new Dimension(140, 60));
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        return button;
    } // end intro_button()


    /**
     *
     */
    private static final long serialVersionUID = 1L;

}   // end class