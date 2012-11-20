package util;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;
import org.mockito.Mockito;

import scene.pixel.ScenePixel;
import scene.ray.Ray;
import surface.Surface;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;

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

	@Test
	public void getRefractedRay_With1sForIndexesOfRefraction_ExpectUnchangedRefractedRay()
	{
		//Given
		UtilImpl classUnderTest = new UtilImpl();

		double originalN = 1.0;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0,0.0,0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		//When
		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		//Then
		Assert.assertTrue(result.getD().equals(r));
		Assert.assertEquals(result.getEye(), p);
	}
	
	@Test
	public void getRefractedRay_WithNewNGreater_ExpectUnchangedRefractedRay()
	{
		//Given
		UtilImpl classUnderTest = new UtilImpl();

		double originalN = 1.0;
		double newN = 8.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0,0.0,0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);
		
		Vector expected = new Vector(0.5492382799400274, 0.5909049466066941, 0.5909049466066941);

		//When
		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		//Then
		Assert.assertTrue(result.getD().equals(expected));
		Assert.assertEquals(result.getEye(), p);
	}
	
	@Test
	public void getRefractedRay_WithTotalInternalReflection_ExpectNullRay()
	{
		//Given
		UtilImpl classUnderTest = new UtilImpl();

		double originalN = 8.0;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0,0.0,0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		//When
		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		//Then
		Assert.assertNull(result);
	}
	
	@Test
	public void getRefractedRay_WithOriginalNLess_ExpectNullRay()
	{
		//Given
		UtilImpl classUnderTest = new UtilImpl();

		double originalN = 1.5;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0,0.0,0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		Vector expected = new Vector(0.19371294336139655, 0.6937129433613966, 0.6937129433613966);
		
		//When
		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		//Then
		Assert.assertTrue(expected.equals(result.getD()));
		Assert.assertEquals(result.getEye(), p);
	}
	
	@Test
	public void clamp_WithValuesOver1_Expect1s()
	{
		//Given
		Color c = new Color(1.1, 1.4, 1.5);
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		Color actual = classUnderTest.clamp(c);
		
		//Then
		Color expected = new Color(1.0,1.0,1.0);
		Assert.assertTrue(expected.equals(actual));
	}
	
	@Test
	public void clamp_WithValuesUnder1_ExpectUnchanged()
	{
		//Given
		Color c = new Color(0.1, 0.4, 0.5);
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		Color actual = classUnderTest.clamp(c);
		
		//Then
		Color expected = new Color(0.1,0.4,0.5);
		Assert.assertTrue(expected.equals(actual));
	}
	
	@Test
	public void getReflectedRay_WithValues_ExpectCall()
	{
		//Given
		Vector d = new Vector(0.3,0.5,0.7);
		Point eye = new Point(0.0,0.0,0.0);
		Ray r = new Ray(d, eye);
		Vector normal = new Vector(1.5,-0.5,2.5);
		normal = normal.normalizeReturn();
		UtilImpl classUnderTest = new UtilImpl();
		Point p = new Point(0.5,0.5,0.5);
		
		//When
		Ray actual = classUnderTest.getReflectedRay(r, p, normal);
		
		//Then
		Vector expectedD = new Vector(-0.40455970110485867, 0.7934387936397616, -0.4547376485287172);
		Point expectedEye = new Point(0.5,0.5,0.5);
		Ray expected = new Ray(expectedD, expectedEye);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getReflectedReturnColor_WithNonReflectiveSurfaceMocked_ExpectBlack() throws RaytracerException
	{
		//Given
		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.isReflective()).thenReturn(false);
		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		Ray r = Mockito.mock(Ray.class);
		int currentDepth = -1;
		HitData hit = Mockito.mock(HitData.class);
		ScenePixel pixel = Mockito.mock(ScenePixel.class);
		
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		Color actual = classUnderTest.getReflectedColor(r, currentDepth, hit, mockSurface, pixel);
		
		//Then
		Color expected = new Color(0.0,0.0,0.0);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getReflectedReturnColor_WithMockedRayInsideOfSurface_ExpectBlack() throws RaytracerException
	{
		//Given
		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.isReflective()).thenReturn(true);
		Surface mockSurface = Mockito.mock(Surface.class);
		Vector mockVec = Mockito.mock(Vector.class);
		Mockito.when(mockVec.dot(mockVec)).thenReturn(1.0);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		Ray r = Mockito.mock(Ray.class);
		Mockito.when(r.getD()).thenReturn(mockVec);
		int currentDepth = -1;
		HitData hit = Mockito.mock(HitData.class);
		ScenePixel pixel = Mockito.mock(ScenePixel.class);
		
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		Color actual = classUnderTest.getReflectedColor(r, currentDepth, hit, mockSurface, pixel);
		
		//Then
		Color expected = new Color(0.0,0.0,0.0);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getReflectedReturnColor_WithMockedTotalInternalReflection_ExpectBlack() throws RaytracerException
	{
		//Given
		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.isReflective()).thenReturn(true);
		Surface mockSurface = Mockito.mock(Surface.class);
		Vector mockVec = Mockito.mock(Vector.class);
		Mockito.when(mockVec.dot(mockVec)).thenReturn(-1.0);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		Ray r = Mockito.mock(Ray.class);
		Mockito.when(r.getD()).thenReturn(mockVec);
		int currentDepth = -1;
		Point mockPoint = Mockito.mock(Point.class);
		HitData hit = Mockito.mock(HitData.class);
		Mockito.when(hit.getNormal()).thenReturn(mockVec);
		Mockito.when(hit.getP()).thenReturn(mockPoint);
		ScenePixel pixel = Mockito.mock(ScenePixel.class);
		
		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(null).when(classUnderTest).getReflectedRay(r, mockPoint, mockVec);
		
		//When
		Color actual = classUnderTest.getReflectedColor(r, currentDepth, hit, mockSurface, pixel);
		
		//Then
		Color expected = new Color(0.0,0.0,0.0);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getReflectedReturnColor_WithRefractedRay_ExpectCall() throws RaytracerException
	{
		//Given
		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.isReflective()).thenReturn(true);
		Surface mockSurface = Mockito.mock(Surface.class);
		Vector mockVec = Mockito.mock(Vector.class);
		Mockito.when(mockVec.dot(mockVec)).thenReturn(-1.0);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		Ray r = Mockito.mock(Ray.class);
		Mockito.when(r.getD()).thenReturn(mockVec);
		int currentDepth = -1;
		Point mockPoint = Mockito.mock(Point.class);
		HitData hit = Mockito.mock(HitData.class);
		Mockito.when(hit.getNormal()).thenReturn(mockVec);
		Mockito.when(hit.getP()).thenReturn(mockPoint);
		Color reflectReturn = Mockito.mock(Color.class);
		ScenePixel pixel = Mockito.mock(ScenePixel.class);
		Mockito.when(pixel.recurse(r, currentDepth + 1)).thenReturn(reflectReturn);
		
		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Color clampReturn = new Color(0.1,0.3,0.7);
		Mockito.doReturn(r).when(classUnderTest).getReflectedRay(r, mockPoint, mockVec);
		Mockito.doReturn(clampReturn).when(classUnderTest).clamp(reflectReturn);
		
		//When
		Color actual = classUnderTest.getReflectedColor(r, currentDepth, hit, mockSurface, pixel);
		
		//Then
		Color expected = new Color(0.1,0.3,0.7);
		Assert.assertEquals(expected, actual);
	}
}
