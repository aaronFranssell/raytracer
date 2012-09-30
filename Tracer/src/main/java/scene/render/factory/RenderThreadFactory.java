package scene.render.factory;

import math.Point;
import math.UVW;
import scene.Scene;
import scene.render.RenderThread;
import scene.viewer.ViewingVolume;

public interface RenderThreadFactory
{
	public RenderThread getRenderThread(Point incomingEye, ViewingVolume incomingVolume, int incomingPictureWidth, int incomingPictureHeight, UVW incomingBasis,
										Point incomingLight, Scene incomingScene, int incomingStartHeight, int incomingThreadHeight);
}
