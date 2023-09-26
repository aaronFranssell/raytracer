package surface.primitives.extension;

import etc.Color;
import etc.Effects;
import math.Point;
import math.Vector;
import scene.ray.Ray;
import surface.primitives.Plane;

public class ChessPlane extends Plane {
  public ChessPlane(
      Vector incomingNormal,
      Point incomingPoint,
      Color incomingCR,
      Color incomingCL,
      Color incomingCA,
      Effects incomingEffects) {
    super(incomingNormal, incomingPoint, incomingCR, incomingCL, incomingCA, incomingEffects);
  }

  @Override
  public Color getColor(Ray r, Point light, Point eye, boolean inShadow, Vector n, Point p) {
    Color returnColor;
    Color white = new Color(0.7, 0.7, 0.7);
    Color black = new Color(0.1, 0.1, 0.1);
    if (Math.cos(p.z) > 0) {
      if (Math.sin(p.x) > 0) {
        returnColor = white;
      } else {
        returnColor = black;
      }
    } else {
      if (Math.sin(p.x) > 0) {
        returnColor = black;
      } else {
        returnColor = white;
      }
    }
    if (effects.getPhong() != null) {
      return ops.getColorPhong(
          returnColor, cL, cA, n, light, eye, effects.getPhong().getExponent(), p, inShadow);
    } else {
      return ops.getColorLambertian(returnColor, cA, cL, n, light, p, inShadow);
    }
  }
}
