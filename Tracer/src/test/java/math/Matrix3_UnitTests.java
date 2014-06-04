package math;


import org.junit.Assert;
import org.junit.Test;

import util.Constants;

public class Matrix3_UnitTests
{
	@Test
	public void det_WithValues_ExpectResult()
	{
		//Given
		Matrix3 classUnderTest = new Matrix3(1, -1, 0,
											 2, -2, 5,
											 7, -3, 5);
		
		//When
		double result = classUnderTest.det();
		
		//Then
		Assert.assertEquals(-20, result, Constants.POSITIVE_ZERO);
	}
}
