import edu.princeton.cs.algs4.StdRandom;
//import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private final int size;
	private final int trials;
	private double mean = 0;
	private double deviation = 0;
	private double[] threshold;
	
	private double single_run(Percolation perc)
	{
		int row, col, i;
		
		for(i = 0; i < size * size; i++)
		{
			do
			{
				row = StdRandom.uniform(size);
				col = StdRandom.uniform(size);
				if((row == size) || (col == size)) System.out.println("BINGO!");
//				else System.out.println("row = " + row + ", col = " + col);
			} while (perc.isOpen(row, col));
			
			perc.open(row, col);
			if(perc.percolates())
			{
				System.out.println("The system percolates at " + i + " open sites (" + (double)i/(size*size) + ")");
				break;
			}
		}

		return (double)i/(size*size);
	}
	
	public PercolationStats(int n, int T)    // perform trials independent experiments on an n-by-n grid
	{
		double thres_sum = 0;
		trials = T;
		size = n;
		
		threshold = new double[trials];
		for(int i = 0; i < trials; i++)
		{
			System.out.println("Trial #" + i);
			Percolation perc = new Percolation(size);
			threshold[i] = single_run(perc);
			thres_sum += threshold[i]; 
			perc = null;
		}
		
		mean = thres_sum / trials;
   }
	
   public double mean()                          // sample mean of percolation threshold
   {
	   return mean;
   }
   
   public double stddev()                        // sample standard deviation of percolation threshold
   {
	   double dev = 0;
	   
	   for(int i = 0; i < trials; i++)
	   {
		   	double tmp = threshold[i] - mean;
		   	dev += tmp * tmp;
	   }
	   dev = dev/(trials - 1);
	   deviation = Math.sqrt(dev);
	   return deviation;
   }
   
   private double five_percent()
   {
	   return 1.96 * deviation / Math.sqrt(trials);
   }
   public double confidenceLo()                  // low  endpoint of 95% confidence interval
   {
	   return mean - five_percent();
   }
   
   public double confidenceHi()                  // high endpoint of 95% confidence interval
   {
	   return mean + five_percent();
   }
   
   public static void main(String[] args)        // test client (described below)
   {
//	   if(args.length < 2)
//		   throw new IllegalArgumentException("Please specify size of the field and number of trials");
//	   
//	   PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	   PercolationStats stats = new PercolationStats(30, 10);
	   System.out.println("mean                    = " + stats.mean());
	   System.out.println("stddev                  = " + stats.stddev());
	   System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", "+ stats.confidenceHi() + "]");
   }
}
