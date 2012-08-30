package util;
import scene.ray.Ray;
import math.Point;
public interface Util
{
	public double[] sort(double[] incomingTArray);
	public Point getP(double smallestT, Ray r);
}
