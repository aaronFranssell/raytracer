package scenes.render;

import java.util.concurrent.Callable;
import math.Point;
import math.UVW;
import etc.Color;
import etc.Ray;
import etc.ScenePixel;
import java.util.LinkedList;

import primitives.Surface;

public class RenderThread implements Callable<int[][][]>
{
	private Point eye;
	private int left;
	private int right;
	private int bottom;
	private int top;
	private int width;
	private int height;
	private int[][][] imageData;
	private UVW basis;
	private LinkedList<Surface> surfaceList;
	private Point light;
	private int startHeight;
	private int threadHeight;
	
	public RenderThread(Point incomingEye, int incomingLeft, int incomingRight, int incomingBottom, int incomingTop, int incomingWidth, int incomingHeight, UVW incomingBasis,
						Point incomingLight, LinkedList<Surface> incomingSurfaceList, int incomingStartHeight, int incomingThreadHeight)
	{
		eye = incomingEye;
		left = incomingLeft;
		right = incomingRight;
		bottom = incomingBottom;
		top = incomingTop;
		width = incomingWidth;
		height = incomingHeight;
		basis = incomingBasis;
		light = incomingLight;
		imageData = new int[width][height][3];
		surfaceList = incomingSurfaceList;
		startHeight = incomingStartHeight;
		threadHeight = incomingThreadHeight;
	}
	
	public int[][][] call()
	{
		Color returnColor = null;
		for(int w = 0; w < width; w++)
		{
			for(int h = 0; h < threadHeight; h++)
			{
				Ray r = new Ray(eye, left, right, bottom, top, width, height, w, h + startHeight, basis.getU(), basis.getV(), basis.getW());
				try
				{
					ScenePixel pixel = new ScenePixel(r,surfaceList,0, eye, light);
					returnColor = pixel.getPixelColor();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(0);
				}
				double red = (float) returnColor.red;
				double green = (float) returnColor.green;
				double blue = (float) returnColor.blue;
				imageData[w][h][0] = SceneRenderer.convertToInt(red);
				imageData[w][h][1] = SceneRenderer.convertToInt(green);
				imageData[w][h][2] = SceneRenderer.convertToInt(blue);
			}
		}
		return imageData;
	}
}
