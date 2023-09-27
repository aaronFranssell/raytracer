package surface.csg.operation;

public class IntervalFactoryImpl implements IntervalFactory {

	@Override
	public Interval getInterval(double[] hitTs) throws Exception {
		return new IntervalImpl(hitTs);
	}
}
