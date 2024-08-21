package data_structures;

import entities.LeaderboardEntry;

public class UserQueue {
    LeaderboardEntry[] users;
    int capacity;
    int size;
    int front;
    int rear;
    
    public UserQueue(int size) {
        this.size = this.capacity = size;
        this.front = -1;
        this.rear = -1;
        this.users = new LeaderboardEntry[size];
    }
    
    public int currentSize() {
        if (front == -1) {
            return 0;
        } else if (rear >= front) {
            return rear - front + 1;
        } else {
            return size - front + rear + 1;
        }
    }

    public int capacity(){
        return capacity;
    }
    
    public void enqueue(LeaderboardEntry entry) {
        if ((rear + 1) % size == front) {
            System.out.println("Queue Is Full");
            return;
        }
        
        if (front == -1) {
            front = 0; // Set front to 0 when adding the first element
        }
        
        rear = (rear + 1) % size;
        users[rear] = entry;
    }
    
    public LeaderboardEntry dequeue() {
        if (front == -1) {
            System.out.println("Queue Is Empty");
            return null;
        }
        
        LeaderboardEntry user = users[front];
        
        if (front == rear) {
            front = rear = -1; // Reset queue to empty state
        } else {
            front = (front + 1) % size;
        }
        
        return user;
    }
    
    public void display() {
        if (front == -1) {
            System.out.println("Queue Is Empty");
        } else {
            int i = front;
            while (true) {
                System.out.println(users[i]);
                if (i == rear) break;
                i = (i + 1) % size;
            }
        }
    }
}
