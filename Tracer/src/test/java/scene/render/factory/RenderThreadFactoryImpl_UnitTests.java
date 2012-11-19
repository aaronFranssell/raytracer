package scene.render.factory;

import junit.framework.Assert;
import math.Point;
import math.UVW;

import org.junit.Test;
import org.mockito.Mockito;

import scene.Scene;
import scene.render.RenderThread;
import scene.viewer.ViewingVolume;

public class RenderThreadFactoryImpl_UnitTests
{
	@Test
	public void getRenderThread_WithValues_ExpectRenderThread()
	{
		//Given
		Point eye = Mockito.mock(Point.class);
		ViewingVolume volume = Mockito.mock(ViewingVolume.class);
		int pictureWidth  = 10;
		int pictureHeight = 11;
		UVW basis = Mockito.mock(UVW.class);
		Point light = Mockito.mock(Point.class);
		Scene scene = Mockito.mock(Scene.class);
		int startHeight = 7;
		int threadHeight = 5;
		int maxDepth = 6;
		RenderThreadFactoryImpl classUnderTest = new RenderThreadFactoryImpl();
		
		//When
		RenderThread ret = classUnderTest.getRenderThread(eye, volume, pictureWidth, pictureHeight, basis, light, scene, startHeight, threadHeight,
														  maxDepth);
		
		//Then
		Assert.assertNotNull(ret);
	}
}
