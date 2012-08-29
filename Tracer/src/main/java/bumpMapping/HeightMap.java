package bumpMapping;

import util.Library;
import util.LogToFile;
import math.Point;
import math.Vector;

/*
 * I think this algorithm needs work... I don't think its right. It offsets the normals in addition to perturbing them.
 * To be honest just using Perlin noise to perturb the normals has a better result.
 */
public class HeightMap implements BumpMap
{
	public static final double MAX = 255.0;
	private int[][][] image;
	private Point center;
	private double radius;
	private int w;
	private int h;
	
	private static final int OFFSET = 125;
	private static final int MAX_VAL = 254;
	
	public HeightMap(String filePath, Point incomingCenter, double incomingRadius)
	{
		center = incomingCenter;
		radius = incomingRadius;
		image = Library.readImage(filePath);
		w = image.length;
		h = image[0].length;
	}
	
	@Override
	public Vector getBump(Point p, Vector normal)
	{
		Vector b4 = normal.copy();
		
		int[] UVArray = Library.getCircleUVImageMapping(p, center, radius, w, h);
		int u = UVArray[0];
		int v = UVArray[1];
		
		double displacement = convert(image[u][v][0]);
		
		normal.x = normal.x + displacement;
		normal.y = normal.y + displacement;
		normal.z = normal.z + displacement;
		
		normal.normalize();
		if(normal.dot(b4) < 0)
		{
			LogToFile.logln("behind");
		}

		return normal;
	}
	
	private static double convert(double val)
	{
		return (val - OFFSET)/(MAX_VAL - OFFSET);
	}
}
