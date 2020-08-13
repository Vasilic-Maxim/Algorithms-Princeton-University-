import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    //==========================================================================
    // Helper classes
    //==========================================================================

    private class Node {
        private Node previous, next;
        private final Item value;

        /**
         * Creates a new node
         * @param value is generic
         */
        public Node(Item value) {
            this.value = value;
        }

        /**
         * Set previous and next pointers to refer to this node
         */
        public void cycle() {
            previous = next = this;
        }

        /**
         * Set previous node
         * @param that a node that will be placed before the current node
         */
        public void previous(Node that) {
            that.previous = previous;
            that.next = this;
            previous.next = previous = that;
        }

        /**
         * Set next node
         * @param that a node that will be placed after the current node
         */
        public void next(Node that) {
            that.previous = this;
            that.next = next;
            next.previous = next = that;
        }

        /**
         * Remove current node from list
         * @return node's value
         */
        public Item remove() {
            previous.next = next;
            next.previous = previous;
            return value;
        }
    }

    private class MyIterator implements Iterator<Item> {
        private Node pointer = dummyRoot;

        @Override
        public boolean hasNext() {
            return !pointer.next.equals(dummyRoot);
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("The queue is empty.");

            pointer = pointer.next;
            return pointer.value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The 'remove' method is supported.");
        }
    }

    //==========================================================================
    // API
    //==========================================================================

    private int size = 0;
    private final Node dummyRoot;

    /**
     * Construct an empty deque
     */
    public Deque() {
        dummyRoot = new Node(null);
        dummyRoot.cycle();
    }

    /**
     * @return is the deque empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the number of items on the deque
     */
    public int size() {
        return size;
    }

    /**
     * Add the item to the front
     * @param item generic value
     */
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("null cannot be added.");

        dummyRoot.next(new Node(item));
        size++;
    }

    /**
     * Add the item to the back
     * @param item generic value
     */
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("null cannot be added.");

        dummyRoot.previous(new Node(item));
        size++;
    }

    /**
     * @return the item from the front after deleting it from the queue
     */
    public Item removeFirst() {
        if (size == 0) throw new NoSuchElementException("The queue is empty.");

        size--;
        return dummyRoot.next.remove();
    }

    /**
     * @return the item from the back after deleting it from the queue
     */
    public Item removeLast() {
        if (size == 0) throw new NoSuchElementException("The queue is empty.");

        size--;
        return dummyRoot.previous.remove();
    }

    /**
     * @return an iterator over items in order from front to back
     */
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    //==========================================================================
    // Testing
    //==========================================================================

    public static void main(String[] args) {
        // Your tests goes here
    }
}
