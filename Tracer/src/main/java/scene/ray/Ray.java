package scene.ray;

import math.Point;
import math.Vector;

public interface Ray
{
	public Vector getD();
	public Point getEye();
	public double getDistance(Point p);
}
