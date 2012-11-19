package surface.primitives;

import math.Point;
import etc.Color;
import etc.Effects;


public class OuterSphere extends Sphere
{
	private static int RADIUS = 100;
	
	public OuterSphere(String incomingFilePath, Effects incomingEffects,Color incomingCA, Color incomingCL, double incomingTextureScale)
	{
		super(new Point(0.0,0.0,0.0), RADIUS, null, incomingCA, incomingCL, incomingEffects, incomingFilePath, incomingTextureScale);
	}
	
	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Outersphere;
	}

}
