package surface.primitives;

import helper.TestsHelper;

import java.util.ArrayList;

import org.junit.Assert;

import math.Point;
import math.Vector;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import scene.ray.Ray;
import scene.ray.RayImpl;

import etc.HitData;
import etc.RaytracerException;

public class Cone_IntegrationTests
{
	@Test
	public void getHitData_WithVectorMissAbove_ExpectNoHitData() throws RaytracerException
	{
		//Given
		Vector direction = new Vector(1.0,1.0,1.0);
		Point vertex = new Point(0.0, 0.0, 0.0);
		double alpha = FastMath.sqrt(2)/2;
		Point basePoint = new Point(-1.0,-1.0,-1.0);
		double length = 2.0;

		Cone classUnderTest = new Cone(direction, vertex, alpha, basePoint, length, null, null, null, null);

		Vector d = new Vector(0.0,1.0,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		//Then
		Assert.assertTrue(actual.size() == 0);
	}

	@Test
	public void getHitData_WithVectorMissBelow_ExpectNoHitData() throws RaytracerException
	{
		//Given
		Vector direction = new Vector(1.0,1.0,1.0);
		Point vertex = new Point(0.0, 0.0, 0.0);
		double alpha = FastMath.sqrt(2)/2;
		Point basePoint = new Point(-1.0,-1.0,-1.0);
		double length = 2.0;

		Cone classUnderTest = new Cone(direction, vertex, alpha, basePoint, length, null, null, null, null);

		Vector d = new Vector(0.0,-1.0,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		//Then
		Assert.assertTrue(actual.size() == 0);
	}

	@Test
	public void getHitData_WithVectorMissBehindBasePoint_ExpectNoHitData() throws RaytracerException
	{
		//Given
		Vector direction = new Vector(1.0,0.0,0.0);
		Point vertex = new Point(0.0, 0.0, 0.0);
		double alpha = FastMath.sqrt(2)/2;
		Point basePoint = new Point(-1.0,0.0,0.0);
		double length = 2.0;

		Cone classUnderTest = new Cone(direction, vertex, alpha, basePoint, length, null, null, null, null);

		Vector d = new Vector(-4.0,0.0,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		//Then
		Assert.assertTrue(actual.size() == 0);
	}

	@Test
	public void getHitData_WithVectorMissAfterLength_ExpectNoHitData() throws RaytracerException
	{
		//Given
		Vector direction = new Vector(1.0,0.0,0.0);
		Point vertex = new Point(0.0, 0.0, 0.0);
		double alpha = FastMath.sqrt(2)/2;
		Point basePoint = new Point(-1.0,0.0,0.0);
		double length = 2.0;

		Cone classUnderTest = new Cone(direction, vertex, alpha, basePoint, length, null, null, null, null);

		Vector d = new Vector(4.0,0.0,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		//Then
		Assert.assertTrue(actual.size() == 0);
	}

	@Test
	public void getHitData_WithVectorHitOnLeftSide_ExpectHitData() throws RaytracerException
	{
		//Given
		Vector direction = new Vector(1.0,0.0,0.0);
		Point vertex = new Point(0.0, 0.0, 0.0);
		double alpha = FastMath.sqrt(2)/2;
		Point basePoint = new Point(-1.0,0.0,0.0);
		double length = 2.0;

		Vector d = new Vector(-0.2,0.0,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		Cone classUnderTest = new Cone(direction, vertex, alpha, basePoint, length, null, null, null, null);

		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData expectedHitData = new HitData(3.4838229927570645, classUnderTest, new Vector(0.6496369390800626, 0.0, 0.76024459707563), 
				new Point(-0.6832339008450572, 0.0, 0.583830495774714));
		expected.add(expectedHitData);
		expectedHitData = new HitData(4.920064981434389, classUnderTest, new Vector(-0.6496369390800626, -0.0, 0.7602445970756301), 
				new Point(-0.9649041287876334, 0.0, -0.8245206439381674));
		expected.add(expectedHitData);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}

	@Test
	public void getHitData_WithVectorHitOnRightSide_ExpectHitData() throws RaytracerException
	{
		//Given
		Vector direction = new Vector(1.0,0.0,0.0);
		Point vertex = new Point(0.0, 0.0, 0.0);
		double alpha = FastMath.sqrt(2)/2;
		Point basePoint = new Point(-1.0,0.0,0.0);
		double length = 2.0;

		Vector d = new Vector(0.2,0.0,-1.0);
		d = d.normalizeReturn();
		Point eye = new Point(0.0,0.0,4.0);
		Ray r = new RayImpl(d, eye);

		Cone classUnderTest = new Cone(direction, vertex, alpha, basePoint, length, null, null, null, null);

		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData expectedHitData = new HitData(3.4838229927570645, classUnderTest, new Vector(-0.6496369390800626, 0.0, 0.76024459707563), 
				new Point(0.6832339008450572, 0.0, 0.583830495774714));
		expected.add(expectedHitData);
		expectedHitData = new HitData(4.920064981434389, classUnderTest, new Vector(0.6496369390800626, -0.0, 0.7602445970756301), 
				new Point(0.9649041287876334, 0.0, -0.8245206439381674));
		expected.add(expectedHitData);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}

	@Test
	public void getHitData_HittingInsideOfConeCup_ExpectHitDataWithNormalsPointingTowardsEye() throws RaytracerException
	{
		//Given
		Vector direction = new Vector(1.0,0.0,0.0);
		Point vertex = new Point(0.0, 0.0, 0.0);
		double alpha = FastMath.sqrt(2)/2;
		Point basePoint = new Point(-1.0,0.0,0.0);
		double length = 2.0;

		Vector d = new Vector(-1.0,0.2,0.0);
		d = d.normalizeReturn();
		Point eye = new Point(4.0,0.0,0.0);
		Ray r = new RayImpl(d, eye);

		Cone classUnderTest = new Cone(direction, vertex, alpha, basePoint, length, null, null, null, null);

		ArrayList<HitData> expected = new ArrayList<HitData>();
		HitData expectedHitData = new HitData(3.305545576505693, classUnderTest, new Vector(0.6496369390800626, -0.7602445970756301, -0.0), 
											  new Point(0.7586458850629154, 0.6482708229874169, 0.0));
		expected.add(expectedHitData);

		//When
		ArrayList<HitData> actual = classUnderTest.getHitData(r);

		//Then
		TestsHelper.arrayListSubsets(expected, actual);
	}
}
