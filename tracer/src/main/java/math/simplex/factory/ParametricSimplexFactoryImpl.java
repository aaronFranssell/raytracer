package math.simplex.factory;

import math.Point;
import math.simplex.ParametricSimplexImpl;
import math.simplex.Simplex;

public class ParametricSimplexFactoryImpl implements SimplexFactory {
	@Override
	public Simplex getSimplex(Point a, Point b, Point c) {
		return new ParametricSimplexImpl(a, b, c);
	}
}
