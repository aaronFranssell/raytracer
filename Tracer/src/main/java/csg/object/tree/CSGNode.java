package csg.object.tree;

import csg.operation.Operation;
import primitives.Surface;

public class CSGNode
{
	private Operation operation;
	private Surface surface;
	private CSGNode leftChild;
	private CSGNode rightChild;
	
	public CSGNode(Operation incomingOperation)
	{
		operation = incomingOperation;
	}
	
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
