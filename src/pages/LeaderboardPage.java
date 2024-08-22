package pages;

import javax.swing.*;
import javax.swing.border.*;

import dao.LeaderboardDao;
import data_structures.UserQueue;
import entities.LeaderboardEntry;

import java.awt.*;

public class LeaderboardPage extends JFrame {
    private static final Color CARD_COLOR = new Color(255, 255, 255, 230);
    private static final Color PRIMARY_TEXT = new Color(33, 33, 33);
    private static final Color SECONDARY_TEXT = new Color(117, 117, 117);
    private static final Color ACCENT_COLOR = new Color(66, 133, 244);
    private static final Font HEADER_FONT = new Font("Roboto", Font.BOLD, 24);
    private static final Font TITLE_FONT = new Font("Roboto", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("Roboto", Font.PLAIN, 16);

    LeaderboardDao leaderboardDao;

    public LeaderboardPage() {
        leaderboardDao = new LeaderboardDao();

        setTitle("Leaderboard");
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

        createLeaderboardPanel(contentPanel);

        background.add(contentPanel, BorderLayout.CENTER);
        add(background);
    }

    private void createLeaderboardPanel(JPanel contentPanel) {
        addHeader(contentPanel);
        addLeaderboardCard(contentPanel);
    }

    private void addHeader(JPanel panel) {
        JLabel headerLabel = new JLabel("Leaderboard");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_TEXT);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void addLeaderboardCard(JPanel panel) {
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        UserQueue entries = loadLeaderboardData();
        entries.display();

        int size = entries.currentSize();
        for (int i = 0; i < size; i++) {
            LeaderboardEntry entry = entries.dequeue();
            addLeaderboardEntry(card, i + 1, entry);
            if (i < entries.capacity()) {
                card.add(new JSeparator());
            }
        }

        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(350, 400));

        panel.add(scrollPane);
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

    private void addLeaderboardEntry(JPanel panel, int rank, LeaderboardEntry entry) {
        JPanel entryPanel = new JPanel(new BorderLayout());
        entryPanel.setOpaque(false);

        JLabel rankLabel = new JLabel("#" + rank +" ");
        rankLabel.setFont(TITLE_FONT);
        rankLabel.setForeground(ACCENT_COLOR);
        entryPanel.add(rankLabel, BorderLayout.WEST);

        JLabel nameLabel = new JLabel(entry.getName());
        nameLabel.setFont(BODY_FONT);
        nameLabel.setForeground(PRIMARY_TEXT);
        entryPanel.add(nameLabel, BorderLayout.CENTER);

        JLabel scoreLabel = new JLabel(entry.getTasksCompleted() + " tasks");
        scoreLabel.setFont(BODY_FONT);
        scoreLabel.setForeground(SECONDARY_TEXT);
        entryPanel.add(scoreLabel, BorderLayout.EAST);

        panel.add(entryPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private UserQueue loadLeaderboardData() {
        return leaderboardDao.getList();
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
        SwingUtilities.invokeLater(() -> new LeaderboardPage());
    }
}