package scene.viewer.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import scene.viewer.ViewingVolume;
import util.Constants;

public class ViewVolumeFactoryImpl_UnitTests {
  @Test
  public void getVolume_WithValues_ExpectViewingVolume() {

    double top = 5;
    double bottom = 7;
    double left = 11;
    double right = 13;

    ViewingVolumeFactoryImpl classUnderTest = new ViewingVolumeFactoryImpl();

    ViewingVolume ret = classUnderTest.getVolume(left, right, bottom, top);

    assertEquals(ret.getTop(), top, Constants.POSITIVE_ZERO);
    assertEquals(ret.getBottom(), bottom, Constants.POSITIVE_ZERO);
    assertEquals(ret.getLeft(), left, Constants.POSITIVE_ZERO);
    assertEquals(ret.getRight(), right, Constants.POSITIVE_ZERO);
  }
}
