import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private final int size;
    private final int[][] board;
    private int zeroRow, zeroCol;
    private Board twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("Null instead of tiles");

        size = tiles.length;

        // create a board clone and detect zero-value coordinates
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size).append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) s.append(String.format("%2d ", board[i][j]));
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int counter = 0;
        for (int i = 1; i < size * size; i++) {
            int row = (i - 1) / size;
            int col = (i - 1) % size;
            if (board[row][col] != i) counter++;
        }

        return counter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int counter = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) continue;

                int spotCol = (board[row][col] - 1) % size;
                int spotRow = (board[row][col] - 1) / size;
                counter += Math.abs(spotRow - row) + Math.abs(spotCol - col);
            }
        }

        return counter;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 1; i < size * size; i++) {
            int row = (i - 1) / size;
            int col = (i - 1) % size;
            if (board[row][col] != i) return false;
        }

        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        Board that = (Board) other;
        if (size != that.size) return false;
        // Here is the tricky part! Because 'equals' is defined
        // within the scope it has direct access to private members
        // of 'that'.
        return Arrays.deepEquals(board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<>();

        if (isValid(zeroRow - 1)) neighbors.add(neighbor(zeroRow - 1, zeroCol));
        if (isValid(zeroRow + 1)) neighbors.add(neighbor(zeroRow + 1, zeroCol));
        if (isValid(zeroCol - 1)) neighbors.add(neighbor(zeroRow, zeroCol - 1));
        if (isValid(zeroCol + 1)) neighbors.add(neighbor(zeroRow, zeroCol + 1));

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // singleton...
        if (twin != null) return twin;

        // select a random cell
        int twinFromRow = zeroRow;
        int twinFromCol = zeroCol;
        while (isZeroCell(twinFromRow, twinFromCol)) {
            twinFromRow = StdRandom.uniform(0, size);
            twinFromCol = StdRandom.uniform(0, size);
        }

        // select a random neighbor what is not Zero-cell
        int twinToRow = twinFromRow;
        int twinToCol = twinFromCol;
        while (isZeroCell(twinToRow, twinToCol)
                || twinFromRow == twinToRow && twinFromCol == twinToCol) {
            twinToRow = StdRandom.uniform(0, size);
            twinToCol = StdRandom.uniform(0, size);
        }

        twin = anagram(twinFromRow, twinFromCol, twinToRow, twinToCol);
        return twin;
    }

    /**
     * @param r second cell's row-coordinate
     * @param c second cell's col-coordinate
     * @return new Board with a swap
     */
    private Board neighbor(int r, int c) {
        return anagram(zeroRow, zeroCol, r, c);
    }

    /**
     * @param r1 first cell's row-coordinate
     * @param c1 first cell's col-coordinate
     * @param r2 second cell's row-coordinate
     * @param c2 second cell's col-coordinate
     * @return new Board with a swap
     */
    private Board anagram(int r1, int c1, int r2, int c2) {
        swap(r1, c1, r2, c2);
        Board result = new Board(board);
        swap(r2, c2, r1, c1);
        return result;
    }

    /**
     * Swaps values of two cells on board
     *
     * @param r1 first cell's row-coordinate
     * @param c1 first cell's col-coordinate
     * @param r2 second cell's row-coordinate
     * @param c2 second cell's col-coordinate
     */
    private void swap(int r1, int c1, int r2, int c2) {
        int tmp = board[r2][c2];
        board[r2][c2] = board[r1][c1];
        board[r1][c1] = tmp;
    }

    /**
     * @param row row-coordinate
     * @param col col-coordinate
     * @return equivalence of two cells' coordinates
     */
    private boolean isZeroCell(int row, int col) {
        return row == zeroRow && col == zeroCol;
    }

    /**
     * @param coordinate int - row- or col-coordinate
     * @return true if and only if row- or col-coordinate
     * is inside the board. Otherwise, returns false.
     */
    private boolean isValid(int coordinate) {
        return 0 <= coordinate && coordinate < size;
    }

    public static void main(String[] args) {
        System.out.println("asdasd");
        // read in the board specified in the filename
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // solve the slider puzzle
        Board initial = new Board(tiles);
        System.out.println(initial.twin());
        System.out.println(initial.twin());
    }
}
