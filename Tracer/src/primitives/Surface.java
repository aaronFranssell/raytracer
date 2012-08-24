package primitives;

import bumpMapping.BumpMap;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;
import etc.RaytracerException;
import math.Point;
import math.Vector;
import noise.NoiseColor;
import util.Library;
import util.Util;


public abstract class Surface
{
	protected Color cR;
	protected Color cA;
	protected Color cL;
	protected Util ops;
	
	public enum SurfaceType { Cylinder, Sphere, Outersphere, Torus, Triangle, Plane, CSGTree, Cone};
	
	public abstract Vector getNormal(Point p, Ray r);
	
	public abstract SurfaceType getType();
	
	protected Effects effects;
	
	public abstract HitData getHitData(Ray r) throws RaytracerException;
	
	public Color getColor(Point light, Point eye, int phongExponent, boolean inShadow, Vector n, Point p)
	{
		Vector normal = n;
		if(effects.getBumpMapClass() != null)
		{
			BumpMap map = effects.getBumpMapClass();
			normal = map.getBump(p, normal);
		}
		if(effects.getNoiseMappedColorClass() != null)
		{
			NoiseColor nc = effects.getNoiseMappedColorClass();
			cR = nc.getColor(p);
		}
		if(effects.isPhong())
		{
			return Library.getColorPhong(cR, cA, cL, normal, light, eye, phongExponent, p, inShadow);
		}
		else if(effects.isLambertian())
		{
			return Library.getColorLambertian(cR, cA, cL, normal, light, p, inShadow);
		}
		return new Color(0.0,0.0,0.0);
	}
	
	public Color getCA()
	{
		return cA;
	}

	public Color getCL()
	{
		return cL;
	}

	public Color getCR()
	{
		return cR;
	}

	public Effects getEffects()
	{
		return effects;
	}
}
