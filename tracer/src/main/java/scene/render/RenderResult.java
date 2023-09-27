package scene.render;

import java.awt.image.BufferedImage;
import util.stopwatch.StopWatch;

public class RenderResult {
	private StopWatch stopWatch;
	private BufferedImage image;

	public StopWatch getStopWatch() {
		return stopWatch;
	}

	public void setStopWatch(StopWatch stopWatch) {
		this.stopWatch = stopWatch;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
