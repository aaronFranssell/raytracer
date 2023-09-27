package etc.mapper;

import java.io.IOException;

import etc.Color;
import math.Point;
import util.Util;
import util.UtilImpl;

public class SphereMapper extends ImageMapper {
	private Point center;
	private double radius;

	public SphereMapper(
			String filePath, Point incomingCenter, double incomingRadius, double incomingTextureScale)
			throws Exception {
		this(filePath, incomingCenter, incomingRadius, incomingTextureScale, new UtilImpl());
	}

	public SphereMapper(
			String filePath,
			Point incomingCenter,
			double incomingRadius,
			double incomingTextureScale,
			Util incomingUtil) throws IOException {
		center = incomingCenter;
		radius = incomingRadius;
		util = incomingUtil;
		textureScale = incomingTextureScale;

		super.readImage(filePath);
	}

	@Override
	public Color getColor(Point point) {
		int[] UVArray = util.getCircleUVImageMapping(
				point, center, radius, image.length, image[image.length - 1].length);

		int uFinal = UVArray[0];
		int vFinal = UVArray[1];

		Color returnValue = convertToNativeColor(uFinal, vFinal);
		return returnValue;
	}
}
