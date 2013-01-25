package math.simplex.factory;

import junit.framework.Assert;
import math.Point;
import math.simplex.ParametricSimplexImpl;

import org.junit.Test;

public class ParametricSimplexFactory_UnitTests
{
	@Test
	public void getSimplex_WithValues_ExpectParametricSimplex()
	{
		//Given
		Point a = new Point(1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(0.0,0.0,1.0);
		
		ParametricSimplexFactoryImpl classUnderTest = new ParametricSimplexFactoryImpl();
		
		//When
		ParametricSimplexImpl result = (ParametricSimplexImpl) classUnderTest.getSimplex(a, b, c);
		
		//Then
		Assert.assertEquals(result.getA(), a);
		Assert.assertEquals(result.getB(), b);
		Assert.assertEquals(result.getC(), c);
	}
}
