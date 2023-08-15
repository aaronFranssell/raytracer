package scene.render;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

public class SceneRendererImpl_IntegrationTests
{
	@Test
	public void render_WithCompleteScene_ExpectPicture() throws Exception
	{
		System.out.println("hewwwwoo");
		//Given
		BufferedImage answer = ImageIO.read(new File("src\\test\\resources\\result.png"));
		
		//When
		RenderResult result = SceneRendererImpl_TestsHelper.getResult();
		
		//Then
		assertTrue(SceneRendererImpl_TestsHelper.imagesAreEqual(result.getImage(), answer, 10));
	}
}
