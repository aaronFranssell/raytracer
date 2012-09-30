package scene.viewer.factory;

import scene.viewer.ViewingVolume;

public class ViewingVolumeFactoryImpl implements ViewingVolumeFactory
{

	@Override
	public ViewingVolume getVolume(double incomingLeft, double incomingRight,double incomingBottom, double incomingTop)
	{
		return new ViewingVolume(incomingLeft, incomingRight, incomingBottom, incomingTop);
	}

}
