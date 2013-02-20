package scene.ray;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import util.Constants;

public class RayImpl_IntegrationTests
{
	@Test
	public void getDistance_WithPoint_ExpectDistance()
	{
		//Given
		Point p = new Point(0.0,1.0,0.0);
		Point eye = new Point(0.0,2.0,2.0);
		Vector d = new Vector(0.0,0.0,-1.0);
		
		RayImpl classUnderTest = new RayImpl(d, eye);
		
		//When
		double actual = classUnderTest.getDistance(p);
		
		//Then
		double expected = 1.0;
		Assert.assertEquals(expected, actual, Constants.POSITIVE_ZERO);
	}
}
