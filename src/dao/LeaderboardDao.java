package dao;

import java.sql.*;

import javax.swing.JOptionPane;

import data_structures.UserQueue;
import entities.LeaderboardEntry;
import utils.ConnectionProvider;

public class LeaderboardDao {
    Connection con;
    public LeaderboardDao(){
        this.con = ConnectionProvider.getConnection();
    }
    public UserQueue getList(){
        UserQueue entries = null;
        try {
            String sql = "{CALL get_leaderboard(?)}";
            CallableStatement cstmt = con.prepareCall(sql);
            cstmt.registerOutParameter(1, Types.INTEGER);
            ResultSet rs = cstmt.executeQuery();
            int num = cstmt.getInt(1);
            if(num>10){
                entries = new UserQueue(10);   
            }else{
                entries = new UserQueue(num);
            }
            while (rs.next()) {
                String name = rs.getString("name");
                int tasksCompleted = rs.getInt("tasks_completed");
                entries.enqueue(new LeaderboardEntry(name, tasksCompleted));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading leaderboard: " + e.getMessage());
        }
        return entries;
    }
}
