package scene.pixel;

import math.Point;
import scene.Scene;
import scene.ray.Ray;

public class ScenePixelFactoryImpl implements ScenePixelFactory
{

	@Override
	public ScenePixelImpl createScenePixel(Ray incomingRay, Scene incomingScene, Point incomingEye, Point incomingLight)
	{
		return new ScenePixelImpl(incomingRay, incomingScene, incomingEye, incomingLight);
	}

}
