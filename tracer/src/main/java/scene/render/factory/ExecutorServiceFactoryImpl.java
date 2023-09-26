package scene.render.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceFactoryImpl implements ExecutorServiceFactory {
  @Override
  public ExecutorService getExecutorService() {
    return Executors.newCachedThreadPool();
  }
}
