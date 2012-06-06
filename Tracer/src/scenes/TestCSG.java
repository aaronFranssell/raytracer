package scenes;

import java.util.LinkedList;

import math.Point;
import math.Vector;
import noise.noiseClasses.NoiseStone;
import primitives.Plane;
import primitives.Sphere;
import primitives.Surface;
import primitives.Torus;
import scenes.render.SceneRenderer;
import util.Constants;
import bumpMapping.StoneBump;
import csg.CSGNode;
import csg.CSGTree;
import csg.Operation;
import etc.Color;
import etc.Effects;

public class TestCSG
{
	/**
	 * The application entry point.
	 * @param args the command line arguments.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		Color cR;
		Point center;
		Effects effects = new Effects();

		Color cL = new Color(0.4,0.4,0.4);
		center = new Point(4.0,0.0,1.0);
		cR = new Color(0.4,0.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		Sphere s1 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null,0.0);
		
		center = new Point(0.0,0.0,0.0);
		cR = new Color(4.0,0.0,0.8);
		effects = new Effects();
		effects.setPhong(true);
		Sphere s2 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null,0.0);

		cL = new Color(0.4,0.4,0.4);
		center = new Point(4.0,1.0,0.0);
		cR = new Color(0.4,0.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		Sphere s3 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null,0.0);
		
		CSGNode halfSphere = new CSGNode(new Operation(Operation.BOUNDED_BY));
		halfSphere.setLeftChild(new CSGNode(s2));
		halfSphere.setRightChild(new CSGNode(s1));
		
		CSGNode quarterSphere = new CSGNode(new Operation(Operation.BOUNDED_BY));
		quarterSphere.setLeftChild(halfSphere);
		quarterSphere.setRightChild(new CSGNode(s3));
		CSGTree theTree = new CSGTree(quarterSphere);
		
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0);
		Plane f = new Plane(normal, point, cR,cL, Constants.cA, effects);
		
		cR = new Color(0.0,0.7,0.5);
		effects = new Effects();
		//effects.setPhong(true);
		effects.setLambertian(true);
		//effects.setReflective(true);
		effects.setNoiseMappedColorClass(new NoiseStone());
		effects.setBumpMapClass(new StoneBump());
		center = new Point(0.0,0.0,0.0);
		Torus t1 = new Torus(center,new Vector(0.0,0.0,1.0),1.25,0.25,cR,Constants.cA,cL,effects);
		
		center = new Point(0.0,0.0,0.0);
		Vector direction = new Vector(0.0,1.0,0.0);
		cR = new Color(0.0,4.0,4.0);
		Torus t2 = new Torus(center, direction, 1.5, 1.0, cR, Constants.cA, cL, effects);
		
		CSGNode root = new CSGNode(new Operation(Operation.BOUNDED_BY));
		root.setLeftChild(new CSGNode(t1));
		root.setRightChild(new CSGNode(t2));
		CSGTree torusTree = new CSGTree(root);
		
		LinkedList<Surface> surfaceList = new LinkedList<Surface>();
		
		//surfaceList.add(theTree);
		//surfaceList.add(f);
		surfaceList.add(torusTree);
		//surfaceList.add(t1);
		
		Vector up = new Vector(0.0, 1.0, 0.0);
		Vector gaze = new Vector (0.0, 0.0, -1.0);
		Point eye = new Point(0.0, 0.0, 4.0);
		int left = -1;
		int right = 1;
		int top = 1;
		int bottom = -1;
		int width = 800;
		int height = 800;
		Point light = new Point(0.0,100.0, 100.0); 
				
		SceneRenderer renderer = new SceneRenderer(up, gaze, eye, left, right, top, bottom, width, height, 1, surfaceList, "yonPicture", light);
		renderer.render();
	}
}