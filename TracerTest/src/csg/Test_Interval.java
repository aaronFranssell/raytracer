package csg;

import static org.junit.Assert.*;
import junit.framework.Assert;

import matchers.IsAnyDoubleArrayMatcher;

import org.junit.Before;
import org.junit.Test;

import csg.operation.Interval;

import etc.RaytracerException;

import util.Constants;
import util.Util;
import util.UtilImpl;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
public class Test_Interval
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
		new Interval(hitTs, ops);
		
		//Assert
		Assert.fail("Expected exception not thrown.");
	}
	
	@Test(expected=NullPointerException.class)
	public void constructor_NullOps_ExpectException() throws RaytracerException
	{
		//Arrange
		double[] hitTs = new double[]{};
		ops = null;
		
		//Act
		new Interval(hitTs, ops);
		
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
		new Interval(hitTs, ops);
		
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
		new Interval(hitTs, ops);
		
		//Assert
		verify(ops, times(1)).sort(hitTs);
	}

	@Test
	public void mergeTIntervals_RightIntervalBetweenLeft_ExpectRightInterval() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 5.0;
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 5.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalGreaterThanLeft_ExpectLeftInterval() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 4.0;
		a2[1] = 5.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalGreaterThanLeftTwoIntervalsRight_ExpectLeftInterval() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[4];
		a2[0] = 8.0;
		a2[1] = 10.0;
		a2[2] = 12.0;
		a2[3] = 14.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalGreaterThanLeftTwoIntervalsLeft_ExpectLeftInterval() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 8.0;
		a2[1] = 10.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalBeforeLeft_ExpectLeftInterval() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = -1.0;
		a2[1] = 0.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

		//Assert
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalBeforeLeftTwoIntervalsRight_ExpectLeftInterval() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = -1.0;
		a2[1] = 0.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}

	@Test
	public void mergeTIntervals_RightIntervalBeforeLeftTwoIntervalsLeft_ExpectLeftInterval() throws RaytracerException
	{
		//Arrange
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[4];
		a2[0] = -5.0;
		a2[1] = -3.0;
		a2[2] = -1.0;
		a2[3] = 0.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

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
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 0.0;
		a2[1] = 4.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

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
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 4.0;
		a2[1] = 8.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

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
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[4];
		a2[0] = -1.0;
		a2[1] = 0.0;
		a2[2] = 4.0;
		a2[3] = 5.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

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
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

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
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

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
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 6.0;
		Interval i2 = new Interval(a2, ops);

		//Act
		double[] answer = i1.mergeTIntervals(i2);

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
		Interval i1 = new Interval(a1, ops);
		double[] a2 = new double[4];
		a2[0] = 0.0;
		a2[1] = 6.0;
		a2[2] = 8.0;
		a2[3] = 10.0;
		Interval i2 = new Interval(a2, ops);

		//Act		
		double[] answer = i1.mergeTIntervals(i2);
		
		//Assert
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 6.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 7.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 10.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 11.0, Constants.POSITIVE_ZERO);
	}
}
