package SwingTest;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Run1 {
    public static void main(String[] args) throws Exception {
        // Устанавливаем тему FlatLaf Dark
        UIManager.setLookAndFeel(new FlatDarkLaf());

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("FlatLaf Dark Demo");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(400, 300);

            JPanel panel = new JPanel();
            panel.add(new JButton("Dark Button"));
            panel.add(new JTextField("Input", 12));

            f.setContentPane(panel);
            f.setVisible(true);
        });
    }
}
