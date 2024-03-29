package noise.noiseClasses;

import etc.Color;
import math.Point;
import noise.NoiseColor;

public class NoiseStone extends NoiseColor {
	public Color getColor(Point p) {
		double value = Math.sin(p.z * 1.4 + 6 * perlinNoise(p, 7, 0.7));

		// c1 is the smaller blotches of color (like the black in a piece of marble)
		Color c1 = new Color(0.1, 0.1, 0.1);
		// c2 is the larger more uniform color (like the white in a piece of marble)
		Color c2 = new Color(0.6, 0.6, 0.6);
		double f = (1 - Math.cos(Math.PI * value)) / 2.0;
		Color color = c1.scaleReturn((1 - f)).add(c2.scaleReturn(f));

		return color;
	}
}
