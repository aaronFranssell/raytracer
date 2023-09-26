package math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.Constants;

public class Transform_UnitTests
{
	private Vector localDirection;
	private Vector worldDirection;
	private Point worldCenter;
	
	@BeforeEach
	public void initVectors()
	{
		localDirection = new Vector(3.0,5.0,7.0);
		worldDirection = new Vector(2.234, -1.56, 3.3);
		worldCenter = new Point(4.555, 6.1, -3.9);
	}
	
	@Test
	public void translateVectorToLocal_WithVector_ExpectTransformedVector()
	{
		
		Vector vec = new Vector(3.21, -5.332, 7.87651);
		Transform transform = new Transform(localDirection, worldDirection, worldCenter);
		Vector answer = new Vector(0.385127744, 0.4111503898, 0.826215454);
		
		
		Vector result = transform.translateVectorToLocal(vec);
		
		
		assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void transformPointToLocal_WithPoint_ExpectTransformedPoint()
	{
		
		Point point = new Point(3.4, -5.0, 2.11111);
		Transform transform = new Transform(localDirection, worldDirection, worldCenter);
		Point answer = new Point(221.99932735742436, -75.52901832400656, 368.73653264967993);
		
		
		Point result = transform.transformPointToLocal(point);
		
		
		assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void translateVectorToWorld_WithVector_ExpectTransformedVector()
	{
		
		Vector vec = new Vector(0.385127744, 0.4111503898, 0.826215454);
		Transform transform = new Transform(localDirection, worldDirection, worldCenter);
		Vector answer = new Vector(3.21, -5.332, 7.87651);
		answer = answer.normalizeReturn();
		
		
		Vector result = transform.translateVectorToWorld(vec);
		result = result.normalizeReturn();
		
		
		assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}
	
	@Test
	public void transformPointToWorld_WithPoint_ExpectTransformedPoint()
	{
		
		Point point = new Point(221.99932735742436, -75.52901832400656, 368.73653264967993);
		Transform transform = new Transform(localDirection, worldDirection, worldCenter);
		Point answer = new Point(3.4, -5.0, 2.11111);
				
		
		Point result = transform.transformPointToWorld(point);
				
		
		assertEquals(answer.x, result.x, Constants.POSITIVE_ZERO);
		assertEquals(answer.y, result.y, Constants.POSITIVE_ZERO);
		assertEquals(answer.z, result.z, Constants.POSITIVE_ZERO);
	}
}
