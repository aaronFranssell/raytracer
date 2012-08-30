package scene.render;

import java.util.concurrent.Callable;

import math.Point;
import math.UVW;
import scene.Scene;
import scene.ray.Ray;
import etc.Color;
import etc.ScenePixel;

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
	private Scene scene;
	private Point light;
	private int startHeight;
	private int threadHeight;
	
	public RenderThread(Point incomingEye, int incomingLeft, int incomingRight, int incomingBottom, int incomingTop, int incomingWidth, int incomingHeight, UVW incomingBasis,
						Point incomingLight, Scene incomingScene, int incomingStartHeight, int incomingThreadHeight)
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
		scene = incomingScene;
		startHeight = incomingStartHeight;
		threadHeight = incomingThreadHeight;
	}
	
	public int[][][] call() throws Exception
	{
		Color returnColor = null;
		for(int w = 0; w < width; w++)
		{
			for(int h = 0; h < threadHeight; h++)
			{
				Ray r = new Ray(eye, left, right, bottom, top, width, height, w, h + startHeight, basis.getU(), basis.getV(), basis.getW());
				ScenePixel pixel = new ScenePixel(r,scene, eye, light);
				returnColor = pixel.getPixelColor();
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
