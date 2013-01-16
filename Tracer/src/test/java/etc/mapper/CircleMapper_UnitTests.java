package etc.mapper;

import java.io.IOException;

import junit.framework.Assert;

import math.Point;

import org.junit.Test;
import org.mockito.Mockito;

import etc.Color;
import etc.RaytracerException;

import util.Util;
import util.UtilImpl;

public class CircleMapper_UnitTests
{
	@Test
	public void getCircleUVImageMapping_WithMocksAndPhiGreaterThanZero_ExpectUVValue() throws IOException, RaytracerException
	{
		//Given
		int[][][] fileBytes = new int[1][1][1];
		String filePath = "path to image";
		Util mockUtil = Mockito.mock(Util.class);
		Mockito.when(mockUtil.readImage(filePath)).thenReturn(fileBytes);
		Point center = new Point(1.0,1.0,1.0);
		double radius = 3.0;
		double textureScale = 5.0;
		Point hitPoint = new Point(3.0,4.0,7.0);
		int w = 101;
		int h = 103;
		
		SphereMapper classUnderTest = new SphereMapper(filePath, center, radius, textureScale, mockUtil);
		
		//When
		int[] actual = classUnderTest.getCircleUVImageMapping(hitPoint, center, radius, w, h);
		
		//Then
		Assert.assertEquals(20, actual[0]);
		Assert.assertEquals(103, actual[1]);
	}
	
	@Test
	public void getCircleUVImageMapping_WithMocksAndPhiLessThanZero_ExpectUVValue() throws IOException, RaytracerException
	{
		//Given
		int[][][] fileBytes = new int[1][1][1];
		String filePath = "path to image";
		Util mockUtil = Mockito.mock(Util.class);
		Mockito.when(mockUtil.readImage(filePath)).thenReturn(fileBytes);
		Point center = new Point(1.0,1.0,1.0);
		double radius = 3.0;
		double textureScale = 5.0;
		Point hitPoint = new Point(-2.0,-2.0,-2.0);
		int w = 101;
		int h = 103;
		
		SphereMapper classUnderTest = new SphereMapper(filePath, center, radius, textureScale, mockUtil);
		
		//When
		int[] actual = classUnderTest.getCircleUVImageMapping(hitPoint, center, radius, w, h);
		
		//Then
		Assert.assertEquals(63, actual[0]);
		Assert.assertEquals(0, actual[1]);
	}
	
	@Test
	public void getColor_WithMocksAndPhiLessThanZero_ExpectUVValue() throws IOException, RaytracerException
	{
		//Given
		int[][][] fileBytes = new int[1][1][3];
		fileBytes[0][0][0] = 7;
		fileBytes[0][0][1] = 13;
		fileBytes[0][0][2] = 17;
		
		String filePath = "path to image";
		Util mockUtil = Mockito.spy(new UtilImpl());
		Point center = new Point(1.0,1.0,1.0);
		double radius = 3.0;
		double textureScale = 1.0;
		Point hitPoint = new Point(-2.0,-2.0,-2.0);
		int[] uvArray = new int[] {0,0};
		Color expected = new Color(7.0/255.0, 13.0/255.0, 17.0/255.0);
		
		Mockito.doReturn(fileBytes).when(mockUtil).readImage(filePath);
		
		SphereMapper classUnderTest = Mockito.spy(new SphereMapper(filePath, center, radius, textureScale, mockUtil));
		
		Mockito.doReturn(uvArray).when(classUnderTest).getCircleUVImageMapping(hitPoint, center, radius, fileBytes.length,
				   															   fileBytes[fileBytes.length - 1].length);
		
		//When
		Color actual = classUnderTest.getColor(hitPoint);
		
		//Then
		Assert.assertTrue(expected.equals(actual));
	}
}
