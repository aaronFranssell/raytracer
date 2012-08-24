package csg.operation;

import etc.HitData;
import etc.Ray;

public class Union implements Operation
{
	@Override
	public HitData getHitData(HitData leftHitData, HitData rightHitData, Ray ray)
	{
		//left operation takes precedence.
		if(leftHitData.isHit())
		{
			return leftHitData;
		}
		if(rightHitData.isHit() && !leftHitData.isHit())
		{
			return rightHitData;
		}		
		//if both missed, then I can just return either hit data object...
		return rightHitData;
	}
}
