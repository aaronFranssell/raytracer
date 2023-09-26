package surface.csg.operation;

import etc.HitData;
import etc.RaytracerException;
import org.apache.commons.lang3.NotImplementedException;
import scene.ray.Ray;
import surface.Surface;
import util.Util;
import util.UtilImpl;

public class BoundedBy implements Operation {
  // private Surface parentSurface;
  // private IntervalFactory factory;
  // private Util ops;

  public BoundedBy(Surface incomingParentSurface) {
    this(incomingParentSurface, new IntervalFactoryImpl());
  }

  public BoundedBy(Surface incomingParentSurface, IntervalFactory incomingFactory) {
    this(incomingParentSurface, incomingFactory, new UtilImpl());
  }

  public BoundedBy(
      Surface incomingParentSurface, IntervalFactory incomingFactory, Util incomingOps) {
    // parentSurface = incomingParentSurface;
    // factory = incomingFactory;
    // ops = incomingOps;
  }

  /**
   * The way I am defining a "bounded by" operation is everything between 2 hit points of a solid.
   * So the left side solid will not be shown if it is hit between 2 points of the right side's "t"
   * values. If this is the case, then the closest positive hit point from the right side will be
   * returned as the near hit point. A side effect of this algorithm is solids like planes and
   * triangles will have no effect as a right handed op unless they are part of another CSG object.
   *
   * @throws RaytracerException
   */
  @Override
  public HitData getHitData(HitData leftHitData, HitData rightHitData, Ray ray)
      throws RaytracerException {
    throw new NotImplementedException();
    /*
     * Some of this code may be useful... at some point. Hopefully the ideas will be.

    //If left side isn't hit, then return the miss. If the right side isn't hit, then I can just return the left side hit data.
    if(!leftHitData.isHit() || !rightHitData.isHit())
    {
    	return leftHitData;
    }

    Interval leftSideInterval = factory.getInterval(leftHitData.getHitTs());
    Interval rightSideInterval = factory.getInterval(rightHitData.getHitTs());

    //this is the case where all positive left side ts lie in the right solid. In this case, there is a miss.
    if(rightSideInterval.allInInterval(leftSideInterval))
    {
    	return new HitData();
    }

    //this is the case where some of the left side solid is enclosed by the right side solid
    if(leftSideInterval.isInInterval(rightHitData.getSmallestT()))
    {
    	double[] mergedTs = leftSideInterval.mergeTIntervalsAndSort(rightSideInterval);
    	return new HitData(leftHitData.getSmallestT(), parentSurface, leftHitData.getNormal(), leftHitData.getP(), mergedTs);
    }

    //this is the case when the bounding solid leaves the original solid unaffected
    if(!rightSideInterval.isInInterval(leftHitData.getSmallestT()))
    {
    	return leftHitData;
    }

    //this is the case where the near t of the main solid lies inside of the bounding solid. The far hit point
    //of the bounding solid will be the hit point.
    if(rightSideInterval.isInInterval(leftHitData.getSmallestT()))
    {
    	double farT = rightSideInterval.getNextGreatestInterval(leftHitData.getSmallestT());
    	Point nearHitPoint = ops.getP(farT, ray);
    	Surface rightSurface = rightHitData.getSurface();
    	Vector normal = rightSurface.getNormal(nearHitPoint, ray);
    	double[] mergedTs = leftSideInterval.mergeTIntervalsAndSort(rightSideInterval);
    	//return new HitData object. The hit point will be the far side of the bounding solid. The normal will
    	//be the reversed right hit data's normal.
    	return new HitData(farT, parentSurface, normal.scaleReturn(-1.0), nearHitPoint, mergedTs);
    }
    return leftHitData;*/
  }
}
