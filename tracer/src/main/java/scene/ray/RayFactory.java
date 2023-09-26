package scene.ray;

import math.Point;
import math.UVW;
import math.Vector;
import scene.viewer.ViewingVolume;

public interface RayFactory {
  public Ray createRay(
      ViewingVolume incomingVolume,
      Point incomingEye,
      UVW incomingBasis,
      int incomingWidth,
      int incomingHeight,
      int incomingCurrLoopWidth,
      int incomingCurrLoopHeightWithOffset);

  public Ray createRay(Vector d, Point eye);
}
