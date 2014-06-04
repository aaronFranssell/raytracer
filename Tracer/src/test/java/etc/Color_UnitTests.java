package etc;

import org.junit.Assert;
import org.junit.Test;

import util.Constants;

public class Color_UnitTests
{
	@Test
	public void add_WithColor_ExpectAddedColor()
	{
		//Given
		Color classUnderTest = new Color(0.3,0.2, 0.5);
		Color other = new Color(0.2, 0.7, 0.3);
		
		//When
		Color result = classUnderTest.add(other);
		
		//Then
		Assert.assertEquals(result.red, 0.5, Constants.POSITIVE_ZERO);
		Assert.assertEquals(result.green, 0.9, Constants.POSITIVE_ZERO);
		Assert.assertEquals(result.blue, 0.8, Constants.POSITIVE_ZERO);
	}
}
