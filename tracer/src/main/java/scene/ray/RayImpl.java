package scene.ray;

import math.Point;
import math.Vector;

public class RayImpl implements Ray {
  private Point eye;
  private Vector d;

  public RayImpl(Vector incomingD, Point incomingEye) {
    d = incomingD;
    eye = incomingEye;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof RayImpl)) {
      return false;
    }
    RayImpl other = (RayImpl) o;
    return other.getD().equals(d) && other.getEye().equals(eye);
  }

  @Override
  public String toString() {
    return "d: " + d + "\neye: " + eye;
  }

  public Vector getD() {
    return d;
  }

  public void setD(Vector d) {
    this.d = d;
  }

  public Point getEye() {
    return eye;
  }

  public void setEye(Point eye) {
    this.eye = eye;
  }

  @Override
  public double getDistance(Point p) {
    Vector vectorToPoint = p.minus(eye);
    return vectorToPoint.cross(d).magnitude();
  }
}
