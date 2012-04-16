package bumpMapping;

import math.Point;
import math.Vector;

public interface BumpMap
{
	public abstract Vector getBump(Point p, Vector normal);
}
