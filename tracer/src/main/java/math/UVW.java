package math;

public class UVW {
  private Vector u, v, w;

  public UVW(Vector incomingU, Vector incomingV, Vector incomingW) {
    u = incomingU;
    v = incomingV;
    w = incomingW;
  }

  public Vector getU() {
    return u;
  }

  public void setU(Vector u) {
    this.u = u;
  }

  public Vector getV() {
    return v;
  }

  public void setV(Vector v) {
    this.v = v;
  }

  public Vector getW() {
    return w;
  }

  public void setW(Vector w) {
    this.w = w;
  }
}
