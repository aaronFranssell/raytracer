package surface.csg.operation;

import scene.ray.Ray;
import etc.HitData;
import etc.RaytracerException;

public interface Operation
{
	public HitData getHitData(HitData leftHitData, HitData rightHitData, Ray ray) throws RaytracerException;
}
