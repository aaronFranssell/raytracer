package math.simplex;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import util.Constants;
import util.UtilImpl;

public class ParametricSimplexImpl extends Simplex {
	private static final double S_AND_T_LIMIT = 0.0 - Constants.POSITIVE_ZERO;
	private static final double S_PLUS_T_LIMIT = 1.0 + Constants.POSITIVE_ZERO;

	private Vector u;
	private Vector v;
	private double precalculatedDenominator;
	private double uDotV;
	private double uDotU;
	private double vDotV;

	public ParametricSimplexImpl(Point incomingA, Point incomingB, Point incomingC) {
		a = incomingA;
		b = incomingB;
		c = incomingC;
		calcNormal();
		ops = new UtilImpl();

		// precompute values, I would rather store these in memory than take the performance hit
		u = b.minus(a);
		v = c.minus(a);
		uDotV = u.dot(v);
		uDotU = u.dot(u);
		vDotV = v.dot(v);
		precalculatedDenominator = uDotV * uDotV - uDotU * vDotV;
	}

	@Override
	public double getT(Ray r) {
		// any of the 3 points will do as the middle argument
		double t = ops.getTOnPlane(r, a, normal);
		Point p = ops.getP(t, r);

		// Algorithm I'm using: http://geomalgorithms.com/a06-_intersect-2.html
		Vector w = p.minus(a);
		double wDotU = w.dot(u);
		double wDotV = w.dot(v);
		double s1 = (uDotV * wDotV - vDotV * wDotU) / precalculatedDenominator;
		double t1 = (uDotV * wDotU - uDotU * wDotV) / precalculatedDenominator;
		// offset this just a little bit so floating point ops don't screw me over
		if (s1 >= S_AND_T_LIMIT && t1 >= S_AND_T_LIMIT && s1 + t1 <= S_PLUS_T_LIMIT) {
			return t;
		} else {
			return Double.NaN;
		}
	}
}
