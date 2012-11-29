package scene.pixel;

import junit.framework.Assert;
import math.Point;

import org.junit.Test;

import scene.Scene;
import scene.SceneImpl;

public class ScenePixelFactoryImpl_UnitTests
{
	@Test
	public void createScenePixel_WithValidValue_ExpectScenePixel()
	{
		//Given
		Scene scene = new SceneImpl(null);
		Point eye = new Point(0.0,0.0,0.0);
		Point light = new Point(0.0,0.0,0.0);
		ScenePixelFactoryImpl classUnderTest = new ScenePixelFactoryImpl();
		
		//When
		ScenePixel ret = classUnderTest.createScenePixel(scene, eye, light, 4);
		
		//Then
		Assert.assertTrue(ret.getEye().equals(eye));
		Assert.assertTrue(ret.getLight().equals(light));
		Assert.assertTrue(ret.getScene().equals(scene));
	}
}
