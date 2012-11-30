package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Constants;
import util.Library;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;


public class Plane extends Surface
{
	private Vector n;
	private Point pointOnPlane;
	public Plane(Vector incomingN, Point incomingPoint, Color incomingCR, Color incomingCL, Color incomingCA, Effects incomingEffects, Util incomingOps)
	{
		n = incomingN;
		pointOnPlane = incomingPoint;
		cR = incomingCR;
		cL = incomingCA;
		cA = incomingCA;
		effects = incomingEffects;
		ops = incomingOps;
	}
	
	public Plane(Vector incomingN, Point incomingPoint, Color incomingCR, Color incomingCL, Color incomingCA, Effects incomingEffects)
	{
		this(incomingN, incomingPoint, incomingCR, incomingCL, incomingCA, incomingEffects, new UtilImpl());
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
		
	public ArrayList<HitData> getHitData(Ray r)
	{
		double[] retTArray = new double[1];
		retTArray[0] = calcT(r);
		double smallestT = Library.getSmallestT(retTArray);
		Point p = ops.getP(smallestT, r);
		HitData hit = new HitData(smallestT, this, n, p);
		ArrayList<HitData> retHitData = new ArrayList<HitData>();
		retHitData.add(hit);
		return retHitData; 
	}
	
	@Override
	public Color getColor(Point light, Point eye, boolean inShadow, Vector n, Point p)
	{
		Color returnColor;
		Color white = new Color(0.7,0.7,0.7);
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
		if(effects.getPhong() != null)
		{
			return ops.getColorPhong(returnColor, cL, cA, n, light, eye, effects.getPhong().getExponent(), p, inShadow);
		}
		else
		{
			return ops.getColorLambertian(returnColor, cA, cL, n, light, p, inShadow);
		}
	}
	
	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Plane;
	}
}
