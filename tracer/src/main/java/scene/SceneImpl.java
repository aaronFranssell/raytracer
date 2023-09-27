package scene;

import java.util.ArrayList;

import etc.HitData;
import scene.ray.Ray;
import surface.Surface;
import util.Constants;

public class SceneImpl implements Scene {
	private ArrayList<Surface> surfaceList;

	public SceneImpl(ArrayList<Surface> incomingSurfaceList) {
		surfaceList = incomingSurfaceList;
	}

	public HitData getSmallestPositiveHitDataOrReturnMiss(Ray r) throws Exception {
		HitData retHitData = new HitData();
		ArrayList<HitData> sceneHitData = getSceneHitData(r);
		for (HitData currentHit : sceneHitData) {
			if (currentHitIsNewSmallest(currentHit, retHitData)
					|| noHitAndCurrentIsHit(currentHit, retHitData)) {
				retHitData = currentHit;
			}
		}
		return retHitData;
	}

	private boolean currentHitIsNewSmallest(HitData currentHit, HitData currentSmallest) {
		return currentSmallest.isHit()
				&& currentHit.isHit()
				&& hitGreaterThanZero(currentHit)
				&& currentHit.getT() < currentSmallest.getT();
	}

	private boolean noHitAndCurrentIsHit(HitData currentHit, HitData currentSmallest) {
		return !currentSmallest.isHit() && currentHit.isHit() && hitGreaterThanZero(currentHit);
	}

	private boolean hitGreaterThanZero(HitData hit) {
		return hit.isHit() && hit.getT() > Constants.POSITIVE_ZERO;
	}

	private ArrayList<HitData> getSceneHitData(Ray r) throws Exception {
		ArrayList<HitData> sceneHitInfo = new ArrayList<HitData>();
		for (Surface surface : surfaceList) {
			ArrayList<HitData> surfaceHitData = surface.getHitData(r);
			sceneHitInfo.addAll(surfaceHitData);
		}
		return sceneHitInfo;
	}
}
