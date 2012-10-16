package util;

import junit.framework.Assert;

import math.Point;
import math.Vector;

import org.junit.Test;

import scene.ray.Ray;

public class UtilImpl_UnitTests
{
	@Test
	public void sort_WithArray_ExpectSortedArray()
	{
		//Given
		double[] array = new double[] {5, 3, 8, 10, -5, 0.4, 5, 9, 10, 0.0};
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		double[] result = classUnderTest.sort(array);
		
		//Then
		Assert.assertEquals(array.length, result.length);
		for(int i = 0; i < result.length; i++)
		{
			double curr = result[i];
			boolean found = false;
			for(int m = 0; m < array.length; m++)
			{
				if(array[m] == curr)
				{
					found = true;
				}
			}
			Assert.assertTrue(found);
		}
		
		for(int i = 0; i < result.length - 1; i++)
		{
			Assert.assertTrue(result[i] <= result[i + 1]);
		}
	}
	
	@Test
	public void getP_WithValues_ExpectP()
	{
		//Given
		Ray r1 = new Ray(new Vector(1.0,1.0,1.0), new Point(1.0,1.0,1.0));
		double t1 = 2.0;
		Ray r2 = new Ray(new Vector(1.0,1.0,1.0), new Point(1.0,1.0,1.0));
		double t2 = 0.0;
		
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		Point result1 = classUnderTest.getP(t1, r1);
		Point result2 = classUnderTest.getP(t2, r2);
		
		//Then
		Assert.assertEquals(3.0, result1.x, 0.0003);
		Assert.assertEquals(3.0, result1.y, 0.0003);
		Assert.assertEquals(3.0, result1.z, 0.0003);
		Assert.assertEquals(1.0, result2.x, 0.0003);
		Assert.assertEquals(1.0, result2.y, 0.0003);
		Assert.assertEquals(1.0, result2.z, 0.0003);
	}
}
