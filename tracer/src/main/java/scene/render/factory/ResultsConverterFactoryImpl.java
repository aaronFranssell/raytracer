package scene.render.factory;

import java.util.Collection;
import java.util.concurrent.Future;
import scene.render.ResultsConverter;

public class ResultsConverterFactoryImpl implements ResultsConverterFactory {

	@Override
	public ResultsConverter getConverter(
			Collection<Future<double[][][]>> incomingResults, int incomingWidth, int incomingHeight) {
		return new ResultsConverter(incomingResults, incomingWidth, incomingHeight);
	}
}
