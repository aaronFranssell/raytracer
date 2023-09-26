package etc;

import org.junit.jupiter.api.Test;
import util.Constants;
import util.UtilImpl;

public class Color_UnitTests {
  @Test
  public void add_WithColor_ExpectAddedColor() {

    Color classUnderTest = new Color(0.3, 0.2, 0.5);
    Color other = new Color(0.2, 0.7, 0.3);

    Color result = classUnderTest.add(other);

    assert UtilImpl.doubleEqual(result.red, 0.5, Constants.POSITIVE_ZERO);
    assert UtilImpl.doubleEqual(result.green, 0.9, Constants.POSITIVE_ZERO);
    assert UtilImpl.doubleEqual(result.blue, 0.8, Constants.POSITIVE_ZERO);
  }
}
