package primitives;

import math.Point;
import util.Constants;
import util.Util;
import etc.Color;
import etc.Effects;


public class OuterSphere extends Sphere
{
	public OuterSphere(String incomingFilePath, Effects incomingEffects,Color incomingCA, Color incomingCL, double incomingTextureScale, Util incomingOps)
	{
		super(new Point(0.0,0.0,0.0), Constants.OUTERSPHERE_RADIUS, null, incomingCA, incomingCL, incomingEffects, incomingFilePath, incomingTextureScale, incomingOps);
	}
	
	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Outersphere;
	}

}
