package csg.operation;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import primitives.Surface;
import csg.operation.factory.IntervalFactory;
import etc.HitData;
import etc.Ray;
import etc.RaytracerException;

public class Test_BoundedBy
{
	@Test
	public void getHitData_WithTwoMisses_ExpectLeftHitData() throws RaytracerException
	{
		//Arrange
		HitData leftHitData = new HitData();
		HitData rightHitData= new HitData();
		Ray ray = new Ray(new Vector(0.0,0.0,0.0), new Point(0.0,0.0,0.0));
		Surface s = null;
		BoundedBy classUnderTest = new BoundedBy(s);
		
		//Act
		HitData retHitData = classUnderTest.getHitData(leftHitData, rightHitData, ray);
		
		//Assert
		Assert.assertTrue(retHitData == leftHitData);
	}
	
	@Test
	public void getHitData_WithRightMiss_ExpectLeftHitData() throws RaytracerException
	{
		//Arrange
		HitData leftHitData = new HitData(1.0, null, null, null, new double[] {-1.0, 1.0});
		HitData rightHitData= new HitData();
		Ray ray = new Ray(new Vector(0.0,0.0,0.0), new Point(0.0,0.0,0.0));
		Surface s = null;
		BoundedBy classUnderTest = new BoundedBy(s);
		
		//Act
		HitData retHitData = classUnderTest.getHitData(leftHitData, rightHitData, ray);
		
		//Assert
		Assert.assertTrue(retHitData == leftHitData);
	}
	
	@Test
	public void getHitData_WithLeftHitsAllInRightInterval_ExpectMiss() throws RaytracerException
	{
		//Arrange
		double[] hits = new double[] {1.0, 2.0};
		Interval mockInterval = mock(Interval.class);

		doReturn(true).when(mockInterval).allInInterval(mockInterval);
		
		IntervalFactory mockFactory = mock(IntervalFactory.class);
		doReturn(mockInterval).when(mockFactory).getInterval(hits);
		HitData leftHitData = new HitData(1.0, null, null, null, hits);
		HitData rightHitData= new HitData(1.0, null, null, null, hits);
		Ray ray = new Ray(new Vector(0.0,0.0,0.0), new Point(0.0,0.0,0.0));
		Surface s = null;
		BoundedBy classUnderTest = new BoundedBy(s, mockFactory);
		
		//Act
		HitData retHitData = classUnderTest.getHitData(leftHitData, rightHitData, ray);
		
		//Assert
		Assert.assertFalse(retHitData.isHit());
	}
	
	@Test
	public void getHitData_WithLeftUnaffectByRight_ExpectLeftHitData() throws RaytracerException
	{
		//Arrange
		double[] hits = new double[] {1.0, 2.0};
		Interval mockInterval = mock(Interval.class);
		
		doReturn(false).when(mockInterval).allInInterval(mockInterval);
		doReturn(false).when(mockInterval).isInInterval(1.0);
		
		IntervalFactory mockFactory = mock(IntervalFactory.class);
		doReturn(mockInterval).when(mockFactory).getInterval(hits);
		HitData leftHitData = new HitData(1.0, null, null, null, hits);
		HitData rightHitData= new HitData(1.0, null, null, null, hits);
		Ray ray = new Ray(new Vector(0.0,0.0,0.0), new Point(0.0,0.0,0.0));
		Surface s = null;
		BoundedBy classUnderTest = new BoundedBy(s, mockFactory);
		
		//Act
		HitData retHitData = classUnderTest.getHitData(leftHitData, rightHitData, ray);
		
		//Assert
		Assert.assertTrue(leftHitData == retHitData);
	}
	
	@Test
	public void getHitData_WithSomeOfRightContainedInLeft_ExpectMergeTCall() throws RaytracerException
	{
		//Arrange
		double[] rightDataHits = new double[] {   2.0,      4.0};
		double[] leftDataHits = new double[] {1.0,     3.0};
		Interval mockRightSideInterval = mock(Interval.class);
		Interval mockLeftSideInterval = mock(Interval.class);
				
		doReturn(false).when(mockRightSideInterval).allInInterval(mockLeftSideInterval);
		doReturn(true).when(mockRightSideInterval).isInInterval(1.0);
		doReturn(true).when(mockLeftSideInterval).isInInterval(1.0);

		doReturn(leftDataHits).when(mockLeftSideInterval).mergeTIntervalsAndSort(mockRightSideInterval);
		
		IntervalFactory mockFactory = mock(IntervalFactory.class);
		doReturn(mockRightSideInterval).when(mockFactory).getInterval(rightDataHits);
		doReturn(mockLeftSideInterval).when(mockFactory).getInterval(leftDataHits);
		
		Vector leftNormal = new Vector(0.0,0.0,0.0);
		Point leftHitPoint = new Point(0.0,0.0,0.0);
		Ray ray = new Ray(new Vector(0.0,0.0,0.0), new Point(0.0,0.0,0.0));
		Surface s = Test_BoundedByHelper.createTestSurface();
		HitData leftHitData = new HitData(1.0, s, leftNormal, leftHitPoint, leftDataHits);
		HitData rightHitData= new HitData(1.0, null, null, null, rightDataHits);
		BoundedBy classUnderTest = new BoundedBy(s, mockFactory);
				
		//Act
		HitData retHitData = classUnderTest.getHitData(leftHitData, rightHitData, ray);
				
		//Assert
		Assert.assertTrue(retHitData.getSmallestT() == 1.0);
		Assert.assertTrue(retHitData.getSurface() == s);
		Assert.assertTrue(retHitData.getNormal().equals(leftNormal));
		Assert.assertTrue(retHitData.getP().equals(leftHitPoint));
		Assert.assertTrue(retHitData.getHitTs().length == leftDataHits.length);
		Assert.assertTrue(retHitData.getHitTs()[0] == leftDataHits[0]);
		Assert.assertTrue(retHitData.getHitTs()[1] == leftDataHits[1]);
		verify(mockLeftSideInterval, times(1)).mergeTIntervalsAndSort(mockRightSideInterval);
	}
	
	@Test
	public void getHitData_WithSomeOfLeftContainedInRight_ExpectMergeTCall() throws RaytracerException
	{
		//Arrange
		double[] rightDataHits = new double[] {3.0, 4.0};
		double[] leftDataHits = new double[] {1.0, 2.0};
		Interval mockRightSideInterval = mock(Interval.class);
		Interval mockLeftSideInterval = mock(Interval.class);
				
		doReturn(false).when(mockRightSideInterval).allInInterval(mockLeftSideInterval);
		doReturn(true).when(mockRightSideInterval).isInInterval(1.0);
		doReturn(false).when(mockLeftSideInterval).isInInterval(1.0);

		doReturn(3.0).when(mockRightSideInterval).getNextGreatestInterval(1.0);
		
		IntervalFactory mockFactory = mock(IntervalFactory.class);
		doReturn(mockRightSideInterval).when(mockFactory).getInterval(rightDataHits);
		doReturn(mockLeftSideInterval).when(mockFactory).getInterval(leftDataHits);
		
		Vector leftNormal = new Vector(0.0,0.0,0.0);
		Point leftHitPoint = new Point(0.0,0.0,0.0);
		Ray ray = new Ray(new Vector(0.0,0.0,0.0), new Point(0.0,0.0,0.0));
		Surface s = Test_BoundedByHelper.createTestSurface();
		HitData leftHitData = new HitData(1.0, s, leftNormal, leftHitPoint, leftDataHits);
		HitData rightHitData= new HitData(3.0, null, null, null, rightDataHits);
		BoundedBy classUnderTest = new BoundedBy(s, mockFactory);
				
		//Act
		HitData retHitData = classUnderTest.getHitData(leftHitData, rightHitData, ray);
				
		//Assert
		Assert.assertTrue(retHitData.getSmallestT() == 1.0);
		Assert.assertTrue(retHitData.getSurface() == s);
		Assert.assertTrue(retHitData.getNormal().equals(leftNormal));
		Assert.assertTrue(retHitData.getP().equals(leftHitPoint));
		Assert.assertTrue(retHitData.getHitTs().length == leftDataHits.length);
		Assert.assertTrue(retHitData.getHitTs()[0] == leftDataHits[0]);
		Assert.assertTrue(retHitData.getHitTs()[1] == leftDataHits[1]);
		verify(mockLeftSideInterval, times(1)).mergeTIntervalsAndSort(mockRightSideInterval);
	}
}
