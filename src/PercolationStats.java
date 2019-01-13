import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// >>>>>>
/* import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner; */
// <<<<<<

public class PercolationStats {
// !!!!!! final
    private final int size;
    private final int trials;
    private final double mean;
    private final double deviation;

// >>>>>>
/*    private boolean randomIn = true;
    private Scanner inFile = null;
    private final String fileName = "test_data/input3.txtss";
    private List<IntPairs> inputBuf = new ArrayList<IntPairs>();
    
    private class IntPairs {
        public int row;
        public int col;
    } */
// <<<<<<<<
    
    public PercolationStats(int n, int trialsIn) // perform trials independent experiments on an n-by-n grid
    {
        if ((n <= 0) || (trialsIn <= 0))
            throw new IllegalArgumentException("Bad input parameters: size = " + n + ", trials = " + trialsIn);
        
        trials = trialsIn;
        size = n;
// >>>>>>>
/*        if ((n == 0) && (trialsIn == 0)) {
            try {
                openFile(fileName);
                System.out.println("Input is taken from " + fileName);
                size = readFilePairs(inputBuf);
                trials = 2;
                randomIn = false;
            } catch (FileNotFoundException nfex) {
                size = 500;
                trials = 10;
                System.out.println("Falling back to random input with default " + size + " size and " + trials + " trials");
            }
        } else {
            trials = trialsIn;
            size = n;
            System.out.println("Random input with user-provided " + size + " size and " + trials + " trials");
        } */
// <<<<<<<<
        double[] threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(size);
            threshold[i] = singleRun(perc);
        }
        mean = StdStats.mean(threshold);
        deviation = StdStats.stddev(threshold);
    }
    
// >>>>>>>>
/*    private void openFile(String name) throws FileNotFoundException
    {
        FileReader file = null;
        file = new FileReader(name);
        inFile = new Scanner(file);
    }
    
    private int readFilePairs(List<IntPairs> buf)
    {
        int len = inFile.nextInt();
        
        try {
            while (true) {
                IntPairs tmp = new IntPairs();
                tmp.row = inFile.nextInt();
                tmp.col = inFile.nextInt();
                buf.add(tmp);
            }
        } catch (NoSuchElementException eof) {
            // File ended
        }
        return len;
    } */
// <<<<<<<<<  
    
    private double singleRun(Percolation perc) {
        int row, col;
        int i = 0;
// >>>>>>>>>
//        if (randomIn) {
// <<<<<<<<<  
        for (i = 0; i < size * size; i++) {
                do {
                    row = StdRandom.uniform(1, size + 1);
                    col = StdRandom.uniform(1, size + 1);
                } while (perc.isOpen(row, col));

                perc.open(row, col);
                if (perc.percolates())
                    break;
            }
// >>>>>>>>>        
/*      } else {
            while (i < inputBuf.size())
            {
                IntPairs tmp = inputBuf.get(i++);
    
                perc.open(tmp.row, tmp.col);
                if (perc.percolates())
                    break;
            }
        } */
// <<<<<<<<<
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
