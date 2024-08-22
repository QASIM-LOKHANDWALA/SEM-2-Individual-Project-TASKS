package pages;

import javax.swing.*;
import javax.swing.border.*;
import com.github.lgooddatepicker.components.DatePicker;

import dao.TaskDao;
import data_structures.MaxHeap;
import data_structures.MinHeap;
import data_structures.TaskLinkedList;
import entities.Task;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class HomePage extends JFrame {
    private JPanel mainPanel, taskListPanel;
    private JTextField newTaskField;
    private JComboBox<String> priorityComboBox;
    private JComboBox<String> filterComboBox;
    private JButton addTaskButton;
    private int userId;
    private JPanel quotePanel;
    private JPanel topPanel;
    private Timer changeQuotesTimer;

    private static final Color PRIMARY_COLOR = new Color(0, 122, 255);  
    private static final Color SECONDARY_COLOR = new Color(242, 242, 247);  
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 59, 48); 
    private static final Color TEXT_COLOR = new Color(0, 0, 0);

    private static final Font HEADER_FONT = new Font("SF Pro Display", Font.BOLD, 34);
    private static final Font BODY_FONT = new Font("SF Pro Text", Font.PLAIN, 17);

    private TaskDao taskDao;
    private List<String> motivationalQuotes;

    public HomePage(int userId) {
        this.userId = userId;
        taskDao = new TaskDao();

        setTitle("Tasks");
        setMinimumSize(new Dimension(800, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        motivationalQuotes = Arrays.asList(
            "The secret of getting ahead is getting started.",
            "The only way to do great work is to love what you do.",
            "Don't watch the clock; do what it does. Keep going.",
            "Believe you can and you're halfway there.",
            "Success is not final, failure is not fatal: it is the courage to continue that counts."
        );

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        createHeader();
        createQuotePanel();
        startQuoteTimer();
        createFilterPanel();
        createTaskPanel();
        createAddButton();
        createPomodoroPanel();

        add(mainPanel);
        loadTasks();

        setIconImage(new ImageIcon("src\\images\\LOGO.png").getImage());
    }

    private void createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(BACKGROUND_COLOR);
        filterPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
    
        JLabel filterLabel = new JLabel("Sort by: ");
        filterLabel.setFont(BODY_FONT);
        filterComboBox = new JComboBox<>(new String[]{
            "Date (Latest first)", 
            "Priority (Highest first)", 
            "Priority (Lowest first)",
            "Due Date (Earliest first)"
        });
        filterComboBox.setFont(BODY_FONT);
        filterComboBox.addActionListener(e -> loadTasks());
    
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
    
        topPanel.add(filterPanel);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
    
        JLabel titleLabel = new JLabel("Tasks");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);
    
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setBackground(BACKGROUND_COLOR);
    
        String[] navItems = {"Profile", "Leaderboard", "Logout"};
        for (String item : navItems) {
            JButton navButton = createNavButton(item);
            navPanel.add(navButton);
        }
    
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(navPanel, BorderLayout.EAST);
    
        topPanel.add(headerPanel);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BODY_FONT);
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(BACKGROUND_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> navigate(text));
        return button;
    }

    private void navigate(String destination) {
        switch (destination) {
            case "Profile":
                new ProfilePage(userId,this).setVisible(true);
                break;
            case "Leaderboard":
                new LeaderboardPage().setVisible(true);
                break;
            case "Logout":
                new LoginPage().setVisible(true);
                this.dispose();
                break;
        }
    }

    private void createTaskPanel() {
        taskListPanel = new JPanel(new GridBagLayout());
        taskListPanel.setBackground(BACKGROUND_COLOR);
    
        JPanel fillerPanel = new JPanel();
        fillerPanel.setOpaque(false);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        taskListPanel.add(fillerPanel, gbc);
    
        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createPomodoroPanel() {
        PomodoroTimer pomodoroTimer = new PomodoroTimer();
        pomodoroTimer.setBackground(BACKGROUND_COLOR);
        pomodoroTimer.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel pomodoroPanel = new JPanel(new BorderLayout());
        pomodoroPanel.setBackground(BACKGROUND_COLOR);
        pomodoroPanel.add(new JLabel("Pomodoro Timer", SwingConstants.CENTER), BorderLayout.NORTH);
        pomodoroPanel.add(pomodoroTimer, BorderLayout.CENTER);
        
        mainPanel.add(pomodoroPanel, BorderLayout.EAST);
    }

    private void createAddButton() {
        addTaskButton = new JButton("Add Task");
        addTaskButton.setFont(BODY_FONT);
        addTaskButton.setForeground(Color.WHITE);
        addTaskButton.setBackground(PRIMARY_COLOR);
        addTaskButton.setBorderPainted(false);
        addTaskButton.setFocusPainted(false);
    
        addTaskButton.addActionListener(e -> showAddTaskDialog());
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addTaskButton);
    
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showAddTaskDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        newTaskField = new JTextField(20);
        newTaskField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        priorityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        priorityComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        DatePicker datePicker = new DatePicker();

        panel.add(new JLabel("Task:"));
        panel.add(newTaskField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityComboBox);
        panel.add(new JLabel("Due Date:"));
        panel.add(datePicker);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            LocalDate selectedDate = datePicker.getDate();
            LocalDate currentDate = LocalDate.now();

            if (selectedDate == null || selectedDate.isBefore(currentDate)) {
                JOptionPane.showMessageDialog(null, "The selected due date is in the past. Please choose a valid date.",
                        "Invalid Date", JOptionPane.ERROR_MESSAGE);
            } else {
                addTask(selectedDate);
            }
        }
    }

    private void addTask(LocalDate dueDate) {
        String content = newTaskField.getText();
        int priority = priorityComboBox.getSelectedIndex() + 1;
    
        if(dueDate==null){
            JOptionPane.showMessageDialog(this,"Please Select A Due Date","Error",JOptionPane.ERROR_MESSAGE );
            return;
        }
        if (!content.isEmpty()) {
            taskDao.addTask(userId, content, priority, dueDate);
            loadTasks();
            newTaskField.setText("");
        }
    }

    private void loadTasks() {
        while (taskListPanel.getComponentCount() > 1) {
            taskListPanel.remove(0);
        }
        
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        switch (selectedFilter) {
            case "Date (Latest first)":
                TaskLinkedList tasks = taskDao.loadTasks(userId);
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    String priorityStr = getPriorityString(task.getTask_priority());
                    addTaskToPanel(task.getTask_id(), priorityStr, task.getContent(), task.getDueDate());
                }
                break;
            case "Priority (Highest first)":
                MinHeap minHeap = taskDao.loadTasksMaxFirst(userId);
                while (minHeap.size() != 0) {
                    Task task = minHeap.extractMin();
                    String priorityStr = getPriorityString(task.getTask_priority());
                    addTaskToPanel(task.getTask_id(), priorityStr, task.getContent(), task.getDueDate());
                }
                break;
            case "Priority (Lowest first)":
                MaxHeap maxHeap = taskDao.loadTasksMinFirst(userId);
                while (maxHeap.size() != 0) {
                    Task task = maxHeap.extractMax();
                    String priorityStr = getPriorityString(task.getTask_priority());
                    addTaskToPanel(task.getTask_id(), priorityStr, task.getContent(), task.getDueDate());
                }
                break;
            case "Due Date (Earliest first)":
                TaskLinkedList tasksByDueDate = taskDao.loadTasksByDueDate(userId);
                for (int i = 0; i < tasksByDueDate.size(); i++) {
                    Task task = tasksByDueDate.get(i);
                    String priorityStr = getPriorityString(task.getTask_priority());
                    addTaskToPanel(task.getTask_id(), priorityStr, task.getContent(), task.getDueDate());
                }
                break;
        }
    
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private void addTaskToPanel(int taskId, String priority, String content, LocalDate dueDate) {
        JPanel taskPanel = new JPanel(new BorderLayout(10, 10));
        taskPanel.setBackground(SECONDARY_COLOR);
        taskPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
        taskPanel.setPreferredSize(new Dimension(taskListPanel.getWidth() - 20, 90));
    
        String taskContent = "<html>" + content + "<br>";
        if (dueDate != null) {
            String dueDateStr = dueDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
            if (LocalDate.now().isAfter(dueDate)) {
                taskContent += "<font color='red'>Overdue: " + dueDateStr + "</font>";
            } else {
                taskContent += "<font color='gray'>Due: " + dueDateStr + "</font>";
            }
        } else {
            taskContent += "<font color='gray'>No due date</font>";
        }
        taskContent += "</html>";
    
        JLabel taskLabel = new JLabel(taskContent);
        taskLabel.setFont(BODY_FONT);
    
        JLabel priorityLabel = new JLabel(priority, JLabel.LEFT);
        priorityLabel.setPreferredSize(new Dimension(70,taskPanel.getHeight()));
        priorityLabel.setFont(BODY_FONT);
        priorityLabel.setForeground(getPriorityColor(priority));
    
        JButton removeButton = new JButton("Remove");
        removeButton.setFont(BODY_FONT);
        removeButton.setForeground(ACCENT_COLOR);
        removeButton.setBackground(SECONDARY_COLOR);
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> removeTask(taskId, userId));
    
        taskPanel.add(priorityLabel, BorderLayout.WEST);
        taskPanel.add(taskLabel, BorderLayout.CENTER);
        taskPanel.add(removeButton, BorderLayout.EAST);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
    
        taskListPanel.add(taskPanel, gbc, 0);
    
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private void removeTask(int taskId, int userId) {
        taskDao.removeTask(taskId, userId);
        loadTasks();
    }

    private String getPriorityString(int priority) {
        switch (priority) {
            case 1: return "Low";
            case 2: return "Medium";
            case 3: return "High";
            default: return "Unknown";
        }
    }

    private Color getPriorityColor(String priority) {
        switch (priority) {
            case "Low": return new Color(52, 199, 89); // Apple Green
            case "Medium": return new Color(255, 149, 0); // Apple Orange
            case "High": return ACCENT_COLOR; // Apple Red
            default: return TEXT_COLOR;
        }
    }
    JLabel quoteLabel;
    private void createQuotePanel() {
        quotePanel = new JPanel();
        quotePanel.setBackground(new Color(245, 245, 245));
        quotePanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        quotePanel.setLayout(new BorderLayout());
    
        quoteLabel = new JLabel(getRandomQuote());
        quoteLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
        quoteLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        quotePanel.add(quoteLabel, BorderLayout.CENTER);
        topPanel.add(quotePanel);
    }

    private String getRandomQuote() {
        Random random = new Random();
        return motivationalQuotes.get(random.nextInt(motivationalQuotes.size()));
    }

    private void startQuoteTimer() {
        changeQuotesTimer = new Timer("Quotes");
        changeQuotesTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    quoteLabel.setText(getRandomQuote());
                });
            }
        }, 0, 5000); 
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HomePage(1).setVisible(true);
        });
    }
}