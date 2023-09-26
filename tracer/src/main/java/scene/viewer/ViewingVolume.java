package scene.viewer;

public class ViewingVolume {
  private double left;
  private double right;
  private double bottom;
  private double top;

  public ViewingVolume(
      double incomingLeft, double incomingRight, double incomingBottom, double incomingTop) {
    left = incomingLeft;
    right = incomingRight;
    bottom = incomingBottom;
    top = incomingTop;
  }

  public double getLeft() {
    return left;
  }

  public double getRight() {
    return right;
  }

  public double getBottom() {
    return bottom;
  }

  public double getTop() {
    return top;
  }
}
