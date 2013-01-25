package math.simplex;

import junit.framework.Assert;

import math.Point;
import math.Vector;
import math.simplex.BarycentricSimplexImpl;

import org.junit.Test;

import scene.ray.Ray;
import util.Constants;
import util.UtilImpl;

public class BarycentricSimplex_IntegrationTests
{
	@Test
	public void getT_WithGammaMissGreaterThanZero_ExpectNan()
	{
		//Given
		Point a = new Point(-1.0, 1.0, 0.0);
		Point b = new Point(-1.0, -1.0, 0.0);
		Point c = new Point(1.0, -1.0, 0.0);
		
		Vector d = (new Vector(1.0,1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new Ray(d,eye);
		
		BarycentricSimplexImpl classUnderTest = new BarycentricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithGammaMissLessThanZero_ExpectNan()
	{
		//Given
		Point a = new Point(-1.0, 1.0, 0.0);
		Point b = new Point(-1.0, -1.0, 0.0);
		Point c = new Point(1.0, -1.0, 0.0);
		
		Vector d = (new Vector(1.0,1.0,1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new Ray(d,eye);
		
		BarycentricSimplexImpl classUnderTest = new BarycentricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithBetaMissLessThanZero_ExpectNan()
	{
		//Given
		Point a = new Point(-1.0, 1.0, 0.0);
		Point b = new Point(-1.0, -1.0, 0.0);
		Point c = new Point(1.0, -1.0, 0.0);
		
		Vector d = (new Vector(0.0,1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new Ray(d,eye);
		
		BarycentricSimplexImpl classUnderTest = new BarycentricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithBetaMissGreaterThanZero_ExpectNan()
	{
		//Given
		Point a = new Point(-1.0, 1.0, 0.0);
		Point b = new Point(-1.0, -1.0, 0.0);
		Point c = new Point(1.0, -1.0, 0.0);
		
		Vector d = (new Vector(0.0,-1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new Ray(d,eye);
		
		BarycentricSimplexImpl classUnderTest = new BarycentricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithHit_ExpectT()
	{
		//Given
		Point a = new Point(-1.0, 1.0, 0.0);
		Point b = new Point(-1.0, -1.0, 0.0);
		Point c = new Point(1.0, -1.0, 0.0);
		
		Vector d = (new Vector(0.0,0.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new Ray(d,eye);
		
		BarycentricSimplexImpl classUnderTest = new BarycentricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(UtilImpl.doubleEqual(4.0, actual, Constants.POSITIVE_ZERO));
	}
}
