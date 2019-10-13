import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int count;

    private class QueueIterator implements Iterator<Item> {
        private int index;
        private Item[] iterQueue;

        public QueueIterator() {
            iterQueue = (Item[]) new Object[queue.length];
            for (int i = 0; i < queue.length; i++) {
                iterQueue[i] = queue[i];
            }
            StdRandom.shuffle(iterQueue, 0, count);
        }

        public boolean hasNext() {
            return index < count;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = iterQueue[index];
            index += 1;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        count = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    private void resize(int n) {
        Item[] newQueue = (Item[]) new Object[n];
        int index = 0;

        for (Item i : queue) {
            if (i == null) {
                continue;
            }

            newQueue[index] = i;
            index += 1;
        }

        queue = newQueue;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (count == queue.length) {
            resize(2 * queue.length);
        }

        queue[count] = item;
        count += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(0, count);
        Item item = queue[index];
        queue[index] = queue[count - 1];
        queue[count - 1] = null;
        count -= 1;

        if (count < queue.length / 4) {
            resize(queue.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(0, count);
        return queue[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        return;
    }
}
