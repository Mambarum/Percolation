import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int[] sites;
	WeightedQuickUnionUF compounds;
	private final int sites_num;
	private final int N;
	private int open_sites_num = 0;
	private int virt_node_top;
	private int virt_node_bot;
	public Percolation(int n)                // create n-by-n grid, with all sites blocked
	{
		if(n <= 0) 
			throw new IllegalArgumentException("Invalid number of sites specified");
		
		compounds = new WeightedQuickUnionUF(n * n + 2);
		
		virt_node_top = n * n;
		virt_node_bot = n * n + 1;
		
		sites = new int[n * n];
		sites_num = n * n;
		N = n;
		for(int i = 0; i < sites_num; i++)
			sites[i] = -1;
	}
	

	private void sanity_check(int col, int row)
	{
		if( (row <= 0)  ||
			(row > N) ||
			(col <= 0)  ||
			(col > N) ||
			((row * col) > sites_num))
				throw new IllegalArgumentException("Invalid site index specified: col = " + col + ", row = " + row);
	}
	
	private int make_index(int row, int col)
	{
		return (row * N) + col;
	}
	
	public void open(int row, int col)   // open site (row, col) if it is not open already
	{
		sanity_check(col, row);
		col--;
		row--;
		
		if(check_open(row, col))
			return;
		
		int index = make_index(row, col);
		open_sites_num++;
		sites[index] = 0;
		
		if(row == 0)
			compounds.union(index, virt_node_top);
		else if(row == N - 1)
			compounds.union(index, virt_node_bot);
		else
		{
			if(check_open(row - 1, col))
				compounds.union(index, make_index((row - 1), col));
			
			if(check_open(row + 1, col))
				compounds.union(index, make_index((row + 1), col));
		}
		
		if((col != 0) && check_open(row, col - 1))
			compounds.union(index, make_index(row, (col - 1)));
		
		if((col != N - 1) && check_open(row, col + 1))
			compounds.union(index, make_index(row, (col + 1)));
	}
	
	private boolean check_open(int row, int col)
	{
		return (sites[make_index(row, col)] != -1);
	}
	
	public boolean isOpen(int row, int col) // is site (row, col) open?
	{
		sanity_check(col, row);
		col--;
		row--;
		return check_open(row, col);
	}
	
	public boolean isFull(int row, int col) // is site (row, col) full?
	{
		sanity_check(col, row);
		col--;
		row--;
		return (compounds.find(row * col) == compounds.find(virt_node_top));
	}
	
	public     int numberOfOpenSites()      // number of open sites
	{
		return open_sites_num;
	}
	
	public boolean percolates()             // does the system percolate?
	{
		return (compounds.find(virt_node_bot) == compounds.find(virt_node_top));
	}
}
