package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.Transform;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Library;
import etc.Color;
import etc.Effects;
import etc.HitData;
import util.*;

public class Torus extends Surface
{
	private double largeR;
	private double smallR;
	private Vector direction;
	private Transform transform;
	
	public static final Vector OFFSET_DIRECTION_VECTOR = new Vector(0.0,0.0000001, 0.0);
	public static final Vector ZERO = new Vector(0.0,0.0,0.0);
	public static final Vector SURFACE_DIRECTION = new Vector(0.0,0.0,1.0);
	
	public Torus(Point incomingCenter, Vector incomingDirection, double incomingLargeR, double incomingSmallR, Color incomingCR,Color incomingCA, Color incomingCL,
				 Effects incomingEffects, Util incomingOps) throws Exception
	{
		smallR = incomingSmallR;
		largeR = incomingLargeR;
		cR = incomingCR;
		cA = incomingCA;
		cL = incomingCL;
		effects = incomingEffects;
		
		//because I couldn't find a generalized torus rendering algorithm, I will have to use matrix
		//transformations in order to render things the way I want to :-(
		direction = incomingDirection;
		direction.normalize();
		if(SURFACE_DIRECTION.cross(direction).equals(ZERO))
		{
			System.out.println("WARNING: Torus world direction parallel to surface local direction, offseting by: " + OFFSET_DIRECTION_VECTOR);
			direction = direction.add(OFFSET_DIRECTION_VECTOR);
			direction.normalize();
		}
		transform = new Transform(SURFACE_DIRECTION, direction, incomingCenter);
		ops = incomingOps;
	}
	
	public Torus(Point incomingCenter, Vector incomingDirection, double incomingLargeR, double incomingSmallR, Color incomingCR,Color incomingCA, Color incomingCL,
			 Effects incomingEffects) throws Exception
	{
		this(incomingCenter, incomingDirection, incomingLargeR, incomingSmallR, incomingCR, incomingCA, incomingCL, incomingEffects, new UtilImpl());
	}
	
	private Vector getLocalNormal(Point p)
	{
		Vector normal = new Vector(0.0,0.0,0.0);
		normal.x=4*p.x*(Math.pow(p.x,2)+Math.pow(p.y,2)+Math.pow(p.z,2)-Math.pow(smallR, 2)-Math.pow(largeR,2));
		normal.y=4*p.y*(Math.pow(p.x,2)+Math.pow(p.y,2)+Math.pow(p.z,2)-Math.pow(smallR, 2)-Math.pow(largeR,2));
		normal.z=4*p.z*(Math.pow(p.x,2) + Math.pow(p.y,2) + Math.pow(p.z,2) - Math.pow(smallR, 2) - Math.pow(largeR, 2))
			  + 8 * Math.pow(largeR, 2) * p.z;
		normal.normalize();
		return normal;
	}
	
	public Vector getNormal(Point p, Ray r)
	{
		Vector localNormal = getLocalNormal(transform.transformPointToLocal(p));
		return transform.translateVectorToWorld(localNormal);
	}
	
	public ArrayList<HitData> getHitData(Ray r)
	{
		Ray transformedRay = new Ray(transform.translateVectorToLocal(r.getD()),
									 transform.transformPointToLocal(r.getEye()));
		double[] retTArray = getTArray(transformedRay);
		ArrayList<HitData> retList = new ArrayList<HitData>();
		//the ray direction + ray eye are being transformed, the eye will stay the same "t" distance away from the torus,
		//since the magnitude of the eye point will stay the same relative to the torus. I think. I hope...
		for(double t : retTArray)
		{
			Point p = ops.getP(t, transformedRay);
			Vector normal = transform.translateVectorToWorld(getLocalNormal(p));
			p = transform.transformPointToWorld(p);
			HitData hit = new HitData(t, this, normal, p);
			retList.add(hit);
		}
		return retList;
	}
	
	private double[] getTArray(Ray r)
	{
		double Dz = r.getD().z;
		double Dy = r.getD().y;
		double Dx = r.getD().x;
		double largeR = this.largeR;
		double smallR = this.smallR;
		double Pz = r.getEye().z;
		double Py = r.getEye().y;
		double Px = r.getEye().x;

		//constants: these are the equivalent of A, B, and C in Ax^2.0 + Bx + C
		double A = 1.0*Math.pow(Dx,4) + 2.0*Math.pow(Dx,2)*Math.pow(Dy,2) + 2.0*Math.pow(Dx,2)*Math.pow(Dz,2) + 1.0*Math.pow(Dy,4) + 2.0*Math.pow(Dy,2)*Math.pow(Dz,2) + 1.0*Math.pow(Dz,4);
		double B = 4.0*Px*Math.pow(Dx,3) + 4.0*Px*Dx*Math.pow(Dy,2) + 4.0*Px*Dx*Math.pow(Dz,2) + 4.0*Math.pow(Dx,2)*Py*Dy + 4.0*Math.pow(Dx,2)*Pz*Dz + 4.0*Py*Math.pow(Dy,3) + 4.0*Py*Dy*Math.pow(Dz,2) + 4.0*Math.pow(Dy,2)*Pz*Dz + 4.0*Pz*Math.pow(Dz,3);
		double C = 6.0*Math.pow(Px,2)*Math.pow(Dx,2) + 2.0*Math.pow(Px,2)*Math.pow(Dy,2) + 2.0*Math.pow(Px,2)*Math.pow(Dz,2) + 8.0*Px*Dx*Py*Dy + 8.0*Px*Dx*Pz*Dz + 2.0*Math.pow(Dx,2)*Math.pow(Py,2) + 2.0*Math.pow(Dx,2)*Math.pow(Pz,2)-2.0*Math.pow(Dx,2)*Math.pow(smallR,2)-2.0*Math.pow(Dx,2)*Math.pow(largeR,2) + 6.0*Math.pow(Py,2)*Math.pow(Dy,2) + 2.0*Math.pow(Py,2)*Math.pow(Dz,2) + 8.0*Py*Dy*Pz*Dz + 2.0*Math.pow(Dy,2)*Math.pow(Pz,2)-2.0*Math.pow(Dy,2)*Math.pow(smallR,2)-2.0*Math.pow(Dy,2)*Math.pow(largeR,2) + 6.0*Math.pow(Pz,2)*Math.pow(Dz,2)-2.0*Math.pow(Dz,2)*Math.pow(smallR,2) + 2.0*Math.pow(Dz,2)*Math.pow(largeR,2);
		double D = 4.0*Math.pow(Px,3)*Dx + 4.0*Math.pow(Px,2)*Py*Dy + 4.0*Math.pow(Px,2)*Pz*Dz + 4.0*Px*Dx*Math.pow(Py,2) + 4.0*Px*Dx*Math.pow(Pz,2)-4.0*Px*Dx*Math.pow(smallR,2)-4.0*Px*Dx*Math.pow(largeR,2) + 4.0*Math.pow(Py,3)*Dy + 4.0*Math.pow(Py,2)*Pz*Dz + 4.0*Py*Dy*Math.pow(Pz,2)-4.0*Py*Dy*Math.pow(smallR,2)-4.0*Py*Dy*Math.pow(largeR,2) + 4.0*Math.pow(Pz,3)*Dz-4.0*Pz*Dz*Math.pow(smallR,2) + 4.0*Pz*Dz*Math.pow(largeR,2);
		double E = 1.0*Math.pow(Px,4) + 2.0*Math.pow(Px,2)*Math.pow(Py,2) + 2.0*Math.pow(Px,2)*Math.pow(Pz,2)-2.0*Math.pow(Px,2)*Math.pow(smallR,2)-2.0*Math.pow(Px,2)*Math.pow(largeR,2) + 1.0*Math.pow(Py,4) + 2.0*Math.pow(Py,2)*Math.pow(Pz,2)-2.0*Math.pow(Py,2)*Math.pow(smallR,2)-2.0*Math.pow(Py,2)*Math.pow(largeR,2) + 1.0*Math.pow(Pz,4)-2.0*Math.pow(Pz,2)*Math.pow(smallR,2) + 2.0*Math.pow(Pz,2)*Math.pow(largeR,2) + 1.0*Math.pow(smallR,4)-2.0*Math.pow(smallR,2)*Math.pow(largeR,2) + 1.0*Math.pow(largeR,4);

		return Library.solveQuartic(A, B, C, D, E);
	}
	
	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Torus;
	}
}
