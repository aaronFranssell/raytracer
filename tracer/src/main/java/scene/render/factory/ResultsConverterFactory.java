package scene.render.factory;

import java.util.Collection;
import java.util.concurrent.Future;
import scene.render.ResultsConverter;

public interface ResultsConverterFactory {
  public ResultsConverter getConverter(
      Collection<Future<double[][][]>> incomingResults, int incomingWidth, int incomingHeight);
}
