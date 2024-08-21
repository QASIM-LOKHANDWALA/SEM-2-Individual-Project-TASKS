package pages;

import javax.swing.*;
import javax.swing.border.*;

import dao.UserDao;
import utils.ConnectionProvider;
import utils.UIUtils;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfilePage extends JFrame {
    private int userId;
    private static final Color CARD_COLOR = new Color(255, 255, 255, 230);
    private static final Color PRIMARY_TEXT = new Color(33, 33, 33);
    private static final Color SECONDARY_TEXT = new Color(117, 117, 117);
    private static final Color ACCENT_COLOR = new Color(66, 133, 244);
    private static final Font HEADER_FONT = new Font("Roboto", Font.BOLD, 24);
    private static final Font TITLE_FONT = new Font("Roboto", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("Roboto", Font.PLAIN, 16);

    UserDao userDao;
    HomePage homePage;

    public ProfilePage(int userId,JFrame parentFrame) {
        this.userId = userId;
        this.homePage = (HomePage)parentFrame;

        userDao = new UserDao(ConnectionProvider.getConnection());

        setTitle("User Profile");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setIconImage(new ImageIcon("src\\images\\LOGO.png").getImage());

        createBackgroundPanel();

        setVisible(true);
    }

    private void createBackgroundPanel() {
        ImageIcon backgroundImage = new ImageIcon("src\\images\\backgroundImage2.jpg");
        JLabel background = new JLabel(backgroundImage);
        background.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(255, 255, 255, 200));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setOpaque(false);

        createProfilePanel(contentPanel);

        background.add(contentPanel, BorderLayout.CENTER);
        add(background);
    }

    private void createProfilePanel(JPanel contentPanel) {
        addHeader(contentPanel);
        addInfoCard(contentPanel);
        addStatsCard(contentPanel);
        addDeleteAccountButton(contentPanel);
    }

    private void addHeader(JPanel panel) {
        JLabel headerLabel = new JLabel("Profile");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_TEXT);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void addInfoCard(JPanel panel) {
        JPanel card = createCard();
        card.setLayout(new GridLayout(3, 2, 10, 10));

        addInfoField(card, "Name", getUserInfo("name"));
        addInfoField(card, "Email", getUserInfo("email"));
        addInfoField(card, "Member Since", formatDate(getUserInfo("reg_date")));

        panel.add(card);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void addStatsCard(JPanel panel) {
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel statsTitle = new JLabel("Statistics");
        statsTitle.setFont(TITLE_FONT);
        statsTitle.setForeground(ACCENT_COLOR);
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statsTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel tasksCompleted = new JLabel(getUserInfo("tasks_completed"));
        tasksCompleted.setFont(new Font("Roboto", Font.BOLD, 36));
        tasksCompleted.setForeground(ACCENT_COLOR);
        tasksCompleted.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(tasksCompleted);

        JLabel tasksLabel = new JLabel("Tasks Completed");
        tasksLabel.setFont(BODY_FONT);
        tasksLabel.setForeground(SECONDARY_TEXT);
        tasksLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(tasksLabel);

        panel.add(card);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
        return card;
    }

    private void addInfoField(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(TITLE_FONT);
        labelComponent.setForeground(SECONDARY_TEXT);
        panel.add(labelComponent);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(BODY_FONT);
        valueComponent.setForeground(PRIMARY_TEXT);
        panel.add(valueComponent);
    }

    private void addDeleteAccountButton(JPanel panel) {
        JButton deleteButton = new JButton("Delete Account");
        UIUtils.styleButton(deleteButton, new Color(220, 53, 69), Color.WHITE);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // deleteButton.setFont(BODY_FONT);
        // deleteButton.setForeground(Color.WHITE);
        // deleteButton.setBackground(new Color(220, 53, 69));
        // // deleteButton.setFocusPainted(false);
        // deleteButton.setFocusable(false);
        // // deleteButton.setBorder(new RoundedBorder(10));
        deleteButton.setMaximumSize(new Dimension(200, 40));

        deleteButton.addActionListener(e -> deleteAccount());

        panel.add(Box.createVerticalGlue()); 
        panel.add(deleteButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete your account? This action cannot be undone.",
            "Confirm Account Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
    
        if (confirm == JOptionPane.YES_OPTION) {
            boolean done = userDao.deleteAccount(userId);
            if (done) {
                JOptionPane.showMessageDialog(this, "Your account has been deleted successfully.");
                homePage.dispose();
                this.dispose();
                SwingUtilities.invokeLater(
                    () -> new LoginPage().setVisible(true)
                );
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the account. Please try again.");
            }
        }
    }


    private String getUserInfo(String column) {
        return userDao.getInfo(column, userId);
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;
        }
    }

    private static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(CARD_COLOR);
            g2d.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProfilePage(1,null));
    }
}