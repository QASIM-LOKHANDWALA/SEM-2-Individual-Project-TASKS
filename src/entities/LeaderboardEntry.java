package entities;

public class LeaderboardEntry {
    String name;
    int tasksCompleted;

    public LeaderboardEntry(String name, int tasksCompleted) {
        this.name = name;
        this.tasksCompleted = tasksCompleted;
    }

    public String getName() {
        return name;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    @Override
    public String toString() {
        return "LeaderboardEntry [name=" + name + ", tasksCompleted=" + tasksCompleted + "]";
    }
}
