package math;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import etc.RaytracerException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UVWFactoryImpl_UnitTests {
	@Test
	public void createUVW_WithMockParallelVectors_ExpectException() throws RaytracerException {

		Vector mockGaze = Mockito.mock(Vector.class);
		Mockito.when(mockGaze.copy()).thenReturn(mockGaze);
		Mockito.when(mockGaze.normalizeReturn()).thenReturn(mockGaze);

		Vector mockUp = Mockito.mock(Vector.class);
		Mockito.when(mockUp.copy()).thenReturn(mockUp);
		Mockito.when(mockUp.normalizeReturn()).thenReturn(mockUp);
		UVWFactoryImpl classUnderTest = new UVWFactoryImpl();

		Vector mockWCrossT = Mockito.mock(Vector.class);
		Mockito.when(mockWCrossT.magnitude()).thenReturn(0.0);

		Mockito.when(mockGaze.cross(mockUp)).thenReturn(mockWCrossT);

		assertThrows(
				RaytracerException.class,
				() -> {
					classUnderTest.createUVW(mockUp, mockGaze);
				});
	}

	@Test
	public void createUVW_WithMockVectors_ExpectUVW() throws RaytracerException {

		Vector mockGaze = Mockito.mock(Vector.class);
		Vector mockWCrossTCrossW = Mockito.mock(Vector.class);
		Vector mockWCrossT = Mockito.mock(Vector.class);
		Vector mockUp = Mockito.mock(Vector.class);

		Mockito.when(mockWCrossT.normalizeReturn()).thenReturn(mockWCrossT);
		Mockito.when(mockWCrossT.magnitude()).thenReturn(1.0);
		Mockito.when(mockWCrossT.cross(mockGaze)).thenReturn(mockWCrossTCrossW);

		Mockito.when(mockGaze.copy()).thenReturn(mockGaze);
		Mockito.when(mockGaze.normalizeReturn()).thenReturn(mockGaze);
		Mockito.when(mockGaze.cross(mockUp)).thenReturn(mockWCrossT);

		Mockito.when(mockUp.copy()).thenReturn(mockUp);
		Mockito.when(mockUp.normalizeReturn()).thenReturn(mockUp);

		UVWFactoryImpl classUnderTest = new UVWFactoryImpl();

		UVW retUVW = classUnderTest.createUVW(mockUp, mockGaze);

		Mockito.verify(mockGaze).copy();
		Mockito.verify(mockGaze).normalizeReturn();
		Mockito.verify(mockGaze).cross(mockUp);
		Mockito.verify(mockUp).copy();
		Mockito.verify(mockUp).normalizeReturn();
		Mockito.verify(mockWCrossT).normalizeReturn();
		Mockito.verify(mockWCrossT).magnitude();
		Mockito.verify(mockWCrossT).cross(mockGaze);
		assertTrue(retUVW.getU().equals(mockWCrossT));
		assertTrue(retUVW.getV().equals(mockWCrossTCrossW));
		assertTrue(retUVW.getW().equals(mockGaze));
	}
}
