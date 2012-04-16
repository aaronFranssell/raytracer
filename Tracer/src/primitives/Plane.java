package primitives;

import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;
import math.Point;
import math.Vector;
import util.Library;


public class Plane extends Surface
{
	private Vector n;
	private Point pointOnPlane;
	private Color cR;
	private Color cL;
	private Color cA;
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
		
		if(hitT < 0.0003)
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
		Color white = new Color(0.9,0.9,0.9);
		Color black = new Color(0.1,0.1,0.1);
		if(Math.cos(p.z) > 0)
		{
			if(Math.sin(p.x) > 0)
			{
				returnColor = white;
			}//if
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
			}//if
			else
			{
				returnColor =  white;
			}
		}
		return Library.getColorLambertian(returnColor, cA, cL, n, light, p, inShadow);
	}
	
	@Override
	public int getType()
	{
		return Surface.PLANE;
	}

	public Color getCA() {
		return cA;
	}

	public void setCA(Color ca) {
		cA = ca;
	}

	public Color getCL() {
		return cL;
	}

	public void setCL(Color cl) {
		cL = cl;
	}

	public Color getCR() {
		return cR;
	}

	public void setCR(Color cr) {
		cR = cr;
	}

	public Vector getN() {
		return n;
	}

	public void setN(Vector n) {
		this.n = n;
	}

	public Point getPointOnPlane() {
		return pointOnPlane;
	}

	public void setPointOnPlane(Point pointOnPlane) {
		this.pointOnPlane = pointOnPlane;
	}
}
