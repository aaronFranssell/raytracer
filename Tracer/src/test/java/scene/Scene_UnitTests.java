package scene;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import etc.HitData;
import etc.RaytracerException;
import scene.ray.Ray;
import surface.Surface;


public class Scene_UnitTests
{
	@Test
	public void getSmallestPositiveHitData_WithNoHits_ExpectMiss() throws RaytracerException
	{
		//Given
		HitData miss = new HitData();
		ArrayList<HitData> misses = new ArrayList<HitData>();
		misses.add(miss);
		misses.add(miss);
		Ray mockRay = Mockito.mock(Ray.class);
		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getHitData(mockRay)).thenReturn(misses);
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		
		SceneImpl classUnderTest = new SceneImpl(surfaces);
		
		//When
		HitData smallest = classUnderTest.getSmallestPositiveHitDataOrReturnMiss(mockRay);
		
		//Then
		Assert.assertFalse(smallest.isHit());
	}
	
	@Test
	public void getSmallestPositiveHitData_WithAllNegativeHits_ExpectMiss() throws RaytracerException
	{
		//Given
		Ray mockRay = Mockito.mock(Ray.class);
		Surface mockSurface = Mockito.mock(Surface.class);
		HitData lessThanZeroHit = new HitData(-1.0, mockSurface, null, null);
		ArrayList<HitData> lessThanZeroHits = new ArrayList<HitData>();
		lessThanZeroHits.add(lessThanZeroHit);
		lessThanZeroHits.add(lessThanZeroHit);

		Mockito.when(mockSurface.getHitData(mockRay)).thenReturn(lessThanZeroHits);
		
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		surfaces.add(mockSurface);
		
		SceneImpl classUnderTest = new SceneImpl(surfaces);
		
		//When
		HitData smallest = classUnderTest.getSmallestPositiveHitDataOrReturnMiss(mockRay);
		
		//Then
		Assert.assertFalse(smallest.isHit());
	}
	
	@Test
	public void getSmallestPositiveHitData_WithNegativeMissesAndPositiveHits_ExpectSmallestPositiveHit() throws RaytracerException
	{
		//Given
		Ray mockRay = Mockito.mock(Ray.class);
		Surface mockSurface = Mockito.mock(Surface.class);
		HitData lessThanZeroHit = new HitData(-1.0, mockSurface, null, null);
		HitData smallestPositive = new HitData(1.0, mockSurface, null, null);
		HitData largestPositive = new HitData(2.0, mockSurface, null, null);
		HitData miss = new HitData();
		ArrayList<HitData> hits = new ArrayList<HitData>();
		hits.add(lessThanZeroHit);
		hits.add(lessThanZeroHit);
		hits.add(miss);
		hits.add(miss);
		hits.add(largestPositive);
		hits.add(smallestPositive);

		Mockito.when(mockSurface.getHitData(mockRay)).thenReturn(hits);
		
		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		surfaces.add(mockSurface);
		
		SceneImpl classUnderTest = new SceneImpl(surfaces);
		
		//When
		HitData smallest = classUnderTest.getSmallestPositiveHitDataOrReturnMiss(mockRay);
		
		//Then
		Assert.assertTrue(smallest == smallestPositive);
	}
}
