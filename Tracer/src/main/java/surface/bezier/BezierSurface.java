package surface.bezier;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import math.simplex.Simplex;
import math.simplex.factory.ParametricSimplexFactoryImpl;
import math.simplex.factory.SimplexFactory;

import org.apache.commons.math3.util.FastMath;

import scene.Scene;
import scene.SceneImpl;
import scene.ray.Ray;
import surface.Surface;
import surface.primitives.Sphere;
import surface.primitives.Triangle;
import util.Constants;
import util.UtilImpl;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;

public class BezierSurface extends Surface
{
	private static final double UPPER_LIMIT = 1.0;
	private static final double LOWER_LIMIT = 0.0;

	private ArrayList<ArrayList<Point>> points;
	private Scene boundingScene;
	private SimplexFactory simplexFactory;

	public BezierSurface(ArrayList<ArrayList<Point>> incomingPoints) throws RaytracerException
	{
		cR = new Color(0.0,0.5,0.0);
		cL = new Color(0.4,0.4,0.4);
		cA = Constants.cA;
		effects = new Effects();
		effects.setLambertian(true);
		ops = new UtilImpl();
		simplexFactory = new ParametricSimplexFactoryImpl();
		points = incomingPoints;
		verifySquareLattice();
		ArrayList<Surface> bounds = getBoundingSolids();
		bounds.addAll(getPointSpheres());
		boundingScene = new SceneImpl(bounds);
	}

	private void verifySquareLattice() throws RaytracerException
	{
		int length = points.size();
		for(ArrayList<Point> row : points)
		{
			if(row.size() != length)
			{
				throw new RaytracerException("Not all rows contain the same number of points!");
			}
		}
	}

	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Bezier;
	}

	@Override
	public ArrayList<HitData> getHitData(Ray r) throws RaytracerException
	{
		ArrayList<HitData> boundingSolidHits = getBoundingSolidHits(r);
		if(boundingSolidHits.size() == 0)
		{
			return new ArrayList<HitData>();
		}
		HitData surfaceHit = getSurfaceHitData(r);
		if(!surfaceHit.isHit())
		{
			return new ArrayList<HitData>();
		}
		ArrayList<HitData> ret = new ArrayList<HitData>();
		ret.add(surfaceHit);
		return ret;
	}
	
	private HitData getSurfaceHitData(Ray r)
	{
		//http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/surface/bspline-de-boor.html
		//pick an arbitrary value for v... do a binary search for u
		double u = getClosestU(0.5, LOWER_LIMIT, UPPER_LIMIT, r);
		double v = getClosestV(u, LOWER_LIMIT, UPPER_LIMIT, r);
		Point surfacePoint = getSurfacePoint(u, v);
		if(r.getDistance(surfacePoint) < Constants.POSITIVE_ZERO)
		{
			return new HitData();
		}
		Simplex simplex = getSimplex(u,v,surfacePoint, r);
		Vector normal = simplex.getNormal(r);
		double t = simplex.getT(r);
		Point p = ops.getP(t, r);
		return new HitData(t, this, normal, p);
	}
	
	private Simplex getSimplex(double u, double v, Point hit, Ray r)
	{
		//eff this dun care about performance yet, just get it to work...
		double uPrime = u + 0.001;
		double vPrime = v + 0.001;
		Point hitUPrime = getSurfacePoint(uPrime, v);
		Point hitVPrime = getSurfacePoint(u, vPrime);
		return simplexFactory.getSimplex(hit, hitUPrime, hitVPrime);
	}
	
	private double getClosestV(double u, double lowerLimit, double upperLimit, Ray r)
	{
		if(FastMath.abs(lowerLimit - upperLimit) < Constants.POSITIVE_ZERO)
		{
			return lowerLimit;
		}
		double lowerPointDistance = r.getDistance(getSurfacePoint(u, lowerLimit));
		double upperPointDistance = r.getDistance(getSurfacePoint(u, upperLimit));
		if(lowerPointDistance < upperPointDistance)
		{
			return getClosestV(u, lowerLimit, (lowerLimit + upperLimit)/2.0, r);
		}
		else
		{
			return getClosestV(u, (lowerLimit + upperLimit)/2.0, upperLimit, r);
		}
	}
	
	private double getClosestU(double v, double lowerLimit, double upperLimit, Ray r)
	{
		if(FastMath.abs(lowerLimit - upperLimit) < Constants.POSITIVE_ZERO)
		{
			return lowerLimit;
		}
		double lowerPointDistance = r.getDistance(getSurfacePoint(lowerLimit, v));
		double upperPointDistance = r.getDistance(getSurfacePoint(upperLimit, v));
		if(lowerPointDistance < upperPointDistance)
		{
			return getClosestU(v, lowerLimit, (lowerLimit + upperLimit)/2.0, r);
		}
		else
		{
			return getClosestU(v, (lowerLimit + upperLimit)/2.0, upperLimit, r);
		}
	}

	private Point getSurfacePoint(double u, double v)
	{
		ArrayList<Point> ithPoints = new ArrayList<Point>();
		for(ArrayList<Point> row : points)
		{
			ithPoints.add(deCasteljau(v, row));
		}
		return deCasteljau(u, ithPoints);
	}

	private Point deCasteljau(double t, ArrayList<Point> points)
	{
		if(points.size() == 1)
		{
			return points.get(0);
		}
		Point leftSide = deCasteljau(t, new ArrayList<Point>(points.subList(0, points.size() - 2))).scaledReturn(1-t);
		Point rightSide = deCasteljau(t, new ArrayList<Point>(points.subList(1, points.size() - 1))).scaledReturn(t);
		return leftSide.add(rightSide);
	}

	private ArrayList<HitData> getBoundingSolidHits(Ray r)	throws RaytracerException
	{
		ArrayList<HitData> ret = new ArrayList<HitData>();
		HitData data = boundingScene.getSmallestPositiveHitDataOrReturnMiss(r);
		if(data.isHit())
		{
			ret.add(data);
		}
		return ret;
	}

	private ArrayList<Surface> getBoundingSolids()
	{
		ArrayList<Surface> solids = new ArrayList<Surface>();
		for(int i = 0; i < points.size() - 1; i++)
		{
			ArrayList<Point> row = points.get(i);
			for(int m = 0; m < row.size() - 1; m++)
			{
				solids.add(new Triangle(cR, cL, cA, row.get(m), row.get(m + 1),	points.get(i + 1).get(m +1), effects));
				solids.add(new Triangle(cR, cL, cA, row.get(m), points.get(i + 1).get(m), points.get(i + 1).get(m +1), effects));
			}
		}
		return solids;
	}

	private ArrayList<Surface> getPointSpheres()
	{
		ArrayList<Surface> solids = new ArrayList<Surface>();
		Point a = points.get(0).get(0);
		Point b = points.get(0).get(1);
		double distance = a.minus(b).magnitude();
		double radius = distance/10.0;
		for(ArrayList<Point> row : points)
		{
			for(Point point : row)
			{
				solids.add(new Sphere(point, radius, new Color(0.7,0.7,0.7), cA, cL, effects));
			}
		}
		return solids;
	}

}
