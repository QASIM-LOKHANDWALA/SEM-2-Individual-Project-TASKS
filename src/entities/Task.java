package entities;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Task {
    private int task_id;
    private int user_id;
    private int task_priority;
    private String content;
    private Timestamp upload_date;
    private LocalDate dueDate;
    private String priorityName;

    public Task(int task_id, int user_id, int task_priority,String priorityName, String content, Timestamp upload_date, LocalDate dueDate) {
        this.task_id = task_id;
        this.user_id = user_id;
        this.task_priority = task_priority;
        this.content = content;
        this.upload_date = upload_date;
        this.dueDate = dueDate;
        this.priorityName = priorityName;
    }

    public Task(int task_id, int user_id, int task_priority, String content, Timestamp upload_date,LocalDate dueDate) {
        this.task_id = task_id;
        this.user_id = user_id;
        this.task_priority = task_priority;
        this.content = content;
        this.upload_date = upload_date;
        this.dueDate = dueDate;
    }
    
    public Task(int task_id, String content, int task_priority, LocalDate dueDate) {
        this.task_id = task_id;
        this.content = content;
        this.task_priority = task_priority;
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getTask_priority(){
        return task_priority;
    }

    public int getTask_id() {
        return task_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getUpload_date() {
        return upload_date;
    } 
    
    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public Task() {
    }

    public Task(int user_id, String content,int task_priority) {
        this.user_id = user_id;
        this.content = content;
        this.task_priority = task_priority;
    }

    public Task(int task_id, int user_id, String content) {
        this.task_id = task_id;
        this.user_id = user_id;
        this.content = content;
    }

    public Task(int id, String content2, int priorityId, String priorityName2, LocalDate localDate) {
        //TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Task [task_id=" + task_id + ", user_id=" + user_id + ", task_priority=" + task_priority + ", content="
                + content + ", upload_date=" + upload_date + "]";
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTask_priority(int task_priority) {
        this.task_priority = task_priority;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpload_date(Timestamp upload_date) {
        this.upload_date = upload_date;
    }
}
