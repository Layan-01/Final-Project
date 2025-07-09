//A screen that appears to the user to display the history of the transfers that were performed using the program
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConversionHistoryWindow extends JFrame {

    public ConversionHistoryWindow() {
        setTitle("Conversion History");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(235, 240, 255));


        //Title
        JLabel titleLabel = new JLabel("Previous Conversions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arabic Typesetting", Font.BOLD, 36));
        titleLabel.setBounds(50, 20, 500, 40);
        titleLabel.setForeground(new Color(40, 50, 100));
        add(titleLabel);

        //History Area
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false); //user cannot write in it
        historyArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyArea.setMargin(new Insets(5, 10, 5, 10));

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBounds(50, 80, 500, 220);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrollPane);


        //Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(30, 60, 120));
        backButton.setBounds(20, 320, 80, 30);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(backButton);

        //Clear History Button
        JButton clearButton = new JButton("Clear History");
        clearButton.setBounds(450, 320, 130, 30);
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBackground(new Color(200, 80, 80));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setBorderPainted(false);
        add(clearButton);


        //Listener :Back
        backButton.addActionListener(e -> {
            this.dispose();
            new FileConverterWindow();
        });


        //Listener :clear history
        //JOptionPane -> To display messages to the user
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all history?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_NO_OPTION) {
                try {
                    java.io.PrintWriter writer = new java.io.PrintWriter("converted_history.txt");
                    writer.close(); // this will clear the content
                    historyArea.setText("No files have been converted yet!");
                    JOptionPane.showMessageDialog(this, "Conversion history cleared!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error cleaning the file!");
                }
            }
        });


        //Load history

        try (BufferedReader reader = new BufferedReader(new FileReader("converted_history.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyArea.append("- " + line + "\n");
            }
        } catch (IOException e) {
            historyArea.setText("No files have been converted yet!");
        }
        setVisible(true);
    }
}
