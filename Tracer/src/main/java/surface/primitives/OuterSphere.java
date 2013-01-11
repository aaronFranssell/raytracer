package surface.primitives;

import math.Point;
import etc.Color;
import etc.Effects;

public class OuterSphere extends Sphere
{
	public static int RADIUS = 100;
	
	public OuterSphere(String incomingFilePath, Effects incomingEffects,Color cR, Color incomingCA, Color incomingCL)
	{
		super(new Point(0.0,0.0,0.0), RADIUS, cR, incomingCA, incomingCL, incomingEffects);
	}
	
	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Outersphere;
	}

}
