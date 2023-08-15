package math.simplex.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import math.Point;
import math.simplex.ParametricSimplexImpl;

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
		assertEquals(result.getA(), a);
		assertEquals(result.getB(), b);
		assertEquals(result.getC(), c);
	}
}
