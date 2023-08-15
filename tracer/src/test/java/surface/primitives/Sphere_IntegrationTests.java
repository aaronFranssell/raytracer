package surface.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import etc.HitData;
import helper.TestsHelper;
import math.Point;
import math.Vector;
import scene.ray.Ray;
import scene.ray.RayImpl;
import util.UtilImpl;

public class Sphere_IntegrationTests
{
	@Test
	public void getHitData_MissLeft_ExpectNoHits()
	{
		//Given
		Point center = new Point(0.0,0.0,0.0);
		double radius = 1.0;
		UtilImpl ops = new UtilImpl();
		
		Vector d = new Vector(0.0,1.0,-1.0);
		Point eye = new Point(0.0,0.0,4.0);
		Ray ray = new RayImpl(d, eye);

		Sphere classUnderTest = new Sphere(center, radius, null,null,null, null, ops);
		
		//When
		ArrayList<HitData> list = classUnderTest.getHitData(ray);
		
		//Then
		assertEquals(0, list.size());
	}
	
	@Test
	public void getHitData_WithHit_ExpectHitData()
	{
		//Given
		Point center = new Point(1.0,0.0,1.0);
		double radius = 1.0;
		UtilImpl ops = new UtilImpl();
		
		Vector d = new Vector(0.0,0.0,-1.0);
		Point eye = new Point(1.0,0.0,4.0);
		Ray ray = new RayImpl(d, eye);

		Sphere classUnderTest = new Sphere(center, radius, null, null, null, null, ops);
		
		ArrayList<HitData> expectedHitData = new ArrayList<HitData>();
		HitData hit = new HitData(2.0, classUnderTest, new Vector(0.0,0.0,1.0), new Point(1.0,0.0,2.0));
		expectedHitData.add(hit);
		hit = new HitData(4.0, classUnderTest, new Vector(0.0,0.0,-1.0), new Point(1.0,0.0,0.0));
		expectedHitData.add(hit);
		
		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(ray);
		
		//Then
		TestsHelper.arrayListSubsets(expectedHitData, actual);
	}
}
