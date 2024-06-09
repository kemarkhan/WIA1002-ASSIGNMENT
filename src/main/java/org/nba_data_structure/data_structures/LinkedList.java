package org.nba_data_structure.data_structures;

public class LinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    // Constructor
    public LinkedList() {
        head = tail = null;
    }

    // isEmpty
    public boolean isEmpty() {
        return head == null;
    }

    // Insert at the beginning
    public void add(E data) {
        Node<E> newNode = new Node<>(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head = newNode;
        }
        size++;
    }

    // Insert at the end
    public void insertLast(E data) {
        Node<E> newNode = new Node<>(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    // Remove a specific node
    public void remove(Node<E> delNode) {
        if (head == null || delNode == null) return;

        if (head == delNode) {
            head = head.getNext();
            if (head == null) tail = null;
            size--;
            return;
        }

        Node<E> current = head;
        while (current != null && current.getNext() != delNode) {
            current = current.getNext();
        }

        if (current == null) return;

        current.setNext(delNode.getNext());
        if (delNode == tail) tail = current;
        size--;
    }

    // Unlink the first node and return its data
    public E unlinkFirst() {
        if (head == null) return null;

        Node<E> firstNode = head;
        E element = firstNode.getData();
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        firstNode.setData(null);
        firstNode.setNext(null);
        size--;
        return element;
    }

    // Poll: Retrieves and removes the head of the list
    public E poll() {
        return unlinkFirst();
    }

    // Remove the last element
    public void removeLast() {
        if (head == null) return;

        if (head == tail) {
            head = tail = null;
        } else {
            Node<E> current = head;
            while (current.getNext() != tail) {
                current = current.getNext();
            }
            current.setNext(null);
            tail = current;
        }
        size--;
    }

    // Get first node
    public Node<E> getFirst() {
        return head;
    }

    // Get last node
    public Node<E> getLast() {
        return tail;
    }

    // Get head node
    public Node<E> getHead() {
        return head;
    }

    // Get the size of the list
    public int size() {
        return size;
    }
}
