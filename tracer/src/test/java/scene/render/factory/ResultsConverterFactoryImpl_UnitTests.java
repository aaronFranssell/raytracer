package scene.render.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

import scene.render.ResultsConverter;

public class ResultsConverterFactoryImpl_UnitTests
{
	@Test
	public void getConverter_WithValues_ExpectNotNull()
	{
		
		Collection<Future<double[][][]>> results = null;
		int width = 4;
		int height = 8;
		
		ResultsConverterFactoryImpl classUnderTest = new ResultsConverterFactoryImpl();
		
		
		ResultsConverter result = classUnderTest.getConverter(results, width, height);
		
		
		assertNotNull(result);
	}
}
