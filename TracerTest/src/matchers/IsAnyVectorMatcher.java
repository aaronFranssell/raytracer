package matchers;

import math.Vector;

import org.mockito.ArgumentMatcher;

public class IsAnyVectorMatcher extends ArgumentMatcher<Vector>
{
	@Override
	public boolean matches(Object argument)
	{
		return argument instanceof Vector;
	}
}
