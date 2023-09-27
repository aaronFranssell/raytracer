package scene.render.factory;

import java.util.Collection;
import java.util.concurrent.Future;
import scene.render.ResultsConverter;
import scene.render.ResultsConverterImpl;

public class ResultsConverterFactoryImpl implements ResultsConverterFactory {

	@Override
	public ResultsConverter getConverter(
			Collection<Future<double[][][]>> incomingResults, int incomingWidth, int incomingHeight) {
		return new ResultsConverterImpl(incomingResults, incomingWidth, incomingHeight);
	}
}
