package scene.ray;

import math.Point;
import math.UVW;
import math.Vector;
import scene.viewer.ViewingVolume;

public class RayFactoryImpl implements RayFactory {
  @Override
  public Ray createRay(
      ViewingVolume incomingVolume,
      Point incomingEye,
      UVW incomingBasis,
      int incomingWidth,
      int incomingHeight,
      int incomingCurrLoopWidth,
      int incomingCurrLoopHeightWithOffset) {
    Point eye = new Point(incomingEye.x, incomingEye.y, incomingEye.z);
    ViewingVolume volume = incomingVolume;
    int pictureWidth = incomingWidth;
    int pictureHeight = incomingHeight;
    int i = incomingCurrLoopWidth;
    int j = incomingCurrLoopHeightWithOffset;
    UVW basis = incomingBasis;

    Vector uvwS =
        new Vector(
            volume.getLeft() + (volume.getRight() - volume.getLeft()) * (i + 0.5) / pictureWidth,
            volume.getBottom() + (volume.getTop() - volume.getBottom()) * (j + 0.5) / pictureHeight,
            1);

    Vector s = new Vector(0, 0, 0);
    s.x = eye.x + uvwS.x * basis.getU().x + uvwS.y * basis.getV().x + uvwS.z * basis.getW().x;
    s.y = eye.y + uvwS.x * basis.getU().y + uvwS.y * basis.getV().y + uvwS.z * basis.getW().y;
    s.z = eye.z + uvwS.x * basis.getU().z + uvwS.y * basis.getV().z + uvwS.z * basis.getW().z;

    Vector d = new Vector(s.x - eye.x, s.y - eye.y, s.z - eye.z);

    d = d.normalizeReturn();

    Ray ret = createRay(d, eye);

    return ret;
  }

  @Override
  public Ray createRay(Vector d, Point eye) {
    return new RayImpl(d, eye);
  }
}
