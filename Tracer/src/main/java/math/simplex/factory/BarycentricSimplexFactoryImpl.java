package math.simplex.factory;

import math.Point;
import math.simplex.BarycentricSimplexImpl;
import math.simplex.Simplex;

public class BarycentricSimplexFactoryImpl implements SimplexFactory
{
	@Override
	public Simplex getSimplex(Point a, Point b, Point c)
	{
		return new BarycentricSimplexImpl(a,b,c);
	}
}
