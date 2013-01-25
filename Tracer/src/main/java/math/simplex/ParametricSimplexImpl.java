package math.simplex;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import util.Util;
import util.UtilImpl;

public class ParametricSimplexImpl extends Simplex
{
	private Util ops;
	private Vector u;
	private Vector v;
	private double precalculatedDenominator;
	private double uDotV;
	private double uDotU;
	private double vDotV;
	
	public ParametricSimplexImpl(Point incomingA, Point incomingB, Point incomingC)
	{
		a = incomingA;
		b = incomingB;
		c = incomingC;
		calcNormal();
		ops = new UtilImpl();
		
		//precompute values, I would rather store these in memory than take the performance hit
		u = b.minus(a);
		v = c.minus(a);
		uDotV = u.dot(v);
		uDotU = u.dot(u);
		vDotV = v.dot(v);
		precalculatedDenominator = uDotV*uDotV - u.dot(u)*v.dot(v);
	}
	
	@Override
	public double getT(Ray r)
	{
		//any of the 3 points will do as the middle argument
		double t = ops.getTOnPlane(r, a, normal);
		Point p = ops.getP(t, r);
		
		//Algorithm I'm using: http://geomalgorithms.com/a06-_intersect-2.html
		Vector w = p.minus(a);
		double wDotU = w.dot(u);
		double wDotV = w.dot(v);
		double s1 = (uDotV*wDotV - vDotV*wDotU)/precalculatedDenominator;
		double t1 = (uDotV*wDotU - uDotU*wDotV)/precalculatedDenominator;
		if(s1 >= 0.0 && t1 >= 0.0 && s1 + t1 <= 1.0)
		{
			return t;
		}
		else
		{
			return Double.NaN;
		}
	}
}
