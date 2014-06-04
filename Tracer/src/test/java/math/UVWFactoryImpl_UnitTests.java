package math;

import org.junit.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import etc.RaytracerException;

public class UVWFactoryImpl_UnitTests
{
	@Test(expected=RaytracerException.class)
	public void createUVW_WithMockParallelVectors_ExpectException() throws RaytracerException
	{
		//Given
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
		
		//When
		classUnderTest.createUVW(mockUp, mockGaze);
		
		//Then
		Assert.fail("Expected exception not thrown.");
	}
	
	@Test
	public void createUVW_WithMockVectors_ExpectUVW() throws RaytracerException
	{
		//Given
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
		
		//When
		UVW retUVW = classUnderTest.createUVW(mockUp, mockGaze);
		
		//Then
		Mockito.verify(mockGaze).copy();
		Mockito.verify(mockGaze).normalizeReturn();
		Mockito.verify(mockGaze).cross(mockUp);
		Mockito.verify(mockUp).copy();
		Mockito.verify(mockUp).normalizeReturn();
		Mockito.verify(mockWCrossT).normalizeReturn();
		Mockito.verify(mockWCrossT).magnitude();
		Mockito.verify(mockWCrossT).cross(mockGaze);
		Assert.assertTrue(retUVW.getU().equals(mockWCrossT));
		Assert.assertTrue(retUVW.getV().equals(mockWCrossTCrossW));
		Assert.assertTrue(retUVW.getW().equals(mockGaze));
	}
}
