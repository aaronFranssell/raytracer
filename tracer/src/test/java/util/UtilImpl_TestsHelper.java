package util;

import math.Point;
import math.Vector;
import org.mockito.Mockito;

public class UtilImpl_TestsHelper {
	public static Vector mockShadowD(Point light, Point hitPoint) {
		Vector mockD = Mockito.mock(Vector.class);
		Mockito.when(mockD.normalizeReturn()).thenReturn(mockD);

		Mockito.when(light.minus(hitPoint)).thenReturn(mockD);

		return mockD;
	}
}
