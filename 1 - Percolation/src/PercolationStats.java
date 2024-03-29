import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int size, area, trials;
    private final double[] ratio;

    /**
     * Perform independent trials on an n-by-n grid
     * @param n size of the grid
     * @param trials number of tests we have to perform
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("Argument 'n' is too small.");
        if (trials <= 0) throw new IllegalArgumentException("Argument 'trials' is too small.");

        this.size = n;
        this.area = n * n;
        this.trials = trials;
        this.ratio = ratio();
    }

    /**
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(ratio);
    }

    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(ratio);
    }

    /**
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    //==========================================================================
    // Utils
    //==========================================================================

    /**
     * @return tests' results
     */
    private double[] ratio() {
        double[] result = new double[trials];

        for (int i = 0; i < trials; i++) result[i] = trial();

        return result;
    }

    /**
     * @return test's result
     */
    private double trial() {
        Percolation system = new Percolation(size);

        while (!system.percolates()) openRandCell(system);

        return (double) system.numberOfOpenSites() / (double) area;
    }

    /**
     * Open one of the cells in the system
     * @param system that we test
     */
    private void openRandCell(Percolation system) {
        int row = StdRandom.uniform(1, size + 1);
        int col = StdRandom.uniform(1, size + 1);
        system.open(row, col);
    }

    //==========================================================================
    // Testing
    //==========================================================================

    public static void main(String[] args) {
        final int SIZE = Integer.parseInt(args[0]);
        final int TRIALS = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(SIZE, TRIALS);
        System.out.printf("mean = %f%n", stats.mean());
        System.out.printf("stddev = %f%n", stats.stddev());
        System.out.printf("95%% confidence interval = [%1$f, %2$f]", stats.confidenceHi(), stats.confidenceLo());
    }
}
