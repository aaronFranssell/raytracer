package etc;

import java.util.LinkedList;

import math.Point;
import math.Vector;
import primitives.Surface;
import util.Constants;
import util.Library;

public class ScenePixel
{
	public Ray r;
	public LinkedList<Surface> surfaceList;
	public int currentDepth;
	private Point eye;
	private Point light;
	
	public ScenePixel(Ray incomingRay, LinkedList<Surface> incomingSurfaceList, int incomingCurrentDepth, Point incomingEye, Point incomingLight)
	{
		r = incomingRay;
		surfaceList = incomingSurfaceList;
		currentDepth = incomingCurrentDepth;
		eye = incomingEye;
		light = incomingLight;
	}
	
	public Color getPixelColor() throws Exception
	{
		return recurse(r,surfaceList,currentDepth);
	}
	
	private Color recurse(Ray r, LinkedList<Surface> surfaceList, int currentDepth) throws RaytracerException
	{
		HitData currHit;
		if(Constants.maxDepth == currentDepth)
		{
			return new Color(0.0,0.0,0.0);
		}
		currHit = shootRay(surfaceList,r);
		if(!currHit.isHit())
		{
			return new Color(0.0,0.0,0.0);
		}
		return colorPixel(r, surfaceList, currentDepth, eye, currHit);
	}
	
	private HitData shootRay(LinkedList<Surface> surfaceList, Ray r) throws RaytracerException
	{
		HitData returnHit = new HitData();
		for(Surface currentSurface : surfaceList)
		{
			HitData currentHit = currentSurface.getHitData(r); 
			if(currentHit.isHit() && (currentHit.getSmallestT() < returnHit.getSmallestT() || !returnHit.isHit()))
			{
				returnHit = currentHit;
			}
		}
		return returnHit;
	}
	
	private Color colorPixel(Ray r, LinkedList<Surface> surfaceList, int currentDepth, Point eye, HitData hit) throws RaytracerException
	{
		Color totalColor = new Color(0.0,0.0,0.0);
		Color surfaceColor = new Color(0.0,0.0,0.0);
		Surface currSurface = hit.getSurface();

		Color reflectReturnColor = getReflectedColor(r, surfaceList, currentDepth, hit, currSurface);
		Color refractReturnColor = getRefractedColor(r, surfaceList, currentDepth, hit, currSurface);
		boolean inShadow = Library.isInShadow(currSurface, surfaceList, light, hit);
		surfaceColor =  currSurface.getColor(light, eye, Constants.PHONG_EXPONENT, inShadow, hit.getNormal(), hit.getP());
		
		totalColor.red += reflectReturnColor.red + refractReturnColor.red + surfaceColor.red;
		totalColor.blue += reflectReturnColor.blue + refractReturnColor.blue + surfaceColor.blue;
		totalColor.green += reflectReturnColor.green + refractReturnColor.green + surfaceColor.green;
		return totalColor;
	}

	private Color getRefractedColor(Ray r, LinkedList<Surface> surfaceList,	int currentDepth, HitData hit, Surface currSurface) throws RaytracerException
	{
		Ray newRay;
		Color refractReturnColor = new Color(0.0,0.0,0.0);
		if(currSurface.getEffects().isRefractive())
		{
			newRay = Library.getRefractedRay(r.getD(), Constants.refractiveN,Constants.refractiveNT, hit);
			refractReturnColor = recurse(newRay,surfaceList,currentDepth+1);
			Library.clamp(refractReturnColor);
		}
		return refractReturnColor.scaleReturn(Constants.scaleRefractReturnColor);
	}

	private Color getReflectedColor(Ray r, LinkedList<Surface> surfaceList,	int currentDepth, HitData hit, Surface currSurface) throws RaytracerException
	{
		//reversedD.dot... code is for when the ray might be refracted inside of a surface.
		//if the ray's dot product is < 0 then the ray is inside of a surface and does not need to
		//be reflected
		Vector reversedD = new Vector(-r.getD().x,-r.getD().y,-r.getD().z);
		Color reflectReturnColor = new Color(0.0,0.0,0.0);
		if(currSurface.getEffects().isReflective() && reversedD.dot(hit.getNormal()) > 0)
		{
			Ray newRay = Library.getReflectedRay(r, hit.getP(), hit.getNormal());
			reflectReturnColor = recurse(newRay, surfaceList, currentDepth + 1);
			Library.clamp(reflectReturnColor);
		}
		return reflectReturnColor.scaleReturn(Constants.scaleReflectReturnColor);
	}
}
