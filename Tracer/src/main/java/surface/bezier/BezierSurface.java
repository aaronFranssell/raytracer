package surface.bezier;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import math.simplex.Simplex;
import math.simplex.factory.ParametricSimplexFactoryImpl;
import math.simplex.factory.SimplexFactory;
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
	
	//for unit testing
	public BezierSurface()
	{}

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
		//return boundingSolidHits;
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
		//subdivide the u/v unit square until the points on the surface are relatively close together...
		double[] UVArray = getUV(r, LOWER_LIMIT, UPPER_LIMIT, LOWER_LIMIT, UPPER_LIMIT);
		if(UVArray == null)
		{
			return new HitData();
		}
		double u = UVArray[0];
		double v = UVArray[1];
		Point surfacePoint = getSurfacePoint(u, v);
		if(r.getDistance(surfacePoint) < -Constants.POSITIVE_ZERO)
		{
			return new HitData();
		}
		Simplex simplex = getSimplex(u,v,surfacePoint, r);
		return simplex.getHitData(r, this);
	}
	
	private double[] getUV(Ray r, double lowerU, double upperU, double lowerV, double upperV)
	{
		//subdivide the unit square formed by u/v coordinates...
		/*
		0,0     0,v
		|-------|
		| a | b |
		|-------|
		| c | d |
		|-------|
		u,0     u,v
		*/
		if(!rayHitSquare(r,lowerU, upperU, lowerV, upperV))
		{
			return null;
		}
		if(squareIsPoint(lowerU,upperU, lowerV, upperV))
		{
			return new double[] {lowerU, lowerV};
		}
		double[] aResult = getUV(r, lowerU, (lowerU + upperU)/2, lowerV, (lowerV + upperV)/2);
		if(aResult != null) return aResult;
		double[] bResult = getUV(r, lowerU, (lowerU + upperU)/2, (lowerV + upperV)/2, upperV);
		if(bResult != null) return bResult;
		double[] cResult = getUV(r, (lowerU + upperU)/2, upperU, lowerV, (lowerV + upperV)/2);
		if(cResult != null) return cResult;
		double[] dResult = getUV(r, (lowerU + upperU)/2, upperU, (lowerV + upperV)/2, upperV);
		return dResult;
	}
	
	private boolean squareIsPoint(double lowerU, double upperU, double lowerV, double upperV)
	{
		Point upperLeft = getSurfacePoint(lowerU, lowerV);
		Point bottomRight = getSurfacePoint(upperU, upperV);
		Vector distance = upperLeft.minus(bottomRight);
		return distance.magnitude() < Constants.POSITIVE_ZERO;
	}
	
	private boolean rayHitSquare(Ray r, double lowerU, double upperU, double lowerV, double upperV)
	{
		Point topLeft = getSurfacePoint(lowerU, lowerV);
		Point topRight = getSurfacePoint(lowerU, upperV);
		Point bottomLeft = getSurfacePoint(upperU, lowerV);
		Point bottomRight = getSurfacePoint(upperU, upperV);
		Simplex left = simplexFactory.getSimplex(topLeft, bottomLeft, bottomRight);
		Simplex right = simplexFactory.getSimplex(topLeft, topRight, bottomRight);
		return (left.isHit(r) || right.isHit(r));
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

	private Point getSurfacePoint(double u, double v)
	{
		ArrayList<Point> ithPoints = new ArrayList<Point>();
		for(ArrayList<Point> row : points)
		{
			Point result = deCasteljau(v, row);
			ithPoints.add(result);
		}
		return deCasteljau(u, ithPoints);
	}

	protected Point deCasteljau(double t, ArrayList<Point> points)
	{
		if(points.size() == 1)
		{
			return points.get(0);
		}
		ArrayList<Point> leftPoints = new ArrayList<Point>(points.subList(0, points.size() - 1));
		Point leftSide = deCasteljau(t, leftPoints);
		leftSide = leftSide.scaledReturn(1-t);
		ArrayList<Point> rightPoints = new ArrayList<Point>(points.subList(1, points.size()));
		Point rightSide = deCasteljau(t, rightPoints);
		rightSide = rightSide.scaledReturn(t);
		Point result = leftSide.add(rightSide);
		return result;
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
