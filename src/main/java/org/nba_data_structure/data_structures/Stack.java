package org.nba_data_structure.data_structures;

public class Stack<E> {
    private ArrayList<E> array;
    private int size = 0;

    public Stack() {
        this.array = new ArrayList<>();
    }

    // push
    public void push(E data) {
        array.add(data);
        size++;
    }

    // pop
    public E pop() {
        if (isEmpty()) {
            return null;
        }

        E removedItem = array.remove(size - 1);
        size--;

        return removedItem;
    }

    // top
    public E top() {
        if (isEmpty()) {
            return null;
        }
        return array.get(size - 1);
    }

    // display
    public void display() {
        for (int i = size - 1; i >= 0; i--) {
            System.out.println(array.get(i));
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
