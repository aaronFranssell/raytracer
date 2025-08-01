package scene.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import etc.Color;
import math.Point;
import math.UVW;
import math.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import scene.Scene;
import scene.pixel.ScenePixel;
import scene.pixel.ScenePixelFactory;
import scene.ray.Ray;
import scene.ray.RayFactory;
import scene.ray.RayImpl;
import scene.viewer.ViewingVolume;
import util.Constants;

public class RenderThreadImpl_UnitTests {
	@Test
	public void call_WithValidParameters_ExpectFactoryCalls() throws Exception {

		Point eye = new Point(0.0, 0.0, 4.0);
		ViewingVolume volume = new ViewingVolume(-1.0, 1.0, -1.0, 1.0);
		int width = 40;
		int height = 40;
		UVW basis = new UVW(new Vector(1.0, 0.0, 0.0), new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 1.0));
		Point light = new Point(0.0, 100.0, 0.0);
		Scene mockScene = Mockito.mock(Scene.class);
		int startHeight = 20;
		int threadHeight = 20;
		int maxDepth = 6;

		Ray ray = new RayImpl(new Vector(1.0, 0.0, 0.0), eye);
		RayFactory mockRayFactory = RenderThreadImpl_TestsHelper.getMockRayFactory(
				width, threadHeight, ray, volume, eye, basis, height, startHeight);

		ScenePixel mockPixel = Mockito.mock(ScenePixel.class);
		double red = 0.7;
		double green = 0.5;
		double blue = 0.3;
		Mockito.when(mockPixel.getPixelColor(ray, 0)).thenReturn(new Color(red, green, blue));

		ScenePixelFactory mockPixelFactory = Mockito.mock(ScenePixelFactory.class);
		Mockito.when(mockPixelFactory.createScenePixel(mockScene, eye, light, maxDepth))
				.thenReturn(mockPixel);

		RenderThread classUnderTest = new RenderThread(
				eye,
				volume,
				width,
				height,
				basis,
				light,
				mockScene,
				startHeight,
				threadHeight,
				mockRayFactory,
				mockPixelFactory,
				maxDepth);

		double[][][] retColors = classUnderTest.call();

		for (int i = 0; i < width; i++) {
			for (int m = 0; m < threadHeight; m++) {
				assertEquals(retColors[i][m][0], red, Constants.POSITIVE_ZERO);
				assertEquals(retColors[i][m][1], green, Constants.POSITIVE_ZERO);
				assertEquals(retColors[i][m][2], blue, Constants.POSITIVE_ZERO);
				Mockito.verify(mockRayFactory, Mockito.times(1))
						.createRay(volume, eye, basis, width, height, i, m + startHeight);
			}
		}
		Mockito.verify(mockPixelFactory, Mockito.times(width * threadHeight))
				.createScenePixel(mockScene, eye, light, maxDepth);
		Mockito.verify(mockPixel, Mockito.times(width * threadHeight)).getPixelColor(ray, 0);
	}
}
