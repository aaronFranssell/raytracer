package scene.ray;

import scene.viewer.ViewingVolume;

public class RayFactoryImpl_Helper {
	public static ViewingVolume getViewingVolume() {
		double left = -1;
		double right = 1;
		double top = 1;
		double bottom = -1;
		ViewingVolume volume = new ViewingVolume(left, right, bottom, top);
		return volume;
	}
}
