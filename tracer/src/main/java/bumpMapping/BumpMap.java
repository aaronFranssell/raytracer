package bumpMapping;

import math.Point;
import math.Vector;
import scene.ray.Ray;

public interface BumpMap {
  public abstract Vector getBump(Ray r, Point p, Vector normal);
}
