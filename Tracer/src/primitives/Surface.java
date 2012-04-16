package primitives;

import bumpMapping.BumpMap;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;
import math.Point;
import math.Vector;
import noise.NoiseColor;
import util.Library;


public abstract class Surface
{
	public static final int CYLINDER = 0;
	public static final int SPHERE = 1;
	public static final int OUTERSPHERE = 2;
	public static final int TORUS = 3;
	public static final int TRIANGLE = 4;
	public static final int PLANE = 5;
	public static final int CSGTREE = 6;
	public static final int CONE = 7;
	
	public static String translateType(Surface surface)
	{
		if(surface == null)
		{
			return "null";
		}
		switch(surface.getType())
		{
		case CYLINDER:
			return "Cylinder";
		case SPHERE:
			return "Sphere";
		case OUTERSPHERE:
			return "Outer sphere";
		case TORUS:
			return "Torus";
		case TRIANGLE:
			return "Triangle";
		case PLANE:
			return "Plane";
		case CSGTREE:
			return "CSGTree";
		case CONE:
			return "CONE";
		default:
			try
			{
				throw new Exception("Unrecognized type: " + surface.getType());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.exit(0);
				return null;
			}
		}
	}
	
	public abstract Vector getNormal(Point p, Ray r) throws Exception;
	
	public abstract int getType();
	
	protected Effects effects;
	
	public abstract HitData getHitData(Ray r) throws Exception;
	
	public abstract Color getCR();
	
	public abstract Color getCA();
	
	public abstract Color getCL();
	
	public Color getColor(Point light, Point eye, int phongExponent, boolean inShadow, Color cR,
						  Point p, Color cA, Color cL, Vector n)
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
		}//if		
		if(effects.isPhong())
		{
			return Library.getColorPhong(cR, cA, cL, normal, light, eye, phongExponent, p, inShadow);
		}//if
		else if(effects.isLambertian())
		{
			return Library.getColorLambertian(cR, cA, cL, normal, light, p, inShadow);
		}//else
		return new Color(0.0,0.0,0.0);
	}

	public Effects getEffects()
	{
		return effects;
	}

	public void setEffects(Effects effects)
	{
		this.effects = effects;
	}
}
