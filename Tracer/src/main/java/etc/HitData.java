package etc;

import math.Point;
import math.Vector;
import surface.Surface;

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
		String retString = "is hit: "+isHit()+"\nt: " + t + "\np: " + p + "\nnormal: " + normal +
						   "\nhit ts: ";
		retString += "\nsurfaceType: " + surface.getType();
		return retString;
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
