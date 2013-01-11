package surface.primitives;

import java.util.ArrayList;

import math.BarycentricSimplex;
import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;

public class Triangle extends Surface
{
	private Point a;
	private Point b;
	private Point c;
	private BarycentricSimplex simplex;
	
	public Triangle(Color incomingCR, Color incomingCL, Color incomingCA, Point incomingA, Point incomingB,	Point incomingC, Effects incomingEffects,
					Util incomingOps)
	{
		cA = incomingCA;
		cL = incomingCL;
		cR = incomingCR;
		a = incomingA;
		b = incomingB;
		c = incomingC;
		simplex = new BarycentricSimplex(a,b,c);
		effects = incomingEffects;
		ops = incomingOps;
	}
	
	public Triangle(Color incomingCR, Color incomingCL, Color incomingCA, Point incomingA, Point incomingB,	Point incomingC, Effects incomingEffects)
	{
		this(incomingCR, incomingCL, incomingCA, incomingA, incomingB, incomingC, incomingEffects, new UtilImpl());
	}
	
	protected Vector getNormal(Point p, Ray r)
	{
		Vector aB = a.minus(b);
		Vector aC = a.minus(c);
		Vector n = aB.cross(aC).normalizeReturn();
		//we need to make the normal point back to the eye.
		if(r.getD().dot(n) > 0)
		{
			n = n.scaleReturn(-1.0);
		}
		return n;
	}
	
	public ArrayList<HitData> getHitData(Ray r)
	{
		double smallestT = simplex.getT(r);
		if(Double.isNaN(smallestT))
		{
			return new ArrayList<HitData>();
		}
		Point p = ops.getP(smallestT,r);
		Vector normal = getNormal(p, r);
		HitData hit = new HitData(smallestT, this, normal, p);
		ArrayList<HitData> retList = new ArrayList<HitData>();
		retList.add(hit);
		return retList;
	}
	
	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Triangle;
	}
}
