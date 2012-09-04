package scene.pixel;

import math.Point;
import scene.Scene;
import scene.ray.Ray;

public interface ScenePixelFactory
{
	public ScenePixelImpl createScenePixel(Ray incomingRay, Scene incomingScene, Point incomingEye, Point incomingLight);
}
