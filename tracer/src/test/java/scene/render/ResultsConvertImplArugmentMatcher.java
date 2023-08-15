package scene.render;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

public class ResultsConvertImplArugmentMatcher implements ArgumentMatcher<int[][][]>
{
	private int[][][] combinedArray;
	
	public ResultsConvertImplArugmentMatcher(int[][][] matchTo) {
		combinedArray = matchTo;
	}
	public static BaseMatcher<int[][][]> getMatcher(final int[][][] combinedArray)
	{
		return new BaseMatcher<int[][][]>()
		{
			@Override
			public boolean matches(Object item)
			{
				if(!(item instanceof int[][][]))
				{
					return false;
				}
				int[][][] array = (int[][][])item;
				if(combinedArray.length != array.length)
				{
					return false;
				}
				for(int i = 0; i < array.length; i++)
				{
					if(combinedArray[i].length != array[i].length)
					{
						return false;
					}
					for(int m = 0; m < array[i].length; m++)
					{
						if(combinedArray[i][m].length != array[i][m].length)
						{
							return false;
						}
						for(int c = 0; c < array[i][m].length; c++)
						{
							if(combinedArray[i][m][c] != array[i][m][c])
							{
								return false;
							}
						}
					}
				}
				return true;
			}
			
			@Override
			public void describeTo(Description arg0)
			{
				
			}
		};
	}

	@Override
	public boolean matches(int[][][] array) {
		if(combinedArray.length != array.length)
		{
			return false;
		}
		for(int i = 0; i < array.length; i++)
		{
			if(combinedArray[i].length != array[i].length)
			{
				return false;
			}
			for(int m = 0; m < array[i].length; m++)
			{
				if(combinedArray[i][m].length != array[i][m].length)
				{
					return false;
				}
				for(int c = 0; c < array[i][m].length; c++)
				{
					if(combinedArray[i][m][c] != array[i][m][c])
					{
						return false;
					}
				}
			}
		}
		return true;
	}
}
