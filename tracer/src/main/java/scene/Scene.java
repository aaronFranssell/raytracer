package scene;

import etc.HitData;
import scene.ray.Ray;

public interface Scene {
	public HitData getSmallestPositiveHitDataOrReturnMiss(Ray r) throws Exception;
}
