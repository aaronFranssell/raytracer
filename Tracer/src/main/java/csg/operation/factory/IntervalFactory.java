package csg.operation.factory;

import csg.operation.Interval;
import etc.RaytracerException;

public interface IntervalFactory
{
	Interval getInterval(double[] hitTs) throws RaytracerException;
}
