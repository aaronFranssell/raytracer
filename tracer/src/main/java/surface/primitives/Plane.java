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

public class Plane extends Surface {
  private Vector normal;
  private Point pointOnPlane;

  public Plane(
      Vector incomingNormal,
      Point incomingPoint,
      Color incomingCR,
      Color incomingCL,
      Color incomingCA,
      Effects incomingEffects,
      Util incomingOps) {
    normal = incomingNormal.normalizeReturn();
    pointOnPlane = incomingPoint;
    cR = incomingCR;
    cL = incomingCA;
    cA = incomingCA;
    effects = incomingEffects;
    ops = incomingOps;
  }

  public Plane(
      Vector incomingN,
      Point incomingPoint,
      Color incomingCR,
      Color incomingCL,
      Color incomingCA,
      Effects incomingEffects) {
    this(
        incomingN,
        incomingPoint,
        incomingCR,
        incomingCL,
        incomingCA,
        incomingEffects,
        new UtilImpl());
  }

  private double calcT(Ray r) {
    return ops.getTOnPlane(r, pointOnPlane, normal);
  }

  protected Vector getNormal(Point p, Ray r) {
    return normal;
  }

  public ArrayList<HitData> getHitData(Ray r) {
    ArrayList<HitData> retHitData = new ArrayList<HitData>();
    double smallestT = calcT(r);
    if (Double.isNaN(smallestT)) {
      return retHitData;
    }
    Point p = ops.getP(smallestT, r);
    HitData hit = new HitData(smallestT, this, normal, p);
    retHitData.add(hit);
    return retHitData;
  }

  @Override
  public SurfaceType getType() {
    return SurfaceType.Plane;
  }
}
