package math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import util.Constants;

public class Point_UnitTests
{
	@Test
	public void scaleReturn_WithScale_ExpectScaledPoint()
	{
		//Given
		Point classUnderTest = new Point(3.0, 5.0, 7.0);
		double scale = 3.0;
		
		//When
		Point result = classUnderTest.scaledReturn(scale);
		
		//Then
		assertEquals(9.0, result.x, Constants.POSITIVE_ZERO);
		assertEquals(15.0, result.y, Constants.POSITIVE_ZERO);
		assertEquals(21.0, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void minus_WithOtherPoint_ExpectSubtractedPoint()
	{
		//Given
		Point classUnderTest = new Point(3.0,5.0,7.0);
		Point subtractPoint = new Point(5.0, -3.0, 7.0);
		
		//When
		Vector result = classUnderTest.minus(subtractPoint);
		
		//Then
		assertEquals(-2.0, result.x, Constants.POSITIVE_ZERO);
		assertEquals(8.0, result.y, Constants.POSITIVE_ZERO);
		assertEquals(0.0, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void add_WithVector_ExpectPoint()
	{
		//Given
		Point classUnderTest = new Point(1.0,3.0,5.0);
		Vector v = new Vector(-7.0, 5.0, 11.0);
		Point expected = new Point(-6.0,8.0,16.0);
		
		//When
		Point actual = classUnderTest.add(v);
		
		//Then
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void add_WithPoint_ExpectPoint()
	{
		//Given
		Point classUnderTest = new Point(3.0, 7.0, -3.0);
		Point toAdd = new Point(7.0, 11.0, 17.0);
		Point expected = new Point(10.0, 18.0, 14.0);
		
		//When
		Point actual = classUnderTest.add(toAdd);
		
		//Then
		assertEquals(expected, actual);
	}
}
