package csg.operation;

import etc.HitData;
import etc.Ray;
import etc.RaytracerException;

public interface Operation
{
	public HitData getHitData(HitData leftHitData, HitData rightHitData, Ray ray) throws RaytracerException;
}
