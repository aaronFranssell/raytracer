package scene.render;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;

public interface ResultsConverter
{
	public static final int MAX_COLOR_INT_VAL = 255;
	
	public BufferedImage getImageFromResults() throws InterruptedException, ExecutionException;
}
