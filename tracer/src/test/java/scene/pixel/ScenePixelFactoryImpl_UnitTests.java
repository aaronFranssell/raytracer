package scene.pixel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import math.Point;
import org.junit.jupiter.api.Test;
import scene.Scene;
import scene.SceneImpl;

public class ScenePixelFactoryImpl_UnitTests {
  @Test
  public void createScenePixel_WithValidValue_ExpectScenePixel() {

    Scene scene = new SceneImpl(null);
    Point eye = new Point(0.0, 0.0, 0.0);
    Point light = new Point(0.0, 0.0, 0.0);
    ScenePixelFactoryImpl classUnderTest = new ScenePixelFactoryImpl();

    ScenePixel ret = classUnderTest.createScenePixel(scene, eye, light, 4);

    assertTrue(ret.getEye().equals(eye));
    assertTrue(ret.getLight().equals(light));
    assertTrue(ret.getScene().equals(scene));
  }
}
