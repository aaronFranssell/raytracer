package surface.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import etc.HitData;
import helper.TestsHelper;
import java.util.ArrayList;
import math.Point;
import math.Vector;
import org.junit.jupiter.api.Test;
import scene.ray.Ray;
import scene.ray.RayImpl;

public class Cylinder_IntegrationTests {
	@Test
	public void getHitData_WithMissAboveCylinder_ExpectNoHitData() {

		Point bottom = new Point(-1.0, 0.0, 0.0);
		double radius = 0.5;
		double height = 2.0;
		Vector direction = new Vector(1.0, 0.0, 0.0);

		Vector d = new Vector(0.0, 1.0, -1.0);
		Point e = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, e);

		Cylinder classUnderTest = new Cylinder(bottom, radius, null, null, null, height, direction, null);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		assertEquals(0, actual.size());
	}

	@Test
	public void getHitData_WithMissBelowCylinder_ExpectNoHitData() {

		Point bottom = new Point(-1.0, 0.0, 0.0);
		double radius = 0.5;
		double height = 2.0;
		Vector direction = new Vector(1.0, 0.0, 0.0);

		Vector d = new Vector(0.0, -1.0, -1.0);
		Point e = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, e);

		Cylinder classUnderTest = new Cylinder(bottom, radius, null, null, null, height, direction, null);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		assertEquals(0, actual.size());
	}

	@Test
	public void getHitData_WithMissThroughCylinder_ExpectNoHitData() {

		Point bottom = new Point(0.0, 0.0, 0.0);
		double radius = 0.5;
		double height = 2.0;
		Vector direction = new Vector(0.0, 0.0, -1.0);

		Vector d = new Vector(0.0, 0.0, -1.0);
		Point e = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, e);

		Cylinder classUnderTest = new Cylinder(bottom, radius, null, null, null, height, direction, null);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		assertEquals(0, actual.size());
	}

	@Test
	public void getHitData_WithMissLeft_ExpectNoHitData() {

		Point bottom = new Point(-1.0, 0.0, 0.0);
		double radius = 0.5;
		double height = 2.0;
		Vector direction = new Vector(1.0, 0.0, 0.0);

		Vector d = new Vector(-2.0, 0.0, -0.2);
		Point e = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, e);

		Cylinder classUnderTest = new Cylinder(bottom, radius, null, null, null, height, direction, null);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		assertEquals(0, actual.size());
	}

	@Test
	public void getHitData_WithMissRight_ExpectNoHitData() {

		Point bottom = new Point(-1.0, 0.0, 0.0);
		double radius = 0.5;
		double height = 2.0;
		Vector direction = new Vector(1.0, 0.0, 0.0);

		Vector d = new Vector(2.0, 0.0, -0.2);
		Point e = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, e);

		Cylinder classUnderTest = new Cylinder(bottom, radius, null, null, null, height, direction, null);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		assertEquals(0, actual.size());
	}

	@Test
	public void getHitData_WithHits_ExpectHitData() {

		Point bottom = new Point(-1.0, 0.0, 0.0);
		double radius = 0.5;
		double height = 2.0;
		Vector direction = new Vector(1.0, 0.0, 0.0);

		Vector d = new Vector(0.1, 0.1, -1.0);
		Point e = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, e);

		Cylinder classUnderTest = new Cylinder(bottom, radius, null, null, null, height, direction, null);

		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData expectedHitData = new HitData(
				3.6592691816684058,
				classUnderTest,
				new Vector(1.1102230246251578E-16, 0.731853836333682, 0.6814616366631892),
				new Point(0.3659269181668406, 0.3659269181668406, 0.34073081833159424));
		expected.add(expectedHitData);
		expectedHitData = new HitData(
				4.261522897539517,
				classUnderTest,
				new Vector(2.2204460492503106E-16, -0.8523045795079024, 0.5230457950790329),
				new Point(0.4261522897539517, 0.4261522897539517, -0.26152289753951674));
		expected.add(expectedHitData);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		TestsHelper.arrayListSubsets(expected, actual);
	}

	@Test
	public void getHitData_WithHitInsideCylinder_ExpectHitData() {

		Point bottom = new Point(0.0, 0.0, 0.0);
		double radius = 1.0;
		double height = 5.0;
		Vector direction = new Vector(0.0, 0.0, -1.0);

		Vector d = new Vector(0.0, 0.2, -1.0);
		Point e = new Point(0.0, 0.0, 4.0);
		Ray r = new RayImpl(d, e);

		Cylinder classUnderTest = new Cylinder(bottom, radius, null, null, null, height, direction, null);

		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData expectedHitData = new HitData(
				5,
				classUnderTest,
				new Vector(-0.0, -1.0, -2.220446049250313E-16),
				new Point(0.0, 1.0, -1.0));
		expected.add(expectedHitData);

		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		TestsHelper.arrayListSubsets(expected, actual);
	}
}
