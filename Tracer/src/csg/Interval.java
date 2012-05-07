package csg;

import util.Library;

public class Interval
{
	private double[] interval;
	
	public Interval(double[] incomingInterval)
	{
		interval = Library.sort(incomingInterval);
	}
	
	/**
	 * This method returns the near t value, will kill the program if there is no near t.
	 * @param smallestT
	 * @return The near double value closest to the smallestT
	 */
	public double getNearInterval(double smallestT)
	{
		for(int i = 0; i < interval.length; i++)
		{
			if(interval[i] < smallestT && smallestT < interval[i + 1])
			{
				return interval[i];
			}
		}
		try
		{
			throw new Exception("Not a valid smallestT for the interval!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		//won't ever get here...
		return 0.0;
	}
	
	/**
	 * This function returns true if the incoming smallest t is in an interval.
	 * @param smallestT
	 * @return true if smallestT is in an interval
	 */
	public boolean isInInterval(double smallestT)
	{
		for(int i = 0; i < interval.length; i+=2)
		{
			if(smallestT > interval[i] && smallestT < interval[i + 1])
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This function assumes a SORTED array.
	 * @param sortedTArray The incoming sorted array.
	 */
	public boolean allInInterval(double[] sortedTArray)
	{
		return interval[0] < sortedTArray[0] && interval[interval.length - 1] > sortedTArray[sortedTArray.length - 1];
	}
	
	/**
	 * This function will return the closest "t" value that is beyond the incoming "t" value. This is useful for determining
	 * if a "t" value is bounded by another solid.
	 * @param smallestT The smallestT we are testing to see if it is in an interval.
	 * @return The next "t" value beyond the one being passed in. Returns Double.NaN if the smallestT is not between anything.
	 */
	public double getNextGreatestInterval(double smallestT)
	{
		//edge case first
		if(interval[0] > smallestT)
		{
			return Double.NaN;
		}
		
		//increment by 2, if the smallestT is inside of an interval
		for(int i = 0; i < interval.length; i += 2)
		{
			if(smallestT > interval[i] && smallestT < interval[i+1])
			{
				//smallestT is inside the invertval, the hit point will be the far end of the interval.
				return interval[i+1];
			}
			//can break out of the loop if we know that the smallestT does not lie in an interval
			if(smallestT < interval[i])
			{
				break;
			}
		}
		
		return Double.NaN;
	}

	public double[] getInterval()
	{
		return interval;
	}

	public void setInterval(double[] interval)
	{
		this.interval = interval;
	}
}
