package surface.csg.operation;

public interface Interval
{
	double[] mergeTIntervalsAndSort(Interval otherInterval);
	boolean isInInterval(double smallestT);
	boolean allInInterval(Interval other);
	double getNextGreatestInterval(double smallestT);
	double[] getInterval();
}
