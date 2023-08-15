package surface.primitives;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import etc.HitData;
import helper.TestsHelper;
import math.Point;
import math.Vector;
import scene.ray.Ray;
import scene.ray.RayImpl;

public class Plane_IntegrationTests
{
	@Test
	public void getHitData_WithHit_ExpectHitData()
	{
		//Given
		Vector normal = new Vector(0.0,0.0,1.0);
		Point point = new Point(0.0,0.0,0.0);
		
		Plane classUnderTest = new Plane(normal, point, null, null, null, null);
		
		Vector d = new Vector(1.0,0.0,0.2);
		Point e = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, e);
		
		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData expectedHitData = new HitData(-20, classUnderTest, normal, new Point(-20.0, 0.0, 0.0));
		expected.add(expectedHitData);
		
		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);
		
		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}
}
