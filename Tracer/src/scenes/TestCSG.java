package scenes;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import math.Point;
import math.UVW;
import math.Vector;
import primitives.Plane;
import primitives.Sphere;
import primitives.Surface;
import util.Constants;
import util.Library;
import util.LogToFile;
import csg.CSGNode;
import csg.CSGTree;
import csg.Operation;
import etc.Color;
import etc.Effects;
import etc.Ray;
import etc.ScenePixel;

public class TestCSG
{
	/**
	 * The application entry point.
	 * @param args the command line arguments.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		String writeToFile = Constants.pathToIMGDirectory + "yonPicture";
		int[][][] imageData = new int[Constants.width][Constants.height][3];
		// Create a BufferedImage with RGB pixels.
		BufferedImage image = new BufferedImage(Constants.width,Constants.height,BufferedImage.TYPE_INT_RGB);
		// Create a raster so we can access the BufferedImage pixels.
		WritableRaster raster = image.getRaster();
		//Library.normalizeLight(light);
		System.out.println("light: " + Constants.light.x + ","+Constants.light.y+","+Constants.light.z);

		Ray r;
		Color cR;
		Point center;
		float red = (float) 0.0;
		float green = (float) 0.0;
		float blue = (float) 0.0;
		Effects effects = new Effects();

		Color cL = new Color(0.4,0.4,0.4);
		center = new Point(0.0,0.0,1.0);
		cR = new Color(0.4,0.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		Sphere s1 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null,0.0);
		
		center = new Point(0.0,0.0,0.0);
		cR = new Color(0.0,0.0,0.8);
		effects = new Effects();
		effects.setPhong(true);
		Sphere s2 = new Sphere(center,2.0, cR, Constants.cA, cL,effects, null,0.0);
		
		CSGNode root = new CSGNode(new Operation(Operation.BOUNDED_BY));
		root.setLeftChild(new CSGNode(s2));
		root.setRightChild(new CSGNode(s1));
		CSGTree tree = new CSGTree(root);
		
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0); 
		Plane f = new Plane(normal, point, cR,cL, Constants.cA, effects);
		
		LinkedList<Surface> surfaceList = new LinkedList<Surface>();
		
		surfaceList.add(tree);
		surfaceList.add(f);
		//surfaceList.add(s1);
		//surfaceList.add(s2);

		Color returnColor = null;
		/*
		 * 	0|
		 *   |
		 *   |
		 *   |
		 *   |
		 *   |
		 *   |
		 *   |
		 *  h|--------------------------
		 *   0                          w
		 */
		UVW basis = new UVW(Constants.up,Constants.gaze);
		System.out.println("u: "+ basis.getU().x + ", " + basis.getU().y + ", " + basis.getU().z);
		System.out.println("v: "+ basis.getV().x + ", " + basis.getV().y + ", " + basis.getV().z);
		System.out.println("w: "+ basis.getW().x + ", " + basis.getW().y + ", " + basis.getW().z);
		for(int w=0;w<Constants.width;w++)
		{
			//System.out.println("w: " + w);
			for(int h=0;h<Constants.height;h++)
			{
				r = new Ray(Constants.eye, Constants.left, Constants.right,Constants.bottom, Constants.top, 
							Constants.width, Constants.height, w, h,basis.getU(),basis.getV(), basis.getW());
				try {
					ScenePixel pixel = new ScenePixel(r,surfaceList,0);
					returnColor = pixel.getPixelColor();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
				red = (float) returnColor.red;
				green = (float) returnColor.green;
				blue = (float) returnColor.blue;
				imageData[w][h][0] = convertToInt(red);
				Constants.totalRGBValues += imageData[w][h][0];
				Constants.totalSamples++;
				if(imageData[w][h][0] > Constants.largestIntColorValue)
				{
					Constants.largestIntColorValue = imageData[w][h][0];
				}//if
				imageData[w][h][1] = convertToInt(green);
				Constants.totalRGBValues += imageData[w][h][1];
				Constants.totalSamples++;
				if(imageData[w][h][1] > Constants.largestIntColorValue)
				{
					Constants.largestIntColorValue = imageData[w][h][1];
				}//if
				imageData[w][h][2] = convertToInt(blue);
				Constants.totalRGBValues += imageData[w][h][2];
				Constants.totalSamples++;
				if(blue > Constants.largestIntColorValue)
				{
					Constants.largestIntColorValue = imageData[w][h][2];
				}//if
				if(red > 1.0)
				{
					Constants.numberAbove255++;
				}//if
				if(green > 1.0)
				{
					Constants.numberAbove255++;
				}//if
				if(blue > 1.0)
				{
					Constants.numberAbove255++;
				}//if
				//raster.setPixel(w,((height-1)-h),imageData[w][h]);
			}//for
		}//for
		Library.scale2(imageData,Constants.imagePctScale,Constants.totalRGBValues, Constants.totalSamples,
					   Constants.largestIntColorValue,Constants.numberAbove255,Constants.width,Constants.height);
		for(int w = 0; w<Constants.width;w++)
		{
			for(int h = 0; h < Constants.height; h++)
			{
				raster.setPixel(w,((Constants.height-1)-h),imageData[w][h]);
			}//for
		}//for
		ImageIO.write(image,"PNG",new File(writeToFile + ".png"));
		LogToFile.close();
	}
	
	private static int convertToInt(double incoming)
	{
		return (int) ((int) (255 * incoming));
	}
}