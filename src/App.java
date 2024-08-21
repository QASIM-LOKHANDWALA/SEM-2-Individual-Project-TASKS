import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import pages.LoginPage;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
