package csg;

import org.junit.Test;

import primitives.Surface;

public class Test_CSGTree
{
	@Test(expected=NullPointerException.class)
	public void TestConstructor_NullSurface_ExpectNullPointerException() throws Exception
	{
		//Arrange
		Surface s = null;
		
		//Act
		new CSGTree(new CSGNode(s));
		
		//Assert
		throw new Exception("Exception not thrown.");
	}
	
	@Test(expected=Exception.class)
	public void TestConstructor_OperationNullChildren_ExpectException() throws Exception
	{
		new CSGTree(new CSGNode(new Operation(Operation.BOUNDED_BY)));
	}
}
