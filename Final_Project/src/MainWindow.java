import javax.swing.*; // import swing library
import java.awt.*;
import java.io.File; // We need it when the user selects a folder or file


    public class MainWindow extends JFrame {

    // === constructor ===
    public MainWindow (){
        setTitle("Digital Life Manager"); // Window title
        setSize(600,400); // Set window size
        setLocationRelativeTo(null); // the window appears in the center of the screen
        setDefaultCloseOperation(EXIT_ON_CLOSE); // This Method will close the window if we click on the close button || By default nothing happens
        setLayout(null); // We will not use any automatic layout, but we will put each element in its place manually using setBounds

        getContentPane().setBackground(new Color(220, 230, 250));


        // Address
        JLabel titleLabel = new JLabel("Digital Life Manager" , SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arabic Typesetting", Font.BOLD, 36));
        titleLabel.setBounds(100, 30, 400, 50);
        titleLabel.setForeground(new Color(40, 50, 100)); // dark blue
        add(titleLabel);


        //folder selection button
        JButton scanButton = new JButton("Scan folder");
        scanButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 28));
        scanButton.setBounds(200, 110, 200, 50);
        scanButton.setBackground(new Color(30, 60, 120));
        scanButton.setForeground(Color.WHITE);
        scanButton.setFocusPainted(false);
        scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(scanButton);


        // exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 28));
        exitButton.setBounds(200, 250, 200, 45);
        exitButton.setBackground(new Color(180, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(exitButton);

        // open convert screen button
        JButton convertScreentButton = new JButton("File converter");
        convertScreentButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 28));
        convertScreentButton.setBounds(200, 180, 200, 50);
        convertScreentButton.setBackground(new Color(85, 100, 180));
        convertScreentButton.setForeground(Color.WHITE);
        convertScreentButton.setFocusPainted(false);
        convertScreentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(convertScreentButton);

        //========================
        //Listeners

        scanButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFolder = chooser.getSelectedFile();
                this.dispose();  // to close the current screen
                new FileViewerWindow(selectedFolder);  // to open the FileViewerWindow

            }
        });
        //

        convertScreentButton.addActionListener(e -> {
            this.dispose();
            new FileConverterWindow(); // opening convert screen
        });

        // close the program
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

}
