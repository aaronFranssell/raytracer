package etc.mapper;

import etc.Color;
import etc.RaytracerException;
import java.io.IOException;
import math.Point;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.Util;

public class CircleMapper_UnitTests {

	@Test
	public void getColor_WithMocks_ExpectColor() throws IOException, RaytracerException {

		int[][][] fileBytes = new int[1][1][3];
		fileBytes[0][0][0] = 7;
		fileBytes[0][0][1] = 13;
		fileBytes[0][0][2] = 17;

		Point center = new Point(1.0, 1.0, 1.0);
		double radius = 3.0;
		double textureScale = 1.0;
		Point hitPoint = new Point(-2.0, -2.0, -2.0);
		int[] uvArray = new int[]{0, 0};
		Color expected = new Color(7.0 / 255.0, 13.0 / 255.0, 17.0 / 255.0);
		String filePath = "path to image";
		Util mockUtil = Mockito.mock(Util.class);
		Mockito.doReturn(fileBytes).when(mockUtil).readImage(filePath);
		Mockito.doReturn(uvArray)
				.when(mockUtil)
				.getCircleUVImageMapping(
						hitPoint, center, radius, fileBytes.length, fileBytes[fileBytes.length - 1].length);

		SphereMapper classUnderTest = new SphereMapper(filePath, center, radius, textureScale, mockUtil);

		Color actual = classUnderTest.getColor(hitPoint);

		assert expected.equals(actual);
	}
}
