package math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import util.Constants;

public class Matrix3_UnitTests {
	@Test
	public void det_WithValues_ExpectResult() {

		Matrix3 classUnderTest = new Matrix3(1, -1, 0, 2, -2, 5, 7, -3, 5);

		double result = classUnderTest.det();

		assertEquals(-20, result, Constants.POSITIVE_ZERO);
	}
}
