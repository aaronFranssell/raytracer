package csg;

public class Operation
{
	public static final int UNION = 0;
	public static final int INTERSECTION = 1;
	public static final int BOUNDED_BY = 2;
	
	private int value;
	
	public Operation(int incomingOperation)
	{
		if(incomingOperation != UNION && incomingOperation != INTERSECTION && incomingOperation != BOUNDED_BY)
		{
			try
			{
				throw new Exception("Unrecognized operation: " + incomingOperation);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		value = incomingOperation;
	}

	public int getValue() {
		return value;
	}
}
