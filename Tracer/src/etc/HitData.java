package etc;

import primitives.Surface;
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
		retString += "\nsurfaceType: " + Surface.translateType(surface);
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

	public void setSurface(Surface surface)
	{
		this.surface = surface;
	}

	public double[] getHitTs()
	{
		return hitTs;
	}
}
