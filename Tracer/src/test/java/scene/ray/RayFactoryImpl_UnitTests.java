package scene.ray;

import junit.framework.Assert;
import math.Point;
import math.UVW;
import math.Vector;

import org.junit.Test;
import util.Constants;
import scene.viewer.ViewingVolume;

public class RayFactoryImpl_UnitTests
{
	@Test
	public void createRay_WithValidValuesBottomRight_ExpectRay()
	{
		//Given
		Point eye = new Point(0.0,0.0,4.0);
		Vector u = new Vector(1.0, 0.0, 0.0);
		Vector v = new Vector(0.0, 1.0, 0.0);
		Vector w = new Vector(0.0, 0.0, -1.0);
		UVW uvw = new UVW(u,v,w);
		ViewingVolume volume = RayFactoryImpl_Helper.getViewingVolume();
		
		int pictureWidth = 800;
		int pictureHeight = 800;
		int currX = 799;
		int currY = 1;
		RayFactoryImpl classUnderTest = new RayFactoryImpl();
		Vector expectedD = new Vector(0.5775905291306759, -0.5761447455784088, -0.5783134209068094);
		
		//When
		Ray result = classUnderTest.createRay(volume, eye, uvw, pictureWidth, pictureHeight, currX, currY);
		
		//Then
		Assert.assertTrue(result.getEye().equals(eye));
		Assert.assertTrue(result.getD().equals(expectedD));
		Assert.assertEquals(result.getD().magnitude(), 1.0, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void createRay_WithDiagonalDownLookValidValuesTopLeft_ExpectRay()
	{
		//Given
		Point eye = new Point(0.0,0.0,4.0);
		Vector u = new Vector(0.7071067811865476, 0.0, -0.7071067811865476);
		Vector v = new Vector(-0.4082482904638631, 0.8164965809277261, -0.4082482904638631);
		Vector w = new Vector(-0.5773502691896258, -0.5773502691896258, -0.5773502691896258);
		UVW uvw = new UVW(u,v,w);
		ViewingVolume volume = RayFactoryImpl_Helper.getViewingVolume();
		
		int pictureWidth = 800;
		int pictureHeight = 800;
		int currX = 1;
		int currY = 799;
		RayFactoryImpl classUnderTest = new RayFactoryImpl();
		Vector expectedD = new Vector(-0.9770856118857272, 0.13771128297491325, -0.16229389879874548);
		
		//When
		Ray result = classUnderTest.createRay(volume, eye, uvw, pictureWidth, pictureHeight, currX, currY);
		
		//Then
		Assert.assertTrue(result.getEye().equals(eye));
		Assert.assertTrue(result.getD().equals(expectedD));
		Assert.assertEquals(result.getD().magnitude(), 1.0, Constants.POSITIVE_ZERO);
	}
}
