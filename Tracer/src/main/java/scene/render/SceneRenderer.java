package scene.render;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import etc.RaytracerException;
import javax.imageio.ImageIO;

import math.Point;
import math.UVW;
import math.UVWFactory;
import math.UVWFactoryImpl;
import math.Vector;
import scene.Scene;
import scene.render.factory.RenderThreadFactory;
import scene.render.factory.RenderThreadFactoryImpl;
import scene.viewer.ViewingVolume;
import scene.viewer.factory.ViewingVolumeFactory;
import scene.viewer.factory.ViewingVolumeFactoryImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Constants;
import util.FileLocator;
import util.Library;
import util.stopwatch.StopWatch;
import util.stopwatch.StopWatchImpl;

public class SceneRenderer
{
	private Vector up;
	private Vector gaze;
	private Point eye;
	private int width;
	private int height;
	private int numThreads;
	private Scene scene;
	private String fileName;
	private UVW basis;
	private Point light;
	private StopWatch stopWatch;
	private RenderResult renderResult;
	private BufferedImage image;
	private RenderThreadFactory renderThreadFactory;
	private ViewingVolume viewVolume;
	
	private static final ExecutorService workers = Executors.newCachedThreadPool();
	
	public SceneRenderer(Vector incomingUp, Vector incomingGaze, Point incomingEye, int incomingLeft, int incomingRight, int incomingTop, int incomingBottom, int incomingWidth,
						 int incomingHeight, int incomingNumThreads, Scene incomingScene, String incomingFileName, Point incomingLight, UVWFactory factory,
						 StopWatch incomingStopWatch, RenderThreadFactory incomingRenderThreadFactory, ViewingVolumeFactory incomingViewingVolumeFactory) throws RaytracerException
	{
		if(incomingNumThreads <= 0)
		{
			throw new RaytracerException("Number of threads must be greater than zero!");
		}
		numThreads = incomingNumThreads;
		up = incomingUp;
		gaze = incomingGaze;
		eye = incomingEye;
		width = incomingWidth;
		height = incomingHeight;
		basis = factory.createUVW(up, gaze);
		fileName = incomingFileName;
		light = incomingLight;
		scene = incomingScene;
		stopWatch = incomingStopWatch;
		renderResult = new RenderResult();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		renderThreadFactory = incomingRenderThreadFactory;
		viewVolume = incomingViewingVolumeFactory.getVolume(incomingLeft, incomingRight, incomingBottom, incomingTop);
	}
	
	public SceneRenderer(Vector incomingUp, Vector incomingGaze, Point incomingEye, int incomingLeft, int incomingRight, int incomingTop, int incomingBottom, int incomingWidth,
			 int incomingHeight, int incomingNumThreads, Scene incomingScene, String incomingFileName, Point incomingLight) throws RaytracerException
	{
		this(incomingUp, incomingGaze, incomingEye, incomingLeft, incomingRight, incomingTop, incomingBottom, incomingWidth, incomingHeight,
			 incomingNumThreads, incomingScene, incomingFileName, incomingLight, new UVWFactoryImpl(), new StopWatchImpl(), new RenderThreadFactoryImpl(), new ViewingVolumeFactoryImpl());
	}
	
	public RenderResult render() throws IOException
	{
		stopWatch.start();
		double[][][] imageData = new double[width][height][3];
		
		Collection<Callable<double[][][]>> tasks = new ArrayList<Callable<double[][][]>>();
		
		int threadHeight = height / numThreads;
		
		int leftOverThreadHeight = height % numThreads;
		
		for(int i = 0; i < numThreads; i++)
		{
			int startHeight = i * threadHeight;
			RenderThread thread = renderThreadFactory.getRenderThread(eye, viewVolume, width, height, basis, light, scene, startHeight, threadHeight);
			tasks.add(thread);
		}
		List<Future<double[][][]>> results = workers.invokeAll(tasks);
		
		int index = 0;
		System.out.println("Merging...");
		int largestIntColorValue = 0;
		int totalSamples = 0;
		int totalRGBValues  = 0;
		int numberAbove255 = 0;
		for (Future<double[][][]> f : results)
		{
			double[][][] anImage = null;
			try
			{
				anImage = f.get();
				int finalHeight = threadHeight;
				if(index == 0)
				{
					finalHeight += leftOverThreadHeight;
				}
				int offsetHeight = index * threadHeight;
				
				index++;
				for(int w = 0; w < width; w++)
				{
					for(int h = 0; h < finalHeight; h++)
					{
						totalSamples += 3;
						imageData[w][h + offsetHeight][0] = anImage[w][h][0];
						totalRGBValues += anImage[w][h][0];
						//TODO: convert double 0.0-1.0 to 0-255 using SceneRenderer.convertToInt()
						/*imageData[w][h][0] = SceneRenderer.convertToInt(red);
						imageData[w][h][1] = SceneRenderer.convertToInt(green);
						imageData[w][h][2] = SceneRenderer.convertToInt(blue);*/
						if(anImage[w][h][0] > largestIntColorValue)
						{
							largestIntColorValue = anImage[w][h][0];
						}
						imageData[w][h + offsetHeight][1] = anImage[w][h][1];
						totalRGBValues += anImage[w][h][1];
						if(anImage[w][h][1] > largestIntColorValue)
						{
							largestIntColorValue = anImage[w][h][1];
						}
						imageData[w][h + offsetHeight][2] = anImage[w][h][2];
						totalRGBValues += anImage[w][h][2];
						if(anImage[w][h][2] > largestIntColorValue)
						{
							largestIntColorValue = anImage[w][h][2];
						}
						if(anImage[w][h][0] > 255)
						{
							numberAbove255++;
						}
						if(anImage[w][h][1] > 255)
						{
							numberAbove255++;
						}
						if(anImage[w][h][2] > 255)
						{
							numberAbove255++;
						}
					}
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
			catch (ExecutionException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		System.out.println("Writing to files...");
		Library.scale2(imageData, Constants.imagePctScale, totalRGBValues, totalSamples, largestIntColorValue, numberAbove255, width, height);

		WritableRaster raster = image.getRaster();
		for(int w = 0; w < width; w++)
		{
			for(int h = 0; h < height; h++)
			{
				raster.setPixel(w,((height-1)-h),imageData[w][h]);
			}
		}
		
		FileLocator loc = new FileLocator();
		String writeToFile = loc.getImageDirectory() + fileName;
		ImageIO.write(image,"PNG",new File(writeToFile + ".png"));
		renderResult.setImage(image);
		renderResult.setStopWatch(stopWatch);
		stopWatch.stop();
		return renderResult;
	}
		
	public static int convertToInt(double incoming)
	{
		return (int) ((int) (255 * incoming));
	}
}
