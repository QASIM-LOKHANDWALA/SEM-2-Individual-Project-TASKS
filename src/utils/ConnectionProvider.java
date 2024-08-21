package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider {
    static Connection con;
    public static Connection getConnection(){
        if(con!=null){
            return con;
        }else{
            try {
                con = DriverManager.getConnection("jdbc:mysql:///taskstodo1", "root", "SQLjourney");
                return con;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
