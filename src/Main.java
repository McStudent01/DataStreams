import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataStreamsGUI gui = new DataStreamsGUI();
            gui.setVisible(true);
        });
    }
}
