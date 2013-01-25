package math.simplex;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import scene.ray.Ray;

public class Simplex_UnitTests
{
	@Test
	public void getNormal_WithBarycentricSimplexAndPoints_ExpectNormal()
	{
		//Given
		Point eye = new Point(0.0,0.0,4.0);
		Vector d = new Vector(0.0,0.0,-1.0);
		Ray r = new Ray(d,eye);
		
		Point a = new Point(-2.0,-2.0,0.0);
		Point b = new Point(0.0,2.0,0.0);
		Point c = new Point(2.0,-2.0,0.0);
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		Point hit = new Point(0.0,0.0,0.0);
		Vector expected = new Vector(0.0,0.0,1.0);
		
		//When
		Vector actual = classUnderTest.getNormal(hit, r);
		
		//Then
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getNormal_WithBarycentricSimplexAndRayOnOtherSide_ExpectNormal()
	{
		//Given
		Point eye = new Point(0.0,0.0,-4.0);
		Vector d = new Vector(0.0,0.0,1.0);
		Ray r = new Ray(d,eye);
		
		Point a = new Point(-2.0,-2.0,0.0);
		Point b = new Point(0.0,2.0,0.0);
		Point c = new Point(2.0,-2.0,0.0);
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		Point hit = new Point(0.0,0.0,0.0);
		Vector expected = new Vector(0.0,0.0,-1.0);
		
		//When
		Vector actual = classUnderTest.getNormal(hit, r);
		
		//Then
		Assert.assertEquals(expected, actual);
	}
}
