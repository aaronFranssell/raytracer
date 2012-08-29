package csg.operation;

import etc.HitData;
import etc.Ray;

public class Intersection implements Operation
{
	@Override
	public HitData getHitData(HitData leftHitData, HitData rightHitData, Ray ray)
	{
		if(leftHitData.isHit() && rightHitData.isHit())
		{
			//left side takes precedence
			return leftHitData;
		}
		else if(!leftHitData.isHit())
		{
			return leftHitData;
		}
		else
		{
			return rightHitData;
		}
	}
}