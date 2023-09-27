package surface.csg.operation;

public interface IntervalFactory {
	Interval getInterval(double[] hitTs) throws Exception;
}
