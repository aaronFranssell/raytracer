package scene.pixel;

import junit.framework.Assert;
import math.Point;
import math.Vector;

import org.junit.Test;

import scene.Scene;
import scene.SceneImpl;
import scene.ray.Ray;

public class ScenePixelFactoryImpl_UnitTests
{
	@Test
	public void createScenePixel_WithValidValue_ExpectScenePixel()
	{
		//Given
		Ray ray = new Ray(new Vector(1.0,0.0,0.0), new Point(1.0,0.0,0.0));
		Scene scene = new SceneImpl(null);
		Point eye = new Point(0.0,0.0,0.0);
		Point light = new Point(0.0,0.0,0.0);
		ScenePixelFactoryImpl classUnderTest = new ScenePixelFactoryImpl();
		
		//When
		ScenePixel ret = classUnderTest.createScenePixel(ray, scene, eye, light);
		
		//Then
		Assert.assertTrue(ret.getEye().equals(eye));
		Assert.assertTrue(ret.getLight().equals(light));
		Assert.assertTrue(ret.getScene().equals(scene));
		Assert.assertTrue(ret.getR().equals(ray));
	}
}
