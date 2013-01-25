package math.simplex.factory;

import junit.framework.Assert;
import math.Point;
import math.simplex.BarycentricSimplexImpl;

import org.junit.Test;

public class BarycentricSimplexFactory_UnitTests
{
	@Test
	public void getSimplex_WithPoints_ExpectSimplex()
	{
		//Given
		Point a = new Point(1.0,0.0,0.0);
		Point b = new Point(0.0,1.0,0.0);
		Point c = new Point(0.0,0.0,1.0);
		
		BarycentricSimplexFactoryImpl classUnderTest = new BarycentricSimplexFactoryImpl();
		
		//When
		BarycentricSimplexImpl result = (BarycentricSimplexImpl) classUnderTest.getSimplex(a, b, c);
		
		//Then
		Assert.assertEquals(result.getA(), a);
		Assert.assertEquals(result.getB(), b);
		Assert.assertEquals(result.getC(), c);
	}
}
