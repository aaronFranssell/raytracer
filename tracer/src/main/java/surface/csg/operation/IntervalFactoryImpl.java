package surface.csg.operation;

import etc.RaytracerException;

public class IntervalFactoryImpl implements IntervalFactory {

  @Override
  public Interval getInterval(double[] hitTs) throws RaytracerException {
    return new IntervalImpl(hitTs);
  }
}
