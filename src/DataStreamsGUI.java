import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchTextField;
    private JButton loadButton, searchButton, quitButton;
    private Path filePath;

    public DataStreamsGUI() {
        setTitle("DataStreams");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        searchTextField = new JTextField(20);
        loadButton = new JButton("Load a file");
        searchButton = new JButton("Search the file");
        quitButton = new JButton("Quit");

        loadButton.addActionListener(this::loadFile);
        searchButton.addActionListener(this::searchFile);
        quitButton.addActionListener(e -> System.exit(0));

        searchButton.setEnabled(false); // Initially disabled

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search String:"));
        topPanel.add(searchTextField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);

        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);
        textPanel.add(originalScrollPane);
        textPanel.add(filteredScrollPane);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(textPanel, BorderLayout.CENTER);
    }

    private void loadFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().toPath();
            try {
                String content = new String(Files.readAllBytes(filePath));
                originalTextArea.setText(content);
                filteredTextArea.setText(""); // Clear filtered text area
                searchButton.setEnabled(true); // Enable search button
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile(ActionEvent e) {
        String search = searchTextField.getText();
        try (Stream<String> lines = Files.lines(filePath)) {
            StringBuilder filteredContent = new StringBuilder();
            lines.filter(line -> line.contains(search)).forEach(filteredContent::append);
            filteredTextArea.setText(filteredContent.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
