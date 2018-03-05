import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private int trials;
    private double mean;
    private double stddev;

    private static final double MAGIC = 1.96;

    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        double[] pros = new double[n];
        int[] numbers = StdRandom.permutation(n * n, n * n);

        Percolation percolation = new Percolation(n);
        for (int i = 0; i < n; i++) {
            double counter = 0;
            for (int inx = 0; inx < numbers.length && !percolation.percolates(); inx++) {
                int row = (numbers[inx]) / n + 1;
                int col = (numbers[inx]) % n + 1;
                counter++;
                percolation.open(row, col);
            }
            pros[i] = counter / (n * n);
        }
        this.mean = StdStats.mean(pros);
        this.stddev = StdStats.stddev(pros);

    }

    public double mean() {                    // sample mean of percolation threshold
        return mean;
    }

    public double stddev() {                       // sample standard deviation of percolation threshold
        return stddev;
    }

    public double confidenceLo() {             // low  endpoint of 95% confidence interval
        return this.mean - ((MAGIC * stddev) / Math.sqrt(trials));
    }

    public double confidenceHi() {          // high endpoint of 95% confidence interval
        return mean() + ((MAGIC * stddev()) / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        int n = 200;
        int trials = 100;
        PercolationStats ps = new PercolationStats(n, trials);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean= " + ps.mean());
        StdOut.println("stddev = " + ps.stddev());
        StdOut.println("95%= " + confidence);
    }
}