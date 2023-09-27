package math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import util.Constants;

public class Vector_UnitTests {
	@Test
	public void cross_With2Vectors_ExpectCrossProduct() {

		Vector classUnderTest = new Vector(3.0, -6.111, 7.222);
		Vector other = new Vector(-2.1, 5.1, 44.0);
		Vector answer = new Vector(-305.7162, -147.1662, 2.466899999999999);

		Vector result = classUnderTest.cross(other);

		assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}

	@Test
	public void dot_WithVectors_ExpectDotProduct() {

		Vector classUnderTest = new Vector(3.0, -5.0, 11.0);
		Vector other = new Vector(-2.1, 5.1, -7.0);

		double result = classUnderTest.dot(other);

		assertEquals(-108.8, result, Constants.POSITIVE_ZERO);
	}

	@Test
	public void magnitude_WithVector_ExpectVectorMagnitude() {

		Vector classUnderTest = new Vector(-3.0, 4.0, 5.0);

		double result = classUnderTest.magnitude();

		assertEquals(7.07106781187, result, Constants.POSITIVE_ZERO);
	}

	@Test
	public void scaleReturn_WithVector_ExpectScaledVector() {

		Vector classUnderTest = new Vector(3.0, 5.0, 7.0);
		double scale = 3.0;

		Vector result = classUnderTest.scaleReturn(scale);

		assertEquals(9.0, result.x, Constants.POSITIVE_ZERO);
		assertEquals(15.0, result.y, Constants.POSITIVE_ZERO);
		assertEquals(21.0, result.z, Constants.POSITIVE_ZERO);
	}

	@Test
	public void normalizeReturn_WithVector_ExpectUnitVector() {

		Vector vec = new Vector(3.0, 4.5, 8.111);
		Vector answer = new Vector(0.30773144569001576, 0.46159716853502364, 0.8320032519972393);

		Vector result = vec.normalizeReturn();

		assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}

	@Test
	public void add_WithVectors_ExpectVector() {

		Vector classUnderTest = new Vector(3.0, 5.0, 11.0);
		Vector otherVector = new Vector(7.0, 13.0, -5.0);

		Vector result = classUnderTest.add(otherVector);

		assertEquals(10.0, result.x, Constants.POSITIVE_ZERO);
		assertEquals(18.0, result.y, Constants.POSITIVE_ZERO);
		assertEquals(6.0, result.z, Constants.POSITIVE_ZERO);
	}

	@Test
	public void add_WithPoint_ExpectVector() {

		Vector classUnderTest = new Vector(3.0, 5.0, 11.0);
		Point point = new Point(7.0, 13.0, -5.0);

		Vector result = classUnderTest.add(point);

		assertEquals(10.0, result.x, Constants.POSITIVE_ZERO);
		assertEquals(18.0, result.y, Constants.POSITIVE_ZERO);
		assertEquals(6.0, result.z, Constants.POSITIVE_ZERO);
	}

	@Test
	public void minus_WithVectors_ExpectVector() {

		Vector classUnderTest = new Vector(3.0, 5.0, 11.0);
		Vector otherVector = new Vector(7.0, 13.0, -5.0);

		Vector result = classUnderTest.minus(otherVector);

		assertEquals(-4.0, result.x, Constants.POSITIVE_ZERO);
		assertEquals(-8.0, result.y, Constants.POSITIVE_ZERO);
		assertEquals(16.0, result.z, Constants.POSITIVE_ZERO);
	}
}
