package surface.csg.tree;

import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;
import java.util.ArrayList;
import math.Point;
import math.Vector;
import org.apache.commons.lang3.NotImplementedException;
import scene.ray.Ray;
import surface.Surface;
import surface.csg.factory.CSGTreeFactory;
import surface.csg.factory.CSGTreeFactoryImpl;

/**
 * Some thoughts about this class. THE LEFT SIDE OPERATION TAKES PRECEDENCE! To be more clear, the left surface is assumed to be the "base object." As
 * a result, its color, effects, etc. take precedence. Therefore these CSG operations are geometric only, so all style/effects that are returned are
 * that of the left side surface.
 */
public class CSGTree extends Surface {
	private CSGNode root;
	// private CSGTreeFactory factory;
	private HitData lastHitResult;

	public CSGTree(CSGTreeFactory incomingFactory) throws RaytracerException {
		// factory = incomingFactory;
	}

	public CSGTree() throws RaytracerException {
		this(new CSGTreeFactoryImpl());
	}

	/**
	 * This function ensures that the incomingRoot is a valid CSGTree (all Surfaces with no children, all operations have children).
	 */
	public void validate(CSGNode currNode) throws RaytracerException {
		if (currNode == null) {
			throw new RaytracerException("Node may not be null.");
		}

		if ((currNode.getLeftChild() != null && currNode.getRightChild() == null)
				|| (currNode.getLeftChild() == null && currNode.getRightChild() != null)) {
			throw new NullPointerException("Invalid CSGTree, one child is null, and other child is not.");
		}
		if (currNode.getLeftChild() == null
				&& currNode.getRightChild() == null
				&& currNode.getOperation() != null) {
			throw new RaytracerException("Invalid CSGTree, an operation node has no children.");
		}
		if (currNode.getLeftChild() != null
				&& currNode.getRightChild() != null
				&& currNode.getSurface() != null) {
			throw new RaytracerException("Invalid CSGTree, a node has children and contains a surface.");
		}
		if (currNode.getLeftChild() != null) {
			validate(currNode.getLeftChild());
		}
		if (currNode.getRightChild() != null) {
			validate(currNode.getRightChild());
		}
		// if we traverse the tree without any exceptions, then this is a valid tree
	}

	@Override
	public ArrayList<HitData> getHitData(Ray r) throws RaytracerException {
		if (root == null) {
			throw new RaytracerException(
					"The root has not been set on this tree. Call setRoot() on this instance before use.");
		}

		if (root.getSurface() != null) {
			ArrayList<HitData> retHitData = root.getSurface().getHitData(r);
			return retHitData;
		}

		/* //recurse down the tree until we get hit data from the leaf nodes CSGTree leftBranch = factory.createTree();
		 * leftBranch.setRoot(root.getLeftChild()); HitData leftHitData = leftBranch.getHitData(r);
		 * 
		 * CSGTree rightBranch = factory.createTree(); rightBranch.setRoot(root.getRightChild()); HitData rightHitData = rightBranch.getHitData(r);
		 * 
		 * lastHitResult = root.getOperation().getHitData(leftHitData, rightHitData, r); return lastHitResult; */
		throw new NotImplementedException();
	}

	public void setRoot(CSGNode incomingRoot) throws RaytracerException {
		root = incomingRoot;
		validate(root);
		CSGNode node = getBottomLeftChild();
		effects = node.getSurface().getEffects();
		cA = node.getSurface().getcA();
		cR = node.getSurface().getcR();
		cL = node.getSurface().getcL();
	}

	@Override
	public Effects getEffects() {
		return effects;
	}

	@Override
	public Color getCA() {
		return cA;
	}

	@Override
	public Color getCR() {
		return cR;
	}

	@Override
	public Color getCL() {
		return cL;
	}

	private CSGNode getBottomLeftChild() {
		CSGNode node = root;
		while (node.getSurface() == null) {
			node = node.getLeftChild();
		}
		return node;
	}

	public Vector getNormal(Point p, Ray r) {
		return lastHitResult.getNormal();
	}

	@Override
	public SurfaceType getType() {
		return SurfaceType.CSGTree;
	}
}
