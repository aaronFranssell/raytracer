package scene;

import scene.ray.Ray;
import etc.HitData;
import etc.RaytracerException;

public interface Scene
{
	public HitData getSmallestPositiveHitDataOrReturnMiss(Ray r)  throws RaytracerException;
}
