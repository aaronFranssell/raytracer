package scene.ray;

import scene.viewer.ViewingVolume;
import math.Point;
import math.UVW;

public interface RayFactory
{
	public Ray createRay(ViewingVolume incomingVolume, Point incomingEye, UVW incomingBasis, int incomingWidth, int incomingHeight,
						 int incomingCurrLoopWidth, int incomingCurrLoopHeightWithOffset);
}
