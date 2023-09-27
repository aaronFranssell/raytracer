package surface.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import etc.HitData;
import etc.RaytracerException;
import helper.TestsHelper;
import java.util.ArrayList;
import math.Point;
import math.Vector;
import org.junit.jupiter.api.Test;
import scene.ray.Ray;
import scene.ray.RayImpl;

public class Parallelogram_IntegrationTests {
	@Test
	public void getHitData_WithMiss_ExpectNoData() throws RaytracerException {

		Point basePoint = new Point(-1.0, 1.0, -1.0);
		Vector v1 = new Vector(0.0, -2.0, 1.0);
		Vector v2 = new Vector(2.0, 0.0, 1.0);

		Vector d = new Vector(0.0, 1.0, -1.0);
		Point eye = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, eye);

		Parallelogram classUnderTest = new Parallelogram(basePoint, v1, v2, null, null, null, null);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		assertEquals(0, actual.size());
	}

	@Test
	public void getHitData_WithHit_ExpectHitData() throws RaytracerException {

		Point basePoint = new Point(-1.0, 1.0, -1.0);
		Vector v1 = new Vector(0.0, -2.0, 1.0);
		Vector v2 = new Vector(2.0, 0.0, 1.0);

		Vector d = new Vector(0.0, 0.1, -1.0);
		Point eye = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, eye);

		Parallelogram classUnderTest = new Parallelogram(basePoint, v1, v2, null, null, null, null);

		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData hitData = new HitData(
				4.2105263157894735,
				classUnderTest,
				new Vector(-0.4082482904638631, 0.4082482904638631, 0.8164965809277261),
				new Point(0.0, 0.42105263157894735, -0.21052631578947345));
		expected.add(hitData);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		TestsHelper.arrayListSubsets(expected, actual);
	}
}
