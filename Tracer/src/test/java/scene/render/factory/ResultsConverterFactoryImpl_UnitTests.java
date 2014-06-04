package scene.render.factory;

import java.util.Collection;
import java.util.concurrent.Future;

import org.junit.Assert;

import org.junit.Test;

import scene.render.ResultsConverter;

public class ResultsConverterFactoryImpl_UnitTests
{
	@Test
	public void getConverter_WithValues_ExpectNotNull()
	{
		//Given
		Collection<Future<double[][][]>> results = null;
		int width = 4;
		int height = 8;
		
		ResultsConverterFactoryImpl classUnderTest = new ResultsConverterFactoryImpl();
		
		//When
		ResultsConverter result = classUnderTest.getConverter(results, width, height);
		
		//Then
		Assert.assertNotNull(result);
	}
}
