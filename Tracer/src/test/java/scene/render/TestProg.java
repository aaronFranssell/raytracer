package scene.render;

import java.io.File;

import javax.imageio.ImageIO;

import scene.render.RenderResult;

public class TestProg
{
	public static void main(String[] args) throws Exception
	{
		RenderResult result = SceneRendererImpl_TestsHelper.getResult();
		System.out.println(result.getStopWatch().getDifference());
		ImageIO.write(result.getImage(), "PNG", new File("K:\\raytracer\\Tracer\\img\\yonPicture.png"));
	}
}