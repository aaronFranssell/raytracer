package scene.render;

public interface RenderThread
{
	public static final int RED_INDEX = 0;
	public static final int GREEN_INDEX = 1;
	public static final int BLUE_INDEX = 2;
	
	public double[][][] call() throws Exception;
}
