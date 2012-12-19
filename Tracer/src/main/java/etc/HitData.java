package etc;

import math.Point;
import math.Vector;
import surface.Surface;
import util.Constants;
import util.Library;

public class HitData
{
	private double t;
	private Point p;
	private Vector normal;
	private Surface surface;
	
	public HitData()
	{
		t = Double.NaN;
	}
	
	public HitData(double incomingT, Surface incomingS, Vector incomingNormal, Point incomingHitPoint)
	{
		p = incomingHitPoint;
		surface = incomingS;
		normal = incomingNormal;
		t = incomingT;
	}
	
	public String toString()
	{
		String retString = "is hit: "+isHit()+"\nt: " + t + "\np: " + p + "\nnormal: " + normal;
		retString += "\nsurfaceType: " + surface.getType();
		return retString;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		if(!(o instanceof HitData))
		{
			return false;
		}
		HitData other = (HitData) o;
		if(isHit() != other.isHit())
		{
			return false;
		}
		if(!isHit())
		{
			return true;//everything else will be null, so we can just return true here
		}
		
		if(surface != other.getSurface())
		{
			return false;
		}
		if(!Library.doubleEqual(t, other.getT(), Constants.POSITIVE_ZERO))
		{
			return false;
		}
		if(!normal.equals(other.getNormal()))
		{
			return false;
		}
		if(!p.equals(other.getP()))
		{
			return false;
		}
		return true;
	}

	public boolean isHit()
	{
		return !Double.isNaN(t);
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

	public double getT()
	{
		return t;
	}
	
	public Surface getSurface()
	{
		return surface;
	}
}
