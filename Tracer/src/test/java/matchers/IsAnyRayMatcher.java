package matchers;

import org.mockito.ArgumentMatcher;

import scene.ray.Ray;



public class IsAnyRayMatcher extends ArgumentMatcher<Ray>
{
	@Override
	public boolean matches(Object argument)
	{
		return argument instanceof Ray;
	}
	
}
