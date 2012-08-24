package util.assertions;

public class MyAssert
{
	public static void notNull(Object obj, String message)
	{
		if(obj == null)
		{
			throw new NullPointerException(message);
		}
	}
	
	public static void notNull(Object obj)
	{
		notNull(obj, "Object may not be null.");
	}
}
