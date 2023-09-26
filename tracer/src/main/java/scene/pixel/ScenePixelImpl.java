package scene.pixel;

import etc.Color;
import etc.HitData;
import etc.RaytracerException;
import math.Point;
import scene.Scene;
import scene.ray.Ray;
import surface.Surface;
import util.Util;
import util.UtilImpl;

public class ScenePixelImpl implements ScenePixel {
  public Scene scene;
  public int currentDepth;
  private Point eye;
  private Point light;
  private Util util;
  private int maxDepth;

  public ScenePixelImpl(
      Scene incomingScene,
      Point incomingEye,
      Point incomingLight,
      Util incomingUtil,
      int incomingMaxDepth) {
    scene = incomingScene;
    currentDepth = 0;
    eye = incomingEye;
    light = incomingLight;
    util = incomingUtil;
    maxDepth = incomingMaxDepth;
  }

  public ScenePixelImpl(
      Scene incomingScene, Point incomingEye, Point incomingLight, int incomingMaxDepth) {
    this(incomingScene, incomingEye, incomingLight, new UtilImpl(), incomingMaxDepth);
  }

  @Override
  public Color getPixelColor(Ray r, int currentDepth) throws RaytracerException {
    if (maxDepth == currentDepth) {
      return new Color(0.0, 0.0, 0.0);
    }
    HitData currHit = scene.getSmallestPositiveHitDataOrReturnMiss(r);
    if (!currHit.isHit()) {
      return new Color(0.0, 0.0, 0.0);
    }
    return colorPixel(r, scene, currentDepth, currHit);
  }

  private Color colorPixel(Ray r, Scene scene, int currentDepth, HitData hit)
      throws RaytracerException {
    Surface currSurface = hit.getSurface();

    boolean inShadow = util.isInShadow(scene, light, hit);
    Color surfaceColor = currSurface.getColor(r, light, eye, inShadow, hit.getNormal(), hit.getP());
    Color reflectReturnColor = util.getReflectedColor(r, currentDepth, hit, currSurface, this);
    Color refractReturnColor = util.getRefractedColor(r, currentDepth, hit, currSurface, this);

    double red = reflectReturnColor.red + refractReturnColor.red + surfaceColor.red;
    double green = reflectReturnColor.green + refractReturnColor.green + surfaceColor.green;
    double blue = reflectReturnColor.blue + refractReturnColor.blue + surfaceColor.blue;
    return new Color(red, green, blue);
  }

  public Scene getScene() {
    return scene;
  }

  public int getCurrentDepth() {
    return currentDepth;
  }

  public Point getEye() {
    return eye;
  }

  public Point getLight() {
    return light;
  }
}
