package pictures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import math.Point;
import math.Vector;
import scene.Scene;
import scene.SceneImpl;
import scene.render.RenderResult;
import scene.render.SceneRendererImpl;
import surface.Surface;
import surface.bezier.BezierObject;
import surface.bezier.BezierPatch;
import etc.RaytracerException;

public class BezierObjectPicture
{
	public static void main(String[] args) throws RaytracerException, IOException, InterruptedException, ExecutionException
	{
		BezierPatch patch = getPatch();
		ArrayList<BezierPatch> patches = new ArrayList<BezierPatch>();
		patches.add(patch);
		
		BezierObject bezier = new BezierObject(patches);
		
		ArrayList<Surface> surfaceList = new ArrayList<Surface>();
		surfaceList.add(bezier);
		
		SceneRendererImpl renderer = getSceneRenderer(surfaceList);
		RenderResult result = renderer.render();
		
		javax.imageio.ImageIO.write(result.getImage(), "PNG", new java.io.File("img\\bezier.png"));
		System.out.println(result.getStopWatch().getDifference());
	}
	
	private static BezierPatch getPatch()
	{
		Point a = new Point(-4.0,14.0,0.0);
		Point b = new Point(-4.0,-5.0,0.0);
		Point c = new Point(13.0,-5.0,0.0);
		return new BezierPatch(a,b,c);
	}
	
	private static SceneRendererImpl getSceneRenderer(ArrayList<Surface> surfaceList) throws RaytracerException
	{
		Vector up = new Vector(0.0, 1.0, 0.0);
		Vector gaze = new Vector (0.0, 0.0, -1.0);
		Point eye = new Point(0.0, 0.0, 4.0);
		int left = -1;
		int right = 1;
		int top = 1;
		int bottom = -1;
		int width = 800;
		int height = 800;
		int maxDepth = 4;
		Point light = new Point(0.0,100.0, 100.0);
		
		Scene scene = new SceneImpl(surfaceList);
		SceneRendererImpl renderer = new SceneRendererImpl(up, gaze, eye, left, right, top, bottom, width, height, 3, scene, light, maxDepth);
		return renderer;
	}
}
