package org.nba_data_structure.data_structures;

import org.nba_data_structure.data_structures.LinkedList;
import org.nba_data_structure.data_structures.Node;

public class Queue<E> {
    private LinkedList<E> linkedlist;
    private int size = 0;

    public Queue() {
        linkedlist = new LinkedList<>();
    }

    // Enqueue
    public void enqueue(E data) {
        linkedlist.insertLast(data); // Insert at the end of the list
        size++;
    }

    // Dequeue
    public E dequeue() {
        if (size == 0) {
            return null; // Handle empty queue, or throw an exception if desired
        }

        E data = linkedlist.unlinkFirst(); // Remove from the front of the list
        size--;
        return data;
    }

    // Front
    public E front() {
        if (size == 0) {
            return null; // Handle empty queue case
        }

        return linkedlist.getFirst().getData(); // Return data at the front
    }

    // Display
    public void display() {
        Node<E> curr = linkedlist.getHead();
        while (curr != null) {
            System.out.println(curr.getData());
            curr = curr.getNext();
        }
    }

    // Size
    public int size() {
        return size;
    }

    // IsEmpty
    public boolean isEmpty() {
        return size == 0;
    }
}
