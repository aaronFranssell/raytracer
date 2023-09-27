package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;
import etc.Refractive;
import helper.TestsHelper;
import java.io.IOException;
import math.Point;
import math.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import scene.Scene;
import scene.pixel.ScenePixel;
import scene.ray.Ray;
import scene.ray.RayFactory;
import scene.ray.RayImpl;
import surface.Surface;

public class UtilImpl_UnitTests {
	@Test
	public void sort_WithArray_ExpectSortedArray() {

		double[] array = new double[]{5, 3, 8, 10, -5, 0.4, 5, 9, 10, 0.0};
		UtilImpl classUnderTest = new UtilImpl();

		double[] result = classUnderTest.sort(array);

		assertEquals(array.length, result.length);
		for (int i = 0; i < result.length; i++) {
			double curr = result[i];
			boolean found = false;
			for (int m = 0; m < array.length; m++) {
				if (array[m] == curr) {
					found = true;
				}
			}
			assertTrue(found);
		}

		for (int i = 0; i < result.length - 1; i++) {
			assertTrue(result[i] <= result[i + 1]);
		}
	}

	@Test
	public void getP_WithValues_ExpectP() {

		Ray r1 = new RayImpl(new Vector(1.0, 1.0, 1.0), new Point(1.0, 1.0, 1.0));
		double t1 = 2.0;
		Ray r2 = new RayImpl(new Vector(1.0, 1.0, 1.0), new Point(1.0, 1.0, 1.0));
		double t2 = 0.0;

		UtilImpl classUnderTest = new UtilImpl();

		Point result1 = classUnderTest.getP(t1, r1);
		Point result2 = classUnderTest.getP(t2, r2);

		assertEquals(3.0, result1.x, 0.0003);
		assertEquals(3.0, result1.y, 0.0003);
		assertEquals(3.0, result1.z, 0.0003);
		assertEquals(1.0, result2.x, 0.0003);
		assertEquals(1.0, result2.y, 0.0003);
		assertEquals(1.0, result2.z, 0.0003);
	}

	@Test
	public void getRefractedRay_With1sForIndexesOfRefraction_ExpectUnchangedRefractedRay() {

		double originalN = 1.0;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0, 0.0, 0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		Ray expectedRay = new RayImpl(r, p);

		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(r, p)).thenReturn(expectedRay);

		UtilImpl classUnderTest = new UtilImpl(mockFactory);

		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		assertTrue(result.getD().equals(r));
		assertEquals(result.getEye(), p);
		Mockito.verify(mockFactory).createRay(r, p);
	}

	@Test
	public void getRefractedRay_WithNewNGreater_ExpectUnchangedRefractedRay() {

		double originalN = 1.0;
		double newN = 8.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0, 0.0, 0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		Vector expected = new Vector(0.5492382799400274, 0.5909049466066941, 0.5909049466066941);

		Ray expectedRay = new RayImpl(expected, p);

		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(expected, p)).thenReturn(expectedRay);

		UtilImpl classUnderTest = new UtilImpl(mockFactory);

		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		assertTrue(result.getD().equals(expected));
		assertEquals(result.getEye(), p);
		Mockito.verify(mockFactory).createRay(expected, p);
	}

	@Test
	public void getRefractedRay_WithTotalInternalReflection_ExpectNullRay() {

		UtilImpl classUnderTest = new UtilImpl();

		double originalN = 8.0;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0, 0.0, 0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		assertNull(result);
	}

	@Test
	public void getRefractedRay_WithOriginalNLess_ExpectNullRay() {

		double originalN = 1.5;
		double newN = 1.0;

		Vector r = new Vector(0.5, 1.0, 1.0);
		r = r.normalizeReturn();

		Point p = new Point(0.0, 0.0, 0.0);
		Vector normal = new Vector(1.0, 1.0, 1.0);
		normal = normal.normalizeReturn();
		HitData hitData = new HitData(1.0, null, normal, p);

		Vector expected = new Vector(0.19371294336139655, 0.6937129433613966, 0.6937129433613966);

		Ray expectedRay = new RayImpl(expected, p);

		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(expected, p)).thenReturn(expectedRay);

		UtilImpl classUnderTest = new UtilImpl(mockFactory);

		Ray result = classUnderTest.getRefractedRay(r, originalN, newN, hitData);

		assertTrue(expected.equals(result.getD()));
		assertEquals(result.getEye(), p);
		Mockito.verify(mockFactory).createRay(expected, p);
	}

	@Test
	public void clamp_WithValuesOver1_Expect1s() {

		Color c = new Color(1.1, 1.4, 1.5);
		UtilImpl classUnderTest = new UtilImpl();

		Color actual = classUnderTest.clamp(c);

		Color expected = new Color(1.0, 1.0, 1.0);
		assertTrue(expected.equals(actual));
	}

	@Test
	public void clamp_WithValuesUnder1_ExpectUnchanged() {

		Color c = new Color(0.1, 0.4, 0.5);
		UtilImpl classUnderTest = new UtilImpl();

		Color actual = classUnderTest.clamp(c);

		Color expected = new Color(0.1, 0.4, 0.5);
		assertTrue(expected.equals(actual));
	}

	@Test
	public void getReflectedRay_WithValues_ExpectCall() {

		Vector d = new Vector(0.3, 0.5, 0.7);
		Point eye = new Point(0.0, 0.0, 0.0);
		Ray r = new RayImpl(d, eye);
		Vector normal = new Vector(1.5, -0.5, 2.5);
		normal = normal.normalizeReturn();
		Point p = new Point(0.5, 0.5, 0.5);
		Vector expectedD = new Vector(-0.40455970110485867, 0.7934387936397616, -0.4547376485287172);
		Point expectedEye = new Point(0.5, 0.5, 0.5);

		Ray expectedRay = new RayImpl(expectedD, expectedEye);

		RayFactory mockFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockFactory.createRay(expectedD, expectedEye)).thenReturn(expectedRay);

		UtilImpl classUnderTest = new UtilImpl(mockFactory);

		Ray actual = classUnderTest.getReflectedRay(r, p, normal);

		Ray expected = new RayImpl(expectedD, expectedEye);
		assertEquals(expected, actual);
		Mockito.verify(mockFactory).createRay(expectedD, expectedEye);
	}

	@Test
	public void getReflectedColor_WithNonReflectiveSurfaceMocked_ExpectBlack()
			throws RaytracerException {

		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.isReflective()).thenReturn(false);
		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		Ray r = Mockito.mock(Ray.class);
		int currentDepth = -1;
		HitData hit = Mockito.mock(HitData.class);
		ScenePixel pixel = Mockito.mock(ScenePixel.class);

		UtilImpl classUnderTest = new UtilImpl();

		Color actual = classUnderTest.getReflectedColor(r, currentDepth, hit, mockSurface, pixel);

		Color expected = new Color(0.0, 0.0, 0.0);
		assertEquals(expected, actual);
	}

	@Test
	public void getReflectedColor_WithMockedRayInsideOfSurface_ExpectBlack()
			throws RaytracerException {

		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.isReflective()).thenReturn(true);
		Surface mockSurface = Mockito.mock(Surface.class);
		Vector mockVec = Mockito.mock(Vector.class);
		Mockito.when(mockVec.dot(mockVec)).thenReturn(1.0);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		Ray r = Mockito.mock(Ray.class);
		Mockito.when(r.getD()).thenReturn(mockVec);
		int currentDepth = -1;
		HitData hit = Mockito.mock(HitData.class);
		ScenePixel pixel = Mockito.mock(ScenePixel.class);

		UtilImpl classUnderTest = new UtilImpl();

		Color actual = classUnderTest.getReflectedColor(r, currentDepth, hit, mockSurface, pixel);

		Color expected = new Color(0.0, 0.0, 0.0);
		assertEquals(expected, actual);
	}

	@Test
	public void getReflectedColor_WithReflectedRay_ExpectCall() throws RaytracerException {

		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.isReflective()).thenReturn(true);
		Surface mockSurface = Mockito.mock(Surface.class);
		Vector mockVec = Mockito.mock(Vector.class);
		Mockito.when(mockVec.dot(mockVec)).thenReturn(-1.0);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);
		Ray r = Mockito.mock(Ray.class);
		Mockito.when(r.getD()).thenReturn(mockVec);
		int currentDepth = -1;
		Point mockPoint = Mockito.mock(Point.class);
		HitData hit = Mockito.mock(HitData.class);
		Mockito.when(hit.getNormal()).thenReturn(mockVec);
		Mockito.when(hit.getP()).thenReturn(mockPoint);
		Color reflectReturn = Mockito.mock(Color.class);
		ScenePixel pixel = Mockito.mock(ScenePixel.class);
		Mockito.when(pixel.getPixelColor(r, currentDepth + 1)).thenReturn(reflectReturn);

		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Color clampReturn = new Color(0.1, 0.3, 0.7);
		Mockito.doReturn(r).when(classUnderTest).getReflectedRay(r, mockPoint, mockVec);
		Mockito.doReturn(clampReturn).when(classUnderTest).clamp(reflectReturn);

		Color actual = classUnderTest.getReflectedColor(r, currentDepth, hit, mockSurface, pixel);

		Color expected = new Color(0.1, 0.3, 0.7);
		assertEquals(expected, actual);
	}

	@Test
	public void getRefractedColor_WithNonRefractiveMocks_ExpectBlack() throws RaytracerException {

		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.getRefractive()).thenReturn(null);

		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);

		UtilImpl classUnderTest = new UtilImpl();

		Color actual = classUnderTest.getRefractedColor(null, 0, null, mockSurface, null);

		Color expected = new Color(0.0, 0.0, 0.0);
		assertEquals(expected, actual);
	}

	@Test
	public void getRefractedColor_WithTotalInternalReflection_ExpectBlack()
			throws RaytracerException {

		double n = 1.0;
		double nT = 1.0;
		Refractive mockRefractive = Mockito.mock(Refractive.class);
		Mockito.when(mockRefractive.getN()).thenReturn(n);
		Mockito.when(mockRefractive.getnT()).thenReturn(nT);

		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.getRefractive()).thenReturn(mockRefractive);

		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);

		Vector mockD = Mockito.mock(Vector.class);

		Ray mockRay = Mockito.mock(Ray.class);
		Mockito.when(mockRay.getD()).thenReturn(mockD);

		HitData mockHit = Mockito.mock(HitData.class);

		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(null).when(classUnderTest).getRefractedRay(mockD, n, nT, mockHit);

		Color actual = classUnderTest.getRefractedColor(mockRay, 0, mockHit, mockSurface, null);

		Color expected = new Color(0.0, 0.0, 0.0);
		assertEquals(expected, actual);
	}

	@Test
	public void getRefractedColor_WithMockedRefraction_ExpectCalls() throws RaytracerException {

		int currentDepth = 0;
		double n = 1.0;
		double nT = 1.0;
		Refractive mockRefractive = Mockito.mock(Refractive.class);
		Mockito.when(mockRefractive.getN()).thenReturn(n);
		Mockito.when(mockRefractive.getnT()).thenReturn(nT);

		Effects mockEffects = Mockito.mock(Effects.class);
		Mockito.when(mockEffects.getRefractive()).thenReturn(mockRefractive);

		Surface mockSurface = Mockito.mock(Surface.class);
		Mockito.when(mockSurface.getEffects()).thenReturn(mockEffects);

		Vector mockD = Mockito.mock(Vector.class);

		Ray mockRay = Mockito.mock(Ray.class);
		Mockito.when(mockRay.getD()).thenReturn(mockD);

		HitData mockHit = Mockito.mock(HitData.class);

		Ray mockRefractedRay = Mockito.mock(Ray.class);

		Color mockPixelReturnColor = Mockito.mock(Color.class);

		ScenePixel mockPixel = Mockito.mock(ScenePixel.class);
		Mockito.when(mockPixel.getPixelColor(mockRefractedRay, currentDepth + 1))
				.thenReturn(mockPixelReturnColor);

		Color mockClampedColor = Mockito.mock(Color.class);

		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(mockRefractedRay).when(classUnderTest).getRefractedRay(mockD, n, nT, mockHit);
		Mockito.doReturn(mockClampedColor).when(classUnderTest).clamp(mockPixelReturnColor);

		Color actual = classUnderTest.getRefractedColor(mockRay, currentDepth, mockHit, mockSurface, mockPixel);

		assertTrue(mockClampedColor == actual);
	}

	@Test
	public void isInShadow_WithNoHits_ExpectFalse() throws RaytracerException {

		Point mockHitPoint = Mockito.mock(Point.class);

		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);

		Point mockLight = Mockito.mock(Point.class);

		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);

		Ray mockRayShotToLight = Mockito.mock(Ray.class);

		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);

		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(false);

		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight))
				.thenReturn(mockShadowHitData);

		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);

		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);

		assertFalse(result);
	}

	@Test
	public void isInShadow_HittingOutersphere_ExpectFalse() throws RaytracerException {

		Point mockHitPoint = Mockito.mock(Point.class);

		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);

		Point mockLight = Mockito.mock(Point.class);

		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);

		Ray mockRayShotToLight = Mockito.mock(Ray.class);

		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);

		Surface mockOutersphere = Mockito.mock(Surface.class);
		Mockito.when(mockOutersphere.getType()).thenReturn(Surface.SurfaceType.Outersphere);

		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(true);
		Mockito.when(mockShadowHitData.getSurface()).thenReturn(mockOutersphere);

		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight))
				.thenReturn(mockShadowHitData);

		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);

		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);

		assertFalse(result);
	}

	@Test
	public void isInShadow_HitObjectButLightIsCloser_ExpectFalse() throws RaytracerException {

		Point mockHitPoint = Mockito.mock(Point.class);

		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);

		Point mockLight = Mockito.mock(Point.class);

		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);
		Mockito.when(mockD.magnitude()).thenReturn(10.0);

		Ray mockRayShotToLight = Mockito.mock(Ray.class);

		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);

		Surface mockOutersphere = Mockito.mock(Surface.class);
		Mockito.when(mockOutersphere.getType()).thenReturn(Surface.SurfaceType.Sphere);

		Vector mockShadowPMinusMockHitPoint = Mockito.mock(Vector.class);
		Mockito.when(mockShadowPMinusMockHitPoint.magnitude()).thenReturn(20.0);

		Point mockShadowHitPoint = Mockito.mock(Point.class);
		Mockito.when(mockShadowHitPoint.minus(mockHitPoint)).thenReturn(mockShadowPMinusMockHitPoint);

		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(true);
		Mockito.when(mockShadowHitData.getSurface()).thenReturn(mockOutersphere);
		Mockito.when(mockShadowHitData.getP()).thenReturn(mockShadowHitPoint);

		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight))
				.thenReturn(mockShadowHitData);

		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);

		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);

		assertFalse(result);
	}

	@Test
	public void isInShadow_HitObjectCloserThanLight_ExpectTrue() throws RaytracerException {

		Point mockHitPoint = Mockito.mock(Point.class);

		HitData mockHitData = Mockito.mock(HitData.class);
		Mockito.when(mockHitData.getP()).thenReturn(mockHitPoint);

		Point mockLight = Mockito.mock(Point.class);

		Vector mockD = UtilImpl_TestsHelper.mockShadowD(mockLight, mockHitPoint);
		Mockito.when(mockD.magnitude()).thenReturn(10.0);

		Ray mockRayShotToLight = Mockito.mock(Ray.class);

		RayFactory mockRayFactory = Mockito.mock(RayFactory.class);
		Mockito.when(mockRayFactory.createRay(mockD, mockHitPoint)).thenReturn(mockRayShotToLight);

		Surface mockOutersphere = Mockito.mock(Surface.class);
		Mockito.when(mockOutersphere.getType()).thenReturn(Surface.SurfaceType.Sphere);

		Vector mockShadowPMinusMockHitPoint = Mockito.mock(Vector.class);
		Mockito.when(mockShadowPMinusMockHitPoint.magnitude()).thenReturn(5.0);

		Point mockShadowHitPoint = Mockito.mock(Point.class);
		Mockito.when(mockShadowHitPoint.minus(mockHitPoint)).thenReturn(mockShadowPMinusMockHitPoint);

		HitData mockShadowHitData = Mockito.mock(HitData.class);
		Mockito.when(mockShadowHitData.isHit()).thenReturn(true);
		Mockito.when(mockShadowHitData.getSurface()).thenReturn(mockOutersphere);
		Mockito.when(mockShadowHitData.getP()).thenReturn(mockShadowHitPoint);

		Scene mockScene = Mockito.mock(Scene.class);
		Mockito.when(mockScene.getSmallestPositiveHitDataOrReturnMiss(mockRayShotToLight))
				.thenReturn(mockShadowHitData);

		UtilImpl classUnderTest = new UtilImpl(mockRayFactory);

		boolean result = classUnderTest.isInShadow(mockScene, mockLight, mockHitData);

		assertTrue(result);
	}

	@Test
	public void getColorLambertian_InShadow_ExpectColor() {

		Color cR = new Color(0.2, 0.2, 0.2);
		Color cA = new Color(0.2, 0.2, 0.2);
		Color cL = null;

		Vector mockPointToLight = Mockito.mock(Vector.class);
		Mockito.when(mockPointToLight.normalizeReturn()).thenReturn(mockPointToLight);

		Vector n = Mockito.mock(Vector.class);
		Mockito.when(n.normalizeReturn()).thenReturn(n);

		Point light = Mockito.mock(Point.class);

		Point p = Mockito.mock(Point.class);
		Mockito.when(light.minus(p)).thenReturn(mockPointToLight);
		boolean inShadow = true;

		UtilImpl classUnderTest = new UtilImpl();

		Color expected = new Color(.04, .04, .04);

		Color actual = classUnderTest.getColorLambertian(cR, cA, cL, n, light, p, inShadow);

		assertEquals(expected, actual);
	}

	@Test
	public void getColorLambertian_NotInShadowNDotLGreaterThanZero_ExpectColor() {

		Color cR = new Color(0.2, 0.2, 0.2);
		Color cA = new Color(0.2, 0.2, 0.2);
		Color cL = new Color(0.2, 0.2, 0.2);

		Vector mockPointToLight = Mockito.mock(Vector.class);
		Mockito.when(mockPointToLight.normalizeReturn()).thenReturn(mockPointToLight);

		Point light = Mockito.mock(Point.class);

		Vector n = Mockito.mock(Vector.class);
		Mockito.when(n.normalizeReturn()).thenReturn(n);
		Mockito.when(n.dot(mockPointToLight)).thenReturn(1.0);

		Point p = Mockito.mock(Point.class);
		Mockito.when(light.minus(p)).thenReturn(mockPointToLight);
		boolean inShadow = false;

		UtilImpl classUnderTest = new UtilImpl();

		Color expected = new Color(.08, .08, .08);

		Color actual = classUnderTest.getColorLambertian(cR, cA, cL, n, light, p, inShadow);

		assertEquals(expected, actual);
	}

	@Test
	public void getColorLambertian_NotInShadowNDotLLessThanZero_ExpectColor() {

		Color cR = new Color(0.2, 0.2, 0.2);
		Color cA = new Color(0.2, 0.2, 0.2);
		Color cL = new Color(0.2, 0.2, 0.2);

		Vector mockPointToLight = Mockito.mock(Vector.class);
		Mockito.when(mockPointToLight.normalizeReturn()).thenReturn(mockPointToLight);

		Point light = Mockito.mock(Point.class);

		Vector n = Mockito.mock(Vector.class);
		Mockito.when(n.normalizeReturn()).thenReturn(n);
		Mockito.when(n.dot(mockPointToLight)).thenReturn(-1.0);

		Point p = Mockito.mock(Point.class);
		Mockito.when(light.minus(p)).thenReturn(mockPointToLight);
		boolean inShadow = false;

		UtilImpl classUnderTest = new UtilImpl();

		Color expected = new Color(.04, .04, .04);

		Color actual = classUnderTest.getColorLambertian(cR, cA, cL, n, light, p, inShadow);

		assertEquals(expected, actual);
	}

	@Test
	public void getColorPhong_InShadow_ExpectLambertianColor() {

		Color cR = new Color(0.2, 0.2, 0.2);
		Color cA = new Color(0.2, 0.2, 0.2);
		Color cL = new Color(0.2, 0.2, 0.2);
		Color lambertianReturn = new Color(0.1, 0.1, 0.1);
		boolean inShadow = true;
		int exponent = 32;

		Point mockEye = Mockito.mock(Point.class);

		Vector n = Mockito.mock(Vector.class);

		Point light = Mockito.mock(Point.class);

		Point p = Mockito.mock(Point.class);

		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(lambertianReturn)
				.when(classUnderTest)
				.getColorLambertian(cR, cA, cL, n, light, p, inShadow);

		Color expected = new Color(0.1, 0.1, 0.1);

		Color actual = classUnderTest.getColorPhong(cR, cA, cL, n, light, mockEye, exponent, p, inShadow);

		assertEquals(expected, actual);
	}

	@Test
	public void getColorPhong_NDotHLessThanZero_ExpectLambertianColor() {

		Color cR = new Color(0.2, 0.2, 0.2);
		Color cA = new Color(0.2, 0.2, 0.2);
		Color cL = new Color(0.2, 0.2, 0.2);
		Color lambertianReturn = new Color(0.1, 0.1, 0.1);
		boolean inShadow = false;
		int exponent = 32;

		Point mockEye = Mockito.mock(Point.class);

		Vector n = Mockito.mock(Vector.class);

		Vector mockPointToLight = Mockito.mock(Vector.class);
		Mockito.when(mockPointToLight.normalizeReturn()).thenReturn(mockPointToLight);

		Vector mockPointToEye = Mockito.mock(Vector.class);
		Mockito.when(mockPointToEye.normalizeReturn()).thenReturn(mockPointToEye);

		Vector mockH = Mockito.mock(Vector.class);
		Mockito.when(mockH.normalizeReturn()).thenReturn(mockH);
		Mockito.when(mockPointToLight.add(mockPointToEye)).thenReturn(mockH);

		Mockito.when(n.dot(mockH)).thenReturn(-1.0);

		Point p = Mockito.mock(Point.class);

		Point light = Mockito.mock(Point.class);
		Mockito.when(light.minus(p)).thenReturn(mockPointToLight);

		Mockito.when(mockEye.minus(p)).thenReturn(mockPointToEye);

		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(lambertianReturn)
				.when(classUnderTest)
				.getColorLambertian(cR, cA, cL, n, light, p, inShadow);

		Color expected = new Color(0.1, 0.1, 0.1);

		Color actual = classUnderTest.getColorPhong(cR, cA, cL, n, light, mockEye, exponent, p, inShadow);

		assertEquals(expected, actual);
	}

	@Test
	public void getColorPhong_NDotHGreaterThanZero_ExpectPhongColor() {

		Color cR = new Color(0.2, 0.2, 0.2);
		Color cA = new Color(0.2, 0.2, 0.2);
		Color cL = new Color(0.2, 0.2, 0.2);
		Color lambertianReturn = new Color(0.1, 0.1, 0.1);
		boolean inShadow = false;
		int exponent = 32;

		Point mockEye = Mockito.mock(Point.class);

		Vector n = Mockito.mock(Vector.class);

		Vector mockPointToLight = Mockito.mock(Vector.class);
		Mockito.when(mockPointToLight.normalizeReturn()).thenReturn(mockPointToLight);

		Vector mockPointToEye = Mockito.mock(Vector.class);
		Mockito.when(mockPointToEye.normalizeReturn()).thenReturn(mockPointToEye);

		Vector mockH = Mockito.mock(Vector.class);
		Mockito.when(mockH.normalizeReturn()).thenReturn(mockH);
		Mockito.when(mockPointToLight.add(mockPointToEye)).thenReturn(mockH);

		Mockito.when(n.dot(mockH)).thenReturn(1.0);

		Point p = Mockito.mock(Point.class);

		Point light = Mockito.mock(Point.class);
		Mockito.when(light.minus(p)).thenReturn(mockPointToLight);

		Mockito.when(mockEye.minus(p)).thenReturn(mockPointToEye);

		UtilImpl classUnderTest = Mockito.spy(new UtilImpl());
		Mockito.doReturn(lambertianReturn)
				.when(classUnderTest)
				.getColorLambertian(cR, cA, cL, n, light, p, inShadow);

		Color expected = new Color(0.14, 0.14, 0.14);

		Color actual = classUnderTest.getColorPhong(cR, cA, cL, n, light, mockEye, exponent, p, inShadow);

		assertEquals(expected, actual);
	}

	@Test
	public void solveQuadratic_WithImaginaryDiscriminant_ExpectNans() {

		double[] expected = new double[]{Double.NaN, Double.NaN};
		double a = 2;
		double b = 4;
		double c = 8;
		UtilImpl classUnderTest = new UtilImpl();

		double[] actual = classUnderTest.solveQuadratic(a, b, c);

		TestsHelper.doubleArraysSubsets(expected, actual);
	}

	@Test
	public void solveQuadratic_WithRealDiscriminant_ExpectNans() {

		double[] expected = new double[]{-0.45861873485089033, -6.541381265149109};
		double a = 1;
		double b = 7;
		double c = 3;
		UtilImpl classUnderTest = new UtilImpl();

		double[] actual = classUnderTest.solveQuadratic(a, b, c);

		TestsHelper.doubleArraysSubsets(expected, actual);
	}

	@Test
	public void hasHits_WithANaN_ExpectFalse() {

		double[] hits = new double[]{1.0, Double.NaN};
		UtilImpl classUnderTest = new UtilImpl();

		boolean result = classUnderTest.hasHits(hits);

		assertFalse(result);
	}

	@Test
	public void hasHits_WithNoNaNs_ExpectTrue() {

		double[] hits = new double[]{1.0, -1.0};
		UtilImpl classUnderTest = new UtilImpl();

		boolean result = classUnderTest.hasHits(hits);

		assertTrue(result);
	}

	@Test
	public void solveQuartic_HittingFirstSecondIf_ExpectZeros() {

		UtilImpl classUnderTest = new UtilImpl();
		double a = 1.0;
		double b = 0.0;
		double c = 0.0;
		double d = 0.0;
		double e = 0.0;

		double expected[] = new double[4];
		expected[0] = 0.0;
		expected[1] = 0.0;
		expected[2] = 0.0;
		expected[3] = 0.0;

		double[] actual = classUnderTest.solveQuartic(a, b, c, d, e);

		TestsHelper.doubleArraysSubsets(expected, actual);
	}

	@Test
	public void solveQuartic_HittingFirstThirdIf_ExpectZeros() {

		UtilImpl classUnderTest = new UtilImpl();
		double a = 1.0;
		double b = 0.0;
		double c = 0.0;
		double d = 0.0;
		double e = 1.0;

		double expected[] = new double[4];
		expected[0] = Double.NaN;
		expected[1] = Double.NaN;
		expected[2] = Double.NaN;
		expected[3] = Double.NaN;

		double[] actual = classUnderTest.solveQuartic(a, b, c, d, e);

		TestsHelper.doubleArraysSubsets(expected, actual);
	}

	@Test
	public void solveQuartic_HittingFifthSixthSeventhIf_ExpectZeros() {

		UtilImpl classUnderTest = new UtilImpl();
		double a = 1.0;
		double b = 0.0;
		double c = 0.0;
		double d = 1.0;
		double e = 0.0;

		double expected[] = new double[4];
		expected[0] = Double.NaN;
		expected[1] = Double.NaN;
		expected[2] = 0.0;
		expected[3] = -1.0;

		double[] actual = classUnderTest.solveQuartic(a, b, c, d, e);

		TestsHelper.doubleArraysSubsets(expected, actual);
	}

	@Test
	public void solveQuartic_HittingFourthSixthIf_ExpectZeros() {

		UtilImpl classUnderTest = new UtilImpl();
		double a = 1.0;
		double b = 1.0;
		double c = 0.0;
		double d = 0.0;
		double e = 0.0;

		double expected[] = new double[4];
		expected[0] = 0.0;
		expected[1] = 0.0;
		expected[2] = 0.0;
		expected[3] = -1.0;

		double[] actual = classUnderTest.solveQuartic(a, b, c, d, e);

		TestsHelper.doubleArraysSubsets(expected, actual);
	}

	@Test
	public void solveQuartic_WithTypicalCall_ExpectZeros() {

		UtilImpl classUnderTest = new UtilImpl();
		double a = 2.0;
		double b = 3.0;
		double c = -4.0;
		double d = 5.0;
		double e = 6.0;

		double expected[] = new double[4];
		expected[0] = Double.NaN;
		expected[1] = Double.NaN;
		expected[2] = -0.6992823840584236;
		expected[3] = -2.505678468626153;

		double[] actual = classUnderTest.solveQuartic(a, b, c, d, e);

		TestsHelper.doubleArraysSubsets(expected, actual);
	}

	@Test
	public void getGreaterValue_WithNanVal1_ExpectVal2() {

		UtilImpl classUnderTest = new UtilImpl();
		double expected = 1.0;

		double actual = classUnderTest.getGreaterValue(Double.NaN, 1.0);

		assertEquals(expected, actual, Constants.POSITIVE_ZERO);
	}

	@Test
	public void getGreaterValue_WithNanVal2_ExpectVal1() {

		UtilImpl classUnderTest = new UtilImpl();
		double expected = 1.0;

		double actual = classUnderTest.getGreaterValue(1.0, Double.NaN);

		assertEquals(expected, actual, Constants.POSITIVE_ZERO);
	}

	@Test
	public void getGreaterValue_WithVal1GreaterThanVal2_ExpectVal1() {

		UtilImpl classUnderTest = new UtilImpl();
		double expected = 1.0;

		double actual = classUnderTest.getGreaterValue(1.0, -1.0);

		assertEquals(expected, actual, Constants.POSITIVE_ZERO);
	}

	@Test
	public void getGreaterValue_WithVal2GreaterThanVal1_ExpectVal2() {

		UtilImpl classUnderTest = new UtilImpl();
		double expected = 1.0;

		double actual = classUnderTest.getGreaterValue(-1.0, 1.0);

		assertEquals(expected, actual, Constants.POSITIVE_ZERO);
	}

	@Test
	public void getTOnPlane_WithRay_ExpectT() {

		Vector d = new Vector(0.0, 0.0, -1.0);
		Point eye = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, eye);

		Vector normal = (new Vector(0.0, 1.0, 1.0)).normalizeReturn();
		Point pointOnPlane = new Point(-1.0, 0.0, 0.0);

		UtilImpl classUnderTest = new UtilImpl();
		double expected = 4.0;

		double actual = classUnderTest.getTOnPlane(r, pointOnPlane, normal);

		assertEquals(expected, actual, Constants.POSITIVE_ZERO);
	}

	@Test
	public void getCircleUVImageMapping_PhiGreaterThanZero_ExpectUVValue()
			throws IOException, RaytracerException {

		Point center = new Point(1.0, 1.0, 1.0);
		double radius = 3.0;
		Point hitPoint = new Point(3.0, 4.0, 7.0);
		int w = 101;
		int h = 103;

		UtilImpl classUnderTest = new UtilImpl();

		int[] actual = classUnderTest.getCircleUVImageMapping(hitPoint, center, radius, w, h);

		assertEquals(20, actual[0]);
		assertEquals(103, actual[1]);
	}

	@Test
	public void getCircleUVImageMapping_PhiLessThanZero_ExpectUVValue()
			throws IOException, RaytracerException {

		Point center = new Point(1.0, 1.0, 1.0);
		double radius = 3.0;
		Point hitPoint = new Point(-2.0, -2.0, -2.0);
		int w = 101;
		int h = 103;

		UtilImpl classUnderTest = new UtilImpl();

		int[] actual = classUnderTest.getCircleUVImageMapping(hitPoint, center, radius, w, h);

		assertEquals(63, actual[0]);
		assertEquals(0, actual[1]);
	}
}
