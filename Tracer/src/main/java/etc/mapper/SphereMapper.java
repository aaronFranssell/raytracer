package etc.mapper;

import java.io.IOException;

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
		int[] UVArray = util.getCircleUVImageMapping(point, center, radius, image.length, image[image.length - 1].length);

		int uFinal = UVArray[0];
		int vFinal = UVArray[1];
		
		Color returnValue = convertToNativeColor(uFinal, vFinal);
		return returnValue;
	}

}
