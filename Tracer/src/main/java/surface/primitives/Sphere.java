package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;

public class Sphere extends Surface
{
	protected Point center;
	protected double radius;

	public Sphere(Point incomingCenter, double incomingRadius, Color incomingCR, Color incomingCA, Color incomingCL, Effects incomingEffects, Util incomingOps)
	{
		center = incomingCenter;
		radius = incomingRadius;
		cR = incomingCR;
		cA = incomingCA;
		cL = incomingCL;
		effects = incomingEffects;
		ops = incomingOps;
	}
	
	public Sphere(Point incomingCenter, double incomingRadius, Color incomingCR, Color incomingCA, Color incomingCL, Effects incomingEffects)
	{
		this(incomingCenter, incomingRadius, incomingCR, incomingCA, incomingCL, incomingEffects, new UtilImpl());
	}

	private double getDiscriminant(Ray r)
	{
		Vector eMinusC = r.getEye().minus(center);
		double dDotD = r.getD().dot(r.getD());
		double rSquared = radius * radius;
		double dis = r.getD().dot(eMinusC) * r.getD().dot(eMinusC);
		dis -= dDotD * (eMinusC.dot(eMinusC) - rSquared);
		return dis;
	}

	public ArrayList<HitData> getHitData(Ray r)
	{
		Vector eMinusC = r.getEye().minus(center);
		double discriminant = getDiscriminant(r);
		if (discriminant <= 0)
		{
			return new ArrayList<HitData>();
		}
		double dDotD = r.getD().dot(r.getD());
		discriminant = Math.pow(discriminant, 0.5);
		double greaterT = -1 * (r.getD().dot(eMinusC));
		double smallerT = -1 * (r.getD().dot(eMinusC));
		greaterT += discriminant;
		smallerT -= discriminant;
		greaterT /= dDotD;
		smallerT /= dDotD;
		double[] retTArray = new double[2];
		retTArray[0] = greaterT;
		retTArray[1] = smallerT;
		ArrayList<HitData> retList = new ArrayList<HitData>();
		for(double t : retTArray)
		{
			Point p = ops.getP(t, r);
			Vector normal = getNormal(p, r);
			retList.add(new HitData(t, this, normal, p));
		}
		return retList;
	}

	protected Vector getNormal(Point p, Ray r)
	{
		Vector n = p.minus(center).normalizeReturn();
		return n;
	}

	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Sphere;
	}

}
