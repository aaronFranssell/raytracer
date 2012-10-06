package scene.render.factory;

import java.util.concurrent.ExecutorService;

import junit.framework.Assert;

import org.junit.Test;

public class ExecutorServiceFactoryImpl_UnitTests
{
	@Test
	public void getExecutorService_ExpectNotNull()
	{
		//Given
		ExecutorServiceFactoryImpl classUnderTest = new ExecutorServiceFactoryImpl();
		
		//When
		ExecutorService ret = classUnderTest.getExecutorService();
		
		//Then
		Assert.assertNotNull(ret);
	}
}
