package noise.noiseClasses;

import org.apache.commons.math3.util.FastMath;

import noise.NoiseColor;
import math.Point;
import etc.Color;

public class Test extends NoiseColor
{
	public Color getColor(Point p)
	{
		double value = FastMath.sin(p.y*54 + 6*perlinNoise(p, 7, 0.7));

		//c1 is the smaller blotches of color (like the black in a piece of marble)
		Color c1 = new Color(0.1,0.1,0.1);
		//c2 is the larger more uniform color (like the white in a piece of marble)
		Color c2 = new Color(0.6,0.6,0.6);
		double f = (1 - FastMath.cos(FastMath.PI*value))/2.0;
		Color color = c1.scaleReturn((1-f)).add(c2.scaleReturn(f));
		
		return color;
	}
}
