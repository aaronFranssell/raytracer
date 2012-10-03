package scene.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import math.Point;
import math.UVW;
import math.UVWFactory;
import math.UVWFactoryImpl;
import math.Vector;
import scene.Scene;
import scene.render.factory.RenderThreadFactory;
import scene.render.factory.RenderThreadFactoryImpl;
import scene.render.factory.ResultsConverterFactory;
import scene.render.factory.ResultsConverterFactoryImpl;
import scene.viewer.ViewingVolume;
import scene.viewer.factory.ViewingVolumeFactory;
import scene.viewer.factory.ViewingVolumeFactoryImpl;
import util.stopwatch.StopWatch;
import util.stopwatch.StopWatchImpl;
import etc.RaytracerException;

public class SceneRenderer
{
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
	private RenderResult renderResult;
	private RenderThreadFactory renderThreadFactory;
	private ViewingVolume viewVolume;
	private ResultsConverterFactory resultsConverterFactory;
	
	private static final ExecutorService workers = Executors.newCachedThreadPool();
	
	public SceneRenderer(Vector incomingUp, Vector incomingGaze, Point incomingEye, int incomingLeft, int incomingRight, int incomingTop, int incomingBottom, int incomingWidth,
						 int incomingHeight, int incomingNumThreads, Scene incomingScene, Point incomingLight, UVWFactory factory,
						 StopWatch incomingStopWatch, RenderThreadFactory incomingRenderThreadFactory, ViewingVolumeFactory incomingViewingVolumeFactory,
						 ResultsConverterFactory incomingResultsConverterFactory) throws RaytracerException
	{
		if(incomingNumThreads <= 0)
		{
			throw new RaytracerException("Number of threads must be greater than zero!");
		}
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
		renderResult = new RenderResult();
		renderThreadFactory = incomingRenderThreadFactory;
		viewVolume = incomingViewingVolumeFactory.getVolume(incomingLeft, incomingRight, incomingBottom, incomingTop);
		resultsConverterFactory = incomingResultsConverterFactory;
	}
	
	public SceneRenderer(Vector incomingUp, Vector incomingGaze, Point incomingEye, int incomingLeft, int incomingRight, int incomingTop, int incomingBottom, int incomingWidth,
			 int incomingHeight, int incomingNumThreads, Scene incomingScene, Point incomingLight) throws RaytracerException
	{
		this(incomingUp, incomingGaze, incomingEye, incomingLeft, incomingRight, incomingTop, incomingBottom, incomingWidth, incomingHeight,
			 incomingNumThreads, incomingScene, incomingLight, new UVWFactoryImpl(), new StopWatchImpl(), new RenderThreadFactoryImpl(), new ViewingVolumeFactoryImpl(),
			 new ResultsConverterFactoryImpl());
	}
	
	public RenderResult render() throws IOException, InterruptedException, ExecutionException
	{
		stopWatch.start();

		int threadHeight = height / numThreads;
		
		int leftOverThreadHeight = height % numThreads;
		
		List<Future<double[][][]>> results = renderScene(threadHeight, leftOverThreadHeight);
		
		ResultsConverter resultsConverter = resultsConverterFactory.getConverter(results, width, height);
		
		BufferedImage image = resultsConverter.getImageFromResults();
		
		renderResult.setImage(image);
		renderResult.setStopWatch(stopWatch);
		stopWatch.stop();
		return renderResult;
	}

	private List<Future<double[][][]>> renderScene(int incomingThreadHeight, int leftOverThreadHeight) throws InterruptedException
	{
		Collection<Callable<double[][][]>> tasks = new ArrayList<Callable<double[][][]>>();
		
		for(int i = 0; i < numThreads; i++)
		{
			int threadHeight = incomingThreadHeight;
			int startHeight = i * threadHeight;
			
			if(i == 0)
			{
				threadHeight += leftOverThreadHeight;
			}
			
			RenderThread thread = renderThreadFactory.getRenderThread(eye, viewVolume, width, height, basis, light, scene, startHeight, threadHeight);
			tasks.add(thread);
		}
		List<Future<double[][][]>> results = workers.invokeAll(tasks);
		return results;
	}
}
