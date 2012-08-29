package math;

import util.Constants;


public class Point
{
	public double x;
	public double y;
	public double z;
	
	public Point()
	{
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
	public Point(double incomingX, double incomingY, double incomingZ)
	{
		x = incomingX;
		y = incomingY;
		z = incomingZ;
	}
	
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		if(!(o instanceof Point))
		{
			return false;
		}
		Point other = (Point) o;
		if(other.x + Constants.POSITIVE_ZERO < x || other.x - Constants.POSITIVE_ZERO > x)
		{
			return false;
		}//if
		if(other.y + Constants.POSITIVE_ZERO < y || other.y - Constants.POSITIVE_ZERO > y)
		{
			return false;
		}
		if(other.z + Constants.POSITIVE_ZERO < z || other.z - Constants.POSITIVE_ZERO > z)
		{
			return false;
		}//if
		return true;
	}
	
	public Point scaledReturn(double val)
	{
		return new Point(x*val, y*val, z*val);
	}
	
	public Vector minus(Point p2)
	{
		return new Vector(this.x - p2.x, this.y - p2.y, this.z - p2.z);
	}
	
	public Vector add(Point p2)
	{
		return new Vector(this.x + p2.x,this.y + p2.y, this.z + p2.z);
	}
	
	public Point copy()
	{
		return new Point(x,y,z);
	}
	
	public String toString()
	{
		return "x: " + x + ", y: " + y + ", z: " + z;
	}
}