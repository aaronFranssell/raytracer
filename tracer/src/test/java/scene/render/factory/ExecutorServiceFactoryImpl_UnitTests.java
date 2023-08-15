package scene.render.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.Test;

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
		assertNotNull(ret);
	}
}
