package csg;

import math.Point;
import math.Vector;
import primitives.Surface;
import util.Library;
import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;

/**
 * Some thoughts about this class. THE LEFT SIDE OPERATION TAKES PRECEDENCE! To be more clear, the left surface is assumed
 * to be the "base object." As a result, its color, 't' value, normal, etc. take precedence. Therefore these CSG operations
 * are geometric only, so all style/effects that are returned are that of the left side surface.
 * @author Me! Ha!
 *
 */
public class CSGTree extends Surface
{
	private CSGNode root;
	
	public CSGTree(CSGNode incomingRoot)
	{
		root = incomingRoot;
		try
		{
			validate(root);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		effects = getLeftChildEffects();
	}
	
	private Effects getLeftChildEffects()
	{
		CSGNode node = root;
		while(node.getSurface() == null)
		{
			node = node.getLeftChild();
		}//while
		return node.getSurface().getEffects();
	}
	
	/**
	 * This function ensures that the incomingRoot is a valid BSPTree (all Surfaces with no children, all operations
	 * have children).
	 * @throws Exception If there is an invalid BSP tree
	 *
	 */
	private void validate(CSGNode currNode) throws Exception
	{
		if((currNode.getLeftChild() != null && currNode.getRightChild() == null) ||
		   (currNode.getLeftChild() == null && currNode.getRightChild() != null))
		{
			throw new Exception("Invalid BSPTree, one child is null, and other child is not.");
		}//if
		if(currNode.getLeftChild() == null && currNode.getRightChild() == null && currNode.getOperation() != null)
		{
			throw new Exception("Invalid BSPTree, an operation node has no children.");
		}//if
		if(currNode.getLeftChild() != null && currNode.getRightChild() != null && currNode.getSurface() != null)
		{
			throw new Exception("Invalid BSPTree, a node has children and contains a surface.");
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

	@Override
	public Color getCA()
	{
		CSGNode node = root;
		while(node.getSurface() == null)
		{
			node = node.getLeftChild();
		}//while
		return node.getSurface().getCA();
	}

	@Override
	public Color getCL() {
		CSGNode node = root;
		while(node.getSurface() == null)
		{
			node = node.getLeftChild();
		}//while
		return node.getSurface().getCL();
	}

	@Override
	public Color getCR() {
		CSGNode node = root;
		while(node.getSurface() == null)
		{
			node = node.getLeftChild();
		}//while
		return node.getSurface().getCR();
	}
	
	public Vector getNormal(Point p, Ray r) throws Exception
	{
		HitData hit = getHitData(r);
		Point hitPoint = hit.getP();
		if(!hitPoint.equals(p))
		{
			try
			{
				throw new Exception("Hit point returned does not equal incoming hit point.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		return hit.getNormal();
	}

	@Override
	public HitData getHitData(Ray r) throws Exception
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
			leftHitData = new CSGTree(root.getLeftChild()).getHitData(r);
			rightHitData = new CSGTree(root.getRightChild()).getHitData(r);
		}//else
		
		if(root.getOperation().getValue() == Operation.UNION)
		{
			return getUnion(leftHitData, rightHitData);
		}//if
		
		if(root.getOperation().getValue() == Operation.INTERSECTION)
		{
			return getIntersection(leftHitData, rightHitData);
		}//if
		//Operation.BOUNDED_BY
		else
		{
			return getBoundedBy(leftHitData, rightHitData, r);
		}//else
	}
	
	/**
	 * The way I am defining a "bounded by" operation is everything between 2 hit points of a solid. So the left side
	 * solid will not be shown if it is hit between 2 points of the right side's "t" values. If this is the case, then
	 * the closest positive hit point from the right side will be returned as the near hit point. A side effect of this
     * algorithm is solids like planes and triangles will have no effect as a right handed op unless they are part of 
     * another CSG object.
	 * 
	 * @param leftHitData Object being bounded by the right object.
	 * @param rightHitData Bounding object of the left object.
	 * @return The hit point of the left bounded by the object on the right.
	 * @throws Exception 
	 */
	private HitData getBoundedBy(HitData leftHitData, HitData rightHitData, Ray r) throws Exception
	{
		//If left side isn't hit, then return the miss. If the right side isn't hit, then I can just return the miss
		//left side.
		if(!leftHitData.isHit() || !rightHitData.isHit())
		{
			return leftHitData;
		}//if
		
		Interval leftSideInterval = new Interval(leftHitData.getHitTs());
		Interval rightSideInterval = new Interval(rightHitData.getHitTs());
		
		//this is the case when the bounding solid leaves the original solid unaffected
		if(leftHitData.getSmallestT() < rightHitData.getSmallestT() && !rightSideInterval.isInInterval(leftHitData.getSmallestT()))
		{
			return leftHitData;
		}//if
		
		
		if(r.isShootingToLight())
		{
			return new HitData();
		}//if
		
		//this is the case where the near face of the right solid lies inside of the left solid surface
		if(leftSideInterval.isInInterval(rightHitData.getSmallestT()))
		{

			double nearT = leftSideInterval.getNearInterval(rightHitData.getSmallestT());
			Point nearHitPoint = Library.getP(nearT, r);
			Surface rightSurface = rightHitData.getSurface();
			Vector normal = rightSurface.getNormal(nearHitPoint, r);
			//return new HitData object. The hit point will be the far side of the bounding solid. The normal will
			//be the reversed right hit data's normal.
			return new HitData(nearT, this, normal.scaleReturn(-1.0), nearHitPoint,
							   Library.mergeTArrays(leftHitData.getHitTs(), rightHitData.getHitTs()));
		}//if
		
		//this is the case where all left side ts lie in the right solid. In this case, there is a miss.
		//t arrays are sorted on creation in HitData, so i can just refer to smallestTIndex + 1 for the next smallest t
		if(rightSideInterval.allInInterval(leftHitData.getHitTs()))
		{
			return new HitData();
		}//if
		
		//this is the case where the near t of the main solid lies inside of the bounding solid. The far hit point
		//of the bounding solid will be the hit point.
		if(rightSideInterval.isInInterval(leftHitData.getSmallestT()))
		{
			double farT = rightSideInterval.getNextGreatestInterval(leftHitData.getSmallestT());
			Point nearHitPoint = Library.getP(farT, r);
			Surface rightSurface = rightHitData.getSurface();
			Vector normal = rightSurface.getNormal(nearHitPoint, r);
			//return new HitData object. The hit point will be the far side of the bounding solid. The normal will
			//be the reversed right hit data's normal.
			return new HitData(farT, this, normal.scaleReturn(-1.0), nearHitPoint,
							   Library.mergeTArrays(leftHitData.getHitTs(), rightHitData.getHitTs()));
		}//if
		return leftHitData;
	}
	
	private HitData getIntersection(HitData leftHitData, HitData rightHitData)
	{
		if(leftHitData.isHit() && rightHitData.isHit())
		{
			//left side takes precedence
			return leftHitData;
		}//if
		else if(!leftHitData.isHit())
		{
			return leftHitData;
		}//if
		else
		{
			return rightHitData;
		}//else
	}

	private HitData getUnion(HitData leftHitData, HitData rightHitData)
	{
		//again, the left operation takes precedence.
		if(leftHitData.isHit())
		{
			return leftHitData;
		}//if
		if(rightHitData.isHit() && !leftHitData.isHit())
		{
			return rightHitData;
		}		
		//if both missed, then I can just return either hit data object...
		return rightHitData;
	}

	@Override
	public int getType()
	{
		return Surface.CSGTREE;
	}
}
