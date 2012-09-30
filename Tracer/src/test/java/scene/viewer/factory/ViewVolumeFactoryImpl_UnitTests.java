package scene.viewer.factory;

import junit.framework.Assert;

import org.junit.Test;

import scene.viewer.ViewingVolume;

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
		Assert.assertEquals(ret.getTop(), top);
		Assert.assertEquals(ret.getBottom(), bottom);
		Assert.assertEquals(ret.getLeft(), left);
		Assert.assertEquals(ret.getRight(), right);
	}
}
