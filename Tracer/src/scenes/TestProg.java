package scenes;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import bumpMapping.StoneBump;

import math.Point;
import math.UVW;
import math.Vector;
import noise.noiseClasses.NoiseStone;
import noise.noiseClasses.NoiseWood;
import primitives.Cone;
import primitives.Cylinder;
import primitives.OuterSphere;
import primitives.Plane;
import primitives.Sphere;
import primitives.Surface;
import primitives.Torus;
import primitives.Triangle;
import util.Constants;
import util.Library;
import util.LogToFile;
import etc.Color;
import etc.Effects;
import etc.Ray;
import etc.ScenePixel;

public class TestProg
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
		float red = (float) 0.0;
		float green = (float) 0.0;
		float blue = (float) 0.0;

		Color cL = new Color(0.4,0.4,0.4);
		Point center = new Point(3.0,0.0,1.5);
		
		cR = new Color(0.0,0.0,0.5);
		Effects effects = new Effects();
		effects.setPhong(true);
		//effects.setReflective(true);
		effects.setReflective(true);
		effects.setNoiseMappedColorClass(new NoiseWood());
		center = new Point(0.0,3.0,0.0);
		Sphere s5 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null, 0.0);
		
		cR = new Color(0.0,0.0,0.5);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		center = new Point(-2.0,2.0,0.0);
		Sphere s7 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null, 0.0);
				
		cR = new Color(0.5,0.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		//effects.setRefractive(true);
		center = new Point(-2.0,0.0,0.0);
		Sphere s6 = new Sphere(center,1.0, cR, Constants.cA, cL,effects, null,0.0);
		
		Point bottomCylinder = new Point(2.0,2.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		//effects.setNoiseMappedColorClass(new NoiseStone());
		//effects.setRefractive(true);
		//effects.setLambertian(true);
		Cylinder c3 = new Cylinder(bottomCylinder, 0.5, cR, Constants.cA, cL, 3.0, new Vector(1.0,1.0,1.0), effects);
		
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Point point = new Point(0.0,-2.5,0.0);
		Vector normal =new Vector(0.0,1.0,0.0); 
		Plane f = new Plane(normal, point, cR,cL, Constants.cA, effects);
		
		LinkedList<Surface> surfaceList = new LinkedList<Surface>();
		effects = new Effects();
		effects.setPhong(true);
		String filePath = Constants.pathToIMGDirectory + "hubble1.JPG";
		OuterSphere background = new OuterSphere(800,filePath,effects,Constants.cA,cL);
		
		cR = new Color(0.0,0.7,0.5);
		effects = new Effects();
		//effects.setPhong(true);
		effects.setLambertian(true);
		//effects.setReflective(true);
		effects.setNoiseMappedColorClass(new NoiseStone());
		effects.setBumpMapClass(new StoneBump());
		center = new Point(2.0,0.0,0.0);
		Torus torus = new Torus(center,new Vector(1.0,1.0,1.0),1.25,0.25,cR,Constants.cA,cL,effects);
		
		Point a = new Point(0.0,2.0,0.0);
		Point b = new Point(0.0,0.0,0.0);
		Point c = new Point(2.0,0.0,0.0);
		effects = new Effects();
		effects.setPhong(true);
		effects.setRefractive(true);
		cR = new Color(0.5,0.3,0.3);
		Triangle t = new Triangle(cR,cL, Constants.cA, a, b, c, effects);
		
		center = new Point(-2.0,5.0,-8.0);
		filePath = Constants.pathToIMGDirectory + "moonSurface.jpg";
		effects = new Effects();
		//effects.setPhong(true);
		//effects.setReflective(true);
		effects.setLambertian(true);
		Sphere textureSphere = new Sphere(center,6.0, cR, Constants.cA, cL,effects, filePath, 0.65);
		
		Vector direction = new Vector(1.0,1.0,1.0);
		Point vertex = new Point(0.0,0.0,0.0);
		Point basePoint = new Point(-1.0,-1.0,-1.0);
		double length = 2.0;
		//double alpha = Math.sqrt(2)/2;//45 degrees
		double alpha = Math.sqrt(3)/2;//30 degrees
		cR = new Color(0.0,0.5,0.0);
		cL = new Color(0.3,0.3,0.3);
		effects = new Effects();
		effects.setPhong(true);
		effects.setReflective(true);
		Cone cone = new Cone(direction, vertex, alpha, basePoint, length, cR, Constants.cA, cL,effects);
		
		surfaceList.add(textureSphere);
		surfaceList.add(background);
		surfaceList.add(s6);
		surfaceList.add(s7);
		surfaceList.add(t);
		surfaceList.add(s5);
		surfaceList.add(c3);
		surfaceList.add(f);
		surfaceList.add(torus);
		surfaceList.add(cone);

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