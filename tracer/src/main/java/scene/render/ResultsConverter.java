package scene.render;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ResultsConverter {
	public static final int MAX_COLOR_INT_VAL = 255;

	private Collection<Future<double[][][]>> results;
	private int width;
	private int height;
	private ResultsScaler resultsScaler;
	private BufferedImage image;

	public ResultsConverter(
			Collection<Future<double[][][]>> incomingResults, int incomingWidth, int incomingHeight) {
		this(
				incomingResults,
				incomingWidth,
				incomingHeight,
				new ResultsScalerImpl(),
				new BufferedImage(incomingWidth, incomingHeight, BufferedImage.TYPE_INT_RGB));
	}

	public ResultsConverter(
			Collection<Future<double[][][]>> incomingResults,
			int incomingWidth,
			int incomingHeight,
			ResultsScaler incomingResultsScaler,
			BufferedImage incomingImage) {
		resultsScaler = incomingResultsScaler;
		results = incomingResults;
		height = incomingHeight;
		image = incomingImage;
		width = incomingWidth;
	}

	public BufferedImage getImageFromResults() throws InterruptedException, ExecutionException {
		int offsetHeight = 0;
		int[][][] imageData = new int[width][height][3];

		for (Future<double[][][]> result : results) {
			double[][][] anImage = result.get();
			int threadHeight = anImage[width - 1].length;

			for (int w = 0; w < width; w++) {
				for (int h = 0; h < threadHeight; h++) {
					for (int c = 0; c < anImage[w][h].length; c++) {
						imageData[w][h + offsetHeight][c] = (int) (anImage[w][h][c] * MAX_COLOR_INT_VAL);
					}
				}
			}

			offsetHeight += threadHeight;
		}

		imageData = resultsScaler.scale(imageData);

		setBufferedImageFromData(imageData);

		return image;
	}

	private void setBufferedImageFromData(int[][][] imageData) {
		WritableRaster raster = image.getRaster();
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				raster.setPixel(w, ((height - 1) - h), imageData[w][h]);
			}
		}
	}
}
