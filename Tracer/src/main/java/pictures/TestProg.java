package pictures;

import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import math.Point;
import math.Vector;
import noise.noiseClasses.NoiseStone;
import noise.noiseClasses.NoiseWood;
import scene.Scene;
import scene.SceneImpl;
import scene.render.RenderResult;
import scene.render.SceneRenderer;
import surface.Surface;
import surface.primitives.Cone;
import surface.primitives.Cylinder;
import surface.primitives.OuterSphere;
import surface.primitives.Plane;
import surface.primitives.Sphere;
import surface.primitives.Torus;
import surface.primitives.Triangle;
import util.Constants;
import util.FileLocator;
import bumpMapping.StoneBump;
import etc.Color;
import etc.Effects;

public class TestProg
{
	public static void main(String[] args) throws Exception
	{
		FileLocator loc = new FileLocator();
		Color cR;
		Color cL = new Color(0.4,0.4,0.4);
		Point center = new Point(3.0,0.0,1.5);
		
		cR = new Color(0.0,0.0,0.5);
		Effects effects = new Effects();
		effects.setPhong(true);
		//effects.setReflective(true);
		effects.setReflective(true);
		effects.setNoiseMappedColorClass(new NoiseWood());
		center = new Point(0.0,3.0,0.0);
		Sphere s5 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null, 0.0);
		
		cR = new Color(0.0,0.0,0.5);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		center = new Point(-2.0,2.0,0.0);
		Sphere s7 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null, 0.0);
				
		cR = new Color(0.5,0.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		//effects.setRefractive(true);
		center = new Point(-2.0,0.0,0.0);
		Sphere s6 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null,0.0);
		
		Point bottomCylinder = new Point(2.0,2.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		//effects.setNoiseMappedColorClass(new NoiseStone());
		//effects.setRefractive(true);
		//effects.setLambertian(true);
		Cylinder c3 = new Cylinder(bottomCylinder, 0.5, cR, Constants.cA, cL, 3.0, new Vector(1.0,1.0,1.0), effects);
		
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0); 
		Plane f = new Plane(normal, point, cR,cL, Constants.cA, effects);
		
		ArrayList<Surface> surfaceList = new ArrayList<Surface>();
		effects = new Effects();
		effects.setPhong(true);
		String filePath = loc.getImageDirectory() + "hubble1.JPG";
		OuterSphere background = new OuterSphere(filePath,effects,Constants.cA,cL, 1.0);
		
		cR = new Color(0.0,0.7,0.5);
		effects = new Effects();
		//effects.setPhong(true);
		effects.setLambertian(true);
		//effects.setReflective(true);
		effects.setNoiseMappedColorClass(new NoiseStone());
		effects.setBumpMapClass(new StoneBump());
		center = new Point(2.0,0.0,0.0);
		Torus torus = new Torus(center,new Vector(1.0,1.0,1.0),1.25,0.25,cR,Constants.cA,cL,effects);
		
		Point a = new Point(0.0,2.0,0.0);
		Point b = new Point(0.0,0.0,0.0);
		Point c = new Point(2.0,0.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		effects.setRefractive(true);
		cR = new Color(0.5,0.3,0.3);
		Triangle t = new Triangle(cR,cL, Constants.cA, a, b, c, effects);
		
		center = new Point(-2.0,5.0,-8.0);
		filePath = loc.getImageDirectory() + "moonSurface.jpg";
		effects = new Effects();
		//effects.setPhong(true);
		//effects.setReflective(true);
		effects.setLambertian(true);
		Sphere textureSphere = new Sphere(center,6.0, cR, Constants.cA, cL,effects, filePath, 0.65);
		
		Vector direction = new Vector(1.0,1.0,1.0);
		Point vertex = new Point(0.0,0.0,0.0);
		Point basePoint = new Point(-1.0,-1.0,-1.0);
		double length = 2.0;
		//double alpha = Math.sqrt(2)/2;//45 degrees
		double alpha = Math.sqrt(3)/2;//30 degrees
		cR = new Color(0.0,0.5,0.0);
		cL = new Color(0.3,0.3,0.3);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Cone cone = new Cone(direction, vertex, alpha, basePoint, length, cR, Constants.cA, cL,effects);
		
		surfaceList.add(textureSphere);
		surfaceList.add(background);
		surfaceList.add(s6);
		surfaceList.add(s7);
		surfaceList.add(t);
		surfaceList.add(s5);
		surfaceList.add(c3);
		surfaceList.add(f);
		surfaceList.add(torus);
		surfaceList.add(cone);

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
		
		Scene scene = new SceneImpl(surfaceList);
		SceneRenderer renderer = new SceneRenderer(up, gaze, eye, left, right, top, bottom, width, height, 1, scene, light);
		RenderResult result = renderer.render();
		System.out.println("scene rendered in: " + result.getStopWatch().getDifference());
		ImageIO.write(result.getImage(), "PNG", new File("C:\\raytracer\\Tracer\\img\\yonPicture.png"));
	}
}