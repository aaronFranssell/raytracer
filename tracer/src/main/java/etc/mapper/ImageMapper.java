package etc.mapper;

import etc.Color;
import java.io.IOException;
import math.Point;
import scene.render.ResultsConverter;
import util.Util;

public abstract class ImageMapper {
	protected int[][][] image;
	protected int imageHeight;
	protected int imageWidth;
	protected double textureScale;
	protected Util util;

	public abstract Color getColor(Point point);

	protected void readImage(String filePath) throws IOException {
		image = util.readImage(filePath);
		imageWidth = image.length;
		imageHeight = image[0].length;
	}

	protected Color convertToNativeColor(int u, int v) {
		Color returnValue = new Color(0.0, 0.0, 0.0);

		returnValue.red = image[u][v][0];
		returnValue.green = image[u][v][1];
		returnValue.blue = image[u][v][2];

		// convert to between 0-1
		returnValue.red = (returnValue.red / ResultsConverter.MAX_COLOR_INT_VAL) * textureScale;
		returnValue.green = (returnValue.green / ResultsConverter.MAX_COLOR_INT_VAL) * textureScale;
		returnValue.blue = (returnValue.blue / ResultsConverter.MAX_COLOR_INT_VAL) * textureScale;

		return returnValue;
	}
}
