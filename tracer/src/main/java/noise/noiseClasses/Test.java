package noise.noiseClasses;

import etc.Color;
import math.Point;
import noise.NoiseColor;

public class Test extends NoiseColor {
  public Color getColor(Point p) {
    Point point = p.scaledReturn(5.0);
    double cheerUp = 1.4;
    double val = 0.0;
    double max = 0.9;
    double stepdown = 0.4;
    val = perlinNoise(point, 2, 0.2);
    val = Math.abs(val);
    Color c1;
    if (val >= 0.15) {

      c1 = new Color(stepdown, stepdown, stepdown);
      val *= cheerUp;
      c1 = c1.scaleReturn(val);
    } else {
      c1 = new Color(stepdown, stepdown, max);
      val = (.4 - val);
      val *= cheerUp;
      c1 = c1.scaleReturn(val);
    }

    Color color = c1.copy();
    return color;
  }
}
