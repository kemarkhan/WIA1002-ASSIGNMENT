package org.nba_data_structure.data_structures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ArrayList<E> implements List<E> {

    private static final int INITIAL_CAPACITY = 10;
    private E[] theData;
    private int size = 0;
    private int capacity = 0;

    public ArrayList() {
        capacity = INITIAL_CAPACITY;
        theData = (E[]) new Object[capacity];
    }

    public ArrayList(int initialCapacity) {
        capacity = initialCapacity;
        theData = (E[]) new Object[capacity];
    }

    // New constructor to initialize with default values
    public ArrayList(int initialCapacity, E defaultValue) {
        capacity = initialCapacity;
        theData = (E[]) new Object[capacity];
        for (int i = 0; i < capacity; i++) {
            theData[i] = defaultValue;
        }
        size = capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (theData[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size && theData[currentIndex] != null;
            }

            @Override
            public E next() {
                return theData[currentIndex++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(theData, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(theData, size, a.getClass());
        }
        System.arraycopy(theData, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        if (size == capacity) {
            allocate();
        }
        theData[size++] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (theData[i].equals(o)) {
                for (int j = i; j < size - 1; j++) {
                    theData[j] = theData[j + 1];
                }
                theData[size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        handlingException(index);
        for (E e : c) {
            add(index++, e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            modified |= remove(e);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(theData[i])) {
                remove(theData[i]);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            theData[i] = null;
        }
        size = 0;
    }

    @Override
    public E get(int index) {
        handlingException(index);
        return theData[index];
    }

    @Override
    public E set(int index, E element) {
        handlingException(index);
        E oldValue = theData[index];
        theData[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, E element) {
        handlingException(index);
        if (size == capacity) {
            allocate();
        }
        for (int i = size; i > index; i--) {
            theData[i] = theData[i - 1];
        }
        theData[index] = element;
        size++;
    }

    @Override
    public E remove(int index) {
        handlingException(index);
        E value = theData[index];
        for (int i = index; i < size - 1; i++) {
            theData[i] = theData[i + 1];
        }
        theData[size - 1] = null;
        size--;
        return value;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (theData[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (theData[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListIterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                return theData[currentIndex++];
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            @Override
            public E previous() {
                return theData[--currentIndex];
            }

            @Override
            public int nextIndex() {
                return currentIndex;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                if (currentIndex < 1) throw new IllegalStateException();
                ArrayList.this.remove(currentIndex - 1);
                currentIndex--;
            }

            @Override
            public void set(E e) {
                if (currentIndex < 1) throw new IllegalStateException();
                theData[currentIndex - 1] = e;
            }

            @Override
            public void add(E e) {
                int i = currentIndex++;
                ArrayList.this.add(i, e);
            }
        };
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        handlingException(index);
        return new ListIterator<E>() {
            private int currentIndex = index;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                return theData[currentIndex++];
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            @Override
            public E previous() {
                return theData[--currentIndex];
            }

            @Override
            public int nextIndex() {
                return currentIndex;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                if (currentIndex < 1) throw new IllegalStateException();
                ArrayList.this.remove(currentIndex - 1);
                currentIndex--;
            }

            @Override
            public void set(E e) {
                if (currentIndex < 1) throw new IllegalStateException();
                theData[currentIndex - 1] = e;
            }

            @Override
            public void add(E e) {
                int i = currentIndex++;
                ArrayList.this.add(i, e);
            }
        };
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        handlingException(fromIndex);
        handlingException(toIndex - 1);
        ArrayList<E> sublist = new ArrayList<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sublist.add(theData[i]);
        }
        return sublist;
    }

    private void allocate() {
        capacity = 2 * capacity;
        theData = Arrays.copyOf(theData, capacity);
    }

    private void handlingException(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
