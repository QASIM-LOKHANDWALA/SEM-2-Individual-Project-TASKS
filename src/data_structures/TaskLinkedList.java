package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import entities.Task;

public class TaskLinkedList implements Iterable<Task> {
    private Node head;
    private int size;

    private class Node {
        Task task;
        Node next;

        Node(Task task) {
            this.task = task;
            this.next = null;
        }
    }

    public TaskLinkedList() {
        head = null;
        size = 0;
    }

    public void add(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
        } else {
            newNode.next=head;
            head=newNode;
        }
        size++;
    }

    public void remove(int taskId) {
        if (head == null) return;

        if (head.task.getTask_id() == taskId) {
            head = head.next;
            size--;
            return;
        }

        Node current = head;
        Node prev = null;
        while (current != null && current.task.getTask_id() != taskId) {
            prev = current;
            current = current.next;
        }

        if (current != null) {
            prev.next = current.next;
            size--;
        }
    }

    public Task get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.task;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<Task> iterator() {
        return new TaskIterator();
    }

    private class TaskIterator implements Iterator<Task> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Task next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Task task = current.task;
            current = current.next;
            return task;
        }
    }
}
