package scene.render;

import etc.Color;
import math.Point;
import math.UVW;
import scene.Scene;
import scene.pixel.ScenePixel;
import scene.pixel.ScenePixelFactory;
import scene.pixel.ScenePixelFactoryImpl;
import scene.ray.Ray;
import scene.ray.RayFactory;
import scene.ray.RayFactoryImpl;
import scene.viewer.ViewingVolume;

public class RenderThreadImpl implements RenderThread {
	private Point eye;
	private ViewingVolume volume;
	private int pictureWidth;
	private int pictureHeight;
	private double[][][] imageData;
	private UVW basis;
	private Scene scene;
	private Point light;
	private int startHeight;
	private int threadHeight;
	private RayFactory rayFactory;
	private ScenePixelFactory pixelFactory;
	private int maxDepth;

	public RenderThreadImpl(
			Point incomingEye,
			ViewingVolume incomingVolume,
			int incomingPictureWidth,
			int incomingPictureHeight,
			UVW incomingBasis,
			Point incomingLight,
			Scene incomingScene,
			int incomingStartHeight,
			int incomingThreadHeight,
			RayFactory incomingRayFactory,
			ScenePixelFactory incomingPixelFactory,
			int incomingMaxDepth) {
		volume = incomingVolume;
		eye = incomingEye;
		pictureWidth = incomingPictureWidth;
		pictureHeight = incomingPictureHeight;
		basis = incomingBasis;
		light = incomingLight;
		startHeight = incomingStartHeight;
		threadHeight = incomingThreadHeight;
		scene = incomingScene;
		imageData = new double[pictureWidth][threadHeight][3];
		rayFactory = incomingRayFactory;
		pixelFactory = incomingPixelFactory;
		maxDepth = incomingMaxDepth;
	}

	public RenderThreadImpl(
			Point incomingEye,
			ViewingVolume incomingVolume,
			int incomingPictureWidth,
			int incomingPictureHeight,
			UVW incomingBasis,
			Point incomingLight,
			Scene incomingScene,
			int incomingStartHeight,
			int incomingThreadHeight,
			int incomingMaxDepth) {
		this(
				incomingEye,
				incomingVolume,
				incomingPictureWidth,
				incomingPictureHeight,
				incomingBasis,
				incomingLight,
				incomingScene,
				incomingStartHeight,
				incomingThreadHeight,
				new RayFactoryImpl(),
				new ScenePixelFactoryImpl(),
				incomingMaxDepth);
	}

	public double[][][] call() throws Exception {
		for (int w = 0; w < pictureWidth; w++) {
			for (int h = 0; h < threadHeight; h++) {
				Ray r = rayFactory.createRay(
						volume, eye, basis, pictureWidth, pictureHeight, w, h + startHeight);
				ScenePixel pixel = pixelFactory.createScenePixel(scene, eye, light, maxDepth);
				Color returnColor = pixel.getPixelColor(r, 0);
				imageData[w][h][RenderThread.RED_INDEX] = returnColor.red;
				imageData[w][h][RenderThread.GREEN_INDEX] = returnColor.green;
				imageData[w][h][RenderThread.BLUE_INDEX] = returnColor.blue;
			}
		}
		return imageData;
	}
}
