package matchers;

import org.mockito.ArgumentMatcher;

public class IsAnyDoubleArrayMatcher extends ArgumentMatcher<double[]>
{
	@Override
	public boolean matches(Object argument)
	{
		return argument instanceof double[];
	}
}
