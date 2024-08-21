package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import entities.Task;

public class MaxHeap implements Iterable<Task> {
    private Task[] Heap;
    private int size;
    private int maxsize;

    public MaxHeap(int maxsize) {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new Task[this.maxsize];
    }
    
    public int size(){
        return size;
    }

    private int parent(int pos) {
        return (pos - 1) / 2;
    }

    private int leftChild(int pos) {
        return (2 * pos) + 1;
    }

    private int rightChild(int pos) {
        return (2 * pos) + 2;
    }

    private boolean isLeaf(int pos) {
        return pos >= (size / 2) && pos < size;
    }

    private void swap(int fpos, int spos) {
        Task tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;

        int left = leftChild(pos);
        int right = rightChild(pos);
        int largest = pos;

        if (left < size && Heap[left].getTask_priority() > Heap[largest].getTask_priority())
            largest = left;
        if (right < size && Heap[right].getTask_priority() > Heap[largest].getTask_priority())
            largest = right;

        if (largest != pos) {
            swap(pos, largest);
            maxHeapify(largest);
        }
    }

    public void insert(Task element) {
        if (size >= maxsize) {
            resizeHeap();
        }

        Heap[size] = element;
        int current = size;
        size++;

        while (current != 0 && Heap[current].getTask_priority() > Heap[parent(current)].getTask_priority()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    private void resizeHeap() {
        maxsize = maxsize * 2;
        Task[] newHeap = new Task[maxsize];
        System.arraycopy(Heap, 0, newHeap, 0, size);
        Heap = newHeap;
    }

    public void print() {
        for (int i = 0; i < size / 2; i++) {
            System.out.print("Parent Node : " + Heap[i].getContent() + " (Priority: " + Heap[i].getTask_priority() + ")");

            if (leftChild(i) < size)
                System.out.print(" Left Child Node: " + Heap[leftChild(i)].getContent() + " (Priority: " + Heap[leftChild(i)].getTask_priority() + ")");

            if (rightChild(i) < size)
                System.out.print(" Right Child Node: " + Heap[rightChild(i)].getContent() + " (Priority: " + Heap[rightChild(i)].getTask_priority() + ")");

            System.out.println();
        }
    }

    public Task extractMax() {
        if (size == 0) {
            System.out.println("Heap is empty. Cannot extract.");
            return null;
        }

        Task popped = Heap[0];
        Heap[0] = Heap[--size];
        maxHeapify(0);
        return popped;
    }

    @Override
    public Iterator<Task> iterator() {
        return new MaxHeapIterator();
    }

    private class MaxHeapIterator implements Iterator<Task> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Task next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return Heap[currentIndex++];
        }
    }
}

