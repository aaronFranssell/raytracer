package csg.object.tree;

import static org.mockito.Mockito.mock;
import primitives.Plane;
import primitives.Surface;
import util.Constants;
import util.Util;
import math.Point;
import math.Vector;
import etc.Color;
import etc.Effects;
import etc.Ray;

public class Test_CSGTreeHelper
{
	public static Ray getTestRay()
	{
		return new Ray(new Vector(0.0,0.0,0.0), new Point(0.0,0.0,0.0));
	}
	
	public static Surface createTestSurface()
	{
		Util ops = mock(Util.class);
		Color cR = new Color(0.4,0.8,0.4);
		Color cL = new Color(0.4,0.4,0.4);
		Effects effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0);
		Surface testSurface = new Plane(normal, point, cR,cL, Constants.cA, effects, ops);
		
		return testSurface;
	}
}
