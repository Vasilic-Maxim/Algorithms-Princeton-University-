import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    //==========================================================================
    // Helper classes
    //==========================================================================

    private class MyIterator implements Iterator<Item> {
        private final Item[] itData = (Item[]) new Object[size];
        private int index = 0;

        public MyIterator() {
            System.arraycopy(data, 0, itData, 0, size);
            StdRandom.shuffle(itData);
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("The queue is empty.");

            return itData[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The 'remove' method is supported.");
        }
    }

    //==========================================================================
    // API
    //==========================================================================

    private Item[] data = (Item[]) new Object[2];
    private int size = 0;

    public RandomizedQueue() {
    }

    /**
     * @return is the randomized queue empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the number of items on the randomized queue
     */
    public int size() {
        return size;
    }

    /**
     * Add the item
     *
     * @param item is a generic item
     */
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("The 'null' argument value is allowed.");

        data[size++] = item;
        resize();
    }

    /**
     * Remove and return a random item
     *
     * @return random item
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("The 'dequeue' method cannot be used while queue is empty");

        int index = StdRandom.uniform(size);
        Item value = data[index];
        data[index] = data[--size];
        data[size] = null;

        if (!isEmpty()) resize();

        return value;
    }

    /**
     * Return a random item (but do not remove it)
     *
     * @return random item
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("The 'sample' method cannot be used while queue is empty");
        return data[StdRandom.uniform(size)];
    }

    /**
     * @return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    //==========================================================================
    // Utils
    //==========================================================================

    /**
     * Resize the queue if its size is not appropriate
     */
    private void resize() {
        int appropriateSize;

        if (isFull()) appropriateSize = data.length * 2;
        else if (isAlmostEmpty()) appropriateSize = data.length / 2;
        else return;

        Item[] resized = (Item[]) new Object[appropriateSize];
        System.arraycopy(data, 0, resized, 0, this.size);
        data = resized;
    }

    /**
     * @return is current queue full?
     */
    private boolean isFull() {
        return size == data.length;
    }

    /**
     * @return is queue emptier than it should be?
     */
    private boolean isAlmostEmpty() {
        return data.length / size == 4;
    }

    //==========================================================================
    // Tests
    //==========================================================================

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(382);
        rq.enqueue(942);
        rq.enqueue(604);
        rq.enqueue(366);
        rq.enqueue(172);
        Iterator<Integer> it1 = rq.iterator();
        Iterator<Integer> it2 = rq.iterator();

        System.out.println("===========================");
        while (it1.hasNext() && it2.hasNext()) System.out.printf("%d = %d%n", it1.next(), it2.next());
    }
}
