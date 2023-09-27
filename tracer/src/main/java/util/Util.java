package util;

import etc.Color;
import etc.HitData;
import etc.RaytracerException;
import java.io.IOException;
import java.util.ArrayList;
import math.Point;
import math.Vector;
import scene.Scene;
import scene.pixel.ScenePixel;
import scene.ray.Ray;
import surface.Surface;

public interface Util {
	public double[] sort(double[] incomingTArray);

	public Point getP(double smallestT, Ray r);

	public Ray getRefractedRay(Vector v, double originalN, double newN, HitData hitData);

	public Color clamp(Color c);

	public Ray getReflectedRay(Ray r, Point p, Vector n);

	public Color getReflectedColor(
			Ray r, int currentDepth, HitData hit, Surface currSurface, ScenePixel pixel)
			throws RaytracerException;

	public Color getRefractedColor(
			Ray r, int currentDepth, HitData hit, Surface currSurface, ScenePixel pixel)
			throws RaytracerException;

	public boolean isInShadow(Scene scene, Point light, HitData hitData) throws RaytracerException;

	public Color getColorLambertian(
			Color cR, Color cA, Color cL, Vector n, Point light, Point p, boolean inShadow);

	public Color getColorPhong(
			Color cR,
			Color cA,
			Color cL,
			Vector n,
			Point light,
			Point eye,
			int exponent,
			Point p,
			boolean inShadow);

	public double[] solveQuadratic(double a, double b, double c);

	public boolean hasHits(double[] hits);

	public int[][][] readImage(String filePath) throws IOException;

	public double[] solveQuartic(double A, double B, double C, double D, double E);

	public double getGreaterValue(double val1, double val2);

	public double getTOnPlane(Ray r, Point pointOnPlane, Vector normal);

	public ArrayList<String> readTextFile(String filePath) throws IOException;

	public int[] getCircleUVImageMapping(Point p, Point center, double radius, int w, int h);
}
