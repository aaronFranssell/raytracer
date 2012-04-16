package primitives;

import math.Point;
import math.Vector;
import util.Library;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;

public class Sphere extends Surface
{
	private Point center;

	private double radius;

	private Color cR;

	private Color cA;

	private Color cL;

	private int[][][] image;

	double textureScale;

	int w;

	int h;

	public Sphere(Point incomingCenter, double incomingRadius,
			Color incomingCR, Color incomingCA, Color incomingCL,
			Effects incomingEffects, String filePath,
			double incomingTextureScale)
	{
		center = incomingCenter;
		radius = incomingRadius;
		cR = incomingCR;
		cA = incomingCA;
		cL = incomingCL;
		effects = incomingEffects;
		textureScale = incomingTextureScale;
		if (filePath != null)
		{
			image = Library.readImage(filePath);
			w = image.length;
			h = image[0].length;
		}// if
	}

	@Override
	public Color getColor(Point light, Point eye, int phongExponent,
			boolean inShadow, Color cR, Point p, Color cA, Color cL, Vector n)
	{
		if (image == null)
		{
			return super.getColor(light, eye, phongExponent, inShadow, cR, p,
					cA, cL, n);
		}
		Color returnValue = new Color(0.0, 0.0, 0.0);

		int[] UVArray = Library.getCircleUVImageMapping(p, center, radius, w, h);

		int uFinal = UVArray[0];
		int vFinal = UVArray[1];
		
		returnValue.red = image[uFinal][vFinal][0];
		returnValue.green = image[uFinal][vFinal][1];
		returnValue.blue = image[uFinal][vFinal][2];

		// convert to between 0-1
		returnValue.red = returnValue.red / 255.0 * textureScale;
		returnValue.green = returnValue.green / 255.0 * textureScale;
		returnValue.blue = returnValue.blue / 255.0 * textureScale;

		return returnValue;
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

	public HitData getHitData(Ray r)
	{
		Vector eMinusC = r.getEye().minus(center);
		double discriminant = getDiscriminant(r);
		if (discriminant <= 0)
		{
			return new HitData();
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
		double smallestT = Library.getSmallestT(retTArray);
		Point p = Library.getP(smallestT, r);
		Vector normal = getNormal(p, r);
		return new HitData(smallestT, this, normal, p, retTArray);
	}

	public Vector getNormal(Point p, Ray r)
	{
		Vector n = p.minus(center);
		n.normalize();
		return n;
	}

	@Override
	public int getType()
	{
		return Surface.SPHERE;
	}

	public Color getCA()
	{
		return cA;
	}

	public void setCA(Color ca)
	{
		cA = ca;
	}

	public Point getCenter()
	{
		return center;
	}

	public void setCenter(Point center)
	{
		this.center = center;
	}

	public Color getCL()
	{
		return cL;
	}

	public void setCL(Color cl)
	{
		cL = cl;
	}

	public Color getCR()
	{
		return cR;
	}

	public void setCR(Color cr)
	{
		cR = cr;
	}
}
