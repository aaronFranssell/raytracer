package pictures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import math.Point;
import math.Vector;

import scene.Scene;
import scene.SceneImpl;
import scene.render.RenderResult;
import scene.render.SceneRendererImpl;
import surface.Surface;
import surface.primitives.Sphere;
import util.Constants;
import util.FileLocator;
import etc.Color;
import etc.Effects;
import etc.RaytracerException;
import etc.Refractive;

public class DoINeedToRefractInternalRays
{
	public static void main(String[] args) throws RaytracerException, IOException, InterruptedException, ExecutionException
	{
		System.out.println("Do i need to refract internal rays????");
		FileLocator loc = new FileLocator();
		
		ArrayList<Surface> surfaceList = new ArrayList<Surface>();
		
		Point center = new Point(-2.0,5.0,-8.0);
		String filePath = loc.getImageDirectory() + "moonSurface.jpg";
		Color cL = new Color(0.4,0.4,0.4);
		Effects effects = new Effects();
		//effects.setPhong(true);
		//effects.setReflective(true);
		effects.setLambertian(true);
		Refractive refractive = new Refractive();
		refractive.setN(1.3);
		refractive.setnT(1.0);
		effects.setRefractive(refractive);
		Sphere textureSphere = new Sphere(center,6.0, null, Constants.cA, cL, effects, filePath, 0.65);
		
		surfaceList.add(textureSphere);
		
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
		System.out.println(result.getStopWatch().getDifference());
		ImageIO.write(result.getImage(), "PNG", new File("C:\\raytracer\\Tracer\\img\\yonPicture.png"));
	}
}
