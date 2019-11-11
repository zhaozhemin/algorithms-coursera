import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] when;
    private double meanNumber;
    private double stddevNumber;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        when = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            when[i] = (double) p.numberOfOpenSites() / (n * n);
        }
        meanNumber = StdStats.mean(when);
        stddevNumber = StdStats.stddev(when);
    }

    public double mean() {
        return meanNumber;
    }

    public double stddev() {
        return stddevNumber;
    }

    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(when.length));
    }

    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(when.length));
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        StdOut.print("mean = ");
        StdOut.println(ps.mean());
        StdOut.printf("stddev = ");
        StdOut.println(ps.stddev());
        StdOut.print("95% confidence interval = ");
        StdOut.println("[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
