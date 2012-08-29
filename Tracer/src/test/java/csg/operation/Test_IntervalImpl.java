package csg.operation;

import static org.junit.Assert.*;
import junit.framework.Assert;

import matchers.IsAnyDoubleArrayMatcher;

import org.junit.Before;
import org.junit.Test;

import csg.operation.Interval;
import csg.operation.IntervalImpl;

import etc.RaytracerException;

import util.Constants;
import util.Util;
import util.UtilImpl;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
public class Test_IntervalImpl
{
	Util ops;
	IsAnyDoubleArrayMatcher arrayMatcher;
	
	@Before
	public void setUp()
	{
		ops = new UtilImpl();
		arrayMatcher = new IsAnyDoubleArrayMatcher();
	}
	
	@Test(expected=NullPointerException.class)
	public void constructor_NullTArray_ExpectException() throws RaytracerException
	{
		//Arrange
		double[] hitTs = null;
		ops = null;
		
		//Act
		new IntervalImpl(hitTs);
		
		//Assert
		Assert.fail("Expected exception not thrown.");
	}
	
	@Test(expected=RaytracerException.class)
	public void constructor_LengthOneHitTArray_ExpectException() throws RaytracerException
	{
		//Arrange
		double[] hitTs = new double[]{1};
		ops = mock(Util.class);
		when(ops.sort(argThat(arrayMatcher))).thenReturn(hitTs);
		
		//Act
		new IntervalImpl(hitTs);
		
		//Assert
		Assert.fail("Expected exception not thrown.");
	}
	
	@Test
	public void constructor_ValidTArray_ExpectSortCall() throws RaytracerException
	{
		//Arrange
		double[] hitTs = new double[]{};
		ops = mock(Util.class);
		when(ops.sort(argThat(arrayMatcher))).thenReturn(new double[]{});
		
		//Act
		new IntervalImpl(hitTs, ops);
		
		//Assert
		verify(ops, times(1)).sort(hitTs);
	}
	
	@Test
	public void isInInterval_NotInIntervalSmallSide_ExpectFalse() throws RaytracerException
	{
		//Arrange
		double hitTs[] = new double[] {2.0,4.0, 6.0, 8.0};
		Interval classUnderTest = new IntervalImpl(hitTs);
		
		//Act
		boolean ret = classUnderTest.isInInterval(1.0);
		
		//Assert
		Assert.assertFalse(ret);
	}
	
	@Test
	public void isInInterval_NotInIntervalMiddle_ExpectFalse() throws RaytracerException
	{
		//Arrange
		double hitTs[] = new double[] {2.0,4.0, 6.0, 8.0};
		Interval classUnderTest = new IntervalImpl(hitTs);
		
		//Act
		boolean ret = classUnderTest.isInInterval(5.0);
		
		//Assert
		Assert.assertFalse(ret);
	}
	
	@Test
	public void isInInterval_NotInIntervalGreaterSide_ExpectFalse() throws RaytracerException
	{
		//Arrange
		double hitTs[] = new double[] {2.0,4.0, 6.0, 8.0};
		Interval classUnderTest = new IntervalImpl(hitTs);
		
		//Act
		boolean ret = classUnderTest.isInInterval(9.0);
		
		//Assert
		Assert.assertFalse(ret);
	}
	
	@Test
	public void isInInterval_InIntervalSmallSide_ExpectFalse() throws RaytracerException
	{
		//Arrange
		double hitTs[] = new double[] {2.0,4.0, 6.0, 8.0};
		Interval classUnderTest = new IntervalImpl(hitTs);
		
		//Act
		boolean ret = classUnderTest.isInInterval(3.0);
		
		//Assert
		Assert.assertTrue(ret);
	}
	
	@Test
	public void isInInterval_InIntervalGreaterSide_ExpectFalse() throws RaytracerException
	{
		//Arrange
		double hitTs[] = new double[] {2.0,4.0, 6.0, 8.0};
		Interval classUnderTest = new IntervalImpl(hitTs);
		
		//Act
		boolean ret = classUnderTest.isInInterval(7.0);
		
		//Assert
		Assert.assertTrue(ret);
	}

	@Test
	public void allInInterval_FirstIntervalCoveredSecondIntervalUncovered_ExpectFalse() throws RaytracerException
	{
		//Arrange
		double[] hits1 = new double[]{2.0,          6.0,           10.0,14.0};
		double[] hits2 = new double[]{     3.0,5.0,      7.0,9.0};
		IntervalImpl classUnderTest = new IntervalImpl(hits1);
		IntervalImpl otherInterval = new IntervalImpl(hits2);
		
		//Act
		boolean ret = classUnderTest.allInInterval(otherInterval);
		
		//Assert
		Assert.assertFalse(ret);
	}
	
	@Test
	public void allInInterval_FirstIntervalUnCoveredSecondIntervalCovered_ExpectFalse() throws RaytracerException
	{
		//Arrange
		double[] hits1 = new double[]{         2.0,          6.0,              10.0,14.0};
		double[] hits2 = new double[]{0.0,1.0,     3.0,  5.0};
		IntervalImpl classUnderTest = new IntervalImpl(hits1);
		IntervalImpl otherInterval = new IntervalImpl(hits2);
		
		//Act
		boolean ret = classUnderTest.allInInterval(otherInterval);
		
		//Assert
		Assert.assertFalse(ret);
	}
	
	@Test
	public void allInInterval_BothCovered_ExpectTrue() throws RaytracerException
	{
		//Arrange
		double[] hits1 = new double[]{2.0,          6.0,   10.0,           14.0};
		double[] hits2 = new double[]{     3.0,5.0,              11.0, 13.0};
		IntervalImpl classUnderTest = new IntervalImpl(hits1);
		IntervalImpl otherInterval = new IntervalImpl(hits2);
		
		//Act
		boolean ret = classUnderTest.allInInterval(otherInterval);
		
		//Assert
		Assert.assertTrue(ret);
	}
	
	@Test
	public void allInInterval_OneCovered_ExpectTrue() throws RaytracerException
	{
		//Arrange
		double[] hits1 = new double[]{2.0,          6.0,10.0,14.0};
		double[] hits2 = new double[]{     3.0,5.0};
		IntervalImpl classUnderTest = new IntervalImpl(hits1);
		IntervalImpl otherInterval = new IntervalImpl(hits2);
		
		//Act
		boolean ret = classUnderTest.allInInterval(otherInterval);
		
		//Assert
		Assert.assertTrue(ret);
	}
	
	@Test
	public void getNextGreatestInterval_WithTNotInIntervalInFront_ExpectNan() throws RaytracerException
	{
		//Arrange
		double[] rightSideHits = new double[]{1.0,3.0};
		double t = 0.0;
		IntervalImpl classUnderTest = new IntervalImpl(rightSideHits);
		
		//Act
		double result = classUnderTest.getNextGreatestInterval(t);
		
		//Assert
		Assert.assertEquals(Double.NaN, result);
	}
	
	@Test
	public void getNextGreatestInterval_WithTNotInIntervalInMiddle_ExpectNan() throws RaytracerException
	{
		//Arrange
		double[] rightSideHits = new double[]{1.0,3.0,5.0,7.0};
		double t = 4.0;
		IntervalImpl classUnderTest = new IntervalImpl(rightSideHits);
		
		//Act
		double result = classUnderTest.getNextGreatestInterval(t);
		
		//Assert
		Assert.assertEquals(Double.NaN, result);
	}
	
	@Test
	public void getNextGreatestInterval_WithTNotInIntervalAtEnd_ExpectNan() throws RaytracerException
	{
		//Arrange
		double[] rightSideHits = new double[]{1.0,3.0,5.0,7.0};
		double t = 9.0;
		IntervalImpl classUnderTest = new IntervalImpl(rightSideHits);
		
		//Act
		double result = classUnderTest.getNextGreatestInterval(t);
		
		//Assert
		Assert.assertEquals(Double.NaN, result);
	}
	
	@Test
	public void getNextGreatestInterval_WithTInNearInterval_ExpectNextGreatestInterval() throws RaytracerException
	{
		//Arrange
		double[] rightSideHits = new double[]{1.0,3.0,5.0,7.0};
		double t = 2.0;
		IntervalImpl classUnderTest = new IntervalImpl(rightSideHits);
		
		//Act
		double result = classUnderTest.getNextGreatestInterval(t);
		
		//Assert
		Assert.assertEquals(3.0, result, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void getNextGreatestInterval_WithTInFarInterval_ExpectNextGreatestInterval() throws RaytracerException
	{
		//Arrange
		double[] rightSideHits = new double[]{1.0,3.0,5.0,7.0};
		double t = 6.0;
		IntervalImpl classUnderTest = new IntervalImpl(rightSideHits);
		
		//Act
		double result = classUnderTest.getNextGreatestInterval(t);
		
		//Assert
		Assert.assertEquals(7.0, result, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void mergeTIntervals_RightIntervalBetweenLeft_ExpectRightIntervalImpl() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 5.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 5.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalGreaterThanLeft_ExpectLeftIntervalImpl() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 4.0;
		a2[1] = 5.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalGreaterThanLeftTwoIntervalsRight_ExpectLeftIntervalImpl() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[4];
		a2[0] = 8.0;
		a2[1] = 10.0;
		a2[2] = 12.0;
		a2[3] = 14.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalGreaterThanLeftTwoIntervalsLeft_ExpectLeftIntervalImpl() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 8.0;
		a2[1] = 10.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalBeforeLeft_ExpectLeftIntervalImpl() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = -1.0;
		a2[1] = 0.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalBeforeLeftTwoIntervalsRight_ExpectLeftIntervalImpl() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = -1.0;
		a2[1] = 0.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalBeforeLeftTwoIntervalsLeft_ExpectLeftIntervalImpl() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[4];
		a2[0] = -5.0;
		a2[1] = -3.0;
		a2[2] = -1.0;
		a2[3] = 0.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightBoundsLeftFrontHalf_ExpectLeftNotPresent() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 0.0;
		a2[1] = 4.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 7.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void mergeTIntervals_RightBoundsLeftBackHalf_ExpectLeftNotPresent() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 4.0;
		a2[1] = 8.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}

	public void mergTIntervals_LeftBetweenButUnaffected_ExpectLeftUnchanged() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[4];
		a2[0] = -1.0;
		a2[1] = 0.0;
		a2[2] = 4.0;
		a2[3] = 5.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}

	public void mergeTIntervals_BackLeftBoundedByRight_ExpectBackLeftBoundedByRight() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 2.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void mergeTIntervals_BackLeftBoundedByRightTwoBounds_ExpectBackLeftBoundedByRight() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 2.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void mergeTIntervals_BackLeftBoundedByRightCrossTwoIntervals_ExpectBackLeftBoundedByRight() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 6.0;
		Interval i2 = new IntervalImpl(a2);

		//Act
		double[] answer = i1.mergeTIntervalsAndSort(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 2.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 6.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void mergeTIntervals_BackLeftBoundedByRightMultipleIntervals_ExpectLeftBoundedByRight() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[6];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		a1[4] = 9.0;
		a1[5] = 11.0;
		Interval i1 = new IntervalImpl(a1);
		double[] a2 = new double[4];
		a2[0] = 0.0;
		a2[1] = 6.0;
		a2[2] = 8.0;
		a2[3] = 10.0;
		Interval i2 = new IntervalImpl(a2);

		//Act		
		double[] answer = i1.mergeTIntervalsAndSort(i2);
		
		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 6.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 7.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 10.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 11.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void mergeTIntervals_WithValues_ExpectSortCall() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		double[] a2 = new double[2];
		a2[0] = 0.0;
		a2[1] = 6.0;
		Util mockUtil = mock(Util.class);
		when(mockUtil.sort(argThat(arrayMatcher))).thenReturn(a1);
		Interval classUnderTest = new IntervalImpl(a1, mockUtil);
		Interval i2 = new IntervalImpl(a2);

		//Act		
		double[] answer = classUnderTest.mergeTIntervalsAndSort(i2);
		
		//Assert
		verify(mockUtil, times(1)).sort(answer);
	}
}
