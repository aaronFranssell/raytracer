package bumpMapping;

import math.Point;
import math.Vector;
import noise.NoiseColor;
import scene.ray.Ray;

public class StoneBump implements BumpMap {
	@Override
	public Vector getBump(Ray r, Point p, Vector normal) {
		double noise = NoiseColor.perlinNoise(
				p, 7, 2.0); // for a more fluid looking perturbation use 4 as the middle term
		noise = (noise - (int) noise);
		normal = new Vector(normal.x + noise, normal.y + noise, normal.z + noise).normalizeReturn();
		return normal;
	}
}
