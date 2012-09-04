package scene.render;

import math.Point;
import math.UVW;
import math.Vector;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import scene.Scene;
import scene.viewer.ViewingVolume;

public class RenderThreadImpl_UnitTests
{
	@Test
	public void call_WithValidParameters_ExpectFactoryCalls()
	{
		//Given
		Point eye = new Point(0.0,0.0,4.0);
		ViewingVolume volume = new ViewingVolume(-1.0, 1.0, -1.0, 1.0);
		int width = 800;
		int height = 800;
		UVW basis = new UVW(new Vector(1.0,0.0,0.0), new Vector(0.0,1.0,0.0), new Vector(0.0,0.0,1.0));
		Point light = new Point(0.0,100.0,0.0);
		Scene mockScene = Mockito.mock(Scene.class);
		int startHeight = 400;
		int threadHeight = 400;
		
		
		//When
		
		//Then
	}
}
