package etc;

import primitives.Surface;
import util.Constants;
import util.Library;
import math.Point;
import math.Vector;

public class HitData
{
	private double smallestT;
	private Point p;
	private Vector normal;
	private Surface surface;
	private double[] hitTs;
	
	public HitData()
	{
		smallestT = Double.NaN;
	}
	
	public HitData(double incomingSmallestT, Surface incomingS, Vector incomingNormal, Point incomingHitPoint, 
				   double[] incomingHitTs)
	{		
		p = incomingHitPoint;
		surface = incomingS;
		normal = incomingNormal;
		smallestT = incomingSmallestT;
		hitTs = Library.sort(incomingHitTs);
	}
	
	public String toString()
	{
		String retString = "is hit: "+isHit()+"\nsmallestT: " + smallestT + "\np: " + p + "\nnormal: " + normal +
						   "\nhit ts: ";
		for(int i = 0; i < hitTs.length; i++)
		{
			retString += hitTs[i] + ", ";
		}
		retString += "\nsurfaceType: " + surface.getType();
		return retString;
	}

	public boolean isHit()
	{
		return !Double.isNaN(smallestT);
	}

	public Vector getNormal()
	{
		return normal;
	}

	public void setNormal(Vector normal)
	{
		this.normal = normal;
	}

	public Point getP()
	{
		return p;
	}

	public double getSmallestT()
	{
		return smallestT;
	}
	
	public Surface getSurface()
	{
		return surface;
	}

	public double[] getHitTs()
	{
		return hitTs;
	}
	
	/**
	 * This function returns all positive hit Ts for the object
	 * @return Sorted array of all positive hit Ts
	 */
	public double[] getPositiveHitTs()
	{
		int i = 0;
		for(i = 0; hitTs[i] <= Constants.POSITIVE_ZERO; i++){}
		
		double[] retTs = new double[hitTs.length - i];
		
		int currI = i;
		for(; i < hitTs.length; i++)
		{
			retTs[i - currI] = hitTs[i];
		}
		
		return retTs;
	}
}
