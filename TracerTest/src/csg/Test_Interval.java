package csg;

import static org.junit.Assert.*;

import org.junit.Test;

import util.Constants;

public class Test_Interval
{
	@Test
	public void MergeTIntervals_RightIntervalBetweenLeft_ExpectRightInterval()
	{
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 5.0;
		Interval i1 = new Interval(a1);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new Interval(a2);
		
		double[] answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 5.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void MergeTIntervals_RightIntervalGreaterThanLeft_ExpectLeftInterval()
	{
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new Interval(a1);
		double[] a2 = new double[2];
		a2[0] = 4.0;
		a2[1] = 5.0;
		Interval i2 = new Interval(a2);
		
		double[] answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		
		a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		i1 = new Interval(a1);
		a2 = new double[2];
		a2[0] = 8.0;
		a2[1] = 10.0;
		i2 = new Interval(a2);
		
		answer = i1.mergeTIntervals(i2);
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
		
		a2 = new double[4];
		a2[0] = 8.0;
		a2[1] = 10.0;
		a2[2] = 12.0;
		a2[3] = 14.0;
		i2 = new Interval(a2);
		
		answer = i1.mergeTIntervals(i2);
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void MergeTIntervals_RightIntervalBeforeLeft_ExpectLeftInterval()
	{
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new Interval(a1);
		double[] a2 = new double[2];
		a2[0] = -1.0;
		a2[1] = 0.0;
		Interval i2 = new Interval(a2);
		
		double[] answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		
		a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		i1 = new Interval(a1);

		answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
		
		a2 = new double[4];
		a2[0] = -5.0;
		a2[1] = -3.0;
		a2[2] = -1.0;
		a2[3] = 0.0;
		i2 = new Interval(a2);
		
		answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
	}
	
	public void MergeTIntervals_RightBoundsLeft_ExpectedLeftNotPresent()
	{
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		Interval i1 = new Interval(a1);
		double[] a2 = new double[2];
		a2[0] = 0.0;
		a2[1] = 4.0;
		Interval i2 = new Interval(a2);
		
		double[] answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 7.0, Constants.POSITIVE_ZERO);
		
		a2[0] = 4.0;
		a2[1] = 8.0;
		i2 = new Interval(a2);
		
		answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}
	
	public void MergTIntervals_LeftBetweenButUnaffected_ExpectLeftUnchanged()
	{
		double[] a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new Interval(a1);
		double[] a2 = new double[4];
		a2[0] = -1.0;
		a2[1] = 0.0;
		a2[2] = 4.0;
		a2[3] = 5.0;
		Interval i2 = new Interval(a2);
				
		double[] answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 3.0, Constants.POSITIVE_ZERO);
	}
	
	public void MergeTIntervals_BackLeftBoundedByRight_ExpectBackLeftBoundedByRight()
	{
		double[] a1 = new double[2];
		a1[0] = 1.0;
		a1[1] = 3.0;
		Interval i1 = new Interval(a1);
		double[] a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		Interval i2 = new Interval(a2);
		
		double[] answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 2);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 2.0, Constants.POSITIVE_ZERO);
		
		a1 = new double[4];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		i1 = new Interval(a1);
		a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 4.0;
		i2 = new Interval(a2);

		answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 2.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 5.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
		
		a2 = new double[2];
		a2[0] = 2.0;
		a2[1] = 6.0;
		i2 = new Interval(a2);
		
		answer = i1.mergeTIntervals(i2);
		
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 1.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 2.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 6.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 7.0, Constants.POSITIVE_ZERO);
		
		a1 = new double[6];
		a1[0] = 1.0;
		a1[1] = 3.0;
		a1[2] = 5.0;
		a1[3] = 7.0;
		a1[4] = 9.0;
		a1[5] = 11.0;
		i1 = new Interval(a1);
		a2 = new double[4];
		a2[0] = 0.0;
		a2[1] = 6.0;
		a2[2] = 8.0;
		a2[3] = 10.0;
		i2 = new Interval(a2);
		answer = i1.mergeTIntervals(i2);
		assertEquals(answer.length, 4);
		assertEquals(answer[0], 6.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[1], 7.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[2], 10.0, Constants.POSITIVE_ZERO);
		assertEquals(answer[3], 11.0, Constants.POSITIVE_ZERO);
	}
}