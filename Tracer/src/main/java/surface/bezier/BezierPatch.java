package surface.bezier;

import math.Point;

public class BezierPatch
{
	private Point a;
	private Point b;
	private Point c;
	
	public BezierPatch(Point incomingA, Point incomingB, Point incomingC)
	{
		a = incomingA;
		b = incomingB;
		c = incomingC;
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
