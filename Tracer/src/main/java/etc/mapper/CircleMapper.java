package etc.mapper;

import java.io.IOException;

import org.apache.commons.math3.util.FastMath;

import etc.Color;
import etc.RaytracerException;

import scene.render.ResultsConverter;
import util.Util;
import util.UtilImpl;
import math.Point;

public class CircleMapper extends ImageMapper
{
	private Point center;
	private double radius;
	
	public CircleMapper(String filePath, Point incomingCenter, double incomingRadius, double incomingTextureScale) throws RaytracerException
	{
		this(filePath, incomingCenter, incomingRadius, incomingTextureScale, new UtilImpl());
	}
	
	public CircleMapper(String filePath, Point incomingCenter, double incomingRadius, double incomingTextureScale, Util incomingUtil) throws RaytracerException
	{
		center = incomingCenter;
		radius = incomingRadius;
		util = incomingUtil;
		try
		{
			super.readImage(filePath);
		}
		catch(IOException e)
		{
			throw new RaytracerException(e);
		}
		textureScale = incomingTextureScale;
	}
	
	@Override
	public Color getColor(Point point)
	{
		Color returnValue = new Color(0.0, 0.0, 0.0);

		int[] UVArray = getCircleUVImageMapping(point, center, radius, image.length, image[image.length - 1].length);

		int uFinal = UVArray[0];
		int vFinal = UVArray[1];
		
		returnValue.red = image[uFinal][vFinal][0];
		returnValue.green = image[uFinal][vFinal][1];
		returnValue.blue = image[uFinal][vFinal][2];

		// convert to between 0-1
		returnValue.red = (returnValue.red / ResultsConverter.MAX_COLOR_INT_VAL) * textureScale;
		returnValue.green = (returnValue.green / ResultsConverter.MAX_COLOR_INT_VAL) * textureScale;
		returnValue.blue = (returnValue.blue / ResultsConverter.MAX_COLOR_INT_VAL) * textureScale;

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
