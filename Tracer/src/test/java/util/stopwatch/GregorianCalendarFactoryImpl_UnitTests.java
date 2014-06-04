package util.stopwatch;

import java.util.GregorianCalendar;

import org.junit.Assert;

import org.junit.Test;

public class GregorianCalendarFactoryImpl_UnitTests
{
	@Test
	public void getCalendar_WithFactoryImpl_ExpectNotNull()
	{
		//Given
		GregorianCalendarFactoryImpl classUnderTest = new GregorianCalendarFactoryImpl();
		
		//When
		GregorianCalendar ret = classUnderTest.getCalendar();
		
		//Then
		Assert.assertNotNull(ret);
	}
}
