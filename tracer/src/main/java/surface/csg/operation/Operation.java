package surface.csg.operation;

import etc.HitData;
import etc.RaytracerException;
import scene.ray.Ray;

public interface Operation {
  public HitData getHitData(HitData leftHitData, HitData rightHitData, Ray ray)
      throws RaytracerException;
}
