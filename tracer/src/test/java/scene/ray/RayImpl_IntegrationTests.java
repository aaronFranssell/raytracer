package scene.ray;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import math.Point;
import math.Vector;
import util.Constants;

public class RayImpl_IntegrationTests
{
	@Test
	public void getDistance_WithPoint_ExpectDistance()
	{
		
		Point p = new Point(0.0,1.0,0.0);
		Point eye = new Point(0.0,2.0,2.0);
		Vector d = new Vector(0.0,0.0,-1.0);
		
		RayImpl classUnderTest = new RayImpl(d, eye);
		
		
		double actual = classUnderTest.getDistance(p);
		
		
		double expected = 1.0;
		assertEquals(expected, actual, Constants.POSITIVE_ZERO);
	}
}
