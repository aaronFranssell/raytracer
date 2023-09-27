package scene.pixel;

import etc.Color;
import math.Point;
import scene.Scene;
import scene.ray.Ray;

public interface ScenePixel {

	public Color getPixelColor(Ray r, int currentDepth) throws Exception;

	public Scene getScene();

	public int getCurrentDepth();

	public Point getEye();

	public Point getLight();
}
