package pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class PomodoroTimer extends JPanel {
    private JLabel timerLabel, statusLabel;
    private JProgressBar progressBar;
    private JButton actionButton, resetButton;
    private Timer timer;
    private int secondsLeft;
    private boolean isWorking;
    private boolean isRunning;
    private final int WORK_TIME = 25 * 60; // 25 minutes
    private final int BREAK_TIME = 5 * 60; // 5 minutes

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color TIMER_COLOR = new Color(235, 87, 87);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_COLOR = new Color(235, 87, 87);
    private static final Color BUTTON_HOVER_COLOR = new Color(215, 67, 67);


    // Fonts
    private static final Font TIMER_FONT = new Font("Roboto", Font.BOLD, 48);
    private static final Font BUTTON_FONT = new Font("Roboto", Font.BOLD, 16);
    private static final Font STATUS_FONT = new Font("Roboto", Font.PLAIN, 18);

    public PomodoroTimer() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initializeComponents();
        layoutComponents();

        actionButton.addActionListener(e -> toggleTimer());
        resetButton.addActionListener(e -> resetTimer());

        isWorking = true;
        isRunning = false;
        secondsLeft = WORK_TIME;
        updateDisplay();
    }

    private void initializeComponents() {
        timerLabel = new JLabel("25:00", SwingConstants.CENTER);
        timerLabel.setFont(TIMER_FONT);
        timerLabel.setForeground(TIMER_COLOR);

        statusLabel = new JLabel("Focus Time", SwingConstants.CENTER);
        statusLabel.setFont(STATUS_FONT);
        statusLabel.setForeground(TEXT_COLOR);

        progressBar = new JProgressBar(0, WORK_TIME);
        progressBar.setForeground(TIMER_COLOR);
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(300, 8));

        actionButton = createButton("START");
        resetButton = createButton("RESET");
    }

    private void layoutComponents() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
    
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
    
        // Timer Label
        gbc.weightx = 1.0;
        contentPanel.add(timerLabel, gbc);
    
        // Progress Bar
        gbc.insets = new Insets(15, 0, 15, 0);
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressPanel.setOpaque(false);
        progressPanel.add(progressBar);
        contentPanel.add(progressPanel, gbc);
    
        // Status Label
        gbc.insets = new Insets(5, 0, 20, 0);
        contentPanel.add(statusLabel, gbc);
    
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(actionButton);
        buttonPanel.add(resetButton);
        contentPanel.add(buttonPanel, gbc);
    
        // Add content panel to main panel
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private void toggleTimer() {
        if (isRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (secondsLeft > 0) {
                    secondsLeft--;
                    updateDisplay();
                } else {
                    timer.cancel();
                    isWorking = !isWorking;
                    secondsLeft = isWorking ? WORK_TIME : BREAK_TIME;
                    updateDisplay();
                    showNotification();
                    stopTimer();
                }
            }
        }, 1000, 1000);
        isRunning = true;
        actionButton.setText("PAUSE");
        statusLabel.setText(isWorking ? "Focus Time" : "Break Time");
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isRunning = false;
        actionButton.setText("START");
        statusLabel.setText("Paused");
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isWorking = true;
        isRunning = false;
        secondsLeft = WORK_TIME;
        updateDisplay();
        actionButton.setText("START");
        statusLabel.setText("Focus Time");
    }

    private void updateDisplay() {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        
        int totalTime = isWorking ? WORK_TIME : BREAK_TIME;
        progressBar.setMaximum(totalTime);
        progressBar.setValue(totalTime - secondsLeft);
    }

    private void showNotification() {
        SwingUtilities.invokeLater(() -> {
            String message = isWorking ? "Break time is over. Start focusing!" : "Focus time is over. Take a break!";
            JOptionPane.showMessageDialog(PomodoroTimer.this, message, "Pomodoro Timer", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}