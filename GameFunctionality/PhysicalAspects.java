
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class PhysicalAspects {

    public static JFrame physaspects_frame = new JFrame();  // actual frame
    public static JPanel panel_physaspects = new JPanel();  // add panel to create layout
    public static JPanel panel_question = new JPanel();     // where question displayed
    public static JPanel panel_answer = new JPanel();       // where text and submit is displayed
    public static JPanel panel_choices = new JPanel();      // where choices are displayed

    //    public JLabel intro = info();
    public JButton next = new JButton("NEXT >>>");

    public static JButton next_game = new JButton("NEXT >>>");
    public static JButton next_alt = new JButton("NEXT >>>"); // when switching roles, to sql
    public static JButton next_alt2 = new JButton("NEXT >>>");    // when switching roles, to password

    public static JButton go_to_sql = new JButton("Proceed to task >>>");
    public static JButton go_to_password = new JButton("Proceed to task >>>");
    public static JButton cont = new JButton("CONTINUE");


    /* ***** SET UP ***** */
    public void interface_layout(JFrame frame, JPanel panel_bckgrnd, JPanel panel_buttons, JPanel p1, JPanel p2, JPanel p3, JPanel p4) {
//        frame = new JFrame();
//        frame.setTitle("Physical Aspects of Cybersecurity");
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

        p2.setPreferredSize(new Dimension(600, 500));
        p2.setAlignmentX(Component.CENTER_ALIGNMENT);
        box[0].add(p2);
        box[0].add(p3);
        p4.setPreferredSize(new Dimension(400, 500));
        p4.setAlignmentX(Component.LEFT_ALIGNMENT);
        box[1].add(p4);

        panel_bckgrnd.setBorder(new EmptyBorder(50, 20, 20, 20));

//        physaspects_frame.setSize(1000, 500);
//        physaspects_frame.setVisible(true);
//        physaspects_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } // end interface_layout()

    public JLabel info() { // introduction to game
        String html = "<html>Occupation: ethical hacker <br><br>"
                + "Your Task: You are hired by a private company that specializes "
                + "in selling rare antiques to high-end customers. Recently, there <br>"
                + "has been a rise in crime targeting websites selling high-end "
                + "products, and the company is worried that they might be a target. <br>"
                + "So, they decided to hire you to test the security of their site. <br><br> </html>";
        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 10));

        return label;
    }

    public void intro_lesson() { // lesson on physical security
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

    public JTextField answer_input() { // user input for answer choice
        JTextField answer = new JTextField(5);

        return answer;
    } // end answer_input()

    public JLabel display_answer(String str) { // display answer
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }   // end display_answer()

    public String get_answer(JTextField tf) { // pass in user response from text field
        String answer = tf.getText();
        return answer;
    } // end get_answer()

    public JButton submit() { // submit response
        JButton submit = new JButton("SUBMIT");
        return submit;
    } // end submit()

    /* QUESTION CONTENT */
    public JLabel scene() { // first scene of game
//        String task = to_str(info());

        String html = "<html>To perform your tasks for the day, the manager has given you an access <br>"
                + "card to enter the building and other authorized office spaces. <br><br>"
                + "You walk towards a door locked by card authentication and notice someone <br>"
                + "is suspiciously idling close to the door. What should you do? <br><br></html>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }   // end scene()

    public JLabel scene_choices() { // answer choices for scene one
        String html = "<html>a) Walk towards the door and use your ID card to go in like normal. <br>"
                + "   He's probably just waiting for someone. <br>"
                + "b) Try a different entrance. <br><br></html>";

        JLabel label = new JLabel(html);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }   // end scene_choices()
    /* QUESTION CONTENT */

    public JLabel switch_occupation() { // message introducing SQL game
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        return label;
    }   // end switch_occupation()

    public String to_str(JLabel label) {
        label = new JLabel();
        String str = label.getText();
        return str;
    }   // end to_str()

    public JLabel add_image(String img) {
        ImageIcon imgIcon = new ImageIcon(this.getClass().getResource(img));
        JLabel label = new JLabel(imgIcon);

        return label;
    }   // end add_image()
    /* ***** SET UP ***** */

    public JLabel scene_ = scene();
    public JLabel choices_ = scene_choices();
    public JLabel img_1 = add_image("doorswing.gif");

    public void add_panels(JPanel panel_physaspects, JTextField answer, JButton submit_b) {
        panel_answer.add(answer);
        panel_answer.add(submit_b);
        panel_question.add(scene_);
        panel_question.add(panel_answer);
        panel_choices.add(choices_);
        panel_choices.add(img_1);

        panel_physaspects.add(panel_question);
        panel_physaspects.add(panel_choices);
        physaspects_frame.add(panel_physaspects);
    }   // end add_panels()


    /* ***** GAME CONTENTS ***** */
    /* SCENE ONE */
    public JTextField answer = answer_input();
    public JButton submit_b = submit();

    public void scene_one(JTextField answer, JButton submit_b, JPanel panel, JLabel proceed) {
        submit_b.addActionListener(new ActionListener() {
            boolean hasBeenClicked = false;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if(!hasBeenClicked) {
                    String get_answer_tf = answer.getText(); // pass in user response
                    if(get_answer_tf.equals("a")) {
                        String html = "<html>You greet the person to dismiss suspicion and he greets you back. <br>"
                                + "You scan your card and head into the room.<br></html>";
                        String html2 = "<html>Turns out, he just forgot his ID in his office. <br>"
                                + "However, always be cautious of who you let in. They might cause trouble. </html>";

                        choices_.setText(html);
                        scene_.setText(null);

                        panel.remove(proceed);

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b);

                        panel_choices.remove(img_1);
                        panel_choices.add(next);

                        JOptionPane.showMessageDialog(null, html2);
                    }
                    else if(get_answer_tf.equals("b")) {
                        String html = "<html>Correct ! If possible, try another entrance. <br>"
                                + "If not, make sure to close the door immediately after granted access. <br><br></html>";

                        panel.remove(proceed);

                        choices_.setText(html);
                        scene_.setText(null);

                        panel_choices.remove(img_1);

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b);

                        panel_choices.add(next);
                    }
                    else {
                        String html = "<html>Invalid Choice. Try again/Try lowercase.<br>"
                                + "a) Walk towards the door and use your ID card to go in like normal. <br>"
                                + "He's probably just waiting for someone. <br>"
                                + "b) Try a different entrance. <br><br></html>";

                        panel.remove(proceed);

                        choices_.setText(html);
                    }
                }   // end if clicked
                else if (hasBeenClicked) {
                    String get_answer_tf = answer.getText(); // pass in user response
                    if (get_answer_tf.equals("a")) {
                        String html = "<html>You greet the person to dismiss suspicion and he greets you back. <br>"
                                + "You scan your card and head into the room.<br></html>";
                        String html2 = "<html>Turns out, he just forgot his ID in his office. <br>"
                                + "However, always be cautious of who you let in. They might cause trouble. </html>";

                        panel.remove(proceed);

                        choices_.setText(html);
                        scene_.setText(null);

                        panel_choices.remove(img_1);

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b);

                        panel_choices.add(next);

                        JOptionPane.showMessageDialog(null, html2);
                    }
                    else if (get_answer_tf.equals("b")) {
                        String html = "<html>Correct ! If possible, try another entrance. <br>"
                                + "If not, make sure to close the door immediately after granted access. <br><br></html>";
                        choices_.setText(html);
                        scene_.setText(null);

                        panel_choices.remove(img_1);

                        panel.remove(proceed);

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b);

                        panel_choices.add(next);
                    }
                    else {
                        String html = "<html>Invalid Choice. Try again/Try lowercase.<br>"
                                + "a) Walk towards the door and use your ID card to go in like normal. <br>"
                                + "He's probably just waiting for someone. <br>"
                                + "b) Try a different entrance. <br><br></html>";

                        panel.remove(proceed);

                        choices_.setText(html);
                    }
                } // end else if
                hasBeenClicked = !hasBeenClicked;
            } // end ActionPerformed
        });

    }   // end scene_one()
    /* END SCENE ONE */

    /* SCENE TWO */
    public JButton submit_b2 = submit();
    public JLabel img_2 = add_image("computer.gif");

    public void scene_two(JTextField answer, JButton submit_b2, JButton next_alt, JButton next_alt2) {
        next.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String html = "<html>You are about to begin your tasks, but the client suddenly asks for you. <br>"
                        + "This requires you to leave your machine. What's the smart thing to do? <br><br></html>";
                String html2 = "<html>a) Just leave it. No one's going to come in.<br>But close the door when you walk out.<br>"
                        + "b) Log out of your computer and close the door when you walk out.<br>"
                        + "c) Save all your work, exit out your programs, and log off your computer.<br>Close the door when you walk out.<br>";

                scene_.setText(html);
                choices_.setText(html2);
                answer.setText("");

                panel_choices.remove(img_1);
                panel_choices.add(img_2);

                panel_choices.remove(next);

                panel_answer.add(answer);
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
                        String html = "<html>You get up and leave your computer, but shut the door behind you on the way out. <br></html>";

                        scene_.setText(null);
                        choices_.setText(html);

                        panel_choices.remove(img_2);
                        panel_choices.remove(next);
                        panel_choices.add(next_alt);    // go to next game, perform SQL injection

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b2);

                        JOptionPane.showMessageDialog(null, "Never leave your work open and unattended ! You never know who is going to enter it !");

                    }
                    else if (get_answer_tf.equals("b")) {
                        String html = "<html>Great choice ! <br>You log out and shut the door behind you. <br><br></html>";

                        scene_.setText(null);
                        choices_.setText(html);

                        panel_choices.remove(img_2);
                        panel_choices.remove(next);
                        panel_choices.add(next_alt2); // go to next game, SQL

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b2);

                    }
                    else if (get_answer_tf.equals("c")) {
                        String html = "<html>Excellent ! Doing so makes it much more difficult for an outsider to get in. <br></html>";
                        scene_.setText(null);
                        choices_.setText(html);

                        panel_choices.remove(img_2);
                        panel_choices.remove(next);
                        panel_choices.add(next_game); // normal route

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b2);
                    }
                    else {
                        String html = "<html>Invalid Choice. Try again/Try lowercase.<br>"
                                + "a) Just leave it. No one's going to come in.<br>But close the door when you walk out.<br>"
                                + "b) Log out of your computer and close the door when you walk out.<br>"
                                + "c) Save all your work, exit out your programs, and log off your computer.<br>"
                                + "Close the door when you walk out.<br></html>";

                        choices_.setText(html);
                    }
                }  // end if clicked
                else if (hasBeenClicked) {
                    String get_answer_tf = answer.getText(); // pass in user response
                    if (get_answer_tf.equals("a")) {
                        String html = "<html>You get up and leave your computer, but shut the door behind you on the way out. <br></html>";

                        scene_.setText(null);
                        choices_.setText(html);

                        panel_choices.remove(img_2);
                        panel_choices.remove(next);
                        panel_choices.add(next_alt);    // go to next game, perform SQL injection

                        panel_answer.remove(answer);
                        panel_answer.remove(submit_b2);

                        JOptionPane.showMessageDialog(null, "Never leave your work open and unattended ! You never know who is going to enter it !");
                    }
                    else if (get_answer_tf.equals("b")) {
                        String html = "<html>You are about to begin your tasks, but the client suddenly asks for you. <br>"
                                + "This requires you to leave your machine. What's the smart thing to do? <br><br></html>";

                        scene_.setText(null);
                        choices_.setText(html);

                        panel_choices.remove(img_2);
                        panel_choices.remove(next);
                        panel_choices.add(next_alt2); // go to next game, perform SQL
                        JOptionPane.showMessageDialog(null, "Great choice !");
                    }
                    else if (get_answer_tf.equals("c")) {
                        String html = "<html>Excellent ! Doing so makes it much more difficult for an outsider to get in. <br></html>";
                        scene_.setText(null);
                        choices_.setText(html);

                        panel_choices.remove(img_2);
                        panel_choices.remove(next);
                        panel_choices.add(next_game); // normal route
                    }
                    else {
                        String html = "<html>Invalid Choice. Try again/Try lowercase.<br>"
                                + "a) Just leave it. No one's going to come in.<br>But close the door when you walk out.<br>"
                                + "b) Log out of your computer and close the door when you walk out.<br>"
                                + "c) Save all your work, exit out your programs, and log off your computer.<br>"
                                + "Close the door when you walk out.<br></html>";

                        choices_.setText(html);
                    }
                }
                hasBeenClicked = !hasBeenClicked;
            }   // end ActionPerformed
        });
    }   // end scene_two()
    /* END SCENE TWO */

    /* SCENE TWO--ALTERNATIVE ROUTE #1 */
    public JLabel img_3 = add_image("hacker.gif");
    public void scene_two_alt(JTextField answer, JButton submit_b2, JButton next_alt, JButton go_to_sql, JButton continue_button) {    // link button to continue -> go_to_Sql
        next_alt.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String html = "<html><body width='%1s'><h1>** Role Switch ! **</h1>"
                        + "Occupation: the attacker <br> "
                        + "Task: You notice that your target left his office room. You are hired by<br> "
                        + "an anonymous client. Your task is to gain access to the database and extract<br> "
                        + "crucial information from the company as discretely as possible. <br>"
                        + "Good luck ! <br><br> </html>";

                scene_.setText(html);
                choices_.setText(null);

                panel_choices.remove(next_alt);
                panel_choices.add(go_to_sql);
                panel_choices.add(img_3);

                panel_answer.remove(answer);
                panel_answer.remove(submit_b2);
            }
        });
    }   // end scene_two_alt()
    /* END SCENE TWO--ALTERNATIVE ROUTE #1 */

    /* SCENE TWO--ALTERNATIVE ROUTE #2 */
    public void scene_two_alt2(JTextField answer, JButton submit_b2, JButton next_alt2, JButton go_to_sql, JButton continue_button) {   // link button to continue -> go_to_Sql
        next_alt2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String html = "<html><body width='%1s'><h1>** Role Switch ! **</h1>" + "Occupation: the attacker <br> "
                        + "Task: You notice that your target left his office room. You are hired by<br> "
                        + "an anonymous client. <br>"
                        +"However, the computer is locked ! <br>"
                        + "Luckily, you were able to figure it out because the user left a really lousy hint. <br>"
                        + "Your task is to figure out how to gain access to the database and extract crucial <br> "
                        + "information from the company as discretely as possible. <br>"
                        + "Good luck ! <br><br> </html>";

                scene_.setText(html);
                choices_.setText(null);

                panel_choices.remove(next_alt2);
                panel_choices.add(go_to_sql);
                panel_choices.add(img_3);

                panel_answer.remove(answer);
                panel_answer.remove(submit_b2);
            }
        });
    }   // end scene_two_alt2()
    /* END SCENE TWO--ALTERNATIVE ROUTE #2 */

    public void ethical_hacker_route(JTextField answer, JButton continue_button) {  // link button to continue -> go_to_Sql
        next_game.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String html = "<html>Looks like you know how important it is to be aware of your environment ! <br><br>"
                        + "Remember, not everyone has good intentions. It's better to be safe than sorry when <br>"
                        + "protecting your workspace, especially in a public area. <br<br>"
                        + "Your client wants you to perform two tasks: <br>"
                        + "1. Perform an SQL Injection on the database to find vulnerabilities.<br>"
                        + "2. Your client mentioned that some employees practice poor security <br>"
                        + "protocols when coming to their own personal items. Perform a <br>"
                        + "password crack to educate them on strong passwords. <br>"
                        + "Click \"CONTINUE\" to proceed. <br><br></html>";

                scene_.setText(html);
                choices_.setText(null);

                panel_choices.remove(next_game);
                panel_choices.remove(img_3);

                panel_answer.remove(answer);
                panel_answer.remove(submit_b2);

                continue_button.setVisible(true);
            }
        });
    }   // end ethical_hacker_route()

    public void transition_game(JButton next_game, JButton continue_button) {
        next_game.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String html = "<html>Your new task: "
                        + "Perform SQL injection on database. <br><br>"
                        + "** click \"CONTINUE\" to proceed. **<br></html>";

                scene_.setText(html);
                choices_.setText(null);

                panel_choices.remove(next_alt);
                panel_choices.remove(next_game);
                panel_choices.remove(img_3);

                panel_answer.remove(answer);
                panel_answer.remove(submit_b2);

                continue_button.setVisible(true);
            }
        });
    }

    public void transition_game_ethical(JButton next_alt, JButton next_game) {
        next_game.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String html = "<html>Your client wants you to perform two tasks: <br>"
                        + "1. Perform an SQL Injection on the database to find vulnerabilities.<br>"
                        + "2. Your client mentioned that some employees practice poor security <br>"
                        + "protocols when coming to their own personal items. Perform a <br>"
                        + "password crack to educate them on strong passwords. <br><br></html>";

                scene_.setText(html);
                choices_.setText(null);

                panel_choices.remove(next_alt);
                panel_choices.remove(next_game);
                panel_choices.remove(img_3);

                panel_answer.remove(answer);
                panel_answer.remove(submit_b2);

            }
        });
    }

    public static void main(String[] args) {

        /* instead of calling these two here, call them in CyberAdventure */
        // info();
        // intro_lesson();

        /* call interface layout function */
        // interface_layout(physaspects_frame, panel_physaspects, panel_question, panel_answer, panel_choices);
        // add_panels(answer, submit_b);

        /* QUESTION 1 */
        // scene_one(answer, submit_b);

        /* QUESTION 2 */
        // scene_two(answer, next_alt, next_alt2);

        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK */
        // scene_two_alt(answer, go_to_sql);        

        /* QUESTION 2 ALTERNATIVE ROUTE -- GO TO SQL ATTACK, DIFFERENT TEXT DISPLAYED */
        // scene_two_alt2(answer, go_to_sql);

        /* QUESTION 3 */
        // JButton cont = new JButton("CONTINUE");
        // ethical_hacker_route(answer, cont);


        // physaspects_frame.setSize(1000, 500);
        // physaspects_frame.setVisible(true);
        // physaspects_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }   // end main()


    /**
     *
     */
    // private static final long serialVersionUID = 1L;
}   // end class