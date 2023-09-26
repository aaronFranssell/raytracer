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

public class Triangle_IntegrationTests
{
	@Test
	public void getHitData_WithMiss_ExpectNoHits()
	{
		
		Point a = new Point(-1.0,1.0,-1.0);
		Point b = new Point(-1.0,-1.0,1.0);
		Point c = new Point(1.0,-1.0,0.0);
		
		Vector d = (new Vector(1.0,1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);
		
		Triangle classUnderTest = new Triangle(null, null, null, a, b, c, null);
		
		
		ArrayList<HitData> actual = classUnderTest.getHitData(r);
		
		
		assertEquals(0, actual.size());
	}
	
	@Test
	public void getHitData_WithHit_ExpectHitData()
	{
		
		Point a = new Point(-1.0,1.0,-1.0);
		Point b = new Point(-1.0,-1.0,1.0);
		Point c = new Point(1.0,-1.0,0.0);
		
		Vector d = (new Vector(0.0,0.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);
		
		Triangle classUnderTest = new Triangle(null, null, null, a, b, c, null);

		HitData hit = new HitData(4.5, classUnderTest, new Vector(0.3333333333333333, 0.6666666666666666, 0.6666666666666666), new Point(0.0,0.0,-0.5));
		ArrayList<HitData> expected = new ArrayList<HitData>();
		expected.add(hit);
		
		
		ArrayList<HitData> actual = classUnderTest.getHitData(r);
		
		
		TestsHelper.arrayListSubsets(expected, actual);
	}
}
