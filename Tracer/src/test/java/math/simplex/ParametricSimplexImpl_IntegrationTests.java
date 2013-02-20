package math.simplex;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import scene.ray.Ray;
import scene.ray.RayImpl;

public class ParametricSimplexImpl_IntegrationTests
{
	@Test
	public void getT_WithMissS1LessThanZero_ExpectNan()
	{
		//Given
		Point a = new Point(-1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(1.0,0.0,0.0);
		
		Vector d = (new Vector(1.0,1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,-4.0);
		Ray r = new RayImpl(d, eye);
		
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithMissS1AndT1GreaterThan1_ExpectNan()
	{
		//Given
		Point a = new Point(-1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(1.0,0.0,0.0);
		
		Vector d = (new Vector(-1.0,-1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,-4.0);
		Ray r = new RayImpl(d, eye);
		
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithMissT1LessThan0_ExpectNan()
	{
		//Given
		Point a = new Point(-1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(1.0,0.0,0.0);
		
		Vector d = (new Vector(0.0,-1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,-4.0);
		Ray r = new RayImpl(d, eye);
		
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		//When
		double actual = classUnderTest.getT(r);
		
		//Then
		Assert.assertTrue(Double.isNaN(actual));
	}
}
