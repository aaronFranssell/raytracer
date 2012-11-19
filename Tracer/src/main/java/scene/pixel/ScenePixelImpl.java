package scene.pixel;

import etc.Color;
import etc.HitData;
import etc.RaytracerException;
import etc.Refractive;
import math.Point;
import math.Vector;
import scene.Scene;
import scene.ray.Ray;
import surface.Surface;
import util.Constants;
import util.Library;
import util.Util;
import util.UtilImpl;

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
	
	private Color recurse(Ray r, int currentDepth) throws RaytracerException
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

		Color reflectReturnColor = getReflectedColor(r, currentDepth, hit, currSurface);
		Color refractReturnColor = getRefractedColor(r, currentDepth, hit, currSurface);
		boolean inShadow = Library.isInShadow(currSurface, scene, light, hit);
		surfaceColor = currSurface.getColor(light, eye, Constants.PHONG_EXPONENT, inShadow, hit.getNormal(), hit.getP());
		
		totalColor.red += reflectReturnColor.red + refractReturnColor.red + surfaceColor.red;
		totalColor.blue += reflectReturnColor.blue + refractReturnColor.blue + surfaceColor.blue;
		totalColor.green += reflectReturnColor.green + refractReturnColor.green + surfaceColor.green;
		return totalColor;
	}

	private Color getRefractedColor(Ray r, int currentDepth, HitData hit, Surface currSurface) throws RaytracerException
	{
		if(currSurface.getEffects().getRefractive() == null)
		{
			return new Color(0.0,0.0,0.0);
		}
		Refractive refractive = currSurface.getEffects().getRefractive();
		Ray newRay = util.getRefractedRay(r.getD(), refractive.getN(),refractive.getnT(), hit);
		if(newRay == null)
		{
			return new Color(0.0,0.0,0.0);
		}
		Color refractReturnColor = recurse(newRay, currentDepth + 1);
		refractReturnColor = util.clamp(refractReturnColor);
		return refractReturnColor;
	}

	private Color getReflectedColor(Ray r, int currentDepth, HitData hit, Surface currSurface) throws RaytracerException
	{
		//reversedD.dot... code is for when the ray might be refracted inside of a surface.
		//if the ray's dot product is < 0 then the ray is inside of a surface and does not need to
		//be reflected
		Vector reversedD = new Vector(-r.getD().x,-r.getD().y,-r.getD().z);
		Color reflectReturnColor = new Color(0.0,0.0,0.0);
		if(currSurface.getEffects().isReflective() && reversedD.dot(hit.getNormal()) > 0)
		{
			Ray newRay = Library.getReflectedRay(r, hit.getP(), hit.getNormal());
			//total internal reflection
			if(newRay != null)
			{
				reflectReturnColor = recurse(newRay, currentDepth + 1);
				reflectReturnColor = util.clamp(reflectReturnColor);
			}
		}
		return reflectReturnColor;
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
