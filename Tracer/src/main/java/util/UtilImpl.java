package util;

import scene.ray.Ray;
import math.Point;

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
}
