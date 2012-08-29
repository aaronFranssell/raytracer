package csg.operation;

import math.Point;
import math.Vector;
import primitives.Plane;
import primitives.Surface;
import util.Constants;
import etc.Color;
import etc.Effects;

public class Test_BoundedByHelper
{
	public static Surface createTestSurface()
	{
		Color cR = new Color(0.4,0.8,0.4);
		Color cL = new Color(0.4,0.4,0.4);
		Effects effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0);
		Surface testSurface = new Plane(normal, point, cR,cL, Constants.cA, effects);
		
		return testSurface;
	}
}
