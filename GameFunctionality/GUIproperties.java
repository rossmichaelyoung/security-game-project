
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GUIproperties extends JFrame {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    public JButton intro_button(JFrame frame) {
        JButton button = new JButton("Continue"); 
        button.setPreferredSize(new Dimension(70, 70));
        frame.add(button, BorderLayout.CENTER);

        return button;
    }   // end intro_button()

    public JPanel intro_panel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(1400, 600));
        panel.setMaximumSize(new Dimension(1400, 600));

        return panel;
    }   // end intro_panel()


    /**
     *
     */
    private static final long serialVersionUID = 1L;

}   // end class