package etc.mapper;

import java.io.IOException;

import util.Util;
import util.UtilImpl;

import math.Point;
import math.Vector;
import etc.Color;
import etc.RaytracerException;

public class ParallelogramMapper extends ImageMapper
{
	private Vector v1Unit;
	private Vector v2Unit;
	private double v1Mag;
	private double v2Mag;
	private Point origin;
	
	/**
	 * Note: The first vector (v1) maps to the height of the image, the second vector (v2) maps to the width of the image.
	 */
	public ParallelogramMapper(String incomingFilePath, Vector incomingV1, Vector incomingV2, Point incomingOrigin, double incomingTextureScale) throws RaytracerException
	{
		this(incomingFilePath, incomingV1, incomingV2, incomingOrigin, incomingTextureScale, new UtilImpl());
	}
	
	public ParallelogramMapper(String incomingFilePath, Vector incomingV1, Vector incomingV2, Point incomingOrigin, double incomingTextureScale,
							   Util incomingUtil) throws RaytracerException
	{
		v1Mag = incomingV1.magnitude();
		v2Mag = incomingV2.magnitude();
		v1Unit = incomingV1.normalizeReturn();
		v2Unit = incomingV2.normalizeReturn();
		origin = incomingOrigin;
		util = incomingUtil;
		textureScale = incomingTextureScale;
		try
		{
			super.readImage(incomingFilePath);
		}
		catch(IOException e)
		{
			throw new RaytracerException(e);
		}
	}

	@Override
	public Color getColor(Point point)
	{
		Vector vectorToHit = point.minus(origin);
		double vectorToHitMag = vectorToHit.magnitude();
		Vector unitVectorToHit = vectorToHit.normalizeReturn();
		
		double componentV1 = getComponentInDirection(unitVectorToHit, vectorToHitMag, v1Unit);
		double componentV2 = getComponentInDirection(unitVectorToHit, vectorToHitMag, v2Unit);
		
		int u = (int) ((componentV2/v2Mag) * (imageWidth - 1));
		int v = (int) ((componentV1/v1Mag) * (imageHeight - 1));
		
		Color returnColor = convertToNativeColor(u,v);
		return returnColor;
	}

	private double getComponentInDirection(Vector unitVectorToHit, double vectorToHitMag, Vector unitDirection)
	{
		double cos = unitVectorToHit.dot(unitDirection);
		double component = vectorToHitMag * cos;
		return component;
	}
}
