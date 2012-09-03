package scene.render;

import java.util.concurrent.Callable;

import math.Point;
import math.UVW;
import scene.Scene;
import scene.ray.Ray;
import scene.ray.RayFactory;
import scene.viewer.ViewingVolume;
import etc.Color;
import etc.ScenePixel;

public class RenderThread implements Callable<int[][][]>
{
	private Point eye;
	private ViewingVolume volume;
	private int pictureWidth;
	private int pictureHeight;
	private int[][][] imageData;
	private UVW basis;
	private Scene scene;
	private Point light;
	private int startHeight;
	private int threadHeight;
	private RayFactory factory;
	
	public RenderThread(Point incomingEye, ViewingVolume incomingVolume, int incomingPictureWidth, int incomingPictureHeight, UVW incomingBasis,
						Point incomingLight, Scene incomingScene, int incomingStartHeight, int incomingThreadHeight)
	{
		volume = incomingVolume;
		eye = incomingEye;
		pictureWidth = incomingPictureWidth;
		pictureHeight = incomingPictureHeight;
		basis = incomingBasis;
		light = incomingLight;
		startHeight = incomingStartHeight;
		threadHeight = incomingThreadHeight;
		scene = incomingScene;
		imageData = new int[pictureWidth][threadHeight][3];
	}
	
	public int[][][] call() throws Exception
	{
		Color returnColor = null;
		for(int w = 0; w < pictureWidth; w++)
		{
			for(int h = 0; h < threadHeight; h++)
			{
				Ray r = factory.createRay(volume, eye, basis, pictureWidth, pictureHeight, w, h + startHeight);
				ScenePixel pixel = new ScenePixel(r, scene, eye, light);
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
