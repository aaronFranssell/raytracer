package surface.primitives;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Constants;
import util.Library;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;

public class Cone extends Surface
{
	private Vector direction;
	private Point vertex;
	private Point basePoint;
	private double length;
	private double alpha;
	private double cosAlphaSquared;
	private double cosAlpha;
	
	/**
	 * @param incomingDirection The direction the cone is pointing in.
	 * @param incomingVertex The vertex of the cone.
	 * @param incomingAlpha The angle (in radians) between the direction vector and the edge of the cone.
	 * @param incomingBasePoint The point from which the length of the cone will be computed. Must be on the direction/vertex line.
	 * @param incomingLength The length of the cone.
	 * @throws Exception When the base point is not on the same line as the vertex and the direction.
	 */
	public Cone(Vector incomingDirection, Point incomingVertex, double incomingAlpha, Point incomingBasePoint, double incomingLength, 
				Color incomingCR, Color incomingCA, Color incomingCL, Effects incomingEffects, Util incomingOps) throws Exception
	{
		direction = incomingDirection.normalizeReturn();
		vertex = incomingVertex;
		alpha = incomingAlpha;
		cosAlpha = Math.cos(alpha);
		cosAlphaSquared = cosAlpha * cosAlpha;
		
		cR = incomingCR;
		cA = incomingCA;
		cL = incomingCL;
		
		effects = incomingEffects;
		
		basePoint = incomingBasePoint;
		length = incomingLength;
		
		ops = incomingOps;
		
		//this gives the direction of the vector between the vertex and the basePoint
		//if this direction vector is within spitting distance of the direction vector then they are on the same line.
		if(!(Library.doubleEqual(vertex.x, basePoint.x, Constants.POSITIVE_ZERO) &&
		     Library.doubleEqual(vertex.y, basePoint.y, Constants.POSITIVE_ZERO) &&
		     Library.doubleEqual(vertex.z, basePoint.z, Constants.POSITIVE_ZERO)))
		{
			Vector basePointVertexDirection = basePoint.minus(vertex).normalizeReturn();
			if(!basePointVertexDirection.equals(direction) && !basePointVertexDirection.scaleReturn(-1.0).equals(direction))
			{
				throw new Exception("The base point: " + basePoint.toString() + " is not on the same line as the vertex: " 
									+ vertex.toString() + " and direction vector: " + direction.toString());
			}
		}
	}
	
	public Cone(Vector incomingDirection, Point incomingVertex, double incomingAlpha, Point incomingBasePoint, double incomingLength, 
			Color incomingCR, Color incomingCA, Color incomingCL, Effects incomingEffects) throws Exception
	{
		this(incomingDirection, incomingVertex, incomingAlpha, incomingBasePoint, incomingLength, incomingCR, incomingCA, incomingCL, incomingEffects,
			 new UtilImpl());
	}

	/**
	 * Derived this myself, so may be a little buggy :-/
	 * If we have a hit point P, then we can use basic trig to find the normal.
	 * The normal will be the vector from a point on the direction vector through the hit point.
	 * In order to find this point, make a triangle using P - Vertex and alpha.
	 * (Imagine that the angle at P is a right angle, curse my crappy picture).
	 * Therefore, the length of the Vertex through Direction segment is:
	 * 
	 * H = (P-Vertex).magnitude()/cos(alpha)
	 * 
	 * Scale the direction vector by H and offset that vector by the Vertex. That is the point from which
	 * we can get the normal. Call this point Nh, or normal on the hypotenuse. Get the vector from P to Nh and normalize
	 * it.
	 * 
	 * The only catch to all this is if the P-Vertex vector does not lie in the same direction as the Direction
	 * vector, then the Nh point will be in the wrong location. Therefore we have to take the dot product of
	 * P-Vertex and the direction to see if they lie in the same direction. If they don't, then multiply direction
	 * by -1 to orient in the correct direction. 
	 *     
	 *     				
			           /
			          /
			      P  /
			        /\
			       /  \
			      /    \
			     /      \
	      Vertex/-------------> Direction
	 * 
	 * One more wrinkle, the normal will be pointing in the wrong direction if the ray hits the cone on the inside of the
	 * cone "cup". If the dot product of the normal/ray is positive, they are pointing the same direction, and the normal should
	 * be reversed.
	 */
	@Override
	public Vector getNormal(Point p, Ray r)
	{		
		Point Nh;
		Vector pMinusV = p.minus(vertex);
		double hypotenuse = pMinusV.magnitude()/cosAlpha;
		if(pMinusV.dot(direction) < 0)
		{
			Nh = (direction.scaleReturn(-1 * hypotenuse).add(vertex)).toPoint();
		}
		else
		{
			Nh = (direction.scaleReturn(hypotenuse).add(vertex)).toPoint();
		}
		Vector normal = p.minus(Nh);
		return normal.normalizeReturn();
	}

	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Cone;
	}

	@Override
	public ArrayList<HitData> getHitData(Ray r) throws RaytracerException
	{
		double Dz = r.getD().z;
		double Dy = r.getD().y;
		double Dx = r.getD().x;
		double Ez = r.getEye().z;
		double Ey = r.getEye().y;
		double Ex = r.getEye().x;
		double Mz = vertex.z;
		double My = vertex.y;
		double Mx = vertex.x;
		double Nx = direction.x;
		double Ny = direction.y;
		double Nz = direction.z;

		//constants: these are the equivalent of A, B, and C in Ax^2.0 + Bx + C
		double A = -1.0*cosAlphaSquared*Math.pow(Dx,2)-1.0*cosAlphaSquared*Math.pow(Dy,2)-1.0*cosAlphaSquared*Math.pow(Dz,2) + 1.0*Math.pow(Dx,2)*Math.pow(Nx,2) + 2.0*Dx*Nx*Dy*Ny + 2.0*Dx*Nx*Dz*Nz + 1.0*Math.pow(Dy,2)*Math.pow(Ny,2) + 2.0*Dy*Ny*Dz*Nz + 1.0*Math.pow(Dz,2)*Math.pow(Nz,2);
		double B = -2.0*cosAlphaSquared*Ex*Dx + 2.0*cosAlphaSquared*Dx*Mx-2.0*cosAlphaSquared*Ey*Dy + 2.0*cosAlphaSquared*Dy*My-2.0*cosAlphaSquared*Ez*Dz + 2.0*cosAlphaSquared*Dz*Mz + 2.0*Ex*Math.pow(Nx,2)*Dx + 2.0*Ex*Nx*Dy*Ny + 2.0*Ex*Nx*Dz*Nz-2.0*Dx*Math.pow(Nx,2)*Mx + 2.0*Dx*Nx*Ey*Ny-2.0*Dx*Nx*My*Ny + 2.0*Dx*Nx*Ez*Nz-2.0*Dx*Nx*Mz*Nz-2.0*Mx*Nx*Dy*Ny-2.0*Mx*Nx*Dz*Nz + 2.0*Ey*Math.pow(Ny,2)*Dy + 2.0*Ey*Ny*Dz*Nz-2.0*Dy*Math.pow(Ny,2)*My + 2.0*Dy*Ny*Ez*Nz-2.0*Dy*Ny*Mz*Nz-2.0*My*Ny*Dz*Nz + 2.0*Ez*Math.pow(Nz,2)*Dz-2.0*Dz*Math.pow(Nz,2)*Mz;
		double C = -1.0*cosAlphaSquared*Math.pow(Ex,2) + 2.0*cosAlphaSquared*Ex*Mx-1.0*cosAlphaSquared*Math.pow(Mx,2)-1.0*cosAlphaSquared*Math.pow(Ey,2) + 2.0*cosAlphaSquared*Ey*My-1.0*cosAlphaSquared*Math.pow(My,2)-1.0*cosAlphaSquared*Math.pow(Ez,2) + 2.0*cosAlphaSquared*Ez*Mz-1.0*cosAlphaSquared*Math.pow(Mz,2) + 1.0*Math.pow(Ex,2)*Math.pow(Nx,2)-2.0*Ex*Math.pow(Nx,2)*Mx + 2.0*Ex*Nx*Ey*Ny-2.0*Ex*Nx*My*Ny + 2.0*Ex*Nx*Ez*Nz-2.0*Ex*Nx*Mz*Nz + 1.0*Math.pow(Mx,2)*Math.pow(Nx,2)-2.0*Mx*Nx*Ey*Ny + 2.0*Mx*Nx*My*Ny-2.0*Mx*Nx*Ez*Nz + 2.0*Mx*Nx*Mz*Nz + 1.0*Math.pow(Ey,2)*Math.pow(Ny,2)-2.0*Ey*Math.pow(Ny,2)*My + 2.0*Ey*Ny*Ez*Nz-2.0*Ey*Ny*Mz*Nz + 1.0*Math.pow(My,2)*Math.pow(Ny,2)-2.0*My*Ny*Ez*Nz + 2.0*My*Ny*Mz*Nz + 1.0*Math.pow(Ez,2)*Math.pow(Nz,2)-2.0*Ez*Math.pow(Nz,2)*Mz + 1.0*Math.pow(Mz,2)*Math.pow(Nz,2);

		double[] retTArray = Library.solveQuadratic(A, B, C);
		double smallestT = Library.getSmallestT(retTArray);
		if(!Double.isNaN(smallestT))
		{
			//determine if the hit point p is to far away from the base point
			retTArray = getHitTsByLimitingLength(retTArray, direction, basePoint, length, r);
		}
		if(!Double.isNaN(Library.getSmallestT(retTArray)))
		{
			ArrayList<HitData> retHitData = new ArrayList<HitData>();
			for(double t : retTArray)
			{
				Point p = ops.getP(smallestT, r);
				Vector normal = getNormal(p, r);
				retHitData.add(new HitData(t, this, normal, p));
			}
			return retHitData;
		}
		else
		{
			return new ArrayList<HitData>();
		}
	}
	
	/**
	 * This function determines whether or not the incoming hit t values are still hits base on whether or not they occur past the
	 * solid length. For instance a point my hit an infinite cylinder, but it will be past the length, or before the base point, so
	 * it should count as a miss.
	 * @param incomingHitTs The default hit t values. These must be valid hits.
	 * @param solidDirection The direction vector of the solid.
	 * @param basePoint The point on the direction vector.
	 * @param length The length of the solid.
	 * @param r The ray.
	 * @return The correct t values, or NaN if there is a miss.
	 * @throws Exception 
	 */
	private double[] getHitTsByLimitingLength(double[] incomingHitTs, Vector solidDirection, Point basePoint, double length, Ray r) throws RaytracerException
	{
		double[] hitTs = new double[incomingHitTs.length];
		for(int i = 0; i < incomingHitTs.length; i++)
		{
			if(Double.isNaN(incomingHitTs[i]))
			{
				throw new RaytracerException("Hit t value is NaN, these values must be valid hits.");
			}
			Point hitP = ops.getP(incomingHitTs[i], r);
			
			Vector vectorToHitPointFromBase = hitP.minus(basePoint);
			Vector vectorToHitPointFromBaseNormalized = vectorToHitPointFromBase.normalizeReturn(); 
			
			//if the dot product is < 0, then the hit occurs behind the base point, so it is a miss
			if(solidDirection.dot(vectorToHitPointFromBaseNormalized) < 0)
			{
				hitTs[i] = Double.NaN;
			}//if
			else
			{
				/*
				Length is not simply the distance between the hitP and basePoint, it is the distance along the solidDirection
				vector. We will set up a triangle having points basePoint and hitP. The angle between solidDirection and 
				vectorToHitPointFromBase is alpha. The distance along the solidDirection vector of the point is:
				cos(alpha) * vectorToHitPointFromBase.magnitude()
				by the properties of cos. 
				*/
				double solidDirectionLength = solidDirection.dot(vectorToHitPointFromBaseNormalized) * 
											  vectorToHitPointFromBase.magnitude();
				if(solidDirectionLength > length)
				{
					hitTs[i] = Double.NaN;
				}//if
				else
				{
					//simply copy over the hitT
					hitTs[i] = incomingHitTs[i];
				}
			}
		}
		
		return hitTs;
	}
}
