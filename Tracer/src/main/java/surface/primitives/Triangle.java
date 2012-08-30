package surface.primitives;

import java.util.ArrayList;

import math.Matrix3;
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
	
	public Triangle(Color incomingCR, Color incomingCL, Color incomingCA, Point incomingA, Point incomingB,	Point incomingC, Effects incomingEffects,
					Util incomingOps)
	{
		cA = incomingCA;
		cL = incomingCL;
		cR = incomingCR;
		a = incomingA;
		b = incomingB;
		c = incomingC;
		effects = incomingEffects;
		ops = incomingOps;
	}
	
	public Triangle(Color incomingCR, Color incomingCL, Color incomingCA, Point incomingA, Point incomingB,	Point incomingC, Effects incomingEffects)
	{
		this(incomingCR, incomingCL, incomingCA, incomingA, incomingB, incomingC, incomingEffects, new UtilImpl());
	}
	
	public double getSmallestT(Ray r)
	{
		Matrix3 A = new Matrix3(a.x - b.x, a.x - c.x, r.getD().x,
				a.y - b.y, a.y - c.y, r.getD().y,
				a.z - b.z, a.z - c.z, r.getD().z);
		double detA = A.det();
		Matrix3 gammaTop = new Matrix3(a.x - b.x, a.x - r.getEye().x, r.getD().x,
							   a.y - b.y, a.y - r.getEye().y, r.getD().y,
							   a.z - b.z, a.z - r.getEye().z, r.getD().z);
		double detGammaTop = gammaTop.det();
		double gamma = detGammaTop/detA;
		
		if(gamma < 0.0 || gamma > 1.0)
		{
			return Double.NaN;
		}
		Matrix3 betaTop = new Matrix3(a.x - r.getEye().x, a.x - c.x, r.getD().x,
		  a.y - r.getEye().y, a.y - c.y, r.getD().y,
		  a.z - r.getEye().z, a.z - c.z, r.getD().z);
		double detBetaTop = betaTop.det();
		
		double beta = detBetaTop/detA;
		if(beta < 0.0 || beta > 1.0 - gamma)
		{
			return Double.NaN;
		}
		Matrix3 tTop = new Matrix3(a.x - b.x, a.x - c.x, a.x - r.getEye().x,
						   a.y - b.y, a.y - c.y, a.y - r.getEye().y,
						   a.z - b.z, a.z - c.z, a.z - r.getEye().z);
		double detTTop = tTop.det();
		return detTTop/detA;
	}
	
	public Vector getNormal(Point p, Ray r)
	{
		Vector aB = a.minus(b);
		Vector aC = a.minus(c);
		Vector n = aB.cross(aC);
		n.normalize();
		//we need to make the normal point back to the eye.
		Vector tempD = new Vector(-r.getD().x, -r.getD().y,-r.getD().z);
		double tempDDotN = tempD.dot(n);
		if(tempDDotN < 0)
		{
			n.x -= 2*n.x;
			n.y -= 2*n.y;
			n.z -= 2*n.z;
		}
		
		return n;
	}
	
	public ArrayList<HitData> getHitData(Ray r)
	{
		double smallestT = getSmallestT(r);
		
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
