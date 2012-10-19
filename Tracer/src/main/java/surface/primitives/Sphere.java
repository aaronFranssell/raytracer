package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Library;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;

public class Sphere extends Surface
{
	protected Point center;
	protected double radius;
	protected int[][][] image;
	protected double textureScale;
	protected int w;
	protected int h;

	public Sphere(Point incomingCenter, double incomingRadius, Color incomingCR, Color incomingCA, Color incomingCL, Effects incomingEffects, String filePath,
				  double incomingTextureScale, Util incomingOps)
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
		}
		ops = incomingOps;
	}
	
	public Sphere(Point incomingCenter, double incomingRadius, Color incomingCR, Color incomingCA, Color incomingCL, Effects incomingEffects, String filePath,
			  double incomingTextureScale)
	{
		this(incomingCenter, incomingRadius, incomingCR, incomingCA, incomingCL, incomingEffects, filePath, incomingTextureScale, new UtilImpl());
	}

	@Override
	public Color getColor(Point light, Point eye, int phongExponent, boolean inShadow, Vector n, Point p)
	{
		if (image == null)
		{
			return super.getColor(light, eye, phongExponent, inShadow, n, p);
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

	public Vector getNormal(Point p, Ray r)
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
