package scene.render;

public class ResultsScalerImpl implements ResultsScaler {
	private final double PCT_VALUE_START_SCALE = 0.75;

	@Override
	public int[][][] scale(int[][][] image) {
		int scaleBeginValue = (int) (PCT_VALUE_START_SCALE * ResultsConverter.MAX_COLOR_INT_VAL);
		int largestColorValue = getLargestColorValue(image);

		int scaleBasis = largestColorValue - scaleBeginValue;

		for (int w = 0; w < image.length; w++) {
			for (int h = 0; h < image[w].length; h++) {
				for (int c = 0; c < image[w][h].length; c++) {
					int value = image[w][h][c];
					if (value > scaleBeginValue) {
						image[w][h][c] = getScaledValue(value, largestColorValue, scaleBasis, scaleBeginValue);
					}
				}
			}
		}
		return image;
	}

	private int getLargestColorValue(int[][][] image) {
		int largest = 0;
		for (int i = 0; i < image.length; i++) {
			for (int m = 0; m < image[i].length; m++) {
				for (int u = 0; u < image[i][m].length; u++) {
					int value = image[i][m][u];
					if (value > largest) {
						largest = value;
					}
				}
			}
		}

		return largest;
	}

	private int getScaledValue(
			int currValue, int largestIntColorValue, int scaleBasis, int scaleBeginValue) {
		int offset = (int) (((double) currValue)
				/ largestIntColorValue
				* (ResultsConverter.MAX_COLOR_INT_VAL - scaleBeginValue));
		return scaleBeginValue + offset;
	}
}
