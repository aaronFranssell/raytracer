package surface.primitives;

import helper.TestsHelper;

import java.util.ArrayList;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import scene.ray.Ray;
import util.UtilImpl;
import etc.HitData;

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
		Ray ray = new Ray(d, eye);

		Sphere classUnderTest = new Sphere(center, radius, null,null,null, null, null, ops);
		
		//When
		ArrayList<HitData> list = classUnderTest.getHitData(ray);
		
		//Then
		Assert.assertEquals(0, list.size());
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
		Ray ray = new Ray(d, eye);

		Sphere classUnderTest = new Sphere(center, radius, null, null, null, null, null, ops);
		
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
