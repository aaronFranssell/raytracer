package util.stopwatch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

public class GregorianCalendarFactoryImpl_UnitTests
{
	@Test
	public void getCalendar_WithFactoryImpl_ExpectNotNull()
	{
		
		GregorianCalendarFactoryImpl classUnderTest = new GregorianCalendarFactoryImpl();
		
		
		GregorianCalendar ret = classUnderTest.getCalendar();
		
		
		assertNotNull(ret);
	}
}
