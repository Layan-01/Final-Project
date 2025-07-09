import javax.swing.*; // import swing library -> to build the graphical interface
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter; // to control the reaction when you click the mouse (such as double-clicking on a file)
import java.awt.event.MouseEvent;
import java.io.File; // import File library -> to handle files
// This class is a new "GUI Window" -> because it extends from JFrame
import java.util.*;
import java.util.List;
import java.util.Map;

//The window responsible for displaying files within the folder selected by the user
public class FileViewerWindow extends JFrame {
    //=====This variable keeps a list of the files currently displayed on the screen (in order)
    private List<File> currentDisplayedFiles = new ArrayList<>();

    // === constructor ===
    public FileViewerWindow(File folder) { // folder = chosen by the user, and we want to display the files in it
        // window shape
        setTitle("Files in Folder "); // Window title
        setSize(700, 500); // Set window size
        setLocationRelativeTo(null); // the window appears in the center of the screen
        setDefaultCloseOperation(EXIT_ON_CLOSE); // This Method will close the window if we click on the close button || By default nothing happens
        setLayout(null); // We will not use any automatic layout, but we will put each element in its place manually using setBounds
        getContentPane().setBackground(new Color(230, 235, 250));

        // ===== ComboBox to filter types =====
        // Filtration System
        String[] types = {"All", "Documents", "Images", "Videos", "Audio", "Others"};
        JComboBox<String> typeSelector = new JComboBox<>(types);
        typeSelector.setBounds(500, 15, 150, 30); //Its location is next to the address
        typeSelector.setFont(new Font("Arabic Typesetting", Font.PLAIN, 22));
        add(typeSelector);

        // We display a title at the top of the window containing the name of the folder we selected
        //folder Title
        JLabel titleLabel = new JLabel("Files found in : " + folder.getName(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arabic Typesetting", Font.BOLD, 32));
        titleLabel.setBounds(30, 10, 600, 40);
        titleLabel.setForeground(new Color(40, 50, 100));
        add(titleLabel);


        //== TEXT AREA FOR DISPLAYING FILES (DO NOT EDIT HERE)
        JTextPane filePane = new JTextPane();
        filePane.setEditable(false);


        //== WE PUT THE TEXT AREA INSIDE A "SCROLLPANE" SO THAT IF THERE ARE MANY FILES -> WE CAN MAKE A SCROLL!!
        JScrollPane scrollPane = new JScrollPane(filePane);
        scrollPane.setBounds(30, 75, 600, 280);
        add(scrollPane);

        //
        filePane.setToolTipText("Double-click a file to open it");

        JLabel infolabel = new JLabel("* Double-click any file to open it");
        infolabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infolabel.setForeground(new Color(60, 80, 150));
        infolabel.setBounds(50, 50, 400, 20);
        add(infolabel);



        File[] files = folder.listFiles(); // array
        displayFiles(filePane, files); // view all files
        if (files != null) {
            StringBuilder builder = new StringBuilder();
            for (File file : files) {
                if (file.isFile()) {
                    builder.append("- " ).append(file.getName()).append("\n");
                }
            }
            filePane.setText(builder.toString());
        } else {
            filePane.setText("No files found !");
        }

        //== WE CREATED A "BACK" BUTTON TO RETURN TO THE MAIN SCREEN
        JButton backButton = new JButton("Back");
        backButton.setBounds(30, 380, 100, 40);
        styleButton(backButton);
        add(backButton);

        //== WHEN THE USER CLICKS "BACK", WE CLOSE THE CURRENT WINDOW AND OPEN THE (MainWindow) again
        backButton.addActionListener(e -> {
            this.dispose(); // closes the current window
            new MainWindow(); //reopens the main window
        });

        // Classify Files Button
        JButton classifyButton = new JButton("Classify Files");
        classifyButton.setBounds(150, 380, 120, 40);
        styleButton(classifyButton);
        add(classifyButton);
        classifyButton.addActionListener(e -> {
            displayClassified(filePane, files);
        });


        //---listener---
        typeSelector.addActionListener(e-> {
            String selectedType = (String) typeSelector.getSelectedItem();
            displayFiltered(filePane, files, selectedType);
        });

        //============ searchButton
        JButton searchButton = new JButton("Search File");
        searchButton.setBounds(290, 380, 120, 40);
        styleButton(searchButton);
        add(searchButton);

        searchButton.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(this, "Enter file name to search:");
            if (keyword != null && !keyword.isEmpty()) {
                displaySearchResults(filePane, files, keyword);
            }
        });

        //MouseListener
        filePane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2){ // double click
                    try {
                        int pos = filePane.viewToModel2D(evt.getPoint());
                        int lineStart = Utilities.getRowStart(filePane, pos);
                        int lineEnd = Utilities.getRowEnd(filePane, pos);
                        String line = filePane.getText().substring(lineStart, lineEnd).trim();

                        if (line.startsWith("- ")) { //We check if the line starts with -
                            line = line.substring(2); //If yes, we remove the first two characters - to get just the file name
                        }

                        for (File f :currentDisplayedFiles){
                            if (f.getName().equals(line)){
                                Desktop.getDesktop().open(f); //open the file
                                break;
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        setVisible(true);
    }
    //================================
    //To make all the buttons in a uniform and elegant format
    private void styleButton(JButton button) {
        button.setFont(new Font("Arabic Typesetting", Font.PLAIN, 22));
        button.setBackground(new Color(30, 60, 120));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
    //=================================
    // Show / Display all files normally
    private void displayFiles (JTextPane pane, File[] files) {
        StyledDocument doc = pane.getStyledDocument();
        pane.setText(""); // clear
        currentDisplayedFiles.clear();
        Style style = pane.addStyle("Default", null);

        try {
            for (File file : files) {
                if (file.isFile()) {
                    doc.insertString(doc.getLength(), "- " + file.getName() + "\n", style);
                    currentDisplayedFiles.add(file);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Displays files sorted by type
    private void displayClassified (JTextPane pane, File [] files) {
        Map<String, List<File>> result = FileSorter.classifyFiles(files);
        StyledDocument doc = pane.getStyledDocument();
        pane.setText("");
        currentDisplayedFiles.clear();

        Style style = pane.addStyle("Category", null);

        try {
            for (String category : result.keySet()) {
                List<File> list = result.get(category);
                if (!list.isEmpty()) {
                    StyleConstants.setForeground(style, Color.BLUE);
                    StyleConstants.setBold(style, true);
                    doc.insertString(doc.getLength(), category + " (" + list.size() + "files):\n", style);

                    StyleConstants.setForeground(style, Color.BLACK);
                    StyleConstants.setBold(style, false);
                    for (File f : list) {
                        doc.insertString(doc.getLength(), "- " + f.getName() + "\n", style);
                        currentDisplayedFiles.add(f);
                    }
                    doc.insertString(doc.getLength(), "___________________\n", style);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    //displays only files of a specific type (as selected from the ComboBox)
    public void displayFiltered (JTextPane pane, File[] files, String type) {
        if (type.equals("All")) {
            displayFiles(pane, files);
        } else {
            Map<String, List<File>> result = FileSorter.classifyFiles(files);
            StyledDocument doc = pane.getStyledDocument();
            pane.setText("");
            currentDisplayedFiles.clear();
            Style style = pane.addStyle("Filter", null);

            try {
                List<File> list = result.get(type);
                if (list != null && !list.isEmpty()) {
                    StyleConstants.setForeground(style, Color.BLUE);
                    StyleConstants.setBold(style, true);
                    doc.insertString(doc.getLength(), type + " (" + list.size() + "files):\n", style);

                    StyleConstants.setForeground(style, Color.BLACK);
                    StyleConstants.setBold(style, false);
                    for (File f : list) {
                        doc.insertString(doc.getLength(), "- " + f.getName() + "\n", style);
                        currentDisplayedFiles.add(f);
                    }
                } else {
                    StyleConstants.setForeground(style, Color.RED);
                    doc.insertString(doc.getLength(), "No files found in this category! \n", style);
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    // SEARCH RESULTS -> displays files contains the search word
    private void displaySearchResults (JTextPane pane, File [] files, String keyword){
        StyledDocument doc = pane.getStyledDocument();
        pane.setText("");
        currentDisplayedFiles.clear();
        Style style = pane.addStyle("Search", null);

        List<File> found = FileSearcher.searchFilesByName(files, keyword);

        try {
            if (found.isEmpty()) {
                StyleConstants.setForeground(style, Color.RED);
                doc.insertString(doc.getLength(), "No files found matching ! \"" + keyword + "\"\n", style);
            } else {
                StyleConstants.setForeground(style, Color.MAGENTA);
                StyleConstants.setBold(style, true);
                doc.insertString(doc.getLength(), "Results for \"" + keyword + "\" (" + found.size() + "):\n", style);

                StyleConstants.setForeground(style, Color.BLACK);
                StyleConstants.setBold(style, false);
                for (File f : found) {
                    doc.insertString(doc.getLength(), "- " + f.getName() + "\n", style);
                    currentDisplayedFiles.add(f);
                }
            }
        } catch (BadLocationException e){
            e.printStackTrace();
        }

    }
}