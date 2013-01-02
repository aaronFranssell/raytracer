package util;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class UtilImpl_IntegrationTests
{
	@Test
	public void readImage_WithTestImage_ExpectImage() throws IOException
	{
		//Given
		UtilImpl classUnderTest = new UtilImpl();
		int[][][] expected = new int[1][1][3];
		expected[0][0][0] = 255;
		expected[0][0][1] = 242;
		expected[0][0][2] = 0;
		
		//When
		int[][][] actual = classUnderTest.readImage("src\\test\\resources\\testReadImage.png");
		
		//Then
		Assert.assertEquals(expected.length, actual.length);
		Assert.assertEquals(expected[0].length, actual[0].length);
		Assert.assertEquals(expected[0][0][0], actual[0][0][0]);
		Assert.assertEquals(expected[0][0][1], actual[0][0][1]);
		Assert.assertEquals(expected[0][0][2], actual[0][0][2]);
	}
}
