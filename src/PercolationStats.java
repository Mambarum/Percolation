import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int size;
    private final int trials;
    private final double[] threshold;

    public PercolationStats(int n, int trialsIn) // perform trials independent experiments on an n-by-n grid
    {
        trials = trialsIn;
        size = n;

        threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(size);
            threshold[i] = singleRun(perc);
        }
    }

    private double singleRun(Percolation perc) {
        int row, col, i;

        for (i = 0; i < size * size; i++) {
            do {
                row = StdRandom.uniform(1, size + 1);
                col = StdRandom.uniform(1, size + 1);
            } while (perc.isOpen(row, col));

            perc.open(row, col);
            if (perc.percolates()) {
                break;
            }
        }

        return (double) perc.numberOfOpenSites() / (size * size);
    }

    public double mean() // sample mean of percolation threshold
    {
        return StdStats.mean(threshold);
    }

    public double stddev() // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(threshold);
    }

    private double fivePercent() {
        return 1.96 * stddev() / Math.sqrt(trials);
    }

    public double confidenceLo() // low endpoint of 95% confidence interval
    {
        return mean() - fivePercent();
    }

    public double confidenceHi() // high endpoint of 95% confidence interval
    {
        return mean() + fivePercent();
    }

    public static void main(String[] args) // test client (described below)
    {
        if (args.length < 2)
            throw new IllegalArgumentException("Please specify size of the field and number of trials");

        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
