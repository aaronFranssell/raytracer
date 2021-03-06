package scene.pixel;

import math.Point;
import scene.Scene;
import scene.ray.Ray;
import etc.Color;
import etc.RaytracerException;

public interface ScenePixel
{

	public Color getPixelColor(Ray r, int currentDepth) throws RaytracerException;

	public Scene getScene();

	public int getCurrentDepth();

	public Point getEye();

	public Point getLight();
}
