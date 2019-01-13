import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *  The {@code PercolationStats} class runs the Monte Carlo
 *  simulation on {@code Percolation} class and calculates 
 *  statistics like mean value of percolation threshold or
 *  standard deviation;
 *  <p>
 *  For additional documentation, see
 *  <a href="http://coursera.cs.princeton.edu/algs4/assignments/percolation.html"></a>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Volodymyr Shanoilo
 */
public class PercolationStats {
    private final int size;
    private final int trials;
    private final double mean;
    private final double deviation;

/**
 * Performs trials of independent experiments on an n-by-n grid
 * @param n - size of the grid
 * @param trialsIn - number of trials to run
 */
    public PercolationStats(int n, int trialsIn)
    {
        if ((n <= 0) || (trialsIn <= 0))
            throw new IllegalArgumentException("Bad input parameters: size = " + n + ", trials = " + trialsIn);
        
        trials = trialsIn;
        size = n;
        
        double[] threshold = new double[trials];    // an array to store the results of each trial
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(size);
            threshold[i] = singleRun(perc);
        }
        // It would be easy and quick to calculate mean value right inside the loop
        // In this case there would be no need in calling StdStats.mean() at all
        // However the number of calls to StdStats methods is checked during the submission
        // (both mean() and stddev() should be called exactly 1 time)
        // This is the reason for calling the methods here
        mean = StdStats.mean(threshold);
        deviation = StdStats.stddev(threshold);
    }

/**
 * Run a single simulation
 * Rows and columns are picked randomly
 * @param perc - an initialized instance of a grid
 * @return - percolation threshold for the current run
 */
    private double singleRun(Percolation perc) {
        int row, col;
        int i = 0;

        for (i = 0; i < size * size; i++) {
                do { // a loop to exclude already opened cells
                    row = StdRandom.uniform(1, size + 1);
                    col = StdRandom.uniform(1, size + 1);
                } while (perc.isOpen(row, col));

                perc.open(row, col);
                if (perc.percolates())
                    break;
            }
        return (double) perc.numberOfOpenSites() / (size * size);
    }

/**
 * Mean of percolation threshold
 * @return
 */
    public double mean()
    {
        return mean;
    }

/**
 * Returns standard deviation of percolation threshold
 * @return
 */
    public double stddev()
    {
        return deviation;
    }

    private double fivePercent() {
        return 1.96 * stddev() / Math.sqrt(trials);
    }

/**
 * Returns low endpoint of 95% confidence interval
 * @return
 */
    public double confidenceLo()
    {
        return mean() - fivePercent();
    }

    /**
     * Returns high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi()
    {
        return mean() + fivePercent();
    }

/**
 * Test client
 * Uses input arguments. 
 * @param args[0] - size of the grid
 * @param args[1] - number of trials
 */
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
