package scene.render;

import math.Point;
import math.UVW;
import org.mockito.Mockito;
import scene.ray.Ray;
import scene.ray.RayFactory;
import scene.viewer.ViewingVolume;

public class RenderThreadImpl_TestsHelper {
	public static RayFactory getMockRayFactory(
			int width,
			int threadHeight,
			Ray ray,
			ViewingVolume volume,
			Point eye,
			UVW basis,
			int height,
			int startHeight) {
		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);

		for (int i = 0; i < width; i++) {
			for (int m = 0; m < threadHeight; m++) {
				Mockito.when(
						mockRayFactory.createRay(volume, eye, basis, width, height, i, m + startHeight))
						.thenReturn(ray);
			}
		}

		return mockRayFactory;
	}
}
