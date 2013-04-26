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
import surface.bezier.BezierSurface;
import etc.RaytracerException;

public class BezierSurfacePicture
{
	public static void main(String[] args) throws RaytracerException, IOException, InterruptedException, ExecutionException
	{
		ArrayList<ArrayList<Point>> rows = getRows();
		BezierSurface surface = new BezierSurface(rows);
		
		ArrayList<Surface> surfaceList = new ArrayList<Surface>();
		surfaceList.add(surface);
		
		SceneRendererImpl renderer = getSceneRenderer(surfaceList);
		RenderResult result = renderer.render();
		
		javax.imageio.ImageIO.write(result.getImage(), "PNG", new java.io.File("img\\bezierSurface.png"));
		System.out.println(result.getStopWatch().getDifference());
	}
	
	private static ArrayList<ArrayList<Point>> getRows()
	{
		ArrayList<Point> row1 = new ArrayList<Point>();
		row1.add(new Point(-2.0,0.0,-2.0));
		row1.add(new Point(0.0,1.0,-2.0));
		row1.add(new Point(2.0,0.0,-2.0));
		
		ArrayList<Point> row2 = new ArrayList<Point>();
		row2.add(new Point(-2.0,1.0,0.0));
		row2.add(new Point(0.0,2.0,0.0));
		row2.add(new Point(2.0,1.0,0.0));
		
		ArrayList<Point> row3 = new ArrayList<Point>();
		row3.add(new Point(-2.0,0.0,2.0));
		row3.add(new Point(0.0,2.0,2.0));
		row3.add(new Point(2.0,0.0,2.0));
		
		ArrayList<ArrayList<Point>> rows = new ArrayList<ArrayList<Point>>();
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		
		return rows;
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
