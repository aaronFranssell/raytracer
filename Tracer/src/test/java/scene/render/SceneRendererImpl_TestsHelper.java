package scene.render;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math.Point;
import math.Vector;
import noise.noiseClasses.NoiseStone;
import noise.noiseClasses.NoiseWood;
import scene.Scene;
import scene.SceneImpl;
import surface.Surface;
import surface.primitives.Cone;
import surface.primitives.Cylinder;
import surface.primitives.OuterSphere;
import surface.primitives.Plane;
import surface.primitives.Sphere;
import surface.primitives.Torus;
import surface.primitives.Triangle;
import util.Constants;
import bumpMapping.StoneBump;
import etc.Color;
import etc.Effects;
import etc.Phong;
import etc.Refractive;

public class SceneRendererImpl_TestsHelper
{
	public static RenderResult getResult() throws Exception
	{
		Color cR;
		Color cL = new Color(0.4,0.4,0.4);
		Point center = new Point(3.0,0.0,1.5);
		Phong phong = new Phong();
		phong.setExponent(32);
		
		cR = new Color(0.0,0.0,0.5);
		Effects effects = new Effects();
		effects.setPhong(phong);
		effects.setReflective(true);
		effects.setNoiseMappedColorClass(new NoiseWood());
		center = new Point(0.0,3.0,0.0);
		Sphere s5 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null, 0.0);
		
		cR = new Color(0.0,0.0,0.5);
		effects = new Effects();
		effects.setPhong(phong);
		effects.setReflective(true);
		center = new Point(-2.0,2.0,0.0);
		Sphere s7 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null, 0.0);
				
		cR = new Color(0.5,0.0,0.0);
		effects = new Effects();
		effects.setPhong(phong);
		effects.setReflective(true);
		center = new Point(-2.0,0.0,0.0);
		Sphere s6 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null,0.0);
		
		Point bottomCylinder = new Point(1.5,1.5,0.0);
		effects = new Effects();
		effects.setPhong(phong);
		effects.setReflective(true);
		Cylinder c3 = new Cylinder(bottomCylinder, 0.5, cR, Constants.cA, cL, 1.5, new Vector(1.0,1.0,1.0), effects);
		
		effects = new Effects();
		effects.setPhong(phong);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0); 
		Plane f = new Plane(normal, point, cR,cL, Constants.cA, effects);
		
		ArrayList<Surface> surfaceList = new ArrayList<Surface>();
		effects = new Effects();
		effects.setPhong(phong);
		String filePath = "src\\test\\resources\\hubble.JPG";
		OuterSphere background = new OuterSphere(filePath,effects,Constants.cA,cL, 1.0);
		
		cR = new Color(0.0,0.7,0.5);
		effects = new Effects();
		effects.setLambertian(true);
		effects.setNoiseMappedColorClass(new NoiseStone());
		effects.setBumpMapClass(new StoneBump());
		center = new Point(2.0,0.0,0.0);
		Torus torus = new Torus(center,new Vector(1.0,1.0,1.0),1.25,0.25,cR,Constants.cA,cL,effects);
		
		Point a = new Point(0.0,2.0,0.0);
		Point b = new Point(0.0,0.0,0.0);
		Point c = new Point(2.0,0.0,0.0);
		effects = new Effects();
		effects.setPhong(phong);
		Refractive refractive = new Refractive();
		refractive.setN(1.0);
		refractive.setnT(1.0);
		effects.setRefractive(refractive);
		cR = new Color(0.5,0.3,0.3);
		Triangle t = new Triangle(cR,cL, Constants.cA, a, b, c, effects);
		
		center = new Point(-2.0,5.0,-8.0);
		filePath = "src\\test\\resources\\moonSurface.jpg";
		effects = new Effects();
		effects.setLambertian(true);
		Sphere textureSphere = new Sphere(center,6.0, cR, Constants.cA, cL,effects, filePath, 0.65);
		
		Vector direction = new Vector(1.0,1.0,1.0);
		Point vertex = new Point(0.0,0.0,0.0);
		Point basePoint = new Point(-1.0,-1.0,-1.0);
		double length = 2.0;
		double alpha = Math.sqrt(3)/2;//30 degrees
		cR = new Color(0.0,0.5,0.0);
		cL = new Color(0.3,0.3,0.3);
		effects = new Effects();
		effects.setPhong(phong);
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
		int maxDepth = 6;
		Point light = new Point(0.0,100.0, 100.0);
		
		Scene scene = new SceneImpl(surfaceList);
		SceneRendererImpl renderer = new SceneRendererImpl(up, gaze, eye, left, right, top, bottom, width, height, 3, scene, light, maxDepth);
		RenderResult result = renderer.render();
		
		//ImageIO.write(result.getImage(), "png", new File("src\\test\\resources\\result.png"));
		
		return result;
	}
	
	public static boolean imagesAreEqual(BufferedImage b1, BufferedImage b2, int numDifferencesTolerated)
	{
		int[] pixelsB1 = getPixels(b1);
		int[] pixelsB2 = getPixels(b2);
		
		if(pixelsB1.length != pixelsB2.length)
		{
			return false;
		}
		
		int differences = 0;
		for(int i = 0; i < pixelsB1.length; i++)
		{
			if(pixelsB1[i] != pixelsB2[i])
			{
				differences++;
			}
		}
		
		boolean result = (differences <= numDifferencesTolerated);
		
		if(!result)
		{
			System.out.println("Found " + differences + " differences.");
		}
		
		return result;
	}
	
	private static int[] getPixels(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		return pixels;
	}
}
