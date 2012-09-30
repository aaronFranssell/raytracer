package scene.render;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ResultsConverterImpl implements ResultsConverter
{
	private Collection<Future<double[][][]>> results;
	private int width;
	private int height;
	
	public ResultsConverterImpl(Collection<Future<double[][][]>> incomingResults, int incomingWidth, int incomingHeight)
	{
		results = incomingResults;
		height = incomingHeight;
	}
	
	@Override
	public BufferedImage getImageFromResults() throws InterruptedException, ExecutionException
	{
		int offsetHeight = 0;
		int[][][] imageData = new int[width][height][3];
		
		for(Future<double[][][]> result : results)
		{
			double[][][] anImage = result.get();
			int threadHeight = anImage[width - 1].length;
			
			for(int w = 0; w < width; w++)
			{
				for(int h = 0; h < threadHeight; h++)
				{
					imageData[w][h + offsetHeight][RenderThread.RED_INDEX] = (int) anImage[w][h][RenderThread.RED_INDEX] * MAX_COLOR_INT_VAL;
					imageData[w][h + offsetHeight][RenderThread.GREEN_INDEX] = (int) anImage[w][h][RenderThread.GREEN_INDEX] * MAX_COLOR_INT_VAL;
					imageData[w][h + offsetHeight][RenderThread.BLUE_INDEX] = (int) anImage[w][h][RenderThread.BLUE_INDEX] * MAX_COLOR_INT_VAL;
				}
			}
			
			offsetHeight += threadHeight;
		}
		return null;
	}
}
