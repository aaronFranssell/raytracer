package surface.primitives;

import helper.TestsHelper;

import java.util.ArrayList;

import junit.framework.Assert;

import math.Point;
import math.Vector;

import org.junit.Test;

import etc.HitData;

import scene.ray.Ray;

public class Plane_IntegrationTests
{
	@Test
	public void getHitData_WithParellelRayToPlane_ExpectNoHitData()
	{
		//Given
		Vector normal = new Vector(0.0,0.0,1.0);
		Point point = new Point(0.0,0.0,0.0);
		
		Plane classUnderTest = new Plane(normal, point, null, null, null, null);
		
		Vector d = new Vector(1.0,0.0,0.0);
		Point e = new Point(0.0,0.0,4.0);
		Ray r = new Ray(d, e);
		
		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);
		
		//Then
		Assert.assertEquals(0, actual.size());
	}
	
	@Test
	public void getHitData_WithHit_ExpectHitData()
	{
		//Given
		Vector normal = new Vector(0.0,0.0,1.0);
		Point point = new Point(0.0,0.0,0.0);
		
		Plane classUnderTest = new Plane(normal, point, null, null, null, null);
		
		Vector d = new Vector(1.0,0.0,0.2);
		Point e = new Point(0.0,0.0,4.0);
		Ray r = new Ray(d, e);
		
		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData expectedHitData = new HitData(-20, classUnderTest, normal, new Point(-20.0, 0.0, 0.0));
		expected.add(expectedHitData);
		
		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);
		
		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}
}
