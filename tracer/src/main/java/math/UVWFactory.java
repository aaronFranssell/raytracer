package math;

import etc.RaytracerException;

public interface UVWFactory {
  public UVW createUVW(Vector up, Vector gaze) throws RaytracerException;
}
