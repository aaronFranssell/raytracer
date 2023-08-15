package noise.noiseClasses;

import etc.Color;
import math.Point;
import noise.NoiseColor;

public class NightSkyBackground extends NoiseColor
{
	public Color getColor(Point p)
	{
		Color color = new Color(0.0,0.0,0.0);
		color = color.add(getStarfield(p));
		color = color.add(getCloudColor(p));
		return color;
	}

	private Color getCloudColor(Point p)
	{
		Point point = p.scaledReturn(0.02);//new Point(p.x, p.y, p.z);
		double val = 0.0;
		val += Math.abs(perlinNoise(point, 128, 0.45));
		val += Math.abs(perlinNoise(point, 128, 0.7))/2;
		val += Math.abs(perlinNoise(point, 128, 0.9))/4;
		if(val < 0.2)
		{
			return new Color(0.0,0.0,0.0);
		}
		val += 0.1;
		val = Math.min(val, 1.0);
		return new Color(val/4.0, val/2.0, val);
	}
	
	private Color getStarfield(Point p)
	{
		Color c1;
		Point point = p.scaledReturn(3);
		double val = perlinNoise(point, 7, 0.2);
		if(0.5 < val)
		{
			double bumpUp = 0.4;
			double weight = val;
			c1 = new Color(weight + bumpUp, weight + bumpUp, weight + bumpUp);
		}
		else
		{
			c1 = new Color(0.0,0.0,0.0);
		}
		return c1;
	}
}
