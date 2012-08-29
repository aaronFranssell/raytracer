package matchers;

import org.mockito.ArgumentMatcher;

import surface.Surface;


public class IsAnySurfaceMatcher extends ArgumentMatcher<Surface>
{
	@Override
	public boolean matches(Object argument)
	{
		return argument instanceof Surface;
	}
}
