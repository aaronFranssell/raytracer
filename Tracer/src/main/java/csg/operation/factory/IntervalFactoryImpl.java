package csg.operation.factory;

import csg.operation.Interval;
import csg.operation.IntervalImpl;
import etc.RaytracerException;

public class IntervalFactoryImpl implements IntervalFactory
{

	@Override
	public Interval getInterval(double[] hitTs) throws RaytracerException
	{
		return new IntervalImpl(hitTs);
	}

}
