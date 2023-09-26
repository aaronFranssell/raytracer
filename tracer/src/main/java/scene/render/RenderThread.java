package scene.render;

import java.util.concurrent.Callable;

public interface RenderThread extends Callable<double[][][]> {
  public static final int RED_INDEX = 0;
  public static final int GREEN_INDEX = 1;
  public static final int BLUE_INDEX = 2;

  public double[][][] call() throws Exception;
}
