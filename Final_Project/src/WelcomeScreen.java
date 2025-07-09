import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;  //Swing library for creating graphical interfaces (ממשקים)
import java.awt.*; //Library of colors, fonts...


public class WelcomeScreen extends JFrame {

    private static final Log log = LogFactory.getLog(WelcomeScreen.class);

    // constructor responsible for screen design
    public WelcomeScreen (){
     //   Setting the window
     setTitle("Welcome");
     setSize(600,400);
     setLocationRelativeTo(null); // window appear in the center of the screen
     setDefaultCloseOperation(EXIT_ON_CLOSE);
     setLayout(null); // control the position of the elements manually using setBounds

         // alm background color (purple-blue)
        getContentPane().setBackground(new Color(220, 230, 250));

        // Address
        JLabel titleLabel = new JLabel("Digital Life Manager" , SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arabic Typesetting", Font.BOLD, 48));
        titleLabel.setBounds(50, 60, 500, 60); // centre
        titleLabel.setForeground(new Color(40, 50, 100)); // dark blue
        add(titleLabel);

        // project logo
        ImageIcon logoIcon = new ImageIcon ( "assets/logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(180, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel =new JLabel(new ImageIcon(logoImage));
        logoLabel.setBounds(200, 100, 200, 160); //centre - under the address
        add(logoLabel);

        //Start button2
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 30));
        startButton.setBounds(220, 250, 160, 50);
        startButton.setBackground(new Color(30, 60, 120));
        startButton.setForeground(Color.WHITE);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // A hand when passed over
        add(startButton);

        //When we press Start → we open the MainWindow and close the current screen

        startButton.addActionListener(e -> { // lambda expression -> shorthand way (קיצור הדרך) of writing event code
            this.dispose(); //close the Welcome screen
            new MainWindow(); //go to the main interface
        });

        setVisible(true);


    }
}
