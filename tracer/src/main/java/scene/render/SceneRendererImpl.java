package scene.render;

import etc.RaytracerException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import math.Point;
import math.UVW;
import math.UVWFactory;
import math.UVWFactoryImpl;
import math.Vector;
import scene.Scene;
import scene.render.factory.ExecutorServiceFactory;
import scene.render.factory.ExecutorServiceFactoryImpl;
import scene.render.factory.RenderThreadFactory;
import scene.render.factory.RenderThreadFactoryImpl;
import scene.render.factory.ResultsConverterFactory;
import scene.render.factory.ResultsConverterFactoryImpl;
import scene.viewer.ViewingVolume;
import scene.viewer.factory.ViewingVolumeFactory;
import scene.viewer.factory.ViewingVolumeFactoryImpl;
import util.stopwatch.StopWatch;
import util.stopwatch.StopWatchImpl;

public class SceneRendererImpl {
  private Vector up;
  private Vector gaze;
  private Point eye;
  private int width;
  private int height;
  private int numThreads;
  private Scene scene;
  private UVW basis;
  private Point light;
  private StopWatch stopWatch;
  private RenderThreadFactory renderThreadFactory;
  private ViewingVolume viewVolume;
  private ResultsConverterFactory resultsConverterFactory;
  private ArrayList<Callable<double[][][]>> tasks;
  private ExecutorService workers;
  private int maxDepth;

  public SceneRendererImpl(
      Vector incomingUp,
      Vector incomingGaze,
      Point incomingEye,
      int incomingLeft,
      int incomingRight,
      int incomingTop,
      int incomingBottom,
      int incomingWidth,
      int incomingHeight,
      int incomingNumThreads,
      Scene incomingScene,
      Point incomingLight,
      UVWFactory factory,
      StopWatch incomingStopWatch,
      RenderThreadFactory incomingRenderThreadFactory,
      ViewingVolumeFactory incomingViewingVolumeFactory,
      ResultsConverterFactory incomingResultsConverterFactory,
      ArrayList<Callable<double[][][]>> incomingTasks,
      ExecutorServiceFactory serviceFactory,
      int incomingMaxDepth)
      throws RaytracerException {
    numThreads = incomingNumThreads;
    up = incomingUp;
    gaze = incomingGaze;
    eye = incomingEye;
    width = incomingWidth;
    height = incomingHeight;
    basis = factory.createUVW(up, gaze);
    light = incomingLight;
    scene = incomingScene;
    stopWatch = incomingStopWatch;
    renderThreadFactory = incomingRenderThreadFactory;
    viewVolume =
        incomingViewingVolumeFactory.getVolume(
            incomingLeft, incomingRight, incomingBottom, incomingTop);
    resultsConverterFactory = incomingResultsConverterFactory;
    tasks = incomingTasks;
    workers = serviceFactory.getExecutorService();
    maxDepth = incomingMaxDepth;
  }

  public SceneRendererImpl(
      Vector incomingUp,
      Vector incomingGaze,
      Point incomingEye,
      int incomingLeft,
      int incomingRight,
      int incomingTop,
      int incomingBottom,
      int incomingWidth,
      int incomingHeight,
      int incomingNumThreads,
      Scene incomingScene,
      Point incomingLight,
      int incomingMaxDepth)
      throws RaytracerException {
    this(
        incomingUp,
        incomingGaze,
        incomingEye,
        incomingLeft,
        incomingRight,
        incomingTop,
        incomingBottom,
        incomingWidth,
        incomingHeight,
        incomingNumThreads,
        incomingScene,
        incomingLight,
        new UVWFactoryImpl(),
        new StopWatchImpl(),
        new RenderThreadFactoryImpl(),
        new ViewingVolumeFactoryImpl(),
        new ResultsConverterFactoryImpl(),
        new ArrayList<Callable<double[][][]>>(),
        new ExecutorServiceFactoryImpl(),
        incomingMaxDepth);
  }

  public RenderResult render() throws IOException, InterruptedException, ExecutionException {
    RenderResult renderResult = new RenderResult();

    stopWatch.start();

    int threadHeight = height / numThreads;

    int leftOverThreadHeight = height % numThreads;

    List<Future<double[][][]>> results = renderScene(threadHeight, leftOverThreadHeight);

    ResultsConverter resultsConverter =
        resultsConverterFactory.getConverter(results, width, height);

    BufferedImage image = resultsConverter.getImageFromResults();

    renderResult.setImage(image);
    renderResult.setStopWatch(stopWatch);
    stopWatch.stop();
    return renderResult;
  }

  private List<Future<double[][][]>> renderScene(int incomingThreadHeight, int leftOverThreadHeight)
      throws InterruptedException {
    removeAllTasks();
    for (int i = 0; i < numThreads; i++) {
      int threadHeight = incomingThreadHeight;
      int startHeight = i * threadHeight;

      if (i == 0) {
        threadHeight += leftOverThreadHeight;
      }

      RenderThread thread =
          renderThreadFactory.getRenderThread(
              eye,
              viewVolume,
              width,
              height,
              basis,
              light,
              scene,
              startHeight,
              threadHeight,
              maxDepth);
      tasks.add(thread);
    }
    List<Future<double[][][]>> results = workers.invokeAll(tasks);
    return results;
  }

  private void removeAllTasks() {
    while (tasks.size() > 0) {
      tasks.remove(tasks.get(0));
    }
  }
}
