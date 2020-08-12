import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final int size;
    private final boolean[] opened;
    private final WeightedQuickUnionUF source, sink;
    private int numberOfOpenSites = 0;
    private boolean percolates = false;

    /**
     * Creates a system with specified size
     * @param n size of the system
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Argument 'n' is too small.");

        int totalItems = n * n + 1;

        size = n;
        source = new WeightedQuickUnionUF(totalItems);
        sink = new WeightedQuickUnionUF(totalItems);
        opened = new boolean[totalItems];

        // Source is opened by default
        opened[0] = true;
    }

    /**
     * Opens a cell if it was not already opened
     * @param row coordinate
     * @param col coordinate
     */
    public void open(int row, int col) {
        int encoded = encode(row, col);

        if (isOpen(encoded)) return;

        opened[encoded] = true;
        numberOfOpenSites++;

        uniteWithNeighbors(encoded, row, col);

        if (row == 1)
            source.union(encoded, 0);
        if (row == size)
            sink.union(encoded, 0);
        if (!percolates)
            percolates = percolates(encoded);
    }

    /**
     * @param row coordinate
     * @param col coordinate
     * @return open state
     */
    public boolean isOpen(int row, int col) {
        if (outOfRange(row, col)) throw new IllegalArgumentException("Invalid coordinate.");
        return opened[encode(row, col)];
    }

    /**
     * @param encoded current cell 1D coordinate
     * @return open state
     */
    private boolean isOpen(int encoded) {
        return opened[encoded];
    }

    /**
     * @param row coordinate
     * @param col coordinate
     * @return state of being connected to the source
     */
    public boolean isFull(int row, int col) {
        if (outOfRange(row, col)) throw new IllegalArgumentException("Invalid coordinate.");

        int encoded = encode(row, col);
        if (!isOpen(encoded)) return false;
        return connectedToRoot(source, encoded);
    }

    /**
     * @return total number of open sites
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * @return percolation status
     */
    public boolean percolates() {
        return percolates;
    }

    /**
     * @param encoded current cell 1D coordinate
     * @return checks if current cell is making the system to percolate
     */
    private boolean percolates(int encoded) {
        return connectedToRoot(source, encoded) && connectedToRoot(sink, encoded);
    }

    //==========================================================================
    // Utils
    //==========================================================================

    /**
     * @param resource because I use two Union-Find objects for preventing
     *                 appearance of the backwash problem I have to specify
     *                 which objects root I want to check
     * @param item cell that might be connected to the root
     * @return state of the cell being connected to the root of the resource
     */
    private boolean connectedToRoot(WeightedQuickUnionUF resource, int item) {
        return resource.find(item) == resource.find(0);
    }

    /**
     * Creates relationship between current cell and its neighbors
     * @param encoded current cell 1D coordinate
     * @param row coordinate
     * @param col coordinate
     */
    private void uniteWithNeighbors(int encoded, int row, int col) {
        uniteWithNeighbor(encoded, row - 1, col);
        uniteWithNeighbor(encoded, row + 1, col);
        uniteWithNeighbor(encoded, row, col - 1);
        uniteWithNeighbor(encoded, row, col + 1);
    }

    /**
     * Creates relationship between current cell and its neighbor
     * @param encoded current cell 1D coordinate
     * @param row coordinate
     * @param col coordinate
     */
    private void uniteWithNeighbor(int encoded, int row, int col) {
        if (outOfRange(row, col)) return;

        int neighbor = encode(row, col);
        if (isOpen(neighbor)) {
            source.union(encoded, neighbor);
            sink.union(encoded, neighbor);
        }
    }

    /**
     * @param row coordinate
     * @param col coordinate
     * @return converted 2D coordinates into 1D
     */
    private int encode(int row, int col) {
        return size * (row - 1) + col;
    }

    /**
     * @param row coordinate
     * @param col coordinate
     * @return checks if coordinates are out of the system
     */
    private boolean outOfRange(int row, int col) {
        return outOfRange(row) || outOfRange(col);
    }

    /**
     * @param coordinate row/col
     * @return checks if coordinate is out of the system
     */
    private boolean outOfRange(int coordinate) {
        return coordinate < 1 || size < coordinate;
    }

    //==========================================================================
    // Testing
    //==========================================================================

    public static void main(String[] args) {
        // Some test goes here...
    }
}
