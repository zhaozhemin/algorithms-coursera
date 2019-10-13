import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;
    private int count;

    private static class Node<Item> {
        private Item item;
        private Node<Item> prev;
        private Node<Item> next;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> curr = head;

        public boolean hasNext() {
            return curr != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = curr.item;
            curr = curr.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
        count = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> node = new Node<Item>();
        node.item = item;

        if (head == null) {
            head = node;
            tail = node;
            count += 1;
            return;
        }

        node.next = head;
        head.prev = node;
        head = node;
        count += 1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> node = new Node<Item>();
        node.item = item;

        if (tail == null) {
            head = node;
            tail = node;
            count += 1;
            return;
        }

        node.prev = tail;
        tail.next = node;
        tail = node;
        count += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = head.item;

        if (head.equals(tail)) {
            head = null;
            tail = null;
            count -= 1;
            return item;
        }

        head = head.next;
        head.prev = null;
        count -= 1;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = tail.item;

        if (head.equals(tail)) {
            head = null;
            tail = null;
            count -= 1;
            return item;
        }

        tail = tail.prev;
        tail.next = null;
        count -= 1;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        return;
    }
}
