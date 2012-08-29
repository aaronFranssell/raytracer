package primitives;

import math.Point;
import math.Vector;
import util.Library;
import util.Util;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;

public class Cylinder extends Surface
{
	private Vector preComputedEyeMinusBottom;
	private Vector preComputedRCrossA;
	
	private Point bottom;
	private double radius;
	private double height;
	private Vector direction;
	private double d;
	private Point top;
	
	public Cylinder(Point incomingBottom, double incomingRadius, Color incomingCR, Color incomingCA, Color incomingCL, double incomingHeight, Vector incomingDirection,
					Effects incomingEffects, Util incomingOps)
	{
		bottom = incomingBottom;
		radius = incomingRadius;
		cR = incomingCR;
		cA = incomingCA;
		cL = incomingCL;
		height = incomingHeight;
		direction = incomingDirection;
		direction.normalize();
		effects = incomingEffects;
		top = new Point(direction.x * height, direction.y * height, direction.z * height);
		//Displace the top point by the bottom
		top.x += bottom.x;
		top.y += bottom.y;
		top.z += bottom.z;
		ops = incomingOps;
	}
	
	public Cylinder(Point incomingBottom, double incomingRadius, Color incomingCR, Color incomingCA, Color incomingCL, double incomingHeight, Vector incomingDirection,
					Effects incomingEffects)
	{
		this(incomingBottom, incomingRadius, incomingCR, incomingCA, incomingCL, incomingHeight, incomingDirection, incomingEffects, new UtilImpl());
	}
	
	private double[] getHitTs(Ray r)
	{
		/*
		 * this value gives the "t" distance to the point on the ray closest to the direction vector of the cylinder.
		 * Br is eyepoint, Bc is a point on the cylinder centerline, A is the direction, R is the ray direction.
		 * {(Br - Bc) X A} * (R X A)
		 * ------------------------
		 *          |R X A|^2
		 */
		double numerator = preComputedEyeMinusBottom.cross(direction).dot(preComputedRCrossA);
		double denominator = preComputedRCrossA.magnitude() * preComputedRCrossA.magnitude();
		double distanceToCylinder = numerator/denominator;
				
		Vector O = preComputedRCrossA.cross(direction);
		O.normalize();
		
		/*
		 *s = abs(sqrt(r^2 - d^2)/(R*O)) 
		 */
		double s = Math.sqrt(radius*radius - d*d)/r.getD().dot(O);
		double t1 = distanceToCylinder - s;
		double t2 = distanceToCylinder + s;
		double[] retTArray = new double[2];
		retTArray[0] = onLimitedCylinder(t1, r) ? t1:Double.NaN;
		retTArray[1] = onLimitedCylinder(t2, r) ? t2:Double.NaN;
		return retTArray;
	}
	
	public Vector getNormal(Point hitPoint, Ray r)
	{
		/*
		 * i derived this myself so may be buggy :/
		 * there is a right triangle formed between 3 points: the cylinder bottom, the hit point on the cylinder,
		 * and the point on the centerline of the cylinder that is closest to the hitpoint. The normal will be
		 * the vector that is the hit point minus the close centerline point. To find the centerline point,
		 * find the distance between the close point and bottom of the cylinder point. Once that distance is found,
		 * scale the direction vector by that amount, and then displace that vector by the bottom of the cylinder point.
		 */
		Vector hitPointMinusBottom = hitPoint.minus(bottom);
		//just the pythagorean theorem...
		double distanceToClosePoint = Math.sqrt(hitPointMinusBottom.magnitude() * hitPointMinusBottom.magnitude()
												- radius * radius);
		
		Vector vecToHitPoint = hitPointMinusBottom.copy();
		vecToHitPoint.normalize();
		
		//this holds the cosine of vecToHitPoint.dot(direction)
		//if cosDirection > 0, then the hit point happened in the direction of the direction vector.
		//if cosDirection < 0, then the hit point happened in the opposite direction of the direction vector
		double cosDirection = vecToHitPoint.dot(direction);
		if(cosDirection < 0)
		{
			distanceToClosePoint *= -1;
		}
		Vector pointOnLine = new Vector(direction.x * distanceToClosePoint, direction.y * distanceToClosePoint, 
										direction.z * distanceToClosePoint);		
		//displace the scaled vector by the center, and that is the close point
		pointOnLine.x += bottom.x;
		pointOnLine.y += bottom.y;
		pointOnLine.z += bottom.z;
		
		//the normal is now the difference between the hit point and the close point
		Vector normal = new Vector(hitPoint.x - pointOnLine.x,hitPoint.y - pointOnLine.y,hitPoint.z - pointOnLine.z);
		normal.normalize();
		return normal;
	}
	
	private boolean onLimitedCylinder(double aT, Ray r)
	{
		Point pointOnCylinder = ops.getP(aT, r);
		//calculate if hit point is in the height of the cylinder specified
		//take the dot product of (hit point - cylinder bottom point).dot(direction vector)
		//if this is > 0, then the hit point lies in front of the bottom.
		Vector bottomToP = pointOnCylinder.minus(bottom);
		bottomToP.normalize();
		//the hit point is below the bottom of the cylinder, therefore a miss...
		if(bottomToP.dot(direction) < 0)
		{
			return false;
		}
		//Now do the same as above, except in reverse.	
		Vector topToP = pointOnCylinder.minus(top);
		topToP.normalize();
		//now need to reverse the direction vector, in order to reverse the direction of the cylinder's direction
		Vector reversedDirection = new Vector(direction.x * -1, direction.y * -1, direction.z * -1);
		if(topToP.dot(reversedDirection) < 0)
		{
			return false;
		}
		return true;
	}
	
	public HitData getHitData(Ray r)
	{
		if(!rayHitInfiniteCylinder(r))
		{
			return new HitData();
		}
		double[] hitTs = getHitTs(r);
		double smallestT = Library.getSmallestT(hitTs);
		if(Double.isNaN(smallestT))
		{
			return new HitData();
		}
		Point hitPoint = ops.getP(smallestT, r);
		Vector normal = getNormal(hitPoint, r);
		HitData hit = new HitData(smallestT,this, normal, hitPoint, hitTs);
		return hit;
	}
	
	private boolean rayHitInfiniteCylinder(Ray r)
	{
		preComputedEyeMinusBottom = bottom.minus(r.getEye());
		
		preComputedRCrossA = r.getD().cross(direction);
		Vector tempRCrossA = preComputedRCrossA.copy();
		tempRCrossA.normalize();
		
		//i don't want to think about this case. Eff parallel rays.
		if(r.getD().x == 0.0/0.0 || r.getD().y == 0.0/0.0 || r.getD().z == 0.0/0.0)
		{
			return false;
		}
		
		Vector eyeMinusBottom = r.getEye().minus(bottom);
		//"d" represents the closest the ray will get to the center vector/line of the cylinder.
		//if "d" <= the radius, then there is a hit somewhere, since i'm counting parallel rays as misses
		d = Math.abs(eyeMinusBottom.dot(tempRCrossA));
		if(d <= radius)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Cylinder;
	}
}
