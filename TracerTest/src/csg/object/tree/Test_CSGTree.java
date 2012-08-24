package csg.object.tree;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import junit.framework.Assert;

import org.junit.Test;

import primitives.Surface;
import csg.object.tree.factory.CSGTreeFactory;
import csg.operation.BoundedBy;
import csg.operation.Operation;
import etc.HitData;
import etc.Ray;
import etc.RaytracerException;
public class Test_CSGTree
{
	@Test(expected=RaytracerException.class)
	public void setRoot_NullSurface_ExpectNullPointerException() throws Exception
	{
		//Arrange
		CSGNode node = null;
		CSGTree classUnderTest = new CSGTree();
		
		//Act
		classUnderTest.setRoot(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=RaytracerException.class)
	public void setRoot_OperationNullChildren_ExpectException() throws Exception
	{
		//Arrange
		CSGTree classUnderTest = new CSGTree();
		CSGNode node = new CSGNode(new BoundedBy(classUnderTest));
		
		//Act
		classUnderTest.setRoot(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=NullPointerException.class)
	public void setRoot_OperationNullLeftChild_ExpectException() throws Exception
	{
		//Arrange
		Surface testSurface = Test_CSGTreeHelper.createTestSurface();
		CSGNode rightChild = new CSGNode(testSurface);
		CSGTree classUnderTest = new CSGTree();
		CSGNode node = new CSGNode(new BoundedBy(classUnderTest));
		node.setRightChild(rightChild);
		
		//Act
		classUnderTest.setRoot(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=NullPointerException.class)
	public void setRoot_OperationNullRightChild_ExpectException() throws Exception
	{
		//Arrange
		Surface testSurface = Test_CSGTreeHelper.createTestSurface();
		CSGNode leftChild = new CSGNode(testSurface);
		CSGTree classUnderTest = new CSGTree();
		CSGNode node = new CSGNode(new BoundedBy(classUnderTest));
		node.setLeftChild(leftChild);
		
		//Act
		classUnderTest.setRoot(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test(expected=RaytracerException.class)
	public void setRoot_SurfaceWithChildren_ExpectException() throws Exception
	{
		//Arrange
		Surface testSurface = Test_CSGTreeHelper.createTestSurface();
		CSGNode leftChild = new CSGNode(testSurface);
		CSGNode rightChild = new CSGNode(testSurface);
		rightChild.setLeftChild(leftChild);
		rightChild.setRightChild(rightChild);
		CSGTree classUnderTest = new CSGTree();
		CSGNode node = new CSGNode(new BoundedBy(classUnderTest));
		node.setLeftChild(leftChild);
		node.setRightChild(rightChild);
		
		//Act
		classUnderTest.setRoot(node);
		
		//Assert
		throw new Exception("Expected exception not thrown.");
	}
	
	@Test
	public void setRoot_OpWithChildren_ExpectNoException() throws Exception
	{
		//Arrange
		Surface testSurface = Test_CSGTreeHelper.createTestSurface();
		CSGNode leftChild = new CSGNode(testSurface);
		CSGNode rightChild = new CSGNode(testSurface);
		CSGTree classUnderTest = new CSGTree();
		CSGNode node = new CSGNode(new BoundedBy(classUnderTest));
		node.setLeftChild(leftChild);
		node.setRightChild(rightChild);
		
		//Act
		classUnderTest.setRoot(node);
		
		//Assert
	}
	
	@Test
	public void setRoot_OpWithUnbalancedTree_ExpectNoException() throws Exception
	{
		//Arrange
		Surface testSurface = Test_CSGTreeHelper.createTestSurface();
		CSGNode leftChild = new CSGNode(testSurface);
		CSGTree classUnderTest = new CSGTree();
		CSGNode rightChild = new CSGNode(new BoundedBy(classUnderTest));
		CSGNode node = new CSGNode(new BoundedBy(classUnderTest));
		rightChild.setLeftChild(leftChild);
		rightChild.setRightChild(new CSGNode(testSurface));
		node.setLeftChild(leftChild);
		node.setRightChild(rightChild);
		
		//Act
		classUnderTest.setRoot(node);
		
		//Assert
	}
	
	@Test(expected=RaytracerException.class)
	public void getHitData_WithNoRootSet_ExpectException() throws RaytracerException
	{
		//Arrange
		CSGTree classUnderTest = new CSGTree();
		Ray testRay = Test_CSGTreeHelper.getTestRay();
		
		//Act
		classUnderTest.getHitData(testRay);
		
		//Assert
		Assert.fail("Expected exception not thrown.");
	}
	
	@Test 
	public void getHitData_WithSurfaceInRoot_ExpectSurfaceHitData() throws RaytracerException
	{
		//Arrange
		Ray testRay = Test_CSGTreeHelper.getTestRay();
		Surface testSurface = mock(Surface.class);
		CSGNode node = new CSGNode(testSurface);
		HitData testHitData = new HitData();
		doReturn(testHitData).when(testSurface).getHitData(testRay);
		
		CSGTree classUnderTest = new CSGTree();
		classUnderTest.setRoot(node);
		
		//Act
		HitData retHitData = classUnderTest.getHitData(testRay);
		
		//Assert
		Assert.assertEquals(testHitData, retHitData);
	}
	
	@Test 
	public void getHitData_OperationConfiguredToReturnRightHitData_ExpectRightHitData() throws RaytracerException
	{
		//Arrange
		Ray testRay = Test_CSGTreeHelper.getTestRay();
		Surface rightSurface = mock(Surface.class);
		Surface leftSurface = mock(Surface.class);
		CSGNode left = new CSGNode(leftSurface);
		CSGNode right = new CSGNode(rightSurface);
		
		HitData rightHitData = new HitData();
		doReturn(rightHitData).when(rightSurface).getHitData(testRay);
		
		HitData leftHitData = new HitData();
		doReturn(leftHitData).when(leftSurface).getHitData(testRay);
		
		Operation mockOperation = mock(Operation.class);
		doReturn(rightHitData).when(mockOperation).getHitData(leftHitData, rightHitData, testRay);
		
		CSGNode node = new CSGNode(mockOperation);
		node.setLeftChild(left);
		node.setRightChild(right);
		
		CSGTreeFactory mockFactory = mock(CSGTreeFactory.class);
		doReturn(new CSGTree()).when(mockFactory).createTree();
		
		CSGTree classUnderTest = new CSGTree(mockFactory);
		classUnderTest.setRoot(node);
		
		//Act
		HitData retHitData = classUnderTest.getHitData(testRay);
		
		//Assert
		verify(mockFactory, times(2)).createTree();
		Assert.assertTrue(rightHitData == retHitData);
	}
}
