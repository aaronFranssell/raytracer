package math;

import util.Constants;
import util.Library;

public class Vector
{
	public double x;
	public double y;
	public double z;
	
	public Vector(double incomingX, double incomingY, double incomingZ)
	{
		x = incomingX;
		y = incomingY;
		z = incomingZ;
	}
	
	public boolean equals(Object other)
	{
		if(other == null)
		{
			return false;
		}
		
		if(!(other instanceof Vector))
		{
			return false;
		}
		Vector otherVector = (Vector) other;
		if(Library.doubleEqual(otherVector.x, x, Constants.POSITIVE_ZERO) &&
		   Library.doubleEqual(otherVector.y, y, Constants.POSITIVE_ZERO) &&
		   Library.doubleEqual(otherVector.z, z, Constants.POSITIVE_ZERO))
		{	
			return true;
		}
		return false;
	}
	
	public Vector copy()
	{
		return new Vector(x,y,z);
	}

	public Vector cross(Vector right)
	{
		double newX;
		double newY;
		double newZ;
		newX = y*right.z - z*right.y;
		newY = -(x*right.z - z*right.x);
		newZ = x*right.y - y*right.x;
		Vector vector = new Vector(newX,newY,newZ);
		return vector;
	}

	public double dot(Vector other)
	{
		return(other.x * x + other.y * y + other.z * z);
	}
	
	public double magnitude()
	{
		return java.lang.Math.sqrt(x*x + y*y + z*z);
	}
	
	public String toString()
	{
		return ("x: " + x + ", y: " + y + ", z: " + z);
	}
	
	public void scale(double constant)
	{
		x *= constant;
		y *= constant;
		z *= constant;
	}
	
	public Vector scaleReturn(double constant)
	{
		return new Vector(x*constant, y*constant, z*constant);
	}
	
	public void normalize()
	{
		double mag = magnitude();
		x /= mag;
		y /= mag;
		z /= mag;
	}
	
	public Vector normalizeReturn()
	{
		double mag = magnitude();
		return new Vector(x/mag, y/mag, z/mag);
	}
	
	public Vector add(Vector v)
	{
		return new Vector(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	public Vector add(Point p)
	{
		return new Vector(this.x + p.x, this.y + p.y, this.z + p.z);
	}
	
	public Vector minus(Vector v)
	{
		return new Vector(this.x - v.x, this.y - v.y, this.z - v.z);
	}
	
	public Point toPoint()
	{
		return new Point(x,y,z);
	}
}
