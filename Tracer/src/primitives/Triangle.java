package primitives;

import util.Library;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;
import math.Matrix3;
import math.Point;
import math.Vector;

public class Triangle extends Surface
{
	private Color cA;
	private Color cL;
	private Color cR;
	private Point a;
	private Point b;
	private Point c;
	
	public Triangle(Color incomingCR, Color incomingCL, Color incomingCA, Point incomingA, Point incomingB, 
					Point incomingC, Effects incomingEffects)
	{
		cA = incomingCA;
		cL = incomingCL;
		cR = incomingCR;
		a = incomingA;
		b = incomingB;
		c = incomingC;
		effects = incomingEffects;
	}//triangle
	
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
		}//if
		Matrix3 betaTop = new Matrix3(a.x - r.getEye().x, a.x - c.x, r.getD().x,
		  a.y - r.getEye().y, a.y - c.y, r.getD().y,
		  a.z - r.getEye().z, a.z - c.z, r.getD().z);
		double detBetaTop = betaTop.det();
		
		double beta = detBetaTop/detA;
		if(beta < 0.0 || beta > 1.0 - gamma)
		{
			return Double.NaN;
		}//if
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
		}//if
		
		return n;
	}
	
	public HitData getHitData(Ray r)
	{
		double smallestT = getSmallestT(r);
		double[] retTArray = new double[1];
		retTArray[0] = smallestT;
		smallestT = Library.getSmallestT(retTArray);
		Point p = Library.getP(smallestT,r);
		Vector normal = getNormal(p, r);
		HitData hit = new HitData(smallestT, this, normal, p, retTArray);
		return hit;
	}//isHit
	
	@Override
	public int getType()
	{
		return Surface.TRIANGLE;
	}

	public Point getA() {
		return a;
	}

	public void setA(Point a) {
		this.a = a;
	}

	public Point getB() {
		return b;
	}

	public void setB(Point b) {
		this.b = b;
	}

	public Point getC() {
		return c;
	}

	public void setC(Point c) {
		this.c = c;
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
}
