package etc.mapper;

import java.io.IOException;

import etc.Color;

import util.Util;
import math.Point;

public abstract class ImageMapper
{
	protected int[][][] image;
	protected int imageHeight;
	protected int imageWidth;
	protected double textureScale;
	protected Util util;
	
	public abstract Color getColor(Point point);
	
	protected void readImage(String filePath) throws IOException
	{
		image = util.readImage(filePath);
		imageWidth = image.length;
		imageHeight = image[0].length;
	}
}
