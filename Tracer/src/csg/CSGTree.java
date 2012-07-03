package csg;

import primitives.Surface;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;
import etc.RaytracerException;

/**
 * Some thoughts about this class. THE LEFT SIDE OPERATION TAKES PRECEDENCE! To be more clear, the left surface is assumed
 * to be the "base object." As a result, its color, 't' value, normal, etc. take precedence. Therefore these CSG operations
 * are geometric only, so all style/effects that are returned are that of the left side surface.
 *
 */
public class CSGTree
{
	private CSGNode root;
	
	public CSGTree(CSGNode incomingRoot) throws RaytracerException
	{
		root = incomingRoot;
		validate(root);
	}
	
	/**
	 * This function ensures that the incomingRoot is a valid CSGTree (all Surfaces with no children, all operations
	 * have children).
	 * @throws Exception If there is an invalid CSG tree
	 *
	 */
	public void validate(CSGNode currNode) throws RaytracerException
	{
		if(currNode == null){ throw new NullPointerException("Node may not be null."); }
		
		if((currNode.getLeftChild() != null && currNode.getRightChild() == null) ||
		   (currNode.getLeftChild() == null && currNode.getRightChild() != null))
		{
			throw new NullPointerException("Invalid CSGTree, one child is null, and other child is not.");
		}
		if(currNode.getLeftChild() == null && currNode.getRightChild() == null && currNode.getOperation() != null)
		{
			throw new RaytracerException("Invalid CSGTree, an operation node has no children.");
		}
		if(currNode.getLeftChild() != null && currNode.getRightChild() != null && currNode.getSurface() != null)
		{
			throw new RaytracerException("Invalid CSGTree, a node has children and contains a surface.");
		}
		if(currNode.getLeftChild() != null)
		{
			validate(currNode.getLeftChild());
		}
		if(currNode.getRightChild() != null)
		{
			validate(currNode.getRightChild());
		}
		//if we traverse the tree without any exceptions, then this is a valid tree
	}
	
	public HitData getHitData(Ray r, Surface surface) throws RaytracerException
	{
		HitData leftHitData;
		HitData rightHitData;
		if(root.getSurface() != null)
		{
			return root.getSurface().getHitData(r);
		}
		else
		{
			//recurse down the tree until we get hit data from the leaf nodes
			leftHitData = new CSGTree(root.getLeftChild()).getHitData(r,surface);
			rightHitData = new CSGTree(root.getRightChild()).getHitData(r,surface);
		}
		
		return root.getOperation().getHitData(leftHitData, rightHitData, r, surface);
	}
	
	public Effects getEffects()
	{
		CSGNode node = getBottomLeftChild();
		return node.getSurface().getEffects();
	}
	
	public Color getCA()
	{
		CSGNode node = getBottomLeftChild();
		return node.getSurface().getCA();
	}
	
	public Color getCR()
	{
		CSGNode node = getBottomLeftChild();
		return node.getSurface().getCR();
	}
	
	public Color getCL()
	{
		CSGNode node = getBottomLeftChild();
		return node.getSurface().getCL();
	}
	
	private CSGNode getBottomLeftChild()
	{
		CSGNode node = root;
		while(node.getSurface() == null)
		{
			node = node.getLeftChild();
		}
		return node;
	}
}
