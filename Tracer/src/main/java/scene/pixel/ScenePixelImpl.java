package scene.pixel;

import math.Point;
import scene.Scene;
import scene.ray.Ray;
import surface.Surface;
import util.Library;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.HitData;
import etc.RaytracerException;

public class ScenePixelImpl implements ScenePixel
{
	public Ray r;
	public Scene scene;
	public int currentDepth;
	private Point eye;
	private Point light;
	private Util util;
	private int maxDepth;
	
	public ScenePixelImpl(Ray incomingRay, Scene incomingScene, Point incomingEye, Point incomingLight, Util incomingUtil, int incomingMaxDepth)
	{
		r = incomingRay;
		scene = incomingScene;
		currentDepth = 0;
		eye = incomingEye;
		light = incomingLight;
		util = incomingUtil;
		maxDepth = incomingMaxDepth;
	}
	
	public ScenePixelImpl(Ray incomingRay, Scene incomingScene, Point incomingEye, Point incomingLight, int incomingMaxDepth)
	{
		this(incomingRay, incomingScene, incomingEye, incomingLight, new UtilImpl(), incomingMaxDepth);
	}
	
	public Color getPixelColor() throws RaytracerException
	{
		return recurse(r,currentDepth);
	}
	
	@Override
	public Color recurse(Ray r, int currentDepth) throws RaytracerException
	{
		if(maxDepth == currentDepth)
		{
			return new Color(0.0,0.0,0.0);
		}
		HitData currHit = scene.getSmallestPositiveHitDataOrReturnMiss(r);
		if(!currHit.isHit())
		{
			return new Color(0.0,0.0,0.0);
		}
		return colorPixel(r, scene, currentDepth, currHit);
	}
	
	private Color colorPixel(Ray r, Scene scene, int currentDepth, HitData hit) throws RaytracerException
	{
		Color totalColor = new Color(0.0,0.0,0.0);
		Color surfaceColor = new Color(0.0,0.0,0.0);
		Surface currSurface = hit.getSurface();

		Color reflectReturnColor = util.getReflectedColor(r, currentDepth, hit, currSurface, this);
		Color refractReturnColor = util.getRefractedColor(r, currentDepth, hit, currSurface, this);
		boolean inShadow = Library.isInShadow(currSurface, scene, light, hit);
		surfaceColor = currSurface.getColor(light, eye, inShadow, hit.getNormal(), hit.getP());
		
		totalColor.red += reflectReturnColor.red + refractReturnColor.red + surfaceColor.red;
		totalColor.blue += reflectReturnColor.blue + refractReturnColor.blue + surfaceColor.blue;
		totalColor.green += reflectReturnColor.green + refractReturnColor.green + surfaceColor.green;
		return totalColor;
	}

	public Ray getR()
	{
		return r;
	}

	public Scene getScene()
	{
		return scene;
	}

	public int getCurrentDepth()
	{
		return currentDepth;
	}

	public Point getEye()
	{
		return eye;
	}

	public Point getLight()
	{
		return light;
	}
}
