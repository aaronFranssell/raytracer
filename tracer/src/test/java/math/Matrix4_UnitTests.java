package math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.Constants;

public class Matrix4_UnitTests
{
	@Test
	public void multiply_WithOtherMatrix_ExpectResult()
	{
		//Given
		double[][] answer = {{-23.0, 17.0, -46.0, 24.0}, 
							 {83.0, 27.0, -114.0, -62.0}, 
							 {-54.0, -88.0, 179.0, 15.0}, 
							 {59.0, -71.0, 74.0, -69.0}}; 
		
		Matrix4 other = new Matrix4(3.0, 5.0, 0.0, -5.0,
									-5.0, -7.0, 11.0, -1.0,
				 					7.0, -3.0, 5.0, -7.0,
				 					3.0, -1.0, -7.0, -2.0);
		Matrix4 classUnderTest = new Matrix4(1.0, 0.0, -5.0, 3.0,
											 5.0, -3.0, 2.0, 13.0,
											 -7.0, 7.0, 5.0, -11.0,
											 -3.0, 3.0, 11.0, 2.0);
		
		//When
		Matrix4 resultMatrix = classUnderTest.multiply(other);
		
		//Then
		double[][] result = resultMatrix.getMatrix();
		assertEquals(result.length, answer.length);
		for(int i = 0; i < result.length; i++)
		{
			assertEquals(result[i].length, answer[i].length);
			for(int m = 0; m < result.length; m++)
			{
				assertEquals(answer[i][m], result[i][m], Constants.POSITIVE_ZERO);
			}
		}
	}
	
	@Test
	public void multiplyBy_WithValues_ExpectScaledMatrix()
	{
		//Given
		Matrix4 classUnderTest = new Matrix4(1.0, 0.0, 3.0, -3.0,
											 -1.0, 5.0, -5.0, 7.0,
											 3.0, 2.5, 6.0, 0.0, 
											 0.0, -2.5, 5.0, 9.0);
		double scaleBy = -2.0;
		
		double[][] answer = {{-2.0, -0.0, -6.0, 6.0},
							 {2.0, -10.0, 10.0, -14.0},
							 { -6.0, -5.0, -12.0, -0.0},
							 {-0.0, 5.0, -10.0, -18.0}};
		
		//When
		Matrix4 resultMatrix = classUnderTest.multiplyBy(scaleBy);
		
		//Then
		double[][] result = resultMatrix.getMatrix();
		for(int i = 0; i < result.length; i++)
		{
			assertEquals(result[i].length, answer[i].length);
			for(int m = 0; m < result.length; m++)
			{
				assertEquals(answer[i][m], result[i][m], Constants.POSITIVE_ZERO);
			}
		}
	}
	
	@Test
	public void getInverse_WithMatrix_ExpectInverse()
	{
		//Given
		Matrix4 answerMatrix = new Matrix4(-2.3, -0.6, 0.9, -0.3, 
									 0.744, 0.288, -0.152, 0.024, 
									 0.84, 0.18, -0.22, 0.14,
									 -0.26, -0.02, 0.08, 0.04);
		
		Matrix4 classUnderTest = new Matrix4(1.0, 0.0, 3.0, -3.0,
											 -1.0, 5.0, -5.0, 7.0,
											 3.0, 2.5, 6.0, 0.0, 
											 0.0, -2.5, 5.0, 9.0);
		
		//When
		Matrix4 resultMatrix = classUnderTest.getInverse();
		
		//Then
		double[][] result = resultMatrix.getMatrix();
		double[][] answer = answerMatrix.getMatrix();
		for(int i = 0; i < result.length; i++)
		{
			assertEquals(result[i].length, answer[i].length);
			for(int m = 0; m < result.length; m++)
			{
				assertEquals(answer[i][m], result[i][m], Constants.POSITIVE_ZERO);
			}
		}
	}
	
	@Test
	public void multiplyHomogeneousCoordinates_WithVector_ExpectVector()
	{
		//Given
		Vector vec = new Vector(3.0,5.0,7.0);
		Matrix4 classUnderTest = new Matrix4(1.0, 0.0, 3.0, -3.0,
											 -1.0, 5.0, -5.0, 7.0,
											 3.0, 2.5, 6.0, 0.0, 
											 0.0, -2.5, 5.0, 9.0);
		Vector answerVector = new Vector(24.0, -13.0, 63.5);
		
		//When
		Vector result = classUnderTest.multiplyHomogeneousCoordinates(vec);
		
		//Then
		assertEquals(answerVector.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(answerVector.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(answerVector.z, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void multiplyHomogeneousCoordinates_WithPoint_ExpectPoint()
	{
		//Given
		Point point = new Point(3.0, -5.0, 7.0);
		Matrix4 classUnderTest = new Matrix4(1.0, 0.0, 3.0, -3.0,
											 -1.0, 5.0, -5.0, 7.0,
											 3.0, 2.5, 6.0, 0.0, 
											 0.0, -2.5, 5.0, 9.0);
		Point expected = new Point(21.0, -56.0, 38.5);
		
		//When
		Point result = classUnderTest.multiplyHomogeneousCoordinates(point);
		
		//Then
		assertEquals(expected.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(expected.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(expected.z, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void add_WithOtherMatrix_ExpectMatrix()
	{
		//Given
		Matrix4 classUnderTest = new Matrix4(1.0, 0.0, 3.0, -3.0,
											 -1.0, 5.0, -5.0, 7.0,
											 3.0, 2.5, 6.0, 0.0, 
											 0.0, -2.5, 5.0, 9.0);
		
		Matrix4 otherMatrix = new Matrix4(1.0, 0.0, 3.0, -3.0,
										  -1.0, 5.0, -5.0, 7.0,
										  3.0, 2.5, 6.0, 0.0, 
										  0.0, -2.5, 5.0, 9.0);
		
		Matrix4 answerMatrix = new Matrix4(2.0, 0.0, 6.0, -6.0,
						 				   -2.0, 10.0, -10.0, 14.0,
										   6.0, 5.0, 12.0, 0.0, 
										   0.0, -5.0, 10.0, 18.0);

		
		//When
		Matrix4 result = classUnderTest.add(otherMatrix);
		
		//Then
		double[][] matrixArray = answerMatrix.getMatrix();
		double[][] resultArray = result.getMatrix();
		for(int i = 0; i < matrixArray.length; i++)
		{
			for(int m = 0; m < resultArray.length; m++)
			{
				assertEquals(matrixArray[i][m], resultArray[i][m], Constants.POSITIVE_ZERO);
			}
		}
	}
}
