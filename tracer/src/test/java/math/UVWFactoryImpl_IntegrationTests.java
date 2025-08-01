package math;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UVWFactoryImpl_IntegrationTests {
	@Test
	public void createUVW_WithParallelVectors_ExpectException() throws Exception {
		Vector gaze = new Vector(1.0, 0.0, 0.0);
		Vector up = new Vector(1.0, 0.0, 0.0);
		UVWFactory classUnderTest = new UVWFactory();

		assertThrows(
				Exception.class,
				() -> {
					classUnderTest.createUVW(up, gaze);
				});
	}

	@Test
	public void createUVW_WithZGazeAndYUp_ExpectUVW() throws Exception {

		Vector gaze = new Vector(0.0, 0.0, -1.0);
		Vector up = new Vector(0.0, 1.0, 0.0);

		Vector expectedU = new Vector(1.0, 0.0, 0.0);
		Vector expectedV = new Vector(0.0, 1.0, 0.0);
		Vector expectedW = new Vector(0.0, 0.0, -1.0);
		UVWFactory classUnderTest = new UVWFactory();

		UVW retUVW = classUnderTest.createUVW(up, gaze);

		assertTrue(retUVW.getU().equals(expectedU));
		assertTrue(retUVW.getV().equals(expectedV));
		assertTrue(retUVW.getW().equals(expectedW));
	}

	@Test
	public void createUVW_WithLookingInMinusAll3Directions_ExpectUVW() throws Exception {

		Vector gaze = new Vector(-1.0, -1.0, -1.0);
		Vector up = new Vector(0.0, 1.0, 0.0);

		Vector expectedU = new Vector(0.7071067811865476, 0.0, -0.7071067811865476);
		Vector expectedV = new Vector(-0.4082482904638631, 0.8164965809277261, -0.4082482904638631);
		Vector expectedW = new Vector(-0.5773502691896258, -0.5773502691896258, -0.5773502691896258);
		UVWFactory classUnderTest = new UVWFactory();

		UVW retUVW = classUnderTest.createUVW(up, gaze);

		assertTrue(retUVW.getU().equals(expectedU));
		assertTrue(retUVW.getV().equals(expectedV));
		assertTrue(retUVW.getW().equals(expectedW));
	}
}
