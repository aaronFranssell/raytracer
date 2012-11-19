package scene.pixel;

import math.Point;
import scene.Scene;
import scene.ray.Ray;

public class ScenePixelFactoryImpl implements ScenePixelFactory
{

	@Override
	public ScenePixel createScenePixel(Ray incomingRay, Scene incomingScene, Point incomingEye, Point incomingLight, int incomingMaxDepth)
	{
		return new ScenePixelImpl(incomingRay, incomingScene, incomingEye, incomingLight, incomingMaxDepth);
	}

}
