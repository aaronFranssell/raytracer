package scene.render;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

public class SceneRendererImpl_IntegrationTests {
  @Test
  public void render_WithCompleteScene_ExpectPicture() throws Exception {
    URL resource = this.getClass().getClassLoader().getResource("result.png");
    BufferedImage answer = ImageIO.read(resource);

    RenderResult result = SceneRendererImpl_TestsHelper.getResult();

    assertTrue(SceneRendererImpl_TestsHelper.imagesAreEqual(result.getImage(), answer, 10));
  }
}
