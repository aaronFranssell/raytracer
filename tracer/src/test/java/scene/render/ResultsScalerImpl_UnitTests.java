package scene.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResultsScalerImpl_UnitTests {
	@Test
	public void scale_WithValidInput_ExpectScaledArray() {

		int[][][] expected = new int[10][1][1];
		expected[0][0][0] = 150;
		expected[1][0][0] = 204;
		expected[2][0][0] = 75;
		expected[3][0][0] = 150;
		expected[4][0][0] = 255;
		expected[5][0][0] = 30;
		expected[6][0][0] = 202;
		expected[7][0][0] = 199;
		expected[8][0][0] = 175;
		expected[9][0][0] = 39;

		int[][][] image = new int[10][1][1];
		image[0][0][0] = 150;
		image[1][0][0] = 300;
		image[2][0][0] = 75;
		image[3][0][0] = 150;
		image[4][0][0] = 1450;
		image[5][0][0] = 30;
		image[6][0][0] = 255;
		image[7][0][0] = 200;
		image[8][0][0] = 175;
		image[9][0][0] = 39;

		ResultsScalerImpl classUnderTest = new ResultsScalerImpl();

		int[][][] retImage = classUnderTest.scale(image);

		for (int i = 0; i < retImage.length; i++) {
			for (int m = 0; m < retImage[i].length; m++) {
				for (int u = 0; u < retImage[i][m].length; u++) {
					assertEquals(expected[i][m][u], retImage[i][m][u]);
				}
			}
		}
	}
}
