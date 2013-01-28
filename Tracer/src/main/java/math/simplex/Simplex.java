package math.simplex;

import math.Point;
import math.Vector;
import scene.ray.Ray;

public abstract class Simplex
{
	protected Point a;
	protected Point b;
	protected Point c;
	protected Vector normal;
	
	public abstract double getT(Ray r);
	
	protected void calcNormal()
	{
		Vector aB = a.minus(b);
		Vector aC = a.minus(c);
		normal = aB.cross(aC).normalizeReturn();
	}
	
	public Vector getNormal(Ray r)
	{
		//we need to make the normal point back to the eye.
		if(r.getD().dot(normal) > 0)
		{
			return normal.scaleReturn(-1.0);
		}
		return normal;
	}
	
	public String toString()
	{
		return "a: " + a.toString() + "\nb: " + b.toString() + "\nc: " + c.toString();
	}
	
	public Point getA()
	{
		return a;
	}

	public void setA(Point a)
	{
		this.a = a;
	}

	public Point getB()
	{
		return b;
	}

	public void setB(Point b)
	{
		this.b = b;
	}

	public Point getC()
	{
		return c;
	}

	public void setC(Point c)
	{
		this.c = c;
	}
}
