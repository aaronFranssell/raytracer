package math;

import org.junit.Assert;

import org.junit.Test;

import etc.RaytracerException;

public class UVWFactoryImpl_IntegrationTests
{
	@Test(expected=RaytracerException.class)
	public void createUVW_WithParallelVectors_ExpectException() throws RaytracerException
	{
		//Given
		Vector gaze = new Vector(1.0, 0.0, 0.0);
		Vector up = new Vector(1.0,0.0,0.0);
		UVWFactoryImpl classUnderTest = new UVWFactoryImpl();
		
		//When
		classUnderTest.createUVW(up, gaze);
		
		//Then
		Assert.fail("Expected exception not thrown.");
	}
	
	@Test
	public void createUVW_WithZGazeAndYUp_ExpectUVW() throws RaytracerException
	{
		//Given
		Vector gaze = new Vector(0.0, 0.0, -1.0);
		Vector up = new Vector(0.0, 1.0, 0.0);
		
		Vector expectedU = new Vector(1.0, 0.0, 0.0);
		Vector expectedV = new Vector(0.0, 1.0, 0.0);
		Vector expectedW = new Vector(0.0, 0.0, -1.0);
		UVWFactoryImpl classUnderTest = new UVWFactoryImpl();
		
		//When
		UVW retUVW = classUnderTest.createUVW(up, gaze);
	
		//Then
		Assert.assertTrue(retUVW.getU().equals(expectedU));
		Assert.assertTrue(retUVW.getV().equals(expectedV));
		Assert.assertTrue(retUVW.getW().equals(expectedW));
	}
	
	@Test
	public void createUVW_WithLookingInMinusAll3Directions_ExpectUVW() throws RaytracerException
	{
		//Given
		Vector gaze = new Vector(-1.0, -1.0, -1.0);
		Vector up = new Vector(0.0, 1.0, 0.0);
		
		Vector expectedU = new Vector(0.7071067811865476, 0.0, -0.7071067811865476);
		Vector expectedV = new Vector(-0.4082482904638631, 0.8164965809277261, -0.4082482904638631);
		Vector expectedW = new Vector(-0.5773502691896258, -0.5773502691896258, -0.5773502691896258);
		UVWFactoryImpl classUnderTest = new UVWFactoryImpl();
		
		//When
		UVW retUVW = classUnderTest.createUVW(up, gaze);
		
		//Then
		Assert.assertTrue(retUVW.getU().equals(expectedU));
		Assert.assertTrue(retUVW.getV().equals(expectedV));
		Assert.assertTrue(retUVW.getW().equals(expectedW));
	}
}
