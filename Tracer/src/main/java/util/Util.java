package util;
import etc.Color;
import etc.HitData;
import scene.ray.Ray;
import math.Point;
import math.Vector;
public interface Util
{
	public double[] sort(double[] incomingTArray);
	public Point getP(double smallestT, Ray r);
	public Ray getRefractedRay(Vector v, double originalN, double newN, HitData hitData);
	Color clamp(Color c);
}
