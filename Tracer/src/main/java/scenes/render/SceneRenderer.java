package scenes.render;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import math.Point;
import math.UVW;
import math.Vector;
import primitives.Surface;
import util.Constants;
import util.FileLocator;
import util.Library;

public class SceneRenderer
{
	private Vector up;
	private Vector gaze;
	private Point eye;
	private int left;
	private int right;
	private int top;
	private int bottom;
	private int width;
	private int height;
	private int numThreads;
	private LinkedList<Surface> surfaceList;
	private String fileName;
	private UVW basis;
	private Point light;
	
	private static final ExecutorService workers = Executors.newCachedThreadPool();
	
	public SceneRenderer(Vector incomingUp, Vector incomingGaze, Point incomingEye, int incomingLeft, int incomingRight, int incomingTop, int incomingBottom, int incomingWidth,
						 int incomingHeight, int incomingNumThreads, LinkedList<Surface> incomingSurfaceList, String incomingFileName, Point incomingLight)
	{
		if(incomingNumThreads <= 0)
		{
			try
			{
				throw new Exception("Number of threads must be great than zero!");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		numThreads = incomingNumThreads;
		up = incomingUp;
		gaze = incomingGaze;
		eye = incomingEye;
		left = incomingLeft;
		right = incomingRight;
		top = incomingTop;
		bottom = incomingBottom;
		width = incomingWidth;
		height = incomingHeight;
		basis = new UVW(up, gaze);
		fileName = incomingFileName;
		light = incomingLight;
		surfaceList = incomingSurfaceList;
	}
	
	public void render() throws IOException
	{
		GregorianCalendar start = new GregorianCalendar();
		int[][][] imageData = new int[width][height][3];
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		
		int largestIntColorValue = 0;
		int totalSamples = 0;
		int totalRGBValues  = 0;
		int numberAbove255 = 0;
		
		Collection<Callable<int[][][]>> tasks = new ArrayList<Callable<int[][][]>>();
		
		int threadHeight = height / numThreads;
		
		int leftOverThreadHeight = height % numThreads;
		
		for(int i = 0; i < numThreads; i++)
		{
			int theHeight = threadHeight;
			int startHeight = i * threadHeight;
			if(i == 0)
			{
				theHeight += leftOverThreadHeight;
				System.out.println("theHeight: " + theHeight);
			}
			tasks.add(new RenderThread(eye, left, right, bottom, top, width, height, basis, light, surfaceList, startHeight, threadHeight));
		}
		List<Future<int[][][]>> results = null;
		try
		{
			results = workers.invokeAll(tasks);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		int index = 0;
		System.out.println("Merging...");
		for (Future<int[][][]> f : results)
		{
			int[][][] anImage = null;
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
		GregorianCalendar end = new GregorianCalendar();
		displayDifference(start, end);
	}
	
	private void displayDifference(GregorianCalendar start, GregorianCalendar end)
	{
		long span = end.getTimeInMillis() - start.getTimeInMillis();
	    long mins = 1000*60;
	    long numMins = span/mins;
	    span -= mins * numMins;
	    
	    long secs = 1000;
	    long numSecs = span/secs; 
	    
	    System.out.println("Executed in " + numMins + " minute(s) and " + numSecs + " seconds.");
	}
	
	public static int convertToInt(double incoming)
	{
		return (int) ((int) (255 * incoming));
	}
}