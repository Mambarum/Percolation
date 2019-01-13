import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int size;
    private final int trials;
    private final double mean;
    private final double deviation;

    public PercolationStats(int n, int trialsIn) // perform trials independent experiments on an n-by-n grid
    {
        if ((n <= 0) || (trialsIn <= 0))
            throw new IllegalArgumentException("Bad input parameters: size = " + n + ", trials = " + trialsIn);
        
        trials = trialsIn;
        size = n;
        double[] threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(size);
            threshold[i] = singleRun(perc);
        }
        mean = StdStats.mean(threshold);
        deviation = StdStats.stddev(threshold);
    }
    
    private double singleRun(Percolation perc) {
        int row, col;
        int i = 0;

        for (i = 0; i < size * size; i++) {
                do {
                    row = StdRandom.uniform(1, size + 1);
                    col = StdRandom.uniform(1, size + 1);
                } while (perc.isOpen(row, col));

                perc.open(row, col);
                if (perc.percolates())
                    break;
            }
        return (double) perc.numberOfOpenSites() / (size * size);
    }

    public double mean() // sample mean of percolation threshold
    {
        return mean;
    }

    public double stddev() // sample standard deviation of percolation threshold
    {
        return deviation;
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
        int size, trials;
        if (args.length < 2) {
            size = 0;
            trials = 0;
        } else {
            size = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }

        PercolationStats stats = new PercolationStats(size, trials);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
