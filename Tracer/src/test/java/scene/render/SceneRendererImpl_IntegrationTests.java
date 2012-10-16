package scene.render;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import junit.framework.Assert;

import org.junit.Test;

public class SceneRendererImpl_IntegrationTests
{
	@Test
	public void render_WithCompleteScene_ExpectPicture() throws Exception
	{
		//Given
		BufferedImage answer = ImageIO.read(new File("src\\test\\resources\\result.png"));
		
		//When
		RenderResult result = SceneRendererImpl_TestsHelper.getResult();
		
		//Then
		Assert.assertTrue(SceneRendererImpl_TestsHelper.imagesAreEqual(result.getImage(), answer, 10));
	}
}
