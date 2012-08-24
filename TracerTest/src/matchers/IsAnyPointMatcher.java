package matchers;

import math.Point;

import org.mockito.ArgumentMatcher;

public class IsAnyPointMatcher extends ArgumentMatcher<Point>
{
	@Override
	public boolean matches(Object argument)
	{
		return argument instanceof Point;
	}
}
