package surface;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import noise.NoiseColor;
import scene.ray.Ray;
import util.Util;
import bumpMapping.BumpMap;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;
import etc.mapper.ImageMapper;


public abstract class Surface
{
	protected Color cR;
	protected Color cA;
	protected Color cL;
	protected Util ops;
	
	public static enum SurfaceType { Cylinder, Sphere, Outersphere, Torus, Triangle, Plane, CSGTree, Cone, Parallelogram, Bezier};
	
	public abstract SurfaceType getType();
	
	protected Effects effects;
	
	public abstract ArrayList<HitData> getHitData(Ray r) throws RaytracerException;
	
	public Color getColor(Ray r, Point light, Point eye, boolean inShadow, Vector n, Point p) throws RaytracerException
	{
		Color currentCR = cR.copy();
		Vector normal = n.copy();
		if(effects.getBumpMapClass() != null)
		{
			BumpMap map = effects.getBumpMapClass();
			normal = map.getBump(r, p, normal);
		}
		if(effects.getNoiseMappedColorClass() != null)
		{
			NoiseColor nc = effects.getNoiseMappedColorClass();
			currentCR = nc.getColor(p).add(currentCR);
		}
		if(effects.getImageMapper() != null)
		{
			ImageMapper mapper = effects.getImageMapper();
			currentCR = mapper.getColor(p).add(currentCR);
		}
		if(effects.getPhong() != null)
		{
			return ops.getColorPhong(currentCR, cA, cL, normal, light, eye, effects.getPhong().getExponent(), p, inShadow);
		}
		else if(effects.isLambertian())
		{
			return ops.getColorLambertian(currentCR, cA, cL, normal, light, p, inShadow);
		}
		throw new RaytracerException("No lighting model present. Must be either lambertian or phong.");
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

	public Color getcR()
	{
		return cR;
	}

	public void setcR(Color cR)
	{
		this.cR = cR;
	}

	public Color getcA()
	{
		return cA;
	}

	public void setcA(Color cA)
	{
		this.cA = cA;
	}

	public Color getcL()
	{
		return cL;
	}

	public void setcL(Color cL)
	{
		this.cL = cL;
	}

	public Util getOps()
	{
		return ops;
	}

	public void setOps(Util ops)
	{
		this.ops = ops;
	}

	public void setEffects(Effects effects)
	{
		this.effects = effects;
	}
}
