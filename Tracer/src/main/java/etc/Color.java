package etc;

public class Color
{
	public double red;
	public double green;
	public double blue;
	
	/**
	 * 
	 * @param incomingRed Assumed to be between 0-255.
	 * @param incomingGreen Assumed to be between 0-255.
	 * @param incomingBlue Assumed to be between 0-255.
	 */
	public Color(int incomingRed, int incomingGreen, int incomingBlue)
	{
		red = ((double)incomingRed)/255.0;
		green = ((double)incomingGreen)/255.0;
		blue = ((double)incomingBlue)/255.0;
	}
	
	public Color scaleReturn(double val)
	{
		return new Color(red*val, green*val, blue*val);
	}
	
	/**
	 * 
	 * @param incomingRed Assumed to be between 0-1.
	 * @param incomingGreen Assumed to be between 0-1.
	 * @param incomingBlue Assumed to be between 0-1.
	 */
	public Color(double incomingRed, double incomingGreen, double incomingBlue)
	{
		red = incomingRed;
		green = incomingGreen;
		blue = incomingBlue;
	}
	
	public Color add(Color other)
	{
		return new Color(red + other.red, green + other.green, blue + other.blue);
	}
	
	public String toString()
	{
		return "red: " + red + " green: " + green + " blue: " + blue;
	}
}
