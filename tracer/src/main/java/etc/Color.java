package etc;

import util.Constants;
import util.UtilImpl;

public class Color {
  public double red;
  public double green;
  public double blue;

  public Color(double incomingRed, double incomingGreen, double incomingBlue) {
    red = incomingRed;
    green = incomingGreen;
    blue = incomingBlue;
  }

  public Color scaleReturn(double val) {
    return new Color(red * val, green * val, blue * val);
  }

  public Color add(Color other) {
    return new Color(red + other.red, green + other.green, blue + other.blue);
  }

  public String toString() {
    return "red: " + red + " green: " + green + " blue: " + blue;
  }

  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }

    if (!(other instanceof Color)) {
      return false;
    }
    Color otherVector = (Color) other;
    if (UtilImpl.doubleEqual(otherVector.red, red, Constants.POSITIVE_ZERO)
        && UtilImpl.doubleEqual(otherVector.green, green, Constants.POSITIVE_ZERO)
        && UtilImpl.doubleEqual(otherVector.blue, blue, Constants.POSITIVE_ZERO)) {
      return true;
    }
    return false;
  }

  public Color copy() {
    return new Color(red, green, blue);
  }
}
