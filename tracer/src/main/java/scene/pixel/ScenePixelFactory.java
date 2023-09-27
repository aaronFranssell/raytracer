package scene.pixel;

import math.Point;
import scene.Scene;

public interface ScenePixelFactory {
	public ScenePixel createScenePixel(
			Scene incomingScene, Point incomingEye, Point incomingLight, int incomingMaxDepth);
}
