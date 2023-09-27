package surface.primitives;

import etc.Color;
import etc.Effects;
import etc.HitData;
import java.util.ArrayList;
import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.Surface;
import util.Util;
import util.UtilImpl;

public class Cylinder extends Surface {
	private Point bottom;
	private double radius;
	private double height;
	private Vector direction;
	private Point top;

	public Cylinder(
			Point incomingBottom,
			double incomingRadius,
			Color incomingCR,
			Color incomingCA,
			Color incomingCL,
			double incomingHeight,
			Vector incomingDirection,
			Effects incomingEffects,
			Util incomingOps) {
		bottom = incomingBottom;
		radius = incomingRadius;
		cR = incomingCR;
		cA = incomingCA;
		cL = incomingCL;
		height = incomingHeight;
		direction = incomingDirection.normalizeReturn();
		effects = incomingEffects;
		top = new Point(direction.x * height, direction.y * height, direction.z * height);
		// Displace the top point by the bottom
		top.x += bottom.x;
		top.y += bottom.y;
		top.z += bottom.z;
		ops = incomingOps;
	}

	public Cylinder(
			Point incomingBottom,
			double incomingRadius,
			Color incomingCR,
			Color incomingCA,
			Color incomingCL,
			double incomingHeight,
			Vector incomingDirection,
			Effects incomingEffects) {
		this(
				incomingBottom,
				incomingRadius,
				incomingCR,
				incomingCA,
				incomingCL,
				incomingHeight,
				incomingDirection,
				incomingEffects,
				new UtilImpl());
	}

	protected Vector getNormal(Point hitPoint, Ray r) {
		/* I derived this myself so may be buggy :-/ There is a right triangle formed between 3 points: the cylinder bottom, the hit point on the
		 * cylinder, and the point on the centerline of the cylinder that is closest to the hitpoint. The normal will be the vector that is the hit
		 * point minus the close centerline point. To find the center-line point, find the distance between the close point and bottom of the cylinder
		 * point. Once that distance is found, scale the direction vector by that amount, and then displace that vector by the bottom of the cylinder
		 * point. If the ray strikes the cylinder on the inside, then the normal needs to point back towards the eye. */
		Vector hitPointMinusBottom = hitPoint.minus(bottom);
		// just the pythagorean theorem...
		double distanceToClosePoint = Math.sqrt(
				hitPointMinusBottom.magnitude() * hitPointMinusBottom.magnitude() - radius * radius);

		Vector vecToHitPoint = hitPointMinusBottom.normalizeReturn();

		// this holds the cosine of vecToHitPoint.dot(direction)
		// if cosDirection > 0, then the hit point happened in the direction of the direction vector.
		// if cosDirection < 0, then the hit point happened in the opposite direction of the direction
		// vector
		double cosDirection = vecToHitPoint.dot(direction);
		distanceToClosePoint = cosDirection < 0 ? distanceToClosePoint * -1 : distanceToClosePoint;

		Vector pointOnLine = new Vector(
				direction.x * distanceToClosePoint,
				direction.y * distanceToClosePoint,
				direction.z * distanceToClosePoint);
		// displace the scaled vector by the center, and that is the close point
		pointOnLine.x += bottom.x;
		pointOnLine.y += bottom.y;
		pointOnLine.z += bottom.z;

		// the normal is now the difference between the hit point and the close point
		Vector normal = new Vector(
				hitPoint.x - pointOnLine.x, hitPoint.y - pointOnLine.y, hitPoint.z - pointOnLine.z);
		normal = normal.normalizeReturn();
		if (normal.dot(r.getD()) > 0) {
			normal = normal.scaleReturn(-1.0);
		}
		return normal;
	}

	private boolean onLimitedCylinder(double t, Ray r) {
		Point pointOnCylinder = ops.getP(t, r);
		// calculate if hit point is in the height of the cylinder specified
		// take the dot product of (hit point - cylinder bottom point).dot(direction vector)
		// if this is > 0, then the hit point lies in front of the bottom.
		Vector bottomToP = pointOnCylinder.minus(bottom);
		bottomToP = bottomToP.normalizeReturn();
		// the hit point is below the bottom of the cylinder, therefore a miss...
		if (bottomToP.dot(direction) < 0) {
			return false;
		}

		// Now do the same as above, except in reverse.
		Vector topToP = pointOnCylinder.minus(top);
		topToP = topToP.normalizeReturn();
		// now need to reverse the direction vector, in order to reverse the direction of the cylinder
		Vector reversedDirection = new Vector(direction.x * -1, direction.y * -1, direction.z * -1);
		if (topToP.dot(reversedDirection) < 0) {
			return false;
		}
		return true;
	}

	public ArrayList<HitData> getHitData(Ray r) {
		ArrayList<Double> hitTs = getHitTs(r);

		ArrayList<HitData> retData = new ArrayList<HitData>();
		for (double t : hitTs) {
			Point hitPoint = ops.getP(t, r);
			Vector normal = getNormal(hitPoint, r);
			HitData hit = new HitData(t, this, normal, hitPoint);
			retData.add(hit);
		}
		return retData;
	}

	public ArrayList<Double> getHitTs(Ray r) {
		Vector eyeMinusBottom = r.getEye().minus(bottom);
		Vector rayCrossDirection = r.getD().cross(direction);

		double rayCrossDirectionMag = rayCrossDirection.magnitude();
		rayCrossDirection = rayCrossDirection.normalizeReturn();

		double distance = Math.abs(eyeMinusBottom.dot(rayCrossDirection));

		boolean isHit = (distance <= radius);
		if (!isHit) {
			return new ArrayList<Double>();
		}

		double numerator = direction.cross(eyeMinusBottom).dot(rayCrossDirection);
		double t = numerator / rayCrossDirectionMag;

		Vector O = rayCrossDirection.cross(direction);
		O = O.normalizeReturn();

		double s = Math.abs(Math.sqrt(radius * radius - distance * distance) / r.getD().dot(O));
		double t1 = t - s;
		double t2 = t + s;
		ArrayList<Double> retTs = new ArrayList<Double>();
		if (onLimitedCylinder(t1, r)) {
			retTs.add(t1);
		}
		if (onLimitedCylinder(t2, r)) {
			retTs.add(t2);
		}
		return retTs;
	}

	@Override
	public SurfaceType getType() {
		return SurfaceType.Cylinder;
	}
}
