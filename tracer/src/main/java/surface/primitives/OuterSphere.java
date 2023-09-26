package surface.primitives;

import etc.Color;
import etc.Effects;
import math.Point;

public class OuterSphere extends Sphere {
  public static int RADIUS = 100;

  public OuterSphere(Effects incomingEffects, Color cR, Color incomingCA, Color incomingCL) {
    super(new Point(0.0, 0.0, 0.0), RADIUS, cR, incomingCA, incomingCL, incomingEffects);
  }

  @Override
  public SurfaceType getType() {
    return SurfaceType.Outersphere;
  }
}
