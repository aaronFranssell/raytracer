package csg.object.tree;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import primitives.Surface;
import csg.object.tree.CSGNode;
import csg.operation.Operation;

public class Test_CSGNode
{
	private CSGNode operationNode;
	private CSGNode surfaceNode;
	private Operation operation;
	private Surface surface;
	
	@Before
	public void setUp() throws Exception
	{
		operation = mock(Operation.class);
		surface = mock(Surface.class);
	}
	
	@Test(expected=NullPointerException.class)
	public void constructor_NullOperation_ExpectException()
	{
		//Arrange
		Operation op = null;
		
		//Act
		new CSGNode(op);
		
		//Assert
		Assert.fail("Expected exception not thrown.");
	}
	
	@Test
	public void constructor_WithOperation_OperationNotNullSurfaceNull()
	{
		//Arrange
		
		//Act
		operationNode = new CSGNode(operation);
		
		//Assert
		assert operationNode.getOperation() != null;
		assert operationNode.getSurface() == null;
	}
	
	@Test
	public void constructor_WithSurface_SurfaceNotNullOperationNull()
	{
		//Arrance
		
		//Act
		surfaceNode = new CSGNode(surface);
		
		//Assert
		assert surfaceNode.getSurface() != null;
		assert surfaceNode.getOperation() == null;
	}
}
