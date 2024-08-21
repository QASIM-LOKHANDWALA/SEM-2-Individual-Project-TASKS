package dao;

import javax.swing.*;

import data_structures.MaxHeap;
import data_structures.MinHeap;
import data_structures.TaskLinkedList;
import entities.Task;
import utils.ConnectionProvider;

import java.sql.*;
import java.time.LocalDate;

public class TaskDao {
    static Connection con;
    
    public TaskDao() {
        con = ConnectionProvider.getConnection();
    }

    public void addTask(int userId, String content, int priorityId, LocalDate dueDate) {
        String sqlTask = "INSERT INTO tasks (content, priority_id, due_date) VALUES (?, ?, ?)";
        String sqlUserTask = "INSERT INTO user_tasks (user_id, task_id) VALUES (?, ?)";
        try (PreparedStatement pstmtTask = con.prepareStatement(sqlTask, Statement.RETURN_GENERATED_KEYS)) {
            pstmtTask.setString(1, content);
            pstmtTask.setInt(2, priorityId);
            pstmtTask.setDate(3, java.sql.Date.valueOf(dueDate));
            pstmtTask.executeUpdate();

            ResultSet generatedKeys = pstmtTask.getGeneratedKeys();
            if (generatedKeys.next()) {
                int taskId = generatedKeys.getInt(1);

                try (PreparedStatement pstmtUserTask = con.prepareStatement(sqlUserTask)) {
                    pstmtUserTask.setInt(1, userId);
                    pstmtUserTask.setInt(2, taskId);
                    pstmtUserTask.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TaskLinkedList loadTasksByDueDate(int userId) {
        TaskLinkedList tasks = new TaskLinkedList();
        String sql = "SELECT t.task_id, t.content, p.priority_id, p.priority_name, t.due_date " +
                     "FROM tasks t " +
                     "JOIN priority_levels p ON t.priority_id = p.priority_id " +
                     "JOIN user_tasks ut ON t.task_id = ut.task_id " +
                     "WHERE ut.user_id = ? ORDER BY t.due_date ASC";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task t = new Task();
                t.setTask_id(rs.getInt(1));
                t.setContent(rs.getString(2));
                t.setTask_priority(rs.getInt(3));
                t.setPriorityName(rs.getString(4));
                t.setDueDate(rs.getDate(5).toLocalDate());
                tasks.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public MinHeap loadTasksMaxFirst(int userId) {
        MinHeap tasks = new MinHeap(10);
        String sql = "SELECT t.task_id, t.content, p.priority_id, p.priority_name, t.due_date " +
                     "FROM tasks t " +
                     "JOIN priority_levels p ON t.priority_id = p.priority_id " +
                     "JOIN user_tasks ut ON t.task_id = ut.task_id " +
                     "WHERE ut.user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task t = new Task();
                    t.setTask_id(rs.getInt(1));
                    t.setContent(rs.getString(2));
                    t.setTask_priority(rs.getInt(3));
                    t.setPriorityName(rs.getString(4));
                    t.setDueDate(rs.getDate(5).toLocalDate());
                    tasks.insert(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }

    public MaxHeap loadTasksMinFirst(int userId) {
        MaxHeap tasks = new MaxHeap(10);
        String sql = "SELECT t.task_id, t.content, p.priority_id, p.priority_name, t.due_date " +
                     "FROM tasks t " +
                     "JOIN priority_levels p ON t.priority_id = p.priority_id " +
                     "JOIN user_tasks ut ON t.task_id = ut.task_id " +
                     "WHERE ut.user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task t = new Task();
                    t.setTask_id(rs.getInt(1));
                    t.setContent(rs.getString(2));
                    t.setTask_priority(rs.getInt(3));
                    t.setPriorityName(rs.getString(4));
                    t.setDueDate(rs.getDate(5).toLocalDate());
                    tasks.insert(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }

    public TaskLinkedList loadTasks(int userId) {
        TaskLinkedList tasks = new TaskLinkedList();
        String sql = "SELECT t.task_id, p.priority_id, p.priority_name, t.content, t.upload_date, t.due_date " +
                     "FROM tasks t " +
                     "JOIN priority_levels p ON t.priority_id = p.priority_id " +
                     "JOIN user_tasks ut ON t.task_id = ut.task_id " +
                     "WHERE ut.user_id = ? ORDER BY t.upload_date DESC";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task t = new Task();
                    t.setTask_id(rs.getInt("task_id"));
                    t.setTask_priority(rs.getInt("priority_id"));
                    t.setPriorityName(rs.getString("priority_name"));
                    t.setContent(rs.getString("content"));
                    t.setUpload_date(rs.getTimestamp("upload_date"));
                    t.setDueDate(rs.getDate("due_date").toLocalDate());
                    tasks.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }

    public void removeTask(int taskId, int userId) {
        try {
            String callProcedure = "{CALL removeTask(?, ?)}";
            try (CallableStatement cstmt = con.prepareCall(callProcedure)) {
                cstmt.setInt(1, taskId);
                cstmt.setInt(2, userId);
                cstmt.execute();
            }
    
            System.out.println("Task removed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error removing task: " + e.getMessage());
        }
    }
    
}
