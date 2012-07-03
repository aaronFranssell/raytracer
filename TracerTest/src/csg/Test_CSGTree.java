package csg;

import math.Point;
import math.Vector;

import org.junit.Before;
import org.junit.Test;

import primitives.Plane;
import primitives.Surface;
import util.Constants;
import csg.operation.BoundedBy;
import etc.Color;
import etc.Effects;
import etc.RaytracerException;

public class Test_CSGTree
{
	private Surface testSurface;
	private Color cL;
	private Color cR;
	
	@Before
	public void setUp() throws Exception
	{
		cR = new Color(0.4,0.8,0.4);
		cL = new Color(0.4,0.4,0.4);
		Effects effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0);
		testSurface = new Plane(normal, point, cR,cL, Constants.cA, effects);
	}
	
	@Test(expected=NullPointerException.class)
	public void TestConstructor_NullSurface_ExpectNullPointerException() throws Exception
	{
		//Arrange
		Surface s = null;
		
		//Act
		new CSGSurface(new CSGNode(s));
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=RaytracerException.class)
	public void TestConstructor_OperationNullChildren_ExpectException() throws Exception
	{
		//Arrange
		CSGNode node = new CSGNode(new BoundedBy());
		
		//Act
		new CSGSurface(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=NullPointerException.class)
	public void TestConstructor_OperationNullLeftChild_ExpectException() throws Exception
	{
		//Arrange
		CSGNode node = new CSGNode(new BoundedBy());
		CSGNode rightChild = new CSGNode(testSurface);
		node.setRightChild(rightChild);
		
		//Act
		new CSGSurface(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=NullPointerException.class)
	public void TestConstructor_OperationNullRightChild_ExpectException() throws Exception
	{
		//Arrange
		CSGNode node = new CSGNode(new BoundedBy());
		CSGNode leftChild = new CSGNode(testSurface);
		node.setLeftChild(leftChild);
		
		//Act
		new CSGSurface(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=RaytracerException.class)
	public void TestConstructor_SurfaceWithChildren_ExpectException() throws Exception
	{
		//Arrange
		CSGNode node = new CSGNode(new BoundedBy());
		CSGNode leftChild = new CSGNode(testSurface);
		CSGNode rightChild = new CSGNode(testSurface);
		rightChild.setLeftChild(leftChild);
		rightChild.setRightChild(rightChild);
		node.setLeftChild(leftChild);
		node.setRightChild(rightChild);
		
		//Act
		new CSGSurface(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test
	public void TestConstructor_OpWithChildren_ExpectNoException() throws Exception
	{
		//Arrange
		CSGNode node = new CSGNode(new BoundedBy());
		CSGNode leftChild = new CSGNode(testSurface);
		CSGNode rightChild = new CSGNode(testSurface);
		node.setLeftChild(leftChild);
		node.setRightChild(rightChild);
		
		//Act
		new CSGSurface(node);
		
		//Assert
	}
	
	@Test
	public void TestConstructor_OpWithUnbalancedTree_ExpectNoException() throws Exception
	{
		//Arrange
		CSGNode node = new CSGNode(new BoundedBy());
		CSGNode leftChild = new CSGNode(testSurface);
		CSGNode rightChild = new CSGNode(new BoundedBy());
		node.setLeftChild(leftChild);
		node.setRightChild(rightChild);
		rightChild.setLeftChild(leftChild);
		rightChild.setRightChild(new CSGNode(testSurface));
		
		//Act
		new CSGSurface(node);
		
		//Assert
	}
	
}
