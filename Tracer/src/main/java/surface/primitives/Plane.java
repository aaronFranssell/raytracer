package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Constants;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;


public class Plane extends Surface
{
	private Vector normal;
	private Point pointOnPlane;
	public Plane(Vector incomingNormal, Point incomingPoint, Color incomingCR, Color incomingCL, Color incomingCA, Effects incomingEffects, Util incomingOps)
	{
		normal = incomingNormal.normalizeReturn();
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
		double dDotN = r.getD().dot(normal);
		if (UtilImpl.doubleEqual(dDotN, 0.0, Constants.POSITIVE_ZERO))
		{
			return Double.NaN;
		}
		
		Vector tempD = r.getD();
		Vector pMinusE = pointOnPlane.minus(r.getEye());
				
		double top = pMinusE.dot(normal);
		double bottom = tempD.dot(normal);
		double hitT = top/bottom;
		return hitT;
	}
	
	protected Vector getNormal(Point p, Ray r)
	{
		return normal;
	}
		
	public ArrayList<HitData> getHitData(Ray r)
	{
		ArrayList<HitData> retHitData = new ArrayList<HitData>();
		double smallestT = calcT(r);
		if(Double.isNaN(smallestT))
		{
			return retHitData;
		}
		Point p = ops.getP(smallestT, r);
		HitData hit = new HitData(smallestT, this, normal, p);
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
