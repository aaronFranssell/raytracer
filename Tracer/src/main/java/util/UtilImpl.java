package util;

import org.apache.commons.math3.util.FastMath;

import etc.HitData;
import scene.ray.Ray;
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
}
