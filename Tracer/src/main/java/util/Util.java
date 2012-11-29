package util;
import etc.Color;
import etc.HitData;
import etc.RaytracerException;
import scene.Scene;
import scene.pixel.ScenePixel;
import scene.ray.Ray;
import surface.Surface;
import math.Point;
import math.Vector;
public interface Util
{
	public double[] sort(double[] incomingTArray);
	public Point getP(double smallestT, Ray r);
	public Ray getRefractedRay(Vector v, double originalN, double newN, HitData hitData);
	public Color clamp(Color c);
	public Ray getReflectedRay(Ray r, Point p, Vector n);
	public Color getReflectedColor(Ray r, int currentDepth, HitData hit, Surface currSurface, ScenePixel pixel) throws RaytracerException;
	public Color getRefractedColor(Ray r, int currentDepth, HitData hit, Surface currSurface, ScenePixel pixel) throws RaytracerException;
	public boolean isInShadow(Scene scene, Point light, HitData hitData) throws RaytracerException;
}
