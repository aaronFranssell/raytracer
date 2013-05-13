package surface.bezier;

import java.util.ArrayList;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import scene.ray.Ray;
import scene.ray.RayImpl;
import etc.HitData;
import etc.RaytracerException;

public class BezierSurface_IntegrationTests
{
	@Test
	public void deCasteljau_WithQuadratic_ExpectInterpolatedPoint()
	{
		//Given
		Point p0 = new Point(-1.0, 0.0, 0.0);
		Point p1 = new Point(0.0, 2.0, 0.0);
		Point p2 = new Point(1.0, 0.0, 0.0);
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(p0);
		points.add(p1);
		points.add(p2);
		BezierSurface classUnderTest = new BezierSurface();
		Point expected = new Point(-.4,.84,0.0);
		
		//When
		Point actual = classUnderTest.deCasteljau(0.3, points);
		
		//Then
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void deCasteljau_WithCubic_ExpectInterpolatedPoint()
	{
		//Given
		Point p0 = new Point(-2.0, -2.0, -2.0);
		Point p1 = new Point(0.0, 0.0, 0.0);
		Point p2 = new Point(2.0, -2.0, 2.0);
		Point p3 = new Point(4.0, 2.0, 4.0);
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(p0);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		BezierSurface classUnderTest = new BezierSurface();
		Point expected = new Point(1,-.75,1);
		
		//When
		Point actual = classUnderTest.deCasteljau(0.5, points);
		
		//Then
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getHitData_WithRectangularSurface_ExpectHit() throws RaytracerException
	{
		//Given
		Vector d = new Vector(0.0,0.0,-1.0);
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		ArrayList<ArrayList<Point>> points = getRectangularSurface();
		BezierSurface classUnderTest = new BezierSurface(points);
		HitData expected = new HitData(4.0, classUnderTest, new Vector(0.0,0.0,1.0), new Point(0.0,0.0,0.0));
		
		//When
		ArrayList<HitData> result = classUnderTest.getHitData(r);
		
		//Then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expected, result.get(0));
	}
	
	@Test
	public void getHitData_WithRectangularSurfaceShootTopRight_ExpectHit() throws RaytracerException
	{
		//Given
		Vector d = new Vector(1.0,1.0,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,2.0);
		Ray r = new RayImpl(d, eye);

		ArrayList<ArrayList<Point>> points = getRectangularSurface();
		BezierSurface classUnderTest = new BezierSurface(points);
		HitData expected = new HitData(3.464101615137754, classUnderTest, new Vector(0.0,0.0,1.0), new Point(2.0, 2.0, 0.0));
		
		//When
		ArrayList<HitData> result = classUnderTest.getHitData(r);
		
		//Then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expected, result.get(0));
	}
	
	@Test
	public void getHitData_WithCurvedSurface_ExpectHit() throws RaytracerException
	{
		//Given
		Vector d = new Vector(0.0,0.3,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		ArrayList<ArrayList<Point>> points = getCurvedSurface();
		BezierSurface classUnderTest = new BezierSurface(points);
		HitData expected = new HitData(3.464101615137754, classUnderTest, new Vector(0.0,0.0,1.0), new Point(2.0, 2.0, 0.0));
		
		//When
		ArrayList<HitData> result = classUnderTest.getHitData(r);
		
		//Then
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expected, result.get(0));
	}
	
	private static ArrayList<ArrayList<Point>> getCurvedSurface()
	{
		ArrayList<Point> row1 = new ArrayList<Point>();
		row1.add(new Point(-2.0,0.0,-2.0));
		row1.add(new Point(0.0,1.0,-2.0));
		row1.add(new Point(2.0,0.0,-2.0));
		
		ArrayList<Point> row2 = new ArrayList<Point>();
		row2.add(new Point(-2.0,2.0,0.0));
		row2.add(new Point(0.0,3.0,0.0));
		row2.add(new Point(2.0,2.0,0.0));
		
		ArrayList<Point> row3 = new ArrayList<Point>();
		row3.add(new Point(-2.0,0.0,2.0));
		row3.add(new Point(0.0,0.0,2.0));
		row3.add(new Point(2.0,0.0,2.0));
		
		ArrayList<ArrayList<Point>> rows = new ArrayList<ArrayList<Point>>();
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		
		return rows;
	}

	private ArrayList<ArrayList<Point>> getRectangularSurface()
	{
		ArrayList<Point> topPoints = new ArrayList<Point>();
		topPoints.add(new Point(-3.0,3.0,0.0));
		topPoints.add(new Point(0.0,3.0,0.0));
		topPoints.add(new Point(3.0,3.0,0.0));
		ArrayList<Point> middlePoints = new ArrayList<Point>();
		middlePoints.add(new Point(-3.0,0.0,0.0));
		middlePoints.add(new Point(0.0,0.0,0.0));
		middlePoints.add(new Point(3.0,0.0,0.0));
		ArrayList<Point> bottomPoints = new ArrayList<Point>();
		bottomPoints.add(new Point(-3.0,-3.0,0.0));
		bottomPoints.add(new Point(0.0,-3.0,0.0));
		bottomPoints.add(new Point(3.0,-3.0,0.0));
		ArrayList<ArrayList<Point>> points = new ArrayList<ArrayList<Point>>();
		points.add(topPoints);
		points.add(middlePoints);
		points.add(bottomPoints);
		return points;
	}
}
