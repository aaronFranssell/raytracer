package pictures;

import java.util.ArrayList;

import math.Point;
import math.Vector;
import noise.noiseClasses.NoiseStone;
import scene.Scene;
import scene.SceneImpl;
import scene.render.SceneRendererImpl;
import surface.Surface;
import surface.csg.operation.BoundedBy;
import surface.csg.operation.Intersection;
import surface.csg.tree.CSGNode;
import surface.csg.tree.CSGTree;
import surface.primitives.Plane;
import surface.primitives.Sphere;
import surface.primitives.Torus;
import util.Constants;
import bumpMapping.StoneBump;
import etc.Color;
import etc.Effects;
import etc.Phong;

public class TestCSG
{
	public static void main(String[] args) throws Exception
	{
		Color cR;
		Point center;
		Effects effects = new Effects();
		Phong phong = new Phong();
		phong.setExponent(32);

		Color cL = new Color(0.4,0.4,0.4);
		center = new Point(1.0,0.0,0.0);
		cR = new Color(0.4,0.0,0.0);
		effects = new Effects();
		effects.setPhong(phong);
		Sphere s1 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null);
		
		center = new Point(0.0,0.0,0.0);
		cR = new Color(0.0,0.0,0.8);
		effects = new Effects();
		effects.setPhong(phong);
		Sphere s2 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null);

		cL = new Color(0.4,0.4,0.4);
		center = new Point(0.0,1.0,0.0);
		cR = new Color(0.4,0.0,0.0);
		effects = new Effects();
		effects.setPhong(phong);
		Sphere s3 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null);
		
		CSGTree aTree = new CSGTree();
		CSGNode halfSphere = new CSGNode(new BoundedBy(aTree));
		halfSphere.setLeftChild(new CSGNode(s2));
		halfSphere.setRightChild(new CSGNode(s1));
		
		CSGNode quarterSphere = new CSGNode(new BoundedBy(aTree));
		quarterSphere.setLeftChild(halfSphere);
		quarterSphere.setRightChild(new CSGNode(s3));
		
		aTree.setRoot(quarterSphere);
		
		effects = new Effects();
		effects.setPhong(phong);
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
		
		CSGTree torusTree = new CSGTree();
		CSGNode root = new CSGNode(new Intersection());
		root.setLeftChild(new CSGNode(t1));
		root.setRightChild(new CSGNode(t2));
		torusTree.setRoot(root);
		
		ArrayList<Surface> surfaceList = new ArrayList<Surface>();
		
		//surfaceList.add(aTree);
		surfaceList.add(f);
		//surfaceList.add(torusTree);
		surfaceList.add(t1);
		//surfaceList.add(t2);
		
		Vector up = new Vector(0.0, 1.0, 0.0);
		Vector gaze = new Vector (0.0, 0.0, -1.0);
		Point eye = new Point(0.0, 0.0, 4.0);
		int left = -1;
		int right = 1;
		int top = 1;
		int bottom = -1;
		int width = 800;
		int height = 800;
		int maxDepth = 6;
		Point light = new Point(0.0,100.0, 100.0);
		
		Scene scene = new SceneImpl(surfaceList);
				
		SceneRendererImpl renderer = new SceneRendererImpl(up, gaze, eye, left, right, top, bottom, width, height, 1, scene, light, maxDepth);
		renderer.render();
	}
}