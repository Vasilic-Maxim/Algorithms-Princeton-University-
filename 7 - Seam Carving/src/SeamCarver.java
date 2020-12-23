import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;


public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Send a picture as an argument.");

        // create a safe-copy of the picture
        this.picture = new Picture(picture);
        // initialize the matrix of pixels energy
        this.energy = energy();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);

        // return precomputed value
        return energy[row][col];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] transposed = transpose(energy);
        return findVerticalSeam(transposed);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findVerticalSeam(energy);
    }

    private int[] findVerticalSeam(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] edgeTo = new int[rows][cols];
        double[][] distTo = new double[rows][cols];
        Stack<Integer> reverse = new Stack<>();

        // mark the cells from first row as root-nodes
        // in digraph
        for (int col = 0; col < cols; col++) {
            edgeTo[0][col] = col;
            distTo[0][col] = 1000;
        }

        // populate the distTo adjacency matrix with
        // maximum integer value
        for (int row = 1; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                distTo[row][col] = Double.POSITIVE_INFINITY;
            }
        }

        // populate the distTo matrix remaining cells
        for (int row = 0; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                // find out if current node can contribute to
                // finding the shortest path to the next node
                int adjRow = row + 1;
                for (int adjCol = col - 1; adjCol <= col + 1; adjCol++) {
                    double newValue = distTo[row][col] + matrix[adjRow][adjCol];
                    if (newValue < distTo[adjRow][adjCol]) {
                        distTo[adjRow][adjCol] = newValue;
                        edgeTo[adjRow][adjCol] = col;
                    }
                }
            }
        }

        // find the minimum total distance to leaves
        int lastRowId = rows - 1;
        double minDist = distTo[lastRowId][0];
        int minDistNodeId = 0;
        for (int col = 1; col < cols - 1; col++) {
            if (distTo[lastRowId][col] < minDist) {
                minDist = distTo[lastRowId][col];
                minDistNodeId = col;
            }
        }

        // collect the path ids
        int prevCol = minDistNodeId;
        for (int prevRow = lastRowId; prevRow >= 0; prevRow--) {
            reverse.push(prevCol);
            prevCol = edgeTo[prevRow][prevCol];
        }

        int[] response = new int[rows];
        int i = 0;
        for (int id : reverse) response[i++] = id;

        return response;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width(), height());

        // remove seam from picture
        boolean[] marked = new boolean[width()];
        Picture newPicture = new Picture(width(), height() - 1);
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (seam[col] == row) marked[col] = true;
                else newPicture.setRGB(col, row - (marked[col] ? 1 : 0), picture.getRGB(col, row));
            }
        }

        picture = newPicture;
        energy = energy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height(), width());

        // remove seam from picture
        boolean[] marked = new boolean[height()];
        Picture newPicture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (seam[row] == col) marked[row] = true;
                else newPicture.setRGB(col - (marked[row] ? 1 : 0), row, picture.getRGB(col, row));
            }
        }

        picture = newPicture;
        energy = energy();
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // comment...
    }

    private double[][] energy() {
        int width = picture.width();
        int height = picture.height();
        double[][] matrix = new double[height][width];

        // precomputed energy of pixels
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                matrix[row][col] = computeEnergy(row, col);

        return matrix;
    }

    private double computeEnergy(int row, int col) {
        // if the cell is a border-pixel return 1000
        if (isBorderPixel(row, col)) return 1000.;

        // computing gradients
        return Math.sqrt(gradientY(row, col) + gradientX(row, col));
    }

    private boolean isBorderPixel(int row, int col) {
        if (row == 0 || row + 1 == height()) return true;
        if (col == 0 || col + 1 == width()) return true;
        return false;
    }

    private int gradientX(int row, int col) {
        int left = picture.getRGB(col - 1, row);
        int right = picture.getRGB(col + 1, row);
        return gradient(left, right);
    }

    private int gradientY(int row, int col) {
        int top = picture.getRGB(col, row - 1);
        int bottom = picture.getRGB(col, row + 1);
        return gradient(top, bottom);
    }

    private int gradient(int c1, int c2) {
        int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;

        int r1 = (c1 & redMask) >> 16;
        int r2 = (c2 & redMask) >> 16;
        int r = Math.abs(r1 - r2);

        int g1 = (c1 & greenMask) >> 8;
        int g2 = (c2 & greenMask) >> 8;
        int g = Math.abs(g1 - g2);

        int b1 = c1 & blueMask;
        int b2 = c2 & blueMask;
        int b = Math.abs(b1 - b2);

        return r * r + g * g + b * b;
    }

    private void validateSeam(int[] seam, int rows, int cols) {
        if (seam == null) throw new IllegalArgumentException("Send a seam as an argument.");
        if (seam.length != rows) throw new IllegalArgumentException("Seam has different length than one of the Picture sides.");
        int prev = seam[0];
        for (int val : seam) {
            if (cols <= val || val < 0) throw new IllegalArgumentException("Seem value out of bound.");
            if (Math.abs(prev - val) > 1) throw new IllegalArgumentException("Entries in seam[] must differ by -1, 0, or +1.");
            prev = val;
        }
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= height())
            throw new IllegalArgumentException("row index must be between 0 and " + (height() - 1) + ": " + row);
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= width())
            throw new IllegalArgumentException("column index must be between 0 and " + (width() - 1) + ": " + col);
    }

    private double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = new double[cols][rows];

        for (int row = 0; row < cols; row++) {
            for (int col = 0; col < rows; col++) {
                result[row][col] = matrix[col][row];
            }
        }

        return result;
    }
}