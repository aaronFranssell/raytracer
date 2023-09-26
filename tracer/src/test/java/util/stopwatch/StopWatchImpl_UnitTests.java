package util.stopwatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StopWatchImpl_UnitTests
{
	@Test
	public void start_WithMocks_ExpectCall()
	{
		
		GregorianCalendar begin = new GregorianCalendar();
		GregorianCalendarFactory factory = Mockito.mock(GregorianCalendarFactory.class);
		Mockito.when(factory.getCalendar()).thenReturn(begin);
		
		StopWatchImpl classUnderTest = new StopWatchImpl(factory);
		
		
		classUnderTest.start();
		
		
		assertTrue(classUnderTest.getStart().equals(begin));
	}
	
	@Test
	public void stop_WithMocks_ExpectCall()
	{
		
		GregorianCalendar end = new GregorianCalendar();
		GregorianCalendarFactory factory = Mockito.mock(GregorianCalendarFactory.class);
		Mockito.when(factory.getCalendar()).thenReturn(end);
		
		StopWatchImpl classUnderTest = new StopWatchImpl(factory);
		
		
		classUnderTest.stop();
		
		
		assertTrue(classUnderTest.getEnd().equals(end));
	}
	
	@Test
	public void getDifference_WithStartAndStopNotCalled_ExpectErrorMessage()
	{
		
		GregorianCalendar c = new GregorianCalendar();
		GregorianCalendarFactory factory = Mockito.mock(GregorianCalendarFactory.class);
		Mockito.when(factory.getCalendar()).thenReturn(c);
		
		StopWatchImpl classUnderTest = new StopWatchImpl(factory);
		
		
		String ret = classUnderTest.getDifference();
		
		
		assertEquals("Start and stop were not called on the stopwatch.", ret);
	}
	
	@Test
	public void getDifference_WithStartNotCalled_ExpectErrorMessage()
	{
		
		GregorianCalendar c = new GregorianCalendar();
		GregorianCalendarFactory factory = Mockito.mock(GregorianCalendarFactory.class);
		Mockito.when(factory.getCalendar()).thenReturn(c);
		
		StopWatchImpl classUnderTest = new StopWatchImpl(factory);
		classUnderTest.stop();
		
		
		String ret = classUnderTest.getDifference();
		
		
		assertEquals("Start was not called on the stopwatch.", ret);
	}
	
	@Test
	public void getDifference_WithStopNotCalled_ExpectErrorMessage()
	{
		
		GregorianCalendar c = new GregorianCalendar();
		GregorianCalendarFactory factory = Mockito.mock(GregorianCalendarFactory.class);
		Mockito.when(factory.getCalendar()).thenReturn(c);
		
		StopWatchImpl classUnderTest = new StopWatchImpl(factory);
		classUnderTest.start();
		
		
		String ret = classUnderTest.getDifference();
		
		
		assertEquals("Stop was not called on the stopwatch.", ret);
	}
	
	@Test
	public void getDifference_StopCalledBeforeStart_ExpectErrorMessage()
	{
		
		GregorianCalendar first = Mockito.mock(GregorianCalendar.class);
		Mockito.when(first.getTimeInMillis()).thenReturn((long) 1);
		GregorianCalendar second = Mockito.mock(GregorianCalendar.class);
		Mockito.when(second.getTimeInMillis()).thenReturn((long) 7);
		
		GregorianCalendarFactory factory = Mockito.mock(GregorianCalendarFactory.class);
		Mockito.when(factory.getCalendar()).thenReturn(first).thenReturn(second);
		
		StopWatchImpl classUnderTest = new StopWatchImpl(factory);
		
		classUnderTest.stop();
		classUnderTest.start();
		
		
		String ret = classUnderTest.getDifference();
		
		
		assertEquals("The stopwatch was stopped before it was started.", ret);
	}
	
	@Test
	public void getDifference_WithMocks_ExpectDifferenceMessage()
	{
		
		GregorianCalendar first = new GregorianCalendar();
		GregorianCalendar second = new GregorianCalendar();
		second.setTime(first.getTime());
		
		first.add(GregorianCalendar.MINUTE, -41);
		first.add(GregorianCalendar.SECOND, -53);
		
		GregorianCalendarFactory factory = Mockito.mock(GregorianCalendarFactory.class);
		Mockito.when(factory.getCalendar()).thenReturn(first).thenReturn(second);
		
		StopWatchImpl classUnderTest = new StopWatchImpl(factory);
		
		classUnderTest.start();
		classUnderTest.stop();
		
		
		String ret = classUnderTest.getDifference();
		
		
		assertEquals("Executed in 41 minute(s) and 53 seconds.", ret);
	}
}
