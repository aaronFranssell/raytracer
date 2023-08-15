package scene.pixel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import math.Point;
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
		assertTrue(ret.getEye().equals(eye));
		assertTrue(ret.getLight().equals(light));
		assertTrue(ret.getScene().equals(scene));
	}
}
