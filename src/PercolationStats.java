import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;

public class PercolationStats {
    private final int size;
    private final int trials;
    private final double[] threshold;
    
    private boolean randomIn = true;
    
    private FileReader file = null;
    private Scanner inFile = null;
    private class intPairs {
        public int row;
        public int col;
    }
    private String fileName = "test_data/input6.txt";
    private Vector<intPairs> input;

    public PercolationStats(int n, int trialsIn) // perform trials independent experiments on an n-by-n grid
    {
        if ((n == 0) && (trialsIn == 0)) {
            if(openFile(fileName)) {
                System.out.println("Input is taken from " + fileName);
                input = new Vector<intPairs>();
                size = readFilePairs(input);
                trials = 1;
                randomIn = false;
            } else {
                size = 100;
                trials = 10;
                System.out.println("Falling back to random input with default " + size + " size and " + trials + " trials");
            }
        } else {
            trials = trialsIn;
            size = n;
            System.out.println("Random input with user-provided " + size + " size and " + trials + " trials");
        }

        threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(size);
            threshold[i] = singleRun(perc);
        }
    }

    private boolean openFile(String name)
    {
        try {
            file = new FileReader(name);
            inFile = new Scanner(file);
        } catch (FileNotFoundException nfex) {
            System.out.println("Cannot open the file");
            return false;
        }
        return true;
    }
    
    private int readFilePairs(Vector<intPairs> buf)
    {
        int len = inFile.nextInt();
        
        try {
            while (true) {
                intPairs tmp = new intPairs();
                tmp.row = inFile.nextInt();
                tmp.col = inFile.nextInt();
                buf.add(tmp);
            }
        } catch (NoSuchElementException eof) {}
        return len;
    }
    
    private double singleRun(Percolation perc) {
        int row, col;
        int i = 0;

        if(randomIn) {
            for (i = 0; i < size * size; i++) {
                do {
                    row = StdRandom.uniform(1, size + 1);
                    col = StdRandom.uniform(1, size + 1);
                } while (perc.isOpen(row, col));

                perc.open(row, col);
                if (perc.percolates())
                    break;
            }
        } else {
            while (i < input.size())
            {
                intPairs tmp = input.get(i++);
    
                perc.open(tmp.row, tmp.col);
                if (perc.percolates())
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
