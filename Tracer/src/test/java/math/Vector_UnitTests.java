package math;

import org.junit.Assert;

import org.junit.Test;

import util.Constants;

public class Vector_UnitTests
{
	@Test
	public void cross_With2Vectors_ExpectCrossProduct()
	{
		//Given
		Vector classUnderTest = new Vector(3.0, -6.111, 7.222);
		Vector other = new Vector(-2.1, 5.1, 44.0);
		Vector answer = new Vector(-305.7162, -147.1662, 2.466899999999999);
		
		//When
		Vector result = classUnderTest.cross(other);
		
		//Then
		Assert.assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void dot_WithVectors_ExpectDotProduct()
	{
		//Given
		Vector classUnderTest = new Vector(3.0, -5.0, 11.0);
		Vector other = new Vector(-2.1, 5.1, -7.0);
		
		//When
		double result = classUnderTest.dot(other);
		
		//Then
		Assert.assertEquals(-108.8, result, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void magnitude_WithVector_ExpectVectorMagnitude()
	{
		//Given
		Vector classUnderTest = new Vector(-3.0, 4.0, 5.0);
		
		//When
		double result = classUnderTest.magnitude();
		
		//Then
		Assert.assertEquals(7.07106781187, result, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void scaleReturn_WithVector_ExpectScaledVector()
	{
		//Given
		Vector classUnderTest = new Vector(3.0,5.0,7.0);
		double scale = 3.0;
		
		//When
		Vector result = classUnderTest.scaleReturn(scale);
		
		//Then
		Assert.assertEquals(9.0, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(15.0, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(21.0, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void normalizeReturn_WithVector_ExpectUnitVector()
	{
		//Given
		Vector vec = new Vector(3.0, 4.5, 8.111);
		Vector answer = new Vector(0.30773144569001576, 0.46159716853502364, 0.8320032519972393);
		
		//When
		Vector result = vec.normalizeReturn();
		
		//Then
		Assert.assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void add_WithVectors_ExpectVector()
	{
		//Given
		Vector classUnderTest = new Vector(3.0, 5.0, 11.0);
		Vector otherVector = new Vector(7.0, 13.0, -5.0);
		
		//When
		Vector result = classUnderTest.add(otherVector);
		
		//Then
		Assert.assertEquals(10.0, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(18.0, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(6.0, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void add_WithPoint_ExpectVector()
	{
		//Given
		Vector classUnderTest = new Vector(3.0, 5.0, 11.0);
		Point point = new Point(7.0, 13.0, -5.0);
		
		//When
		Vector result = classUnderTest.add(point);
		
		//Then
		Assert.assertEquals(10.0, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(18.0, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(6.0, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void minus_WithVectors_ExpectVector()
	{
		//Given
		Vector classUnderTest = new Vector(3.0, 5.0, 11.0);
		Vector otherVector = new Vector(7.0, 13.0, -5.0);
		
		//When
		Vector result = classUnderTest.minus(otherVector);
		
		//Then
		Assert.assertEquals(-4.0, result.x, Constants.POSITIVE_ZERO);
		Assert.assertEquals(-8.0, result.y, Constants.POSITIVE_ZERO);
		Assert.assertEquals(16.0, result.z, Constants.POSITIVE_ZERO);
	}
}
