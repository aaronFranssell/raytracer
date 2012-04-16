package csg;

import primitives.Surface;

public class CSGNode
{
	private Operation operation;
	private Surface surface;
	private CSGNode leftChild;
	private CSGNode rightChild;
	
	/**
	 * A node in the BSP tree will either contain an operation, or a Surface, but not both.
	 * @param operation The operation associated with this node.
	 */
	public CSGNode(Operation incomingOperation)
	{
		operation = incomingOperation;
	}
	
	/**
	 * A node in the BSP tree will either contain an operation, or a Surface, but not both.
	 * @param incomingSurface The surface associated with this node.
	 */
	public CSGNode(Surface incomingSurface)
	{
		surface = incomingSurface;
	}

	public Operation getOperation() {
		return operation;
	}

	public Surface getSurface() {
		return surface;
	}

	public CSGNode getLeftChild() {
		return leftChild;
	}

	public CSGNode getRightChild() {
		return rightChild;
	}

	public void setLeftChild(CSGNode leftChild) {
		this.leftChild = leftChild;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public void setRightChild(CSGNode rightChild) {
		this.rightChild = rightChild;
	}

	public void setSurface(Surface surface) {
		this.surface = surface;
	}
}
