package bumpMapping;

import math.Point;
import math.Vector;
import noise.NoiseColor;

public class StoneBump implements BumpMap
{
	@Override
	public Vector getBump(Point p, Vector normal)
	{
		double noise = NoiseColor.perlinNoise(p, 7, 2.0);//for a more fluid looking perturbation use 4 as the middle term
		noise = (noise - (int)noise);
		normal = new Vector(normal.x + noise, normal.y + noise, normal.z + noise);
		normal.normalize();
		return normal;
	}
}
