package surface.csg.operation;

import etc.RaytracerException;

public interface IntervalFactory {
	Interval getInterval(double[] hitTs) throws RaytracerException;
}
