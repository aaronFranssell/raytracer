package scene.viewer.factory;

import scene.viewer.ViewingVolume;

public interface ViewingVolumeFactory
{
	public ViewingVolume getVolume(double incomingLeft, double incomingRight, double incomingBottom, double incomingTop);
}
