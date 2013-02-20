package surface.primitives;

import helper.TestsHelper;

import java.util.ArrayList;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import scene.ray.Ray;
import scene.ray.RayImpl;
import util.UtilImpl;
import etc.HitData;

public class Torus_IntegrationTests
{
	@Test
	public void constructor_WithDirectionParallelToSurface_ExpectDirectionOffset()
	{
		//Given
		Point center = new Point(0.0,0.0,0.0);
		Vector direction = new Vector(0.0,0.0,1.0);
		double largeR = 5.0;
		double smallR = 1.0;
		
		//When
		Torus classUnderTest = new Torus(center, direction, largeR, smallR, null, null, null, null, null);
		
		//Then
		Assert.assertTrue(classUnderTest.getDirection().y > 0.0);
	}
	
	@Test
	public void constructor_WithDirectionNotParallelToSurface_ExpectNoDirectionOffset()
	{
		//Given
		Point center = new Point(0.0,0.0,0.0);
		Vector direction = new Vector(0.0,1.0,0.0);
		double largeR = 5.0;
		double smallR = 1.0;
		
		//When
		Torus classUnderTest = new Torus(center, direction, largeR, smallR, null, null, null, null, null);
		
		//Then
		Assert.assertTrue(classUnderTest.getDirection().y == 1.0);
	}
	
	@Test
	public void getHitData_WithMiss_ExpectNoHitData()
	{
		//Given
		Point center = new Point(0.0,0.0,0.0);
		Vector direction = new Vector(0.0,1.0,0.0);
		double largeR = 1.0;
		double smallR = 0.25;
		
		Vector d = new Vector(0.0,1.0,-1.0);
		Point eye = new Point(0.0,0.0,4.0);
		Ray ray = new RayImpl(d, eye);
		
		Torus classUnderTest = new Torus(center, direction, largeR, smallR, null, null, null, null, new UtilImpl());
		
		//When
		ArrayList<HitData> result = classUnderTest.getHitData(ray);
		
		//Then
		Assert.assertEquals(0, result.size());
	}
	
	@Test
	public void getHitData_With4Hits_ExpectHitData()
	{
		//Given
		Point center = new Point(0.0,0.0,0.0);
		Vector direction = new Vector(0.0,1.0,0.0);
		double largeR = 1.0;
		double smallR = 0.25;
		
		Vector d = new Vector(0.0,0.0,-1.0);
		Point eye = new Point(0.0,0.0,4.0);
		Ray ray = new RayImpl(d, eye);
		
		Torus classUnderTest = new Torus(center, direction, largeR, smallR, null, null, null, null, new UtilImpl());
		
		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData hit = new HitData(1.25, classUnderTest, new Vector(0.0,0.0, 1.0), new Point(0.0,0.0,2.75));
		expected.add(hit);
		hit = new HitData(-1.25, classUnderTest, new Vector(0.0,0.0, 1.0), new Point(0.0,0.0,5.25));
		expected.add(hit);
		hit = new HitData(0.75, classUnderTest, new Vector(0.0,0.0, 1.0), new Point(0.0,0.0,3.25));
		expected.add(hit);
		hit = new HitData(-0.75, classUnderTest, new Vector(0.0,0.0, 1.0), new Point(0.0,0.0,4.75));
		expected.add(hit);
		
		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(ray);
		
		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}
	
	@Test
	public void getHitData_With2Hits_ExpectHitData()
	{
		//Given
		Point center = new Point(0.0,1.0,0.0);
		Vector direction = new Vector(0.0,0.0,1.0);
		double largeR = 1.0;
		double smallR = 0.25;
		
		Vector d = new Vector(0.0,0.0,-1.0);
		Point eye = new Point(0.0,0.0,4.0);
		Ray ray = new RayImpl(d, eye);
		
		Torus classUnderTest = new Torus(center, direction, largeR, smallR, null, null, null, null, new UtilImpl());
		
		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData hit = new HitData(4.272963481788047, classUnderTest, new Vector(0.0,-0.021861131371787714, -0.9997610169111143), new Point(0.0,0.0,-0.272963481788046));
		expected.add(hit);
		hit = new HitData(3.727036319405667, classUnderTest, new Vector(0.0,-0.021861129007357066, 0.9997610169628158), new Point(0.0,0.0,0.27296368059433335));
		expected.add(hit);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(ray);
		
		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}
}
