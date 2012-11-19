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

	public static final int PHONG_EXPONENT = 32;
}
