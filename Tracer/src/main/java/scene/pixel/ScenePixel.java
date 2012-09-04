package scene.pixel;

import etc.Color;
import etc.RaytracerException;

public interface ScenePixel
{
	public Color getPixelColor() throws RaytracerException;
}
