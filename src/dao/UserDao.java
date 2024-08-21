package dao;

import java.sql.*;
import javax.swing.JOptionPane;
import entities.User;

public class UserDao {
    Connection con;

    public UserDao(Connection con) {
        this.con = con;
    }

    public boolean isEmailRegistered(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(User user) {
        if (isEmailRegistered(user.getEmail())) {
            JOptionPane.showMessageDialog(null, "Email is already in use.", "Failure", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String sql = "INSERT INTO users (name, email, password, tasks_completed) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getTasksCompleted());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount(int userId) {
        try {
            String deleteUserTasks = "DELETE FROM user_tasks WHERE user_id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(deleteUserTasks)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }

            // Then delete the user
            String deleteUser = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(deleteUser)) {
                pstmt.setInt(1, userId);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while deleting the account: " + ex.getMessage());
            return false;
        }
    }

    public User validateLogin(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Timestamp regDate = resultSet.getTimestamp("reg_date");
                int tasksCompleted = resultSet.getInt("tasks_completed");

                return new User(id, name, email, password, regDate, tasksCompleted);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getInfo(String column, int userId) {
        try {
            String sql = "SELECT " + column + " FROM users WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(column);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
}
