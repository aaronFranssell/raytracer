package primitives;

import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;
import math.Point;
import math.Vector;
import util.Constants;
import util.Library;


public class Plane extends Surface
{
	private Vector n;
	private Point pointOnPlane;
	public Plane(Vector incomingN, Point incomingPoint, Color incomingCR, Color incomingCL, Color incomingCA, 
				  Effects incomingEffects)
	{
		n = incomingN;
		pointOnPlane = incomingPoint;
		cR = incomingCR;
		cL = incomingCA;
		cA = incomingCA;
		effects = incomingEffects;
	}
	
	private double calcT(Ray r)
	{
		double dDotN = r.getD().dot(n);
		if (dDotN == 0.0)
		{
			return Double.MAX_VALUE;
		}
		
		Vector tempD = r.getD();
		Vector pMinusE = pointOnPlane.minus(r.getEye());
				
		double top = pMinusE.dot(n);
		double bottom = tempD.dot(n);
		double hitT = top/bottom;
		
		if(hitT < Constants.POSITIVE_ZERO)
		{
			return Double.MAX_VALUE;
		}
		return hitT;
	}
	
	public Vector getNormal(Point p, Ray r)
	{
		return n;
	}
		
	public HitData getHitData(Ray r)
	{
		double[] retTArray = new double[1];
		retTArray[0] = calcT(r);
		double smallestT = Library.getSmallestT(retTArray);
		Point p = Library.getP(smallestT, r);
		HitData hit = new HitData(smallestT, this, n, p, retTArray);
		return hit; 
	}

	public Color getColor(Point light, Point eye, int phongExponent, boolean inShadow, Color cR, Point p, Color cA, 
						  Color cL, Vector n)
	{
		Color returnColor;
		Color white = new Color(0.75,0.75,0.75);
		Color black = new Color(0.1,0.1,0.1);
		if(Math.cos(p.z) > 0)
		{
			if(Math.sin(p.x) > 0)
			{
				returnColor = white;
			}
			else
			{
				returnColor =  black;
			}
		}
		else
		{
			if(Math.sin(p.x) > 0)
			{	
				returnColor = black;
			}
			else
			{
				returnColor =  white;
			}
		}
		return Library.getColorLambertian(returnColor, cA, cL, n, light, p, inShadow);
	}
	
	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Plane;
	}
}
