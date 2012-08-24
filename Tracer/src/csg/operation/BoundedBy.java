package csg.operation;

import math.Point;
import math.Vector;
import primitives.Surface;
import util.Library;
import util.Util;
import util.UtilImpl;
import etc.HitData;
import etc.Ray;
import etc.RaytracerException;

public class BoundedBy implements Operation
{
	private Util ops;
	private Surface parentSurface;
	
	public BoundedBy(Util incomingOps, Surface incomingParentSurface)
	{
		ops = incomingOps;
		parentSurface = incomingParentSurface;
	}
	
	public BoundedBy(Surface incomingParentSurface)
	{
		this(new UtilImpl(), incomingParentSurface);
	}
	
	/**
	 * The way I am defining a "bounded by" operation is everything between 2 hit points of a solid. So the left side
	 * solid will not be shown if it is hit between 2 points of the right side's "t" values. If this is the case, then
	 * the closest positive hit point from the right side will be returned as the near hit point. A side effect of this
     * algorithm is solids like planes and triangles will have no effect as a right handed op unless they are part of 
     * another CSG object.
	 * @throws RaytracerException 
     */
	@Override
	public HitData getHitData(HitData leftHitData, HitData rightHitData, Ray ray) throws RaytracerException
	{
		//If left side isn't hit, then return the miss. If the right side isn't hit, then I can just return the miss left side.
		if(!leftHitData.isHit() || !rightHitData.isHit())
		{
			return leftHitData;
		}

		Interval leftSideInterval = new Interval(leftHitData.getHitTs(),ops);
		Interval rightSideInterval = new Interval(rightHitData.getHitTs(), ops);

		//this is the case where all positive left side ts lie in the right solid. In this case, there is a miss.
		//t arrays are sorted on creation in HitData, so i can just refer to smallestTIndex + 1 for the next smallest t
		if(rightSideInterval.allInInterval(leftHitData.getPositiveHitTs()))
		{
			return new HitData();
		}

		//this is the case when the bounding solid leaves the original solid unaffected
		if(!rightSideInterval.isInInterval(leftHitData.getSmallestT()))
		{
			return leftHitData;
		}

		//this is the case where the near face of the right solid lies inside of the left solid surface
		if(leftSideInterval.isInInterval(rightHitData.getSmallestT()))
		{
			double nearT = leftSideInterval.getNearInterval(rightHitData.getSmallestT());
			Point nearHitPoint = Library.getP(nearT, ray);
			Surface rightSurface = rightHitData.getSurface();
			Vector normal = rightSurface.getNormal(nearHitPoint, ray);
			double[] mergedTs = leftSideInterval.mergeTIntervals(rightSideInterval);
			//return new HitData object. The hit point will be the far side of the bounding solid. The normal will
			//be the reversed right hit data's normal.
			return new HitData(nearT, parentSurface, normal.scaleReturn(-1.0), nearHitPoint, mergedTs, ops);
		}

		//this is the case where the near t of the main solid lies inside of the bounding solid. The far hit point
		//of the bounding solid will be the hit point.
		if(rightSideInterval.isInInterval(leftHitData.getSmallestT()))
		{
			double farT = rightSideInterval.getNextGreatestInterval(leftHitData.getSmallestT());
			Point nearHitPoint = Library.getP(farT, ray);
			Surface rightSurface = rightHitData.getSurface();
			Vector normal = rightSurface.getNormal(nearHitPoint, ray);
			double[] mergedTs = leftSideInterval.mergeTIntervals(rightSideInterval);
			//return new HitData object. The hit point will be the far side of the bounding solid. The normal will
			//be the reversed right hit data's normal.
			return new HitData(farT, parentSurface, normal.scaleReturn(-1.0), nearHitPoint, mergedTs, ops);
		}
		return leftHitData;
	}

}
