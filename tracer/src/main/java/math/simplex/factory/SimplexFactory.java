package math.simplex.factory;

import math.Point;
import math.simplex.Simplex;

public interface SimplexFactory {
	public Simplex getSimplex(Point a, Point b, Point c);
}
