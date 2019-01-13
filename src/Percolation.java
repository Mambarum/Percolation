import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *  The {@code Percolation} class estimate the value \
 *  of the percolation threshold via Monte Carlo simulation. 
 *  It works with square field of n*n size.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://coursera.cs.princeton.edu/algs4/assignments/percolation.html"></a>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Volodymyr Shanoilo
 */
public class Percolation {
    private boolean[] sites;            // defines if the site is opened or closed
    private final WeightedQuickUnionUF compounds;   // contains information about compounds 
    private final int sitesNum;         // n - size of a field
    private final int totalSitesNum;    // n * n 
    private int openSitesNum = 0;       // number of currently opened sites
    private final int virtNodeTop;      // a virtual site that represents all the upper row
    private final int virtNodeBot;      // a virtual site that represents all the bottom row

/**
 * Creates n * n grid with all sites blocked
 * Creates default compounds array
 * @param n - size of the grid
 */
    public Percolation(int n)
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
/**
 * Checks if input values are within allowed range.
 * Index range is 1..N
 * @param col
 * @param row
 */
    private void sanityCheck(int col, int row) {
        if ((row <= 0) || (row > sitesNum) || (col <= 0) || (col > sitesNum) || ((row * col) > totalSitesNum))
            throw new IllegalArgumentException("Invalid site index specified: col = " + col + ", row = " + row);
    }

/**
 * Calculates index in the grid from row and column
 * @param row
 * @param col
 * @return
 */
    private int makeIndex(int row, int col) {
        return (row * sitesNum) + col;
    }

/**
 * Opens a cell by provided row and column.
 * The opened cell is immediately connected to the nearby compound (if exists)
 * if a cell is opened in the topmost row (row #1) then it is connected
 * to the virtual top node that effectively unites all compounds that 
 * exist in the top row. Same works for bottom row (row #N).
 * @param row
 * @param col
 */
    public void open(int row, int col)
    {
        if (isOpen(row, col)) // if the cell is already opened do nothing
            return;
        
        col--;  // decrement row and col to cast the to indexes that start from 0
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

/**
 * Checks if current cell is open
 * @param row
 * @param col
 * @return
 */
    public boolean isOpen(int row, int col)
    {
        sanityCheck(col, row);
        col--;  // decrement row and col to cast the to indexes that start from 0
        row--;
        return checkOpen(row, col);
    }

/**
 * Checks if a given cell belongs to the full compound.
 * The compound is full if it contains at least one cell from the topmost row.
 * Taking into account that all cells at the topmost row are connected to
 * a virtual top node all we have to do is check if the topmost node
 * belongs to the same compound as the one being tested.
 * @param row
 * @param col
 * @return
 */
    public boolean isFull(int row, int col)
    {
        if (!isOpen(row, col))
            return false;
        
        col--;  // decrement row and col to cast the to indexes that start from 0
        row--;
        return (compounds.connected(makeIndex(row, col), virtNodeTop));
    }

/**
 * Returns number of currently opened cells
 * @return
 */
    public int numberOfOpenSites()
    {
        return openSitesNum;
    }

/**
 * Checks if current state of the grid percolates.
 * The grid percolates if at least one of the cells from the bottom row
 * belongs to a full site. 
 * @return
 */
    public boolean percolates()
    {
        return compounds.connected(virtNodeBot, virtNodeTop);
    }
}
