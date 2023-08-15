package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.simplex.Simplex;
import math.simplex.factory.ParametricSimplexFactoryImpl;
import math.simplex.factory.SimplexFactory;
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
	private Simplex simplex;
	
	public Triangle(Color incomingCR, Color incomingCL, Color incomingCA, Point incomingA, Point incomingB,	Point incomingC, Effects incomingEffects,
					Util incomingOps, SimplexFactory simplexFactory)
	{
		cA = incomingCA;
		cL = incomingCL;
		cR = incomingCR;
		a = incomingA;
		b = incomingB;
		c = incomingC;
		simplex = simplexFactory.getSimplex(a, b, c);
		effects = incomingEffects;
		ops = incomingOps;
	}
	
	public Triangle(Color incomingCR, Color incomingCL, Color incomingCA, Point incomingA, Point incomingB,	Point incomingC, Effects incomingEffects)
	{
		this(incomingCR, incomingCL, incomingCA, incomingA, incomingB, incomingC, incomingEffects, new UtilImpl(), new ParametricSimplexFactoryImpl());
	}
	
	public ArrayList<HitData> getHitData(Ray r)
	{
		double smallestT = simplex.getT(r);
		if(Double.isNaN(smallestT))
		{
			return new ArrayList<HitData>();
		}
		HitData hit = simplex.getHitData(r, this);
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
