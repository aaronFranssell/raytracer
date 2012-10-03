package util;

import etc.Color;
public class Constants
{
	/**
	 * This is the amount that a 't' must be greater than in order to qualify as a real hitpoint.
	 * This keeps a ray from hitting its surface object.
	 */
	public static final double POSITIVE_ZERO = 0.00003;
	
	/**
	 * This is the ambient light constant. This is the base color "strength" of an object.
	 */
	public static final Color cA = new Color(0.5, 0.5, 0.5);

	/**
	 * This is the max depth the ray will go. At most it will have 5 bounces total for reflection/refraction.
	 */
	public static int maxDepth = 6;

	public static final int PHONG_EXPONENT = 32;
	
	/**
	 * Outersphere radius. Just has to be sufficiently large enough so that no shapes are behind it.
	 */
	public static final double OUTERSPHERE_RADIUS = 100.0;
	
	/**
	 * I dunno how these interact exactly. Their ratio means something about whether things appear bigger or 
	 * smaller... Lolz.
	 */
	public static double refractiveN = 1.0;
	public static double refractiveNT = 1.0;
	
}
