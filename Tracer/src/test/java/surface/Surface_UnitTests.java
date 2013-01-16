package surface;

import junit.framework.Assert;
import math.Point;
import math.Vector;
import noise.NoiseColor;

import org.junit.Test;
import org.mockito.Mockito;

import util.Util;

import bumpMapping.BumpMap;

import etc.Color;
import etc.Effects;
import etc.Phong;
import etc.RaytracerException;
import etc.mapper.ImageMapper;

public class Surface_UnitTests
{
	@Test(expected = RaytracerException.class)
	public void getColor_WithNoLightingModel_ExpectException() throws RaytracerException
	{
		//Given
		Point light = Mockito.mock(Point.class);
		Point eye = Mockito.mock(Point.class);
		boolean inShadow = false;
		Vector n = Mockito.mock(Vector.class);
		Mockito.when(n.copy()).thenReturn(n);
		Point p = Mockito.mock(Point.class);
		Color cR = new Color(0.5, 0.5, 0.5);
		
		Effects effects = new Effects();

		Surface classUnderTest = Mockito.mock(Surface.class, Mockito.CALLS_REAL_METHODS);
		classUnderTest.setEffects(effects);
		classUnderTest.setcR(cR);

		//When
		classUnderTest.getColor(light, eye, inShadow, n, p);

		//Then
		Assert.fail("Expected exception not thrown.");
	}

	@Test
	public void getColor_WithPhongAndMockedBumpAndNose_ExpectCalls() throws RaytracerException
	{
		//Given
		Color mockCA = Mockito.mock(Color.class);
		Color mockCL = Mockito.mock(Color.class);
		int exponent = 32;
		Point light = Mockito.mock(Point.class);
		Point eye = Mockito.mock(Point.class);
		boolean inShadow = false;
		Vector n = Mockito.mock(Vector.class);
		Mockito.when(n.copy()).thenReturn(n);
		Point p = Mockito.mock(Point.class);
		Color cR = new Color(0.5, 0.5, 0.5);

		ImageMapper mockMapper = Mockito.mock(ImageMapper.class);
		Mockito.when(mockMapper.getColor(p)).thenReturn(cR);
		
		Vector mockedBumpVector = Mockito.mock(Vector.class);
		BumpMap mockBump = Mockito.mock(BumpMap.class);
		Mockito.when(mockBump.getBump(p, n)).thenReturn(mockedBumpVector);
		
		Color noiseColor = Mockito.mock(Color.class);
		NoiseColor mockNoise = Mockito.mock(NoiseColor.class);
		Mockito.when(mockNoise.getColor(p)).thenReturn(noiseColor);
		
		Color phongColor = new Color(0.2,0.2,0.2);
		Util mockUtil = Mockito.mock(Util.class);
		Mockito.when(mockUtil.getColorPhong(cR, mockCA, mockCL, mockedBumpVector, light, eye, exponent, p, inShadow)).thenReturn(phongColor);
		
		Phong phong = new Phong();
		phong.setExponent(exponent);
		
		Effects effects = new Effects();
		effects.setBumpMapClass(mockBump);
		effects.setNoiseMappedColorClass(mockNoise);
		effects.setImageMapper(mockMapper);
		effects.setPhong(phong);

		Surface classUnderTest = Mockito.mock(Surface.class, Mockito.CALLS_REAL_METHODS);
		classUnderTest.setEffects(effects);
		classUnderTest.setcA(mockCA);
		classUnderTest.setcL(mockCL);
		classUnderTest.setOps(mockUtil);
		classUnderTest.setcR(cR);

		Color expected = new Color(0.2,0.2,0.2);
		
		//When
		Color actual = classUnderTest.getColor(light, eye, inShadow, n, p);

		//Then
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getColor_WithLambertian_ExpectCalls() throws RaytracerException
	{
		//Given
		Color mockCA = Mockito.mock(Color.class);
		Color mockCL = Mockito.mock(Color.class);
		Point light = Mockito.mock(Point.class);
		Point eye = Mockito.mock(Point.class);
		boolean inShadow = false;
		Vector n = Mockito.mock(Vector.class);
		Mockito.when(n.copy()).thenReturn(n);
		Point p = Mockito.mock(Point.class);
		Color cR = new Color(0.5, 0.5, 0.5);
		
		Color lambertianColor = new Color(0.2,0.2,0.2);
		Util mockUtil = Mockito.mock(Util.class);
		Mockito.when(mockUtil.getColorLambertian(cR, mockCA, mockCL, n, light, p, inShadow)).thenReturn(lambertianColor);
		
		Effects effects = new Effects();
		effects.setLambertian(true);

		Surface classUnderTest = Mockito.mock(Surface.class, Mockito.CALLS_REAL_METHODS);
		classUnderTest.setEffects(effects);
		classUnderTest.setcA(mockCA);
		classUnderTest.setcL(mockCL);
		classUnderTest.setcR(cR);
		classUnderTest.setOps(mockUtil);

		Color expected = new Color(0.2,0.2,0.2);
		
		//When
		Color actual = classUnderTest.getColor(light, eye, inShadow, n, p);

		//Then
		Assert.assertEquals(expected, actual);
	}
}
