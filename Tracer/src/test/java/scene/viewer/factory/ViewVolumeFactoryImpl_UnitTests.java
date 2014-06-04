package scene.viewer.factory;

import org.junit.Assert;
import org.junit.Test;

import scene.viewer.ViewingVolume;
import util.Constants;

public class ViewVolumeFactoryImpl_UnitTests
{
	@Test
	public void getVolume_WithValues_ExpectViewingVolume()
	{
		//Given
		double top = 5;
		double bottom = 7;
		double left = 11;
		double right = 13;
		
		ViewingVolumeFactoryImpl classUnderTest = new ViewingVolumeFactoryImpl();
		
		//When
		ViewingVolume ret = classUnderTest.getVolume(left, right, bottom, top);
		
		//Then
		Assert.assertEquals(ret.getTop(), top, Constants.POSITIVE_ZERO);
		Assert.assertEquals(ret.getBottom(), bottom, Constants.POSITIVE_ZERO);
		Assert.assertEquals(ret.getLeft(), left, Constants.POSITIVE_ZERO);
		Assert.assertEquals(ret.getRight(), right, Constants.POSITIVE_ZERO);
	}
}
