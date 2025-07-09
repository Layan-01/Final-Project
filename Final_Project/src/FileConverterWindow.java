//An elegant window that allows the user to select a file and convert it from one type to another
import javax.swing.*; // import swing library -> to build the graphical interface
import java.awt.*;
import java.io.File;

public class FileConverterWindow extends JFrame {

    private File selectedFile = null;

    public FileConverterWindow(){

        setTitle("File Converter");
        setSize(600, 450); // Set window size
        setLocationRelativeTo(null); // the window appears in the center of the screen
        setDefaultCloseOperation(EXIT_ON_CLOSE); // This Method will close the window if we click on the close button || By default nothing happens
        setLayout(null); // We will not use any automatic layout, but we will put each element in its place manually using setBounds
        getContentPane().setBackground(new Color(235, 240, 255));


        //Title
        JLabel titleLabel = new JLabel("File Converter" , SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arabic Typesetting", Font.BOLD, 42));
        titleLabel.setBounds(100, 30, 400, 50);
        titleLabel.setForeground(new Color(40, 50, 100));
        add(titleLabel);

        //Select file button
        JButton selectFileButton = new JButton("Select File");
        selectFileButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 28));
        selectFileButton.setBounds(180, 100, 240, 45);
        selectFileButton.setBackground(new Color(30, 60, 120));
        selectFileButton.setForeground(Color.WHITE);
        selectFileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(selectFileButton);

        JLabel selectedLabel = new JLabel("No file selected !");
        selectedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        selectedLabel.setBounds(100, 155, 400, 25);
        selectedLabel.setForeground(new Color(100, 100, 100));
        add(selectedLabel);

        //ComboBox to select conversion type
        String[] options = {"PDF to TXT", "TXT to PDF", "JPG to PDF", "TXT to DOCX"};
        JComboBox<String> conversionType = new JComboBox<>(options);
        conversionType.setFont(new Font("Arabic Typesetting", Font.PLAIN, 26));
        conversionType.setBounds(180, 190, 240, 45);
        add(conversionType);

        //Convert button
        JButton convertButton = new JButton("convert");
        convertButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 34));
        convertButton.setBounds(180, 250, 240, 50);
        convertButton.setBackground(new Color(85, 100, 180));
        convertButton.setForeground(Color.WHITE);
        convertButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(convertButton);

        //Conversion history button
        JButton historyButton = new JButton("Conversion History");
        historyButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 16));
        historyButton.setBounds(430, 330, 130, 35);
        historyButton.setBackground(new Color(120, 110, 200));
        historyButton.setForeground(Color.WHITE);
        historyButton.setFocusPainted(false);
        historyButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // When you hover over the button, the mouse changes to a hand
        add(historyButton);

        //Back button
        JButton backButton = new JButton(" Back");
        backButton.setFont(new Font("Arabic Typesetting", Font.PLAIN, 20));
        backButton.setBounds(10, 10, 80, 30);
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(backButton);


        // Exit button (small)
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 13));
        exitButton.setBounds(510, 10, 60, 28);
        exitButton.setBackground(new Color(190, 60, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(exitButton);


        //Listener :Select file
        selectFileButton.addActionListener(e ->{
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION){
                selectedFile = chooser.getSelectedFile();
                selectedLabel.setText("Selected: " + selectedFile.getName());
            }
        });


        //Listener :Convert
        convertButton.addActionListener(e ->{
            if (selectedFile == null){
                JOptionPane.showMessageDialog(this, "Please select a file first!");
                return;
            }

            String type = (String) conversionType.getSelectedItem();
            boolean success = false;

            if (type.equals("PDF to TXT")) {
                success = FileConverter.convertPdfToTxt(selectedFile);
            } else if (type.equals("TXT to PDF")) {
                success = FileConverter.convertTxtToPdf(selectedFile);
            } else if (type.equals("JPG to PDF")) {
                success = FileConverter.convertJpgToPdf(selectedFile);
            } else if (type.equals("TXT to DOCX")) {
                success = FileConverter.convertTxtToDocx(selectedFile);
            }

            if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Conversion completed successfully!");
                } else  {
                    JOptionPane.showMessageDialog(this, "❌ Conversion failed!");
                }
        });

        //Listener :History
        historyButton.addActionListener(e -> {
            new ConversionHistoryWindow();
            this.dispose();
        });

        //Listener :Back
        backButton.addActionListener(e ->{
            this.dispose();
            new MainWindow();
        });

        exitButton.addActionListener(e -> System.exit(0));


        setVisible(true);

    }



}
