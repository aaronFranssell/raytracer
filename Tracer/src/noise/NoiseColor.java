package noise;

import math.Point;
import etc.Color;

public abstract class NoiseColor
{
	public abstract Color getColor(Point p);
	
	public static double perlinNoise(Point point, int octaves, double persistence)
	{
		ImprovedNoise noise = ImprovedNoise.getInstance();
		double noiseValue = 0.0;
		double p = 1.0;
		double frequencey = 1.0;
		//compute the sum of several octave functions
		for(int i = 0; i < octaves; i++)
		{
			double noiseMapValue = noise.getNoiseMap(point.scaledReturn(frequencey))*p;
			noiseValue += noiseMapValue;
			p *= persistence;
			frequencey *= 2.0;
		}
		
		return noiseValue * (1-persistence)/(1-p);
	}
}
