import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] sites;
    private final WeightedQuickUnionUF compounds;
    private final int totalSitesNum;
    private final int sitesNum;
    private int openSitesNum = 0;
    private final int virtNodeTop;
    private final int virtNodeBot;

    public Percolation(int n) // create n-by-n grid, with all sites blocked
    {
        if (n <= 0)
            throw new IllegalArgumentException("Invalid number of sites specified");

        compounds = new WeightedQuickUnionUF(n * n + 2);

        virtNodeTop = n * n;
        virtNodeBot = n * n + 1;

        sites = new boolean[n * n];
        totalSitesNum = n * n;
        sitesNum = n;
        for (int i = 0; i < totalSitesNum; i++)
            sites[i] = false;
    }

    private void sanityCheck(int col, int row) {
        if ((row <= 0) || (row > sitesNum) || (col <= 0) || (col > sitesNum) || ((row * col) > totalSitesNum))
            throw new IllegalArgumentException("Invalid site index specified: col = " + col + ", row = " + row);
    }

    private int makeIndex(int row, int col) {
        return (row * sitesNum) + col;
    }

    public void open(int row, int col) // open site (row, col) if it is not open already
    {
        if (isOpen(row, col))
            return;
        
        col--;
        row--;

        int index = makeIndex(row, col);
        openSitesNum++;
        sites[index] = true;

        if (row == 0)
            compounds.union(index, virtNodeTop);
        else if (checkOpen(row - 1, col))
            compounds.union(index, makeIndex((row - 1), col));
        
        if (row == sitesNum - 1)
            compounds.union(index, virtNodeBot);
        else if (checkOpen(row + 1, col))
            compounds.union(index, makeIndex((row + 1), col));

        if ((col != 0) && checkOpen(row, col - 1))
            compounds.union(index, makeIndex(row, (col - 1)));

        if ((col != sitesNum - 1) && checkOpen(row, col + 1))
            compounds.union(index, makeIndex(row, (col + 1)));
    }

    private boolean checkOpen(int row, int col) {
        return sites[makeIndex(row, col)];
    }

    public boolean isOpen(int row, int col) // is site (row, col) open?
    {
        sanityCheck(col, row);
        col--;
        row--;
        return checkOpen(row, col);
    }

    public boolean isFull(int row, int col) // is site (row, col) full?
    {
        if (!isOpen(row, col))
            return false;
        
        col--;
        row--;
        return (compounds.find(makeIndex(row, col)) == compounds.find(virtNodeTop));
    }

    public int numberOfOpenSites() // number of open sites
    {
        return openSitesNum;
    }

    public boolean percolates() // does the system percolate?
    {
        return (compounds.find(virtNodeBot) == compounds.find(virtNodeTop));
    }
}
