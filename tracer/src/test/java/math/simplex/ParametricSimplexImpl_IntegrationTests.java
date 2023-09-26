package math.simplex;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import math.Point;
import math.Vector;
import scene.ray.Ray;
import scene.ray.RayImpl;

public class ParametricSimplexImpl_IntegrationTests
{
	@Test
	public void getT_WithMissS1LessThanZero_ExpectNan()
	{
		
		Point a = new Point(-1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(1.0,0.0,0.0);
		
		Vector d = (new Vector(1.0,1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,-4.0);
		Ray r = new RayImpl(d, eye);
		
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		
		double actual = classUnderTest.getT(r);
		
		
		assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithMissS1AndT1GreaterThan1_ExpectNan()
	{
		
		Point a = new Point(-1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(1.0,0.0,0.0);
		
		Vector d = (new Vector(-1.0,-1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,-4.0);
		Ray r = new RayImpl(d, eye);
		
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		
		double actual = classUnderTest.getT(r);
		
		
		assertTrue(Double.isNaN(actual));
	}
	
	@Test
	public void getT_WithMissT1LessThan0_ExpectNan()
	{
		
		Point a = new Point(-1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(1.0,0.0,0.0);
		
		Vector d = (new Vector(0.0,-1.0,-1.0)).normalizeReturn();
		Point eye = new Point(0.0,0.0,-4.0);
		Ray r = new RayImpl(d, eye);
		
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a,b,c);
		
		
		double actual = classUnderTest.getT(r);
		
		
		assertTrue(Double.isNaN(actual));
	}
}
