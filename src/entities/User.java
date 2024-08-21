package entities;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private java.sql.Timestamp regDate;
    private int tasksCompleted;

    // Constructor
    public User(int id, String name, String email, String password, java.sql.Timestamp regDate, int tasksCompleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.regDate = regDate;
        this.tasksCompleted = tasksCompleted;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public java.sql.Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(java.sql.Timestamp regDate) {
        this.regDate = regDate;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + 
               ", password=" + password + ", regDate=" + regDate + 
               ", tasksCompleted=" + tasksCompleted + "]";
    }
}

