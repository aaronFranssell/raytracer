package util;

import helper.TestsHelper;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;

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
	
	@Test
	public void readTextFile_WithTextFile_ExpectTest() throws IOException
	{
		//Given
		String filePath = "src\\test\\resources\\textFile.txt";
		
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("asdf asdf");
		expected.add(";lkj ;lkj");
		
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		ArrayList<String> actual = classUnderTest.readTextFile(filePath);
		
		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}
}
