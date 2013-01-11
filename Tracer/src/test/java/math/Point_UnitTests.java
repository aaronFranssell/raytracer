package math;

import junit.framework.Assert;

import org.junit.Test;

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
		Assert.assertEquals(9.0, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(15.0, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(21.0, result.z, Constants.POSITIVE_ZERO);
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
		Assert.assertEquals(-2.0, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(8.0, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(0.0, result.z, Constants.POSITIVE_ZERO);
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
		Assert.assertTrue(expected.equals(actual));
	}
}
