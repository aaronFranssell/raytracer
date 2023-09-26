package scene.render.factory;

import math.Point;
import math.UVW;
import scene.Scene;
import scene.render.RenderThread;
import scene.render.RenderThreadImpl;
import scene.viewer.ViewingVolume;

public class RenderThreadFactoryImpl implements RenderThreadFactory {
  @Override
  public RenderThread getRenderThread(
      Point incomingEye,
      ViewingVolume incomingVolume,
      int incomingPictureWidth,
      int incomingPictureHeight,
      UVW incomingBasis,
      Point incomingLight,
      Scene incomingScene,
      int incomingStartHeight,
      int incomingThreadHeight,
      int incomingHeight) {
    return new RenderThreadImpl(
        incomingEye,
        incomingVolume,
        incomingPictureWidth,
        incomingPictureHeight,
        incomingBasis,
        incomingLight,
        incomingScene,
        incomingStartHeight,
        incomingThreadHeight,
        incomingHeight);
  }
}
