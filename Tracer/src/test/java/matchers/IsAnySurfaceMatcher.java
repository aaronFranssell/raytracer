package matchers;

import org.mockito.ArgumentMatcher;

import primitives.Surface;

public class IsAnySurfaceMatcher extends ArgumentMatcher<Surface>
{
	@Override
	public boolean matches(Object argument)
	{
		return argument instanceof Surface;
	}
}
