package scene.pixel;

import math.Point;
import scene.Scene;

public class ScenePixelFactoryImpl implements ScenePixelFactory {

  @Override
  public ScenePixel createScenePixel(
      Scene incomingScene, Point incomingEye, Point incomingLight, int incomingMaxDepth) {
    return new ScenePixelImpl(incomingScene, incomingEye, incomingLight, incomingMaxDepth);
  }
}
