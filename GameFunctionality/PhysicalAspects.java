
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class PhysicalAspects {

    public static void interface_layout(JFrame frame, JPanel p1, JPanel p2, JPanel p3, JPanel p4) {
        frame = new JFrame();
        frame.setTitle("Physical Aspects of Cybersecurity");
        p1 = new JPanel();

        LayoutManager layout = new BoxLayout(p1, BoxLayout.X_AXIS);
        Box box[] = new Box[2];
        box[0] = Box.createVerticalBox(); // where game content will be displayed
        box[1] = Box.createVerticalBox(); // where buttons are displayed
        p1.setBackground(Color.WHITE);
        p1.setLayout(layout);
        p1.add(box[0]);
        p1.add(box[1]);

        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();

        p2.setPreferredSize(new Dimension(400, 500));
        p2.setAlignmentX(Component.CENTER_ALIGNMENT);
        box[0].add(p2);
        box[0].add(p3);
        p4.setPreferredSize(new Dimension(600, 500));
        p4.setAlignmentX(Component.LEFT_ALIGNMENT);
        box[1].add(p4);
    } // end interface_layout

    public static JLabel info() { // introduction to game
        String html = "<html>Occupation: ethical hacker <br><br>"
                + "Your Task: You are hired by a private company that specializes <br>"
                + "in selling rare antiques to high-end customers. Recently, there <br>"
                + "has been a rise in crime targetting websites selling high-end <br>"
                + "products, and the company is worried that they might be a target. <br>"
                + "So, they decided to hire you to test the security of their site. <br><br> </html>";
            JLabel label = new JLabel(html);
            label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }

    public static void intro_lesson() { // lesson on physical security
        Runnable r = () -> {
            String html = "<html><body width='%1s'><h1>Cybersecurity Isn't Just About Coding</h1>" 
                    + "A misconception is that cybersecurity is all about coding and what happens in a computer. "
                    + "That isn't always the case. In addition to being safe virtually, being cautious of your surroundings "
                    + "and placing your work in a physically safe space is just as important. "
                    + "If your work is in a safe place, you can minimize the risk of an attack "
                    + "or prevent one from happening in the first place. <br><br>";

            int w = 600;
            int h = 200;

            String str = String.format(html, w, h);
            JLabel label = new JLabel(str);
            label.setFont(new Font("Arial", Font.PLAIN, 20));

            JOptionPane.showMessageDialog(null, label);
        };
        SwingUtilities.invokeLater(r);
    }

    public static JTextField answer_input() { // user input for answer choice
        JTextField answer = new JTextField(5);

        return answer;
    } // end answer_input()

    public static JLabel display_answer(String str) { // display answer
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }   // end display_answer()

    public static String get_answer(JTextField tf) { // pass in user response from text field
        String answer = tf.getText();
        return answer;
    } // end get_answer()

    public static JButton submit() { // submit response
        JButton submit = new JButton("SUBMIT");
        return submit;
    } // end submit()

    /* QUESTION 1 */
    public static JLabel scene_1() { // first scene of game
        String html = "<html>To peform your tasks for the day, the manager has given you an access <br>"
                + "card to enter the building and other authorized office spaces. <br><br>"
                + "You walk towards a door locked by card authentication and notice someone <br>"
                + "is suspiciously idling close to the door. What should you do? <br><br></html>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }   // end scene_1()

    public static JLabel scene_1_choices() { // answer choices for scene one
        String html = "<html>a) Walk towards the door and use your ID card to go in like normal. <br>"
                + "   She/He's probably just waiting for someone. <br>" + "b) Try a different entrance. <br>"
                + "c) Walk towards the door and use your ID card to go in like normal. <br>"
                + "   but shut the door quickly behind you. <br></html>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }   // end scene_1_choices()
    /* QUESTION 1 */

    /* QUESTION 2 */
    public static JLabel scene_2() { // second scene of game
        String html = "<html>You are about to begin your tasks, but the client suddenly asks for you. <br>"
                + "This requires you to leave your machine. What's the smart thing to do? <br><br></html>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }   // end scene_2()

    public static JLabel scene_2_choices() { // answer choices for scene two
        String html = "<html>a) Just leave it. No one's going to come in. <br>"
                + "b) Log out of your computer. <br>"
                + "c) Save all your work, exit out your programs, and log off your computer. <br>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }   // end scenne_2_choices()
    /* QUESTION 2 */

    /* QUESTION 3 */
    public static JLabel scene_3() { // second scene of game
        String html = "<html>You are about to begin your tasks, but the client suddenly asks for you. <br>"
                + "This requires you to leave your machine. What's the smart thing to do? <br><br></html>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    } // end scene_2()

    public static JLabel scene_3_choices() { // answer choices for scene two
        String html = "<html>a) Just leave it. No one's going to come in. <br>" + "b) Log out of your computer. <br>"
                + "c) Save all your work, exit out your programs, and log off your computer. <br>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    } // end scenne_2_choices()
    /* QUESTION 3 */


    public static String to_str(JLabel label) {
        label = new JLabel();
        String str = label.getText();
        return str;
    }   // end to_str()


    public static void main(String[] args) {

        JFrame physaspects_frame = new JFrame();
        JPanel panel_physaspects = new JPanel();
        JPanel panel_question = new JPanel();
        JPanel panel_answer = new JPanel();
        JPanel panel_choices = new JPanel();

        info();
        intro_lesson();

        /* call interface layout function */
        interface_layout(physaspects_frame, panel_physaspects, panel_question, panel_answer, panel_choices);
        panel_physaspects.setBorder(new EmptyBorder(50, 20, 20, 20));

        JTextField answer = answer_input();
        JButton submit_b = submit();

        /* game introduction */
        JLabel intro = info();
        JButton next = new JButton("NEXT >>>");
        panel_choices.add(next);
        panel_question.add(intro);
        panel_physaspects.add(panel_question);
        panel_physaspects.add(panel_choices);
        physaspects_frame.add(panel_physaspects);

        JLabel scene1 = scene_1();
        JLabel choices1 = scene_1_choices();

        // next.addActionListener(new ActionListener() {
        //     public void actionPerformed(final ActionEvent e) {
        //         String html = "<html>To peform your tasks for the day, the manager has given you an access <br>"
        //                 + "card to enter the building and other authorized office spaces. <br><br>"
        //                 + "You walk towards a door locked by card authentication and notice someone <br>"
        //                 + "is suspiciously idling close to the door. What should you do? <br><br></html>";
        //         String html2 = "<html>a) Walk towards the door and use your ID card to go in like normal. <br>"
        //                 + "She/He's probably just waiting for someone. <br>" 
        //                 + "b) Try a different entrance. <br>"
        //                 + "c) Walk towards the door and use your ID card to go in like normal. <br>"
        //                 + "but shut the door quickly behind you. <br></html>";

        //         scene1.setText(html);
        //         choices1.setText(html2);

        //         panel_choices.remove(next);
        //         panel_answer.add(answer);
        //         panel_answer.add(submit_b);
        //         panel_answer.add(scene1);
        //         panel_answer.add(choices1);

        //     }
        // });


        /* question 1 */   
        // JLabel scene1 = scene_1();
        // JLabel choices1 = scene_1_choices();

        panel_answer.add(answer);
        panel_answer.add(submit_b);
        panel_question.add(scene1);
        panel_question.add(panel_answer);
        panel_choices.add(choices1);

        panel_physaspects.add(panel_question);
        panel_physaspects.add(panel_choices);
        physaspects_frame.add(panel_physaspects);

        submit_b.addActionListener(new ActionListener() {
            boolean hasBeenClicked = false;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if(!hasBeenClicked) {
                    String get_answer_tf = answer.getText(); // pass in user response
                    if(get_answer_tf.equals("a")) {
                            choices1.setText("Are you sure? What if they piggyback you?");
                    }
                    else if(get_answer_tf.equals("b")) {
                        String html = "<html>Correct ! If possible, try another entrance. <br>"
                                + "If not, make sure to close the door immediately after granted access. <br><br></html>";
                        choices1.setText(html);
                        panel_choices.add(next);
                    }
                    else {
                        choices1.setText("Invalid Choice. Try again/try lowercase.");
                    }
                }
                else if (hasBeenClicked) {
                    String get_answer_tf = answer.getText(); // pass in user response
                    if(get_answer_tf.equals("a")) {
                        choices1.setText("Incorrect");
                    }
                    else if(get_answer_tf.equals("b")) {
                        choices1.setText("Correct");
                    } 
                    else {
                        choices1.setText("Invalid Choice. Try again/try lowercase.");
                    }
                }
                hasBeenClicked = !hasBeenClicked;
            }
        });
        

        /* question 2 */
        JButton submit_b2 = submit();

        next.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String html = "<html>You are about to begin your tasks, but the client suddenly asks for you. <br>"
                        + "This requires you to leave your machine. What's the smart thing to do? <br><br></html>";
                String html2 = "<html>a) Just leave it. No one's going to come in. <br>"
                        + "b) Log out of your computer. <br>"
                        + "c) Save all your work, exit out your programs, and log off your computer. <br>";

                scene1.setText(html);
                choices1.setText(html2);
                panel_choices.remove(next);
                panel_answer.remove(submit_b);
                panel_answer.add(submit_b2);

            }
        });

        submit_b2.addActionListener(new ActionListener() {
            boolean hasBeenClicked = false;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!hasBeenClicked) {
                    String get_answer_tf = answer.getText(); // pass in user response
                    if (get_answer_tf.equals("a")) {
                        choices1.setText("Correct");
                    } else if (get_answer_tf.equals("b")) {
                        choices1.setText("Inorrect");
                    } else {
                        choices1.setText("Invalid Choice. Try again/try lowercase.");
                    }
                } else if (hasBeenClicked) {
                    String get_answer_tf = answer.getText(); // pass in user response
                    if (get_answer_tf.equals("a")) {
                        choices1.setText("Correct");
                    } else if (get_answer_tf.equals("b")) {
                        choices1.setText("Inorrect");
                    } else {
                        choices1.setText("Invalid Choice. Try again/try lowercase.");
                    }
                }
                hasBeenClicked = !hasBeenClicked;
            }
        });
        /* question 2 */

        /* question 3 */
        JButton submit_b3 = submit();

        // next.addActionListener(new ActionListener() {
        //     public void actionPerformed(final ActionEvent e) {
        //         String html = "<html>You are about to begin your tasks, but the client suddenly asks for you. <br>"
        //                 + "This requires you to leave your machine. What's the smart thing to do? <br><br></html>";
        //         String html2 = "<html>a) Just leave it. No one's going to come in. <br>"
        //                 + "b) Log out of your computer. <br>"
        //                 + "c) Save all your work, exit out your programs, and log off your computer. <br>";

        //         scene1.setText(html);
        //         choices1.setText(html2);
        //         panel_choices.remove(next);
        //         panel_answer.remove(submit_b);
        //         panel_answer.add(submit_b3);

        //     }
        // });

        // submit_b3.addActionListener(new ActionListener() {
        //     boolean hasBeenClicked = false;

        //     @Override
        //     public void actionPerformed(final ActionEvent e) {
        //         if (!hasBeenClicked) {
        //             String get_answer_tf = answer.getText(); // pass in user response
        //             if (get_answer_tf.equals("a")) {
        //                 choices1.setText("Correct");
        //             } else if (get_answer_tf.equals("b")) {
        //                 choices1.setText("Inorrect");
        //             } else {
        //                 choices1.setText("Invalid Choice. Try again/try lowercase.");
        //             }
        //         } else if (hasBeenClicked) {
        //             String get_answer_tf = answer.getText(); // pass in user response
        //             if (get_answer_tf.equals("a")) {
        //                 choices1.setText("Correct");
        //             } else if (get_answer_tf.equals("b")) {
        //                 choices1.setText("Inorrect");
        //             } else {
        //                 choices1.setText("Invalid Choice. Try again/try lowercase.");
        //             }
        //         }
        //         hasBeenClicked = !hasBeenClicked;
        //     }
        // });
        /* question 3 */






        physaspects_frame.setSize(1000, 500);
        physaspects_frame.setVisible(true);
        physaspects_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }   // end main()


    /**
     *
     */
    // private static final long serialVersionUID = 1L;
}   // end class