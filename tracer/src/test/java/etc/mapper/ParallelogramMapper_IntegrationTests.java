package etc.mapper;

import etc.Color;
import java.io.IOException;
import math.Point;
import math.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.Util;

public class ParallelogramMapper_IntegrationTests {
	@Test
	public void getColor_WithHitPointInMiddle_ExpectColor() throws Exception {

		String filePath = "aFilePath";
		int[][][] image = getImage();
		Util mockUtil = getMockUtil(filePath, image);

		Vector v1 = new Vector(3.0, 0.0, 0.0);
		Vector v2 = new Vector(0.0, 3.0, 0.0);
		Point point = new Point(0.0, 0.0, 0.0);
		Point hitPoint = new Point(1.0, 1.0, 0.0);

		ParallelogramMapper classUnderTest = new ParallelogramMapper(filePath, v1, v2, point, 0.5, mockUtil);

		Color actual = classUnderTest.getColor(hitPoint);

		Color expected = new Color(0.023529411764705882, 0.023529411764705882, 0.023529411764705882);
		assert expected.equals(actual);
	}

	@Test
	public void getColor_WithHitPointTopLeft_ExpectColor() throws Exception {

		String filePath = "aFilePath";
		int[][][] image = getImage();
		Util mockUtil = getMockUtil(filePath, image);

		Vector v1 = new Vector(3.0, 0.0, 0.0);
		Vector v2 = new Vector(0.0, 3.0, 0.0);
		Point point = new Point(0.0, 0.0, 0.0);
		Point hitPoint = new Point(2.0, 1.0, 0.0);

		ParallelogramMapper classUnderTest = new ParallelogramMapper(filePath, v1, v2, point, 1.0, mockUtil);

		Color actual = classUnderTest.getColor(hitPoint);

		Color expected = new Color(0.054901960784313725, 0.054901960784313725, 0.054901960784313725);
		assert expected.equals(actual);
	}

	@Test
	public void getColor_WithHitPointBottomRight_ExpectColor() throws Exception {

		String filePath = "aFilePath";
		int[][][] image = getImage();
		Util mockUtil = getMockUtil(filePath, image);

		Vector v1 = new Vector(3.0, 0.0, 0.0);
		Vector v2 = new Vector(0.0, 3.0, 0.0);
		Point point = new Point(0.0, 0.0, 0.0);
		Point hitPoint = new Point(1.0, 2.0, 0.0);

		ParallelogramMapper classUnderTest = new ParallelogramMapper(filePath, v1, v2, point, 1.0, mockUtil);

		Color actual = classUnderTest.getColor(hitPoint);

		Color expected = new Color(0.08627450980392157, 0.08627450980392157, 0.08627450980392157);
		assert expected.equals(actual);
	}

	private int[][][] getImage() {
		int[][][] fileBytes = new int[4][4][3];
		for (int i = 0; i < fileBytes.length; i++) {
			for (int m = 0; m < fileBytes[i].length; m++) {
				fileBytes[i][m][0] = 10 * i + (m * 2);
				fileBytes[i][m][1] = 10 * i + (m * 2);
				fileBytes[i][m][2] = 10 * i + (m * 2);
			}
		}

		return fileBytes;
	}

	private Util getMockUtil(String filePath, int[][][] image) throws IOException {
		Util mockUtil = Mockito.mock(Util.class);
		Mockito.when(mockUtil.readImage(filePath)).thenReturn(image);

		return mockUtil;
	}
}
