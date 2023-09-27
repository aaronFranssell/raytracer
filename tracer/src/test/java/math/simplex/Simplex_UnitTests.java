package math.simplex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import math.Point;
import math.Vector;
import org.junit.jupiter.api.Test;
import scene.ray.Ray;
import scene.ray.RayImpl;

public class Simplex_UnitTests {
	@Test
	public void getNormal_WithBarycentricSimplexAndPoints_ExpectNormal() {

		Point eye = new Point(0.0, 0.0, 4.0);
		Vector d = new Vector(0.0, 0.0, -1.0);
		Ray r = new RayImpl(d, eye);

		Point a = new Point(-2.0, -2.0, 0.0);
		Point b = new Point(0.0, 2.0, 0.0);
		Point c = new Point(2.0, -2.0, 0.0);
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a, b, c);

		Vector expected = new Vector(0.0, 0.0, 1.0);

		Vector actual = classUnderTest.getNormal(r);

		assertEquals(expected, actual);
	}

	@Test
	public void getNormal_WithBarycentricSimplexAndRayOnOtherSide_ExpectNormal() {

		Point eye = new Point(0.0, 0.0, -4.0);
		Vector d = new Vector(0.0, 0.0, 1.0);
		Ray r = new RayImpl(d, eye);

		Point a = new Point(-2.0, -2.0, 0.0);
		Point b = new Point(0.0, 2.0, 0.0);
		Point c = new Point(2.0, -2.0, 0.0);
		ParametricSimplexImpl classUnderTest = new ParametricSimplexImpl(a, b, c);

		Vector expected = new Vector(0.0, 0.0, -1.0);

		Vector actual = classUnderTest.getNormal(r);

		assertEquals(expected, actual);
	}
}
