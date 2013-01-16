package etc.mapper;

import java.io.IOException;

import org.apache.commons.math3.util.FastMath;

import etc.Color;
import etc.RaytracerException;

import util.Util;
import util.UtilImpl;
import math.Point;

public class SphereMapper extends ImageMapper
{
	private Point center;
	private double radius;
	
	public SphereMapper(String filePath, Point incomingCenter, double incomingRadius, double incomingTextureScale) throws RaytracerException
	{
		this(filePath, incomingCenter, incomingRadius, incomingTextureScale, new UtilImpl());
	}
	
	public SphereMapper(String filePath, Point incomingCenter, double incomingRadius, double incomingTextureScale, Util incomingUtil) throws RaytracerException
	{
		center = incomingCenter;
		radius = incomingRadius;
		util = incomingUtil;
		textureScale = incomingTextureScale;
		try
		{
			super.readImage(filePath);
		}
		catch(IOException e)
		{
			throw new RaytracerException(e);
		}
		
	}
	
	@Override
	public Color getColor(Point point)
	{
		int[] UVArray = getCircleUVImageMapping(point, center, radius, image.length, image[image.length - 1].length);

		int uFinal = UVArray[0];
		int vFinal = UVArray[1];
		
		Color returnValue = convertToNativeColor(uFinal, vFinal);
		return returnValue;
	}
	
	protected int[] getCircleUVImageMapping(Point p, Point center, double radius, int w, int h)
	{
		double theta = FastMath.acos((p.y - center.y) / radius);
		double phi = FastMath.atan2(p.z - center.z, p.x - center.x);
		if (phi < 0.0)
		{
			phi += 2 * FastMath.PI;
		}
	
		double u = phi / (2 * FastMath.PI);
		double v = (FastMath.PI - theta) / FastMath.PI;
	
		// u,v is in the unit square, so we need to convert them to the image
		// coordinates
		int uFinal = (int) ((int) w * u);
		int vFinal = (int) ((int) h * v);
		
		int[] retArray = new int[2];
		retArray[0] = uFinal;
		retArray[1] = vFinal;
		
		return retArray;
	}

}
