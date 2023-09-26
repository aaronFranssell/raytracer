package scene.viewer.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

import scene.render.ResultsConverter;
import scene.render.factory.ResultsConverterFactoryImpl;

public class ResultsConverterFactoryImpl_UnitTests
{
	@Test
	public void getConverter_WithValue_ExpectNotNull()
	{
		
		Collection<Future<double[][][]>> incomingResults = new ArrayList<Future<double[][][]>>();
		int incomingWidth = 5;
		int incomingHeight = 7;
		
		ResultsConverterFactoryImpl classUnderTest = new ResultsConverterFactoryImpl();
		
		
		ResultsConverter ret = classUnderTest.getConverter(incomingResults, incomingWidth, incomingHeight);
		
		
		assertNotNull(ret);
	}
}
