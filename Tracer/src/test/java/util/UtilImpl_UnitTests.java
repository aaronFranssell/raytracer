package util;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;
import org.mockito.Mockito;

import scene.Scene;
import scene.pixel.ScenePixel;
import scene.ray.Ray;
import scene.ray.RayFactory;
import surface.Surface;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;
import etc.Refractive;

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
		double originalN = 1.0;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0,0.0,0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		Ray expectedRay = new Ray(r, p);
		
		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(r, p)).thenReturn(expectedRay);
		
		UtilImpl classUnderTest = new UtilImpl(mockFactory);
		
		//When
		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		//Then
		Assert.assertTrue(result.getD().equals(r));
		Assert.assertEquals(result.getEye(), p);
		Mockito.verify(mockFactory).createRay(r, p);
	}
	
	@Test
	public void getRefractedRay_WithNewNGreater_ExpectUnchangedRefractedRay()
	{
		//Given
		double originalN = 1.0;
		double newN = 8.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0,0.0,0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);
		
		Vector expected = new Vector(0.5492382799400274, 0.5909049466066941, 0.5909049466066941);
		
		Ray expectedRay = new Ray(expected, p);
		
		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(expected, p)).thenReturn(expectedRay);
		
		UtilImpl classUnderTest = new UtilImpl(mockFactory);

		//When
		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		//Then
		Assert.assertTrue(result.getD().equals(expected));
		Assert.assertEquals(result.getEye(), p);
		Mockito.verify(mockFactory).createRay(expected, p);
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
		double originalN = 1.5;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0,0.0,0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		Vector expected = new Vector(0.19371294336139655, 0.6937129433613966, 0.6937129433613966);
		
		Ray expectedRay = new Ray(expected, p);
		
		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(expected, p)).thenReturn(expectedRay);
		
		UtilImpl classUnderTest = new UtilImpl(mockFactory);
		
		//When
		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		//Then
		Assert.assertTrue(expected.equals(result.getD()));
		Assert.assertEquals(result.getEye(), p);
		Mockito.verify(mockFactory).createRay(expected, p);
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
		Point p = new Point(0.5,0.5,0.5);
		Vector expectedD = new Vector(-0.40455970110485867, 0.7934387936397616, -0.4547376485287172);
		Point expectedEye = new Point(0.5,0.5,0.5);
		
		Ray expectedRay = new Ray(expectedD, expectedEye);
		
		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(expectedD, expectedEye)).thenReturn(expectedRay);
		
		UtilImpl classUnderTest = new UtilImpl(mockFactory);
		
		//When
		Ray actual = classUnderTest.getReflectedRay(r, p, normal);
		
		//Then
		Ray expected = new Ray(expectedD, expectedEye);
		Assert.assertEquals(expected, actual);
		Mockito.verify(mockFactory).createRay(expectedD, expectedEye);
	}
	
	@Test
	public void getReflectedColor_WithNonReflectiveSurfaceMocked_ExpectBlack() throws RaytracerException
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
	public void getReflectedColor_WithMockedRayInsideOfSurface_ExpectBlack() throws RaytracerException
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
	public void getReflectedColor_WithReflectedRay_ExpectCall() throws RaytracerException
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
		Mockito.when(pixel.getPixelColor(r, currentDepth + 1)).thenReturn(reflectReturn);
		
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
	
	@Test
	public void getRefractedColor_WithNonRefractiveMocks_ExpectBlack() throws RaytracerException
	{
		//Given
		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.getRefractive()).thenReturn(null);
		
		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		
		UtilImpl classUnderTest = new UtilImpl();
		
		//When
		Color actual = classUnderTest.getRefractedColor(null, 0, null, mockSurface, null);
		
		//Then
		Color expected = new Color(0.0,0.0,0.0);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getRefractedColor_WithTotalInternalReflection_ExpectBlack() throws RaytracerException
	{
		//Given
		double n = 1.0;
		double nT = 1.0;
		Refractive mockRefractive = Mockito.mock(Refractive.class);
		Mockito.when(mockRefractive.getN()).thenReturn(n);
		Mockito.when(mockRefractive.getnT()).thenReturn(nT);
		
		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.getRefractive()).thenReturn(mockRefractive);
		
		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		
		Vector mockD = Mockito.mock(Vector.class);
		
		Ray mockRay = Mockito.mock(Ray.class);
		Mockito.when(mockRay.getD()).thenReturn(mockD);
		
		HitData mockHit = Mockito.mock(HitData.class);
		
		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(null).when(classUnderTest).getRefractedRay(mockD, n, nT, mockHit);
		
		//When
		Color actual = classUnderTest.getRefractedColor(mockRay, 0, mockHit, mockSurface, null);
		
		//Then
		Color expected = new Color(0.0,0.0,0.0);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getRefractedColor_WithMockedRefraction_ExpectCalls() throws RaytracerException
	{
		//Given
		int currentDepth = 0;
		double n = 1.0;
		double nT = 1.0;
		Refractive mockRefractive = Mockito.mock(Refractive.class);
		Mockito.when(mockRefractive.getN()).thenReturn(n);
		Mockito.when(mockRefractive.getnT()).thenReturn(nT);
		
		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.getRefractive()).thenReturn(mockRefractive);
		
		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		
		Vector mockD = Mockito.mock(Vector.class);
		
		Ray mockRay = Mockito.mock(Ray.class);
		Mockito.when(mockRay.getD()).thenReturn(mockD);
		
		HitData mockHit = Mockito.mock(HitData.class);
		
		Ray mockRefractedRay = Mockito.mock(Ray.class);
		
		Color mockPixelReturnColor = Mockito.mock(Color.class);
		
		ScenePixel mockPixel = Mockito.mock(ScenePixel.class);
		Mockito.when(mockPixel.getPixelColor(mockRefractedRay, currentDepth + 1)).thenReturn(mockPixelReturnColor);
		
		Color mockClampedColor = Mockito.mock(Color.class);
		
		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(mockRefractedRay).when(classUnderTest).getRefractedRay(mockD, n, nT, mockHit);
		Mockito.doReturn(mockClampedColor).when(classUnderTest).clamp(mockPixelReturnColor);
		
		//When
		Color actual = classUnderTest.getRefractedColor(mockRay, currentDepth, mockHit, mockSurface, mockPixel);
		
		//Then
		Assert.assertTrue(mockClampedColor == actual);
	}
	
	@Test
	public void isInShadow_WithNoHits_ExpectFalse() throws RaytracerException
	{
		//Given
		Point mockHitPoint = Mockito.mock(Point.class);
		
		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);
		
		Point mockLight = Mockito.mock(Point.class);
		
		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);
		
		Ray mockRayShotToLight = Mockito.mock(Ray.class);
		
		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);
		
		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(false);
		
		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight)).thenReturn(mockShadowHitData);
		
		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);
		
		//When
		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);
		
		//Then
		Assert.assertFalse(result);
	}
	
	@Test
	public void isInShadow_HittingOutersphere_ExpectFalse() throws RaytracerException
	{
		//Given
		Point mockHitPoint = Mockito.mock(Point.class);
		
		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);
		
		Point mockLight = Mockito.mock(Point.class);
		
		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);
		
		Ray mockRayShotToLight = Mockito.mock(Ray.class);
		
		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);
		
		Surface mockOutersphere = Mockito.mock(Surface.class);
		Mockito.when(mockOutersphere.getType()).thenReturn(Surface.SurfaceType.Outersphere);
		
		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(true);
		Mockito.when(mockShadowHitData.getSurface()).thenReturn(mockOutersphere);
		
		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight)).thenReturn(mockShadowHitData);
		
		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);
		
		//When
		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);
		
		//Then
		Assert.assertFalse(result);
	}
	
	@Test
	public void isInShadow_HitObjectButLightIsCloser_ExpectFalse() throws RaytracerException
	{
		//Given
		Point mockHitPoint = Mockito.mock(Point.class);
		
		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);
		
		Point mockLight = Mockito.mock(Point.class);
		
		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);
		Mockito.when(mockD.magnitude()).thenReturn(10.0);
		
		Ray mockRayShotToLight = Mockito.mock(Ray.class);
		
		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);
		
		Surface mockOutersphere = Mockito.mock(Surface.class);
		Mockito.when(mockOutersphere.getType()).thenReturn(Surface.SurfaceType.Sphere);
		
		Vector mockShadowPMinusMockHitPoint = Mockito.mock(Vector.class);
		Mockito.when(mockShadowPMinusMockHitPoint.magnitude()).thenReturn(20.0);
		
		Point mockShadowHitPoint = Mockito.mock(Point.class);
		Mockito.when(mockShadowHitPoint.minus(mockHitPoint)).thenReturn(mockShadowPMinusMockHitPoint);
		
		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(true);
		Mockito.when(mockShadowHitData.getSurface()).thenReturn(mockOutersphere);
		Mockito.when(mockShadowHitData.getP()).thenReturn(mockShadowHitPoint);
		
		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight)).thenReturn(mockShadowHitData);
		
		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);
		
		//When
		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);
		
		//Then
		Assert.assertFalse(result);
	}
	
	@Test
	public void isInShadow_HitObjectCloserThanLight_ExpectTrue() throws RaytracerException
	{
		//Given
		Point mockHitPoint = Mockito.mock(Point.class);
		
		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);
		
		Point mockLight = Mockito.mock(Point.class);
		
		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);
		Mockito.when(mockD.magnitude()).thenReturn(10.0);
		
		Ray mockRayShotToLight = Mockito.mock(Ray.class);
		
		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);
		
		Surface mockOutersphere = Mockito.mock(Surface.class);
		Mockito.when(mockOutersphere.getType()).thenReturn(Surface.SurfaceType.Sphere);
		
		Vector mockShadowPMinusMockHitPoint = Mockito.mock(Vector.class);
		Mockito.when(mockShadowPMinusMockHitPoint.magnitude()).thenReturn(5.0);
		
		Point mockShadowHitPoint = Mockito.mock(Point.class);
		Mockito.when(mockShadowHitPoint.minus(mockHitPoint)).thenReturn(mockShadowPMinusMockHitPoint);
		
		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(true);
		Mockito.when(mockShadowHitData.getSurface()).thenReturn(mockOutersphere);
		Mockito.when(mockShadowHitData.getP()).thenReturn(mockShadowHitPoint);
		
		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight)).thenReturn(mockShadowHitData);
		
		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);
		
		//When
		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);
		
		//Then
		Assert.assertTrue(result);
	}
}
