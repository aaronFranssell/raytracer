package scene;

import etc.HitData;
import etc.RaytracerException;
import scene.ray.Ray;

public interface Scene {
	public HitData getSmallestPositiveHitDataOrReturnMiss(Ray r) throws RaytracerException;
}
