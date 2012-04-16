package etc;

import java.util.Iterator;
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
	public ScenePixel(Ray incomingRay, LinkedList<Surface> incomingSurfaceList, int incomingCurrentDepth)
	{
		r = incomingRay;
		surfaceList = incomingSurfaceList;
		currentDepth = incomingCurrentDepth;
	}
	
	public Color getPixelColor() throws Exception
	{
		return recurse(r,surfaceList,currentDepth);
	}
	
	private Color recurse(Ray r, LinkedList<Surface> surfaceList, int currentDepth) throws Exception
	{
		Point eye = Constants.eye.copy();
		HitData currHit;
		if(Constants.maxDepth == currentDepth)
		{
			return new Color(0.0,0.0,0.0);
		}
		currHit = shootRay(surfaceList,r);
		if(currHit == null || !currHit.isHit())
		{
			return new Color(0.0,0.0,0.0);
		}
		return colorPixel(r, surfaceList, currentDepth, eye, currHit);
	}//recurse
	
	private HitData shootRay(LinkedList<Surface> surfaceList, Ray r) throws Exception
	{
		Iterator<Surface> surfaceListIt = surfaceList.iterator();
		HitData hit = null;
		while(surfaceListIt.hasNext())
		{
			Surface currentSurface = (Surface) surfaceListIt.next();
			HitData smallestHit = currentSurface.getHitData(r); 
			if(smallestHit.isHit())
			{
				if(hit == null || smallestHit.getSmallestT() < hit.getSmallestT())
				{
					hit = smallestHit;
				}//if
			}//if
		}//while
		return hit;
	}//shootRay
	
	private Color colorPixel(Ray r, LinkedList<Surface> surfaceList, int currentDepth, Point eye, 
			  						HitData hit) throws Exception
	{
		Color reflectReturnColor = new Color(0.0,0.0,0.0);
		Color refractReturnColor = new Color(0.0,0.0,0.0);
		Color totalColor = new Color(0.0,0.0,0.0);
		Color surfaceColor = new Color(0.0,0.0,0.0);
		Surface currSurface = hit.getSurface();

		reflectReturnColor = getReflectedColor(r, surfaceList, currentDepth, hit, reflectReturnColor, currSurface);
		refractReturnColor = getRefractedColor(r, surfaceList, currentDepth, hit, refractReturnColor, currSurface);
		surfaceColor =  currSurface.getColor(Constants.light, eye, Constants.PHONG_EXPONENT,
									Library.isInShadow(currSurface, surfaceList,Constants.light, hit),
									currSurface.getCR(), hit.getP(),currSurface.getCA(),
									currSurface.getCL(), hit.getNormal());

		surfaceColor = surfaceColor.scaleReturn(Constants.scaleReturnColor);
		
		totalColor.red += reflectReturnColor.red + refractReturnColor.red + surfaceColor.red;
		totalColor.blue += reflectReturnColor.blue + refractReturnColor.blue + surfaceColor.blue;
		totalColor.green += reflectReturnColor.green + refractReturnColor.green + surfaceColor.green;
		return totalColor;
	}

	private Color getRefractedColor(Ray r, LinkedList<Surface> surfaceList,
			int currentDepth, HitData hit, Color refractReturnColor,
			Surface currSurface) throws Exception
	{
		Ray newRay;
		if(currSurface.getEffects().isRefractive())
		{
			newRay = Library.getRefractedRay(r.getD(), Constants.refractiveN,Constants.refractiveNT, hit);
			if(newRay != null)
			{
				refractReturnColor = recurse(newRay,surfaceList,currentDepth+1);
				Library.clamp(refractReturnColor);
			}//if
		}//if
		return refractReturnColor.scaleReturn(Constants.scaleRefractReturnColor);
	}

	private Color getReflectedColor(Ray r, LinkedList<Surface> surfaceList,
			int currentDepth, HitData hit, Color reflectReturnColor,
			Surface currSurface) throws Exception
	{
		Ray newRay;
		//reversedD.dot... code is for when the ray might be refracted inside of a surface.
		//if the ray's dot product is < 0 then the ray is inside of a surface and does not need to
		//be reflected
		Vector reversedD = new Vector(-r.getD().x,-r.getD().y,-r.getD().z);
		if(currSurface.getEffects().isReflective() && 
		   reversedD.dot(hit.getNormal()) > 0)
		{
			newRay = Library.getReflectedRay(r, hit.getP(), hit.getNormal());
			reflectReturnColor = recurse(newRay, surfaceList, currentDepth + 1);
			Library.clamp(reflectReturnColor);
		}//if
		return reflectReturnColor.scaleReturn(Constants.scaleReflectReturnColor);
	}
}
