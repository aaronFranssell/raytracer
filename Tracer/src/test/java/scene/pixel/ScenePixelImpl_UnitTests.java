package scene.pixel;

import org.junit.Assert;

import math.Point;
import math.Vector;

import org.junit.Test;
import org.mockito.Mockito;

import scene.Scene;
import scene.ray.Ray;
import surface.Surface;
import util.Util;

import etc.Color;
import etc.HitData;
import etc.RaytracerException;

public class ScenePixelImpl_UnitTests
{
	@Test
	public void getPixelColor_RecursionLimitReached_ExpectBlack() throws RaytracerException
	{
		//Given
		ScenePixelImpl classUnderTest = new ScenePixelImpl(null, null, null, null, 6);
		Color expected = new Color(0.0,0.0,0.0);
		
		//When
		Color actual = classUnderTest.getPixelColor(null, 6);
		
		//Then
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getPixelColor_NoHits_ExpectBlack() throws RaytracerException
	{
		//Given
		HitData mockHit = Mockito.mock(HitData.class);
		Mockito.when(mockHit.isHit()).thenReturn(false);
		
		Ray mockRay = Mockito.mock(Ray.class);
		
		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRay)).thenReturn(mockHit);
		
		ScenePixelImpl classUnderTest = new ScenePixelImpl(mockScene, null, null, null, 6);
		Color expected = new Color(0.0,0.0,0.0);
		
		//When
		Color actual = classUnderTest.getPixelColor(mockRay, 0);
		
		//Then
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getPixelColor_WithHits_ExpectColor() throws RaytracerException
	{
		//Given
		int currentDepth = 0;
		Color surfaceColor = new Color(0.1,0.1,0.1);
		Color refractColor = new Color(0.1,0.1,0.1);
		Color reflectColor = new Color(0.1,0.1,0.1);
		
		Point mockEye = Mockito.mock(Point.class);
		Point mockLight = Mockito.mock(Point.class);
		
		Surface mockSurface = Mockito.mock(Surface.class);
		
		Vector mockNormal = Mockito.mock(Vector.class);
		
		Point mockHitPoint = Mockito.mock(Point.class);
		
		HitData mockHit = Mockito.mock(HitData.class);
		Mockito.when(mockHit.isHit()).thenReturn(true);
		Mockito.when(mockHit.getSurface()).thenReturn(mockSurface);
		Mockito.when(mockHit.getNormal()).thenReturn(mockNormal);
		Mockito.when(mockHit.getP()).thenReturn(mockHitPoint);
		
		Ray mockRay = Mockito.mock(Ray.class);
		
		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRay)).thenReturn(mockHit);
		
		boolean inShadow = false;
		Util mockUtil = Mockito.mock(Util.class);
		Mockito.when(mockUtil.isInShadow(mockScene, mockLight, mockHit)).thenReturn(inShadow);
				
		Mockito.when(mockSurface.getColor(mockRay, mockLight, mockEye, inShadow, mockNormal, mockHitPoint)).thenReturn(surfaceColor);
		
		ScenePixelImpl classUnderTest = new ScenePixelImpl(mockScene, mockEye, mockLight, mockUtil, 6);
		
		Mockito.when(mockUtil.getReflectedColor(mockRay, currentDepth, mockHit, mockSurface, classUnderTest)).thenReturn(reflectColor);
		Mockito.when(mockUtil.getRefractedColor(mockRay, currentDepth, mockHit, mockSurface, classUnderTest)).thenReturn(refractColor);
		
		Color expected = new Color(0.3,0.3,0.3);
		
		//When
		Color actual = classUnderTest.getPixelColor(mockRay, currentDepth);
		
		//Then
		Assert.assertEquals(expected, actual);
	}
}
