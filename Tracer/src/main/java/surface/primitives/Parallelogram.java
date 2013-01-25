package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import math.simplex.BarycentricSimplexImpl;

import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;
import scene.ray.Ray;
import surface.Surface;
import util.Util;
import util.UtilImpl;

public class Parallelogram extends Surface
{
	private Vector normal;
	private BarycentricSimplexImpl abc;
	private BarycentricSimplexImpl bcd;
	
	public Parallelogram(Point incomingBasePoint, Vector incomingV1, Vector incomingV2, Color incomingCR, Color incomingCL, Color incomingCA,
						 Effects incomingEffects)
	{
		this(incomingBasePoint, incomingV1, incomingV2, incomingCR, incomingCL, incomingCA, incomingEffects, new UtilImpl());
	}
	
	public Parallelogram(Point incomingBasePoint, Vector incomingV1, Vector incomingV2, Color incomingCR, Color incomingCL, Color incomingCA,
			 			 Effects incomingEffects, Util incomingUtil)
	{
		ops = incomingUtil;
		cR = incomingCR;
		cL = incomingCL;
		cA = incomingCA;
		effects = incomingEffects;
		
		Vector v1 = incomingV1;
		Vector v2 = incomingV2;
		Point a = incomingBasePoint;
		Point b = a.add(v1);
		Point c = a.add(v2);
		Point d = c.add(v1);
		abc = new BarycentricSimplexImpl(a,b,c);
		bcd = new BarycentricSimplexImpl(b,c,d);
		normal = v1.cross(v2).normalizeReturn();
	}
	
	@Override
	public ArrayList<HitData> getHitData(Ray r) throws RaytracerException
	{
		double abcT = abc.getT(r);
		double bcdT = bcd.getT(r);
		if(Double.isNaN(abcT) && Double.isNaN(bcdT))
		{
			return new ArrayList<HitData>();
		}
		
		double t = ops.getGreaterValue(abcT, bcdT);
		Point p = ops.getP(t ,r);
		Vector normalTemp = normal.copy();
		if(normalTemp.dot(r.getD()) > 0)
		{
			normalTemp = normalTemp.scaleReturn(-1.0);
		}
		HitData hit = new HitData(t, this, normalTemp, p);
		ArrayList<HitData> retList = new ArrayList<HitData>();
		retList.add(hit);
		return retList;
	}

	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Parallelogram;
	}
}
