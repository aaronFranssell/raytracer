package util;

import org.apache.commons.math3.util.FastMath;

import etc.Color;
import etc.HitData;
import etc.RaytracerException;
import scene.pixel.ScenePixel;
import scene.ray.Ray;
import surface.Surface;
import math.Point;
import math.Vector;
public class UtilImpl implements Util
{
	@Override
	public double[] sort(double[] incomingTArray)
	{
		double[] temp = new double[incomingTArray.length];
		for(int i = 0; i < incomingTArray.length; i++)
		{
			temp[i] = incomingTArray[i];
		}
		if(temp.length == 1)
		{
			return temp;
		}
		boolean switchMade;
		do
		{
			switchMade = false;
			for(int i = 0; i < temp.length-1; i++)
			{
				if(temp[i] > temp[i + 1])
				{
					switchMade = true;
					double tempVal = temp[i + 1];
					temp[i + 1] = temp[i];
					temp[i] = tempVal;
				}
			}
		}
		while(switchMade);
		return temp;
	}

	@Override
	public Point getP(double smallestT, Ray r)
	{
		return new Point(r.getEye().x + smallestT * r.getD().x,
				r.getEye().y + smallestT * r.getD().y,
				r.getEye().z + smallestT * r.getD().z);
	}

	@Override
	public Ray getRefractedRay(Vector v, double originalN, double newN,	HitData hitData)
	{
		Vector normal = hitData.getNormal();
		//may need to reverse normal. Normally it points outwards, but if there is a refracted ray 
		//shooting through the sphere the normal should point inwards. We can test this by seeing 
		//if ((reversed d) * n) is greater than 0. If the cos is < 0, we need to reverse the normal.
		if(normal.dot(v) > 0)
		{
			normal = normal.scaleReturn(-1.0);
		}
		double c1 = normal.dot(v) * -1;
		double n = originalN/newN;
		double val = 1 - (n*n) * (1 - c1*c1);
		if(val < 0.0)
		{
			return null;
		}
		double c2 = FastMath.sqrt(val);

		Vector refracted = v.scaleReturn(n).add(normal.scaleReturn(n * c1 - c2));
		refracted = refracted.normalizeReturn();
		return new Ray(refracted, hitData.getP());
	}

	@Override
	public Ray getReflectedRay(Ray r, Point p, Vector n)
	{
		/*V is the ray direction, N is the surface normal
		c1 = -dot_product( N, V )
		Rl = V + (2 * N * c1 )
		 */
		double c1 = - n.dot(r.getD());
		Vector reflectedRay = r.getD().add(n.scaleReturn(2*c1));
		reflectedRay = reflectedRay.normalizeReturn();
		return new Ray(reflectedRay, p);
	}

	@Override
	public Color clamp(Color c)
	{
		Color ret = new Color(c.red, c.green, c.blue);
		if(ret.red > 1.0)
		{
			ret.red = 1.0;
		}
		if(ret.green > 1.0)
		{
			ret.green = 1.0;
		}
		if(ret.blue > 1.0)
		{
			ret.blue = 1.0;
		}
		return ret;
	}

	@Override
	public Color getReflectedColor(Ray r, int currentDepth, HitData hit, Surface currSurface, ScenePixel pixel) throws RaytracerException
	{
		//r.getD().dot... code is for when the ray might be refracted inside of a surface.
		//if the ray's dot product is > 0 then the ray is inside of a surface and does not need to
		//be reflected
		if(!currSurface.getEffects().isReflective() || r.getD().dot(hit.getNormal()) >= 0)
		{
			return new Color(0.0,0.0,0.0);
		}
		Ray newRay = getReflectedRay(r, hit.getP(), hit.getNormal());
		//total internal reflection
		if(newRay == null)
		{
			return new Color(0.0,0.0,0.0);
		}
		Color reflectReturnColor = pixel.recurse(newRay, currentDepth + 1);
		reflectReturnColor = clamp(reflectReturnColor);
		return reflectReturnColor;
	}
}
