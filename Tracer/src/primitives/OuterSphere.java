package primitives;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.Ray;

import util.Library;

import math.Point;
import math.Vector;
import noise.NoiseColor;


public class OuterSphere extends Surface
{
	private Point center;
	private String filePath;
	private double radius;
	private int[][][] image;
	int w;
	int h;
	double t;
	private Color cA;
	private Color cL;
	private Color cR;
	
	public OuterSphere(double incomingRadius, String incomingFilePath,Effects incomingEffects,Color incomingCA, 
					   Color incomingCL)
	{
		cA = incomingCA;
		cL = incomingCL;
		
		center = new Point(0.0,0.0,0.0);
		radius = incomingRadius;
		filePath = incomingFilePath;
		radius = incomingRadius;
		effects = incomingEffects;
		if(effects.getNoiseMappedColorClass() == null)
		{
			readImage();
		}//if
		System.out.println("w: " + w);
		System.out.println("h: " + h);
	}
	
	private void readImage()
	{
		BufferedImage fileImage = null;
		try {
			File yonFile = new File(filePath);
			fileImage = ImageIO.read(yonFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		w = fileImage.getWidth();
		h = fileImage.getHeight();
		image = new int[w][h][3];
		int pixel;
		for(int t = 0; t < h; t++)
		{
			for(int i = 0; i < w; i++)
			{
				pixel = fileImage.getRGB(i, t);
				image[i][t][0] = (pixel >> 16) & 0xff;
				image[i][t][1] = (pixel >> 8) & 0xff;
				image[i][t][2] = pixel & 0xff;
			}
		}
	}
	
	public Vector getNormal(Point p, Ray r)
	{
		Vector n = p.minus(center);
		n.normalize();
		return n;
	}
	
	public HitData getHitData(Ray r)
	{
		double[] hitTs = getHitTs(r);
		double smallestT = Library.getSmallestT(hitTs);
		Point p = Library.getP(smallestT, r);
		Vector normal = getNormal(p, r);
		HitData hit = new HitData(smallestT, this, normal, p, hitTs);
		return hit;
	}

	private double[] getHitTs(Ray r)
	{
		double discriminant;
		Vector eMinusC = r.getEye().minus(center);
		double dDotD = r.getD().dot(r.getD());
		double rSquared = radius* radius;
		discriminant = r.getD().dot(eMinusC) * r.getD().dot(eMinusC);
		discriminant -= dDotD * (eMinusC.dot(eMinusC) - rSquared);
		discriminant = Math.pow(discriminant, 0.5);
		double greaterT = -1 * (r.getD().dot(eMinusC));
		double smallerT = -1 * (r.getD().dot(eMinusC));
		greaterT += discriminant;
		smallerT -= discriminant;
		greaterT /= dDotD;
		smallerT /= dDotD;
		
		double[] retTArray = new double[2];
		retTArray[0] = greaterT;
		retTArray[1] = smallerT;
		return retTArray;
	}
	
	public Color getRGBValue(HitData hitData)
	{
		Color returnValue = new Color(0.0,0.0,0.0);
		
		double theta;
		double phi;
		
		Point p = hitData.getP();
		
		theta = Math.acos((p.z)/radius);
		phi = Math.atan2(p.y, p.x);
		if(phi < 0.0)
		{
			phi += 2*Math.PI;
		}//if
		
		double u = phi/(2* Math.PI);
		double v = (Math.PI - theta)/Math.PI;
		
		//System.out.println("u: " + u);
		//System.out.println("v: " + v);
		
		//u,v is in the unit square, so we need to convert them to the image coordinates
		int uFinal = (int) ((int) w * u);
		int vFinal = (int) ((int) h * v);
		
		returnValue.red = image[uFinal][vFinal][0];
		returnValue.green = image[uFinal][vFinal][1];
		returnValue.blue = image[uFinal][vFinal][2];
				
		//convert to between 0-1
		returnValue.red = returnValue.red/255.0;
		returnValue.green = returnValue.green/255.0;
		returnValue.blue = returnValue.blue/255.0;
		
		return returnValue;
	}

	public Color getColor(Point light, Point eye, int phongExponent, boolean inShadow, Color cR,
		    			  Point p, Color cA, Color cL, Vector n)
	{
		if(effects.getNoiseMappedColorClass() != null)
		{
			NoiseColor nc = effects.getNoiseMappedColorClass();
			
			cR = nc.getColor(p);
			
			if(effects.isPhong())
			{
				return Library.getColorPhong(cR, cA, cL, n, light, eye, phongExponent, p, inShadow);
			}//if
			else if(effects.isLambertian())
			{
				return Library.getColorLambertian(cR, cA, cL, n, light, p, inShadow);
			}//else
		}//if
		else
		{
			double theta;
			double phi;
			
			theta = Math.acos((p.z)/radius);
			phi = Math.atan2(p.y, p.x);
			if(phi < 0.0)
			{
				phi += 2*Math.PI;
			}//if
			
			double u = phi/(2* Math.PI);
			double v = (Math.PI - theta)/Math.PI;
			
			//System.out.println("u: " + u);
			//System.out.println("v: " + v);
			
			//u,v is in the unit square, so we need to convert them to the image coordinates
			int uFinal = (int) ((int) w * u);
			int vFinal = (int) ((int) h * v);
			
			cR = new Color(image[uFinal][vFinal][0],image[uFinal][vFinal][1],image[uFinal][vFinal][2]);
			
			if(effects.isPhong())
			{
				return Library.getColorPhong(cR, cA, cL, n, light, eye, phongExponent, p, inShadow);
			}//if
			else if(effects.isLambertian())
			{
				return Library.getColorLambertian(cR, cA, cL, n, light, p, inShadow);
			}//else
		}//else
		return new Color(0.0,0.0,0.0);
	}
	
	@Override
	public int getType()
	{
		return Surface.OUTERSPHERE;
	}

	public Color getCA() {
		return cA;
	}

	public void setCA(Color ca) {
		cA = ca;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public Color getCL() {
		return cL;
	}

	public void setCL(Color cl) {
		cL = cl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int[][][] getImage() {
		return image;
	}

	public void setImage(int[][][] image) {
		this.image = image;
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public Color getCR() {
		return cR;
	}

	public void setCR(Color cr) {
		cR = cr;
	}
}
