package noise.noiseClasses;

import noise.NoiseColor;
import math.Point;
import etc.Color;

public class NoiseWood extends NoiseColor
{
	public Color getColor(Point p)
	{
		double noise = perlinNoise(p.scaledReturn(0.75), 7, 0.2) * 25;
		noise = noise - (int) noise;
		Color brown = new Color(.31,.2,.08);
		return brown.scaleReturn(1-noise);
	}
}
