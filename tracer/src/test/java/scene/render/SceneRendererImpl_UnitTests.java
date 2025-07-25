package scene.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import math.Point;
import math.UVW;
import math.UVWFactory;
import math.Vector;
import scene.Scene;
import scene.render.factory.ExecutorServiceFactory;
import scene.render.factory.RenderThreadFactory;
import scene.render.factory.ResultsConverterFactory;
import scene.viewer.ViewingVolume;
import scene.viewer.factory.ViewingVolumeFactory;
import util.stopwatch.StopWatch;

public class SceneRendererImpl_UnitTests {
	@Test
	public void render_WithMocks_ExpectCall()
			throws Exception, InterruptedException, ExecutionException, IOException {

		Vector up = Mockito.mock(Vector.class);
		Vector gaze = Mockito.mock(Vector.class);
		Point eye = Mockito.mock(Point.class);
		int left = -1;
		int right = 1;
		int top = 1;
		int bottom = -1;
		int width = 800;
		int height = 800;
		int numThreads = 3;
		int maxDepth = 6;
		Scene scene = Mockito.mock(Scene.class);
		Point light = Mockito.mock(Point.class);
		UVW mockBasis = Mockito.mock(UVW.class);
		ViewingVolume mockVolume = Mockito.mock(ViewingVolume.class);
		UVWFactory mockUVWFactory = Mockito.mock(UVWFactory.class);
		StopWatch mockStopWatch = Mockito.mock(StopWatch.class);
		RenderThreadFactory mockRenderThreadFactory = Mockito.mock(RenderThreadFactory.class);
		ViewingVolumeFactory mockViewingVolumeFactory = Mockito.mock(ViewingVolumeFactory.class);
		ResultsConverterFactory mockConverterFactory = Mockito.mock(ResultsConverterFactory.class);
		ArrayList<Callable<double[][][]>> tasks = new ArrayList<Callable<double[][][]>>();
		RenderThread mockThread = Mockito.mock(RenderThread.class);
		ExecutorService mockExecutorService = Mockito.mock(ExecutorService.class);
		ExecutorServiceFactory mockExecutorServiceFactory = Mockito.mock(ExecutorServiceFactory.class);
		List<Future<double[][][]>> results = new ArrayList<Future<double[][][]>>();
		ResultsConverter mockResultsConverter = Mockito.mock(ResultsConverter.class);
		BufferedImage retImage = Mockito.mock(BufferedImage.class);

		Mockito.when(mockUVWFactory.createUVW(up, gaze)).thenReturn(mockBasis);
		Mockito.when(mockViewingVolumeFactory.getVolume(left, right, bottom, top))
				.thenReturn(mockVolume);
		Mockito.when(
				mockRenderThreadFactory.getRenderThread(
						eye, mockVolume, width, height, mockBasis, light, scene, 0, 268, maxDepth))
				.thenReturn(mockThread);
		Mockito.when(
				mockRenderThreadFactory.getRenderThread(
						eye, mockVolume, width, height, mockBasis, light, scene, 268, 266, maxDepth))
				.thenReturn(mockThread);
		Mockito.when(
				mockRenderThreadFactory.getRenderThread(
						eye, mockVolume, width, height, mockBasis, light, scene, 534, 266, maxDepth))
				.thenReturn(mockThread);
		Mockito.when(mockExecutorServiceFactory.getExecutorService()).thenReturn(mockExecutorService);
		Mockito.when(mockExecutorService.invokeAll(tasks)).thenReturn(results);
		Mockito.when(mockConverterFactory.getConverter(results, width, height))
				.thenReturn(mockResultsConverter);
		Mockito.when(mockResultsConverter.getImageFromResults()).thenReturn(retImage);

		SceneRenderer classUnderTest = new SceneRenderer(
				up,
				gaze,
				eye,
				left,
				right,
				top,
				bottom,
				width,
				height,
				numThreads,
				scene,
				light,
				mockUVWFactory,
				mockStopWatch,
				mockRenderThreadFactory,
				mockViewingVolumeFactory,
				mockConverterFactory,
				tasks,
				mockExecutorServiceFactory,
				maxDepth);

		RenderResult ret = classUnderTest.render();

		assertEquals(ret.getStopWatch(), mockStopWatch);
		assertEquals(ret.getImage(), retImage);
		Mockito.verify(mockStopWatch, Mockito.times(1)).start();
		Mockito.verify(mockStopWatch, Mockito.times(1)).stop();
		assertEquals(tasks.size(), 3);
	}
}
