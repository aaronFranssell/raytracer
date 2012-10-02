package scene.viewer.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Future;

import junit.framework.Assert;

import org.junit.Test;

import scene.render.ResultsConverter;
import scene.render.factory.ResultsConverterFactoryImpl;

public class ResultsConverterFactoryImpl_UnitTests
{
	@Test
	public void getConverter_WithValue_ExpectNotNull()
	{
		//Given
		Collection<Future<double[][][]>> incomingResults = new ArrayList<Future<double[][][]>>();
		int incomingWidth = 5;
		int incomingHeight = 7;
		
		ResultsConverterFactoryImpl classUnderTest = new ResultsConverterFactoryImpl();
		
		//When
		ResultsConverter ret = classUnderTest.getConverter(incomingResults, incomingWidth, incomingHeight);
		
		//Then
		Assert.assertNotNull(ret);
	}
}
