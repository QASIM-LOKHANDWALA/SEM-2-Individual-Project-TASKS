package pages;

import dao.UserDao;
import entities.User;
import utils.ConnectionProvider;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private UserDao userDao = new UserDao(ConnectionProvider.getConnection());

    public LoginPage() {
        setTitle("TASKS");
        setMinimumSize(new Dimension(400, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createLoginPanel(), "login");
        cardPanel.add(createRegisterPanel(), "register");

        add(cardPanel);

        setIconImage(new ImageIcon("src\\images\\LOGO.png").getImage());
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src\\images\\LoginRegisterBG.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
    
        JPanel loginBox = new JPanel(new GridBagLayout());
        loginBox.setBackground(new Color(255, 255, 255, 220));
        loginBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JLabel titleLabel = new JLabel("TASKS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
    
        JLabel subtitleLabel = new JLabel("Organize Your Day With Us.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(102, 102, 102));
    
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Sign in");
        registerButton = new JButton("Create account");
    
        styleComponent(usernameField, "Enter Email");
        styleComponent(passwordField, "Password");
        styleButton(loginButton, new Color(0, 120, 212));
        styleButton(registerButton, Color.WHITE);
        registerButton.setForeground(new Color(0, 120, 212));
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 212)));
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        loginBox.add(titleLabel, gbc);
    
        gbc.gridy++;
        loginBox.add(subtitleLabel, gbc);
    
        gbc.gridy++;
        loginBox.add(usernameField, gbc);
    
        gbc.gridy++;
        loginBox.add(passwordField, gbc);
    
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginBox.add(loginButton, gbc);
    
        gbc.gridy++;
        loginBox.add(registerButton, gbc);
    
        loginButton.addActionListener(e -> {
            String email = usernameField.getText().toLowerCase();
            String password = new String(passwordField.getPassword());
            User user = userDao.validateLogin(email, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + user.getName());
                new HomePage(user.getId()).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password");
            }
        });
    
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "register"));
    
        panel.add(loginBox);
    
        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src\\images\\LoginRegisterBG.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel registerBox = new JPanel(new GridBagLayout());
        registerBox.setBackground(new Color(255, 255, 255, 220));
        registerBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Create account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));

        JTextField nameField = new JTextField(20);
        JTextField newUsernameField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JButton createAccountButton = new JButton("Create account");
        JButton backToLoginButton = new JButton("Back to login");

        styleComponent(nameField, "Enter Name");
        styleComponent(newUsernameField, "Enter Email");
        styleComponent(newPasswordField, "password");
        styleComponent(confirmPasswordField, "password");
        styleButton(createAccountButton, new Color(0, 120, 212));
        styleButton(backToLoginButton, Color.WHITE);
        backToLoginButton.setForeground(new Color(0, 120, 212));
        backToLoginButton.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 212)));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        registerBox.add(titleLabel, gbc);

        gbc.gridy++;
        registerBox.add(nameField, gbc);

        gbc.gridy++;
        registerBox.add(newUsernameField, gbc);

        gbc.gridy++;
        registerBox.add(newPasswordField, gbc);

        gbc.gridy++;
        registerBox.add(confirmPasswordField, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        registerBox.add(createAccountButton, gbc);

        gbc.gridy++;
        registerBox.add(backToLoginButton, gbc);

        createAccountButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = newUsernameField.getText().toLowerCase();
            String password = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match");
                return;
            }

            User newUser = new User(0, name, email, password, new java.sql.Timestamp(System.currentTimeMillis()), 0);
            boolean success = userDao.registerUser(newUser);
            if (success) {
                JOptionPane.showMessageDialog(this, "Account created successfully");
                cardLayout.show(cardPanel, "login");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create account");
            }
        });

        backToLoginButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));

        panel.add(registerBox);

        return panel;
    }

    private void styleComponent(JComponent component, String placeholder) {
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        component.setPreferredSize(new Dimension(300, 40));
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        if (component instanceof JTextField) {
            ((JTextField) component).setForeground(Color.GRAY);
            ((JTextField) component).setText(placeholder);
            ((JTextField) component).addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (((JTextField) component).getText().equals(placeholder)) {
                        ((JTextField) component).setText("");
                        ((JTextField) component).setForeground(Color.BLACK);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (((JTextField) component).getText().isEmpty()) {
                        ((JTextField) component).setForeground(Color.GRAY);
                        ((JTextField) component).setText(placeholder);
                    }
                }
            });
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(bgColor == Color.WHITE ? new Color(0, 120, 212) : Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(bgColor == Color.WHITE);
        button.setPreferredSize(new Dimension(300, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
