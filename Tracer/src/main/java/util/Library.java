package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.Complex;

import math.Point;
import math.Vector;

import scene.Scene;
import scene.ray.Ray;
import surface.Surface;
import etc.Color;
import etc.HitData;
import etc.RaytracerException;



public class Library 
{
	public static double max(double num1, double num2)
	{
		if(num1 > num2)
		{
			return num1;
		}
		return num2;
	}
	
	public static double[] solveQuadratic(double a, double b, double c)
	{
		double[] retArray = new double[2];
		double discriminant = b*b - 4*a*c;
		if(discriminant < 0)
		{
			retArray[0] = Double.NaN;
			retArray[1] = Double.NaN;
			return retArray;
		}//if
		retArray[0] = ((b*-1) + Math.pow(discriminant,0.5))/(2*a);
		retArray[1] = ((b*-1) - Math.pow(discriminant,0.5))/(2*a);
		return retArray;
	}
	
	public static Ray getRefractedRay(Vector d, double refractiveN, double refractiveNT,
									  HitData hitData)
	{
		Vector n = new Vector(hitData.getNormal().x, hitData.getNormal().y, hitData.getNormal().z);
		//may need to reverse normal. Normally it points outwards, but if there is a refracted ray 
		//shooting through the sphere the normal should point inwards. We can test this by seeing 
		//if ((reversed d) * n) is greater than 0. If the cos is < 0, we need to reverse the normal.
		Vector reveresedD = new Vector(-d.x,-d.y,-d.z);
		double tempDDotN = reveresedD.dot(n);

		if(tempDDotN < 0)
		{
			n.x -= 2*n.x;
			n.y -= 2*n.y;
			n.z -= 2*n.z;
		}//if
		double dDotN = d.dot(n);
		double dDotNSquared = dDotN * dDotN;
		double refractiveNSquared = refractiveN*refractiveN;
		double refractiveNTSquared = refractiveNT*refractiveNT;
		double secondHalf = (1 - (refractiveNSquared * (1 - dDotNSquared)) /refractiveNTSquared);
		if(secondHalf < 0)
		{
			return null;
		}
		secondHalf = Math.pow(secondHalf, 0.5);
		Vector secondHalfN = new Vector(n.x, n.y, n.z);

		secondHalfN.x *= secondHalf;
		secondHalfN.y *= secondHalf;
		secondHalfN.z *= secondHalf;
		
		Vector firstHalfN = new Vector(n.x,n.y,n.z);
		firstHalfN.x *= dDotN;
		firstHalfN.y *= dDotN;
		firstHalfN.z *= dDotN;
		
		firstHalfN.x = d.x - firstHalfN.x;
		firstHalfN.y = d.y - firstHalfN.y;
		firstHalfN.z = d.z - firstHalfN.z;
		
		firstHalfN.x *= refractiveN;
		firstHalfN.y *= refractiveN;
		firstHalfN.z *= refractiveN;
		
		firstHalfN.x /= refractiveNT;
		firstHalfN.y /= refractiveNT;
		firstHalfN.z /= refractiveNT;

		Vector t = new Vector(0.0,0.0,0.0);
		t.x = firstHalfN.x - secondHalfN.x;
		t.y = firstHalfN.y - secondHalfN.y;
		t.z = firstHalfN.z - secondHalfN.z;
		
		t = t.normalizeReturn();
		
		Ray newRay = new Ray(t, hitData.getP());
		
		return newRay;
	}
	
	public static boolean isInShadow(Surface currSurface, Scene scene, Point light, HitData hitData) throws RaytracerException
	{
		Vector d = light.minus(hitData.getP());
				
		double distanceToLight = d.magnitude();
		
		d = d.normalizeReturn();
		
		Ray rayShotToLight = new Ray(d, hitData.getP());
		
		HitData shadowData = scene.getSmallestPositiveHitDataOrReturnMiss(rayShotToLight);
		if(shadowData.isHit() && shadowData.getSurface().getType() != Surface.SurfaceType.Outersphere)
		{
			//if the light point occurs closer to a hit point than this object, then the object is not in shadow
			double distanceToClosePoint = shadowData.getP().minus(hitData.getP()).magnitude();
			if(distanceToClosePoint < distanceToLight)
			{
				return true;
			}
		}
		return false;
	}
	
	public static Color getColorLambertian(Color cR, Color cA, Color cL, Vector n, Point light, Point p, boolean inShadow)
	{
		Vector pointToLight = light.minus(p).normalizeReturn();
		n = n.normalizeReturn();
		
		double nDotL = n.dot(pointToLight);
		
		Color RGBValue = new Color(0,0,0);
		if(!inShadow)
		{
			RGBValue.red = cR.red*(cA.red + cL.red*max(0,nDotL));
			RGBValue.green = cR.green*(cA.green + cL.green*max(0,nDotL));
			RGBValue.blue = cR.blue*(cA.blue + cL.blue*max(0,nDotL));
		}//if
		else
		{
			RGBValue.red = cR.red*cA.red;
			RGBValue.green = cR.green*cA.green;
			RGBValue.blue = cR.blue*cA.blue;
		}
		return RGBValue;
	}
	
	public static Color getColorPhong(Color cR, Color cA, Color cL, Vector n, Point light, Point eye, 
									  int exponent, Point p, boolean inShadow)
	{
		Color RGBValue = getColorLambertian(cR, cA, cL, n,light,p, inShadow);
		//if the point is in shadow, just return the lambertian lighting model (since there will be no
		//specular highlight if the object is in shadow).
		if(inShadow)
		{
			return RGBValue;
		}
		
		Vector pointToLight = light.minus(p).normalizeReturn();
		
		Vector pointToEye = eye.minus(p).normalizeReturn();
		
		//h is the halfway vector between the point to light, and the point to eye
		//take the dot product with n to get an approximation for the specular highlight
		Vector h = pointToLight.add(pointToEye).normalizeReturn();
		double phongHighlight = max(0,n.dot(h));
		
		Color phong = new Color(0.0,0.0,0.0);
		phong.red = cL.red * Math.pow(phongHighlight,exponent);
		phong.green = cL.green * Math.pow(phongHighlight,exponent);
		phong.blue = cL.blue * Math.pow(phongHighlight,exponent);
		
		RGBValue.red += cR.red*phong.red;
		RGBValue.green += cR.green*phong.green;
		RGBValue.blue += cR.blue*phong.blue;
		
		return RGBValue;
	}
	
	public static void clamp(Color vec)
	{
		if(vec.red > 1.0)
		{
			vec.red = 1.0;
		}
		if(vec.green > 1.0)
		{
			vec.green = 1.0;
		}
		if(vec.blue > 1.0)
		{
			vec.blue = 1.0;
		}
	}

	public static Ray getReflectedRay(Ray r, Point p, Vector n)
	{
		/*V is the ray direction, N is the surface normal
		c1 = -dot_product( N, V )
		Rl = V + (2 * N * c1 )
		*/
		double c1 = - n.dot(r.getD());
		Vector reflectedRay = r.getD().add(n.scaleReturn(2*c1));
		reflectedRay = reflectedRay.normalizeReturn();
		return new Ray(reflectedRay, p);
	}
	
	public static double[] solveQuartic(double A,double B,double C,double D,double E)
	{
		double [] returnTs = new double[4];
		double smallDis;
		
		double alpha = -3*Math.pow(B,2)/(8*Math.pow(A,2)) + C/A;
		
		double beta = Math.pow(B,3)/(8*Math.pow(A,3)) - B*C/(2*Math.pow(A,2)) + D/A;
		
		double gamma = -3*Math.pow(B,4)/(256*Math.pow(A,4)) + C*Math.pow(B,2)/(16*Math.pow(A,3)) - B*D/(4*Math.pow(A,2)) + E/A;
		
		if(beta > -0.0000000003 && beta < 0.0000000003)
		{
			smallDis = Math.pow(alpha,2)-4*gamma;
			Complex smallDisSQRT = new Complex(smallDis, 0.0);
			List<Complex> smallDisSQRTList = smallDisSQRT.nthRoot(2);
			Iterator<Complex> it = smallDisSQRTList.iterator();
			List<Complex> allRoots = new LinkedList<Complex>();
			while(it.hasNext())
			{
				Complex aRoot = it.next();
				aRoot = aRoot.add(new Complex((-1) * alpha, 0.0));
				aRoot = aRoot.divide(new Complex(2.0, 0.0));
				allRoots.add(aRoot);
			}
			double minusBOver4A = ((-1)*B)/(4* A);
						
			Iterator<Complex> allRootsIt = allRoots.iterator();
			int returnTIndex = 0;
			while(allRootsIt.hasNext())
			{
				Complex allRoot = allRootsIt.next();
				List<Complex> largeDisList = allRoot.nthRoot(2);
				Iterator<Complex> largeDisListIt = largeDisList.iterator();
				while(largeDisListIt.hasNext())
				{
					Complex root = largeDisListIt.next();
					//if the root does not have an imaginary portion, then we will return this root
					//after adding the -B/4A portion...
					if(root.getImaginary() > -0.00003 && root.getImaginary() < 0.00003)
					{
						root.add(new Complex(minusBOver4A, 0.0));
						returnTs[returnTIndex] = root.getReal();
					}
					else
					{
						returnTs[returnTIndex] = Double.NaN;
					}
					returnTIndex++;
				}
			}
			return returnTs;
		}//if
		//these will behave exactly like real numbers for the time being
		Complex P = new Complex((-Math.pow(alpha,2)/12 - gamma), 0.0);
		Complex Q = new Complex(((-1)*Math.pow(alpha,3)/108) + (alpha*gamma/3) - (Math.pow(beta,2)/8), 0.0);
		Complex R;
		
		R = getR(P, Q);
		
		List<Complex> UList = R.nthRoot(3);
		//according to wikipedia, U has:
		//3 complex roots, any one of them will do
		Complex U = UList.get(0);
		Complex y;
		
		Complex minus5AlphaOver6 = new Complex((-5 * alpha)/6, 0.0);
		//if U == 0...
		if((U.getReal() > -0.00003 && U.getReal() < 0.00003) &&
		   (U.getImaginary() > -0.00003 && U.getImaginary() < 0.00003))
		{
			//which root is the correct one to take? Wikipedia doesn't say...
			//LogToFile.log("in which root, A: " + A + ", B: " + B + ", C: " + C + ", D: " + D + ", E: " +E + "\n");
			Complex whichRoot = Q.nthRoot(3).get(0);
			y = minus5AlphaOver6.add(U).subtract(whichRoot);
		}//if
		else
		{
			Complex threeU = new Complex(U.getReal() * 3, U.getImaginary() * 3);
			y = minus5AlphaOver6.add(U).subtract(P.divide(threeU));
		}//else
		Complex complexAlpha = new Complex(alpha, 0.0);
		Complex twoY = new Complex(2*y.getReal(), 2 * y.getImaginary());
		Complex WDis = complexAlpha.add(twoY);
		List<Complex> WList = WDis.nthRoot(2);
		Iterator<Complex> WListIt = WList.iterator();
		
		int returnTIndex = 0;
		while(WListIt.hasNext())
		{
			Complex W = WListIt.next();
			
			Complex threeAlpha = new Complex(3.0 * alpha, 0.0);
			Complex twoBetaOverW = (new Complex(2 * beta, 0.0)).divide(W);
			Complex discriminant = threeAlpha.add(twoY).add(twoBetaOverW);
			discriminant = new Complex(discriminant.getReal() * -1, discriminant.getImaginary() * -1);
			Iterator<Complex> discriminantIt = discriminant.nthRoot(2).iterator();
			while(discriminantIt.hasNext())
			{
				Complex root = discriminantIt.next();
				Complex minusBOver4A = new Complex(((-1 * B)/(4*A)), 0.0);
				Complex x = minusBOver4A.add(root.add(W).divide(new Complex(2.0,0.0)));
				if(x.getImaginary() > -0.00003 && x.getImaginary() < 0.00003)
				{
					returnTs[returnTIndex] = x.getReal();
				}//if
				else
				{
					returnTs[returnTIndex] = Double.NaN;
				}
				returnTIndex++;
			}
		}
		return returnTs;
	}
	
	private static Complex getR(Complex P, Complex Q) {
		Complex R;
		double dis = Math.pow(Q.getReal(), 2.0)/4 + Math.pow(P.getReal(), 3.0)/27;
		R = new Complex(dis,0.0);
		List<Complex> rRoots = R.nthRoot(2);		
		Iterator<Complex> rRootsIterator = rRoots.iterator();
		while(rRootsIterator.hasNext())
		{
			double minusQOver2 = (-1)*Q.getReal()/2;
			Complex root = rRootsIterator.next();
			R = root.add(new Complex(minusQOver2, 0.0));
			//according to wikipedia:
			//either sign of the square root will do
			//so we can break after grabbing the root
			break;
		}
		return R;
	}
	
	/**
	 * This function returns the smallest t in a double array. The double array can include negative numbers, NaNs, and normal
	 * t values. 
	 * @param tArray The array of ts
	 * @return The smallest non-negative, non-zero, non-NaN t in the array. NaN if there isn't one.
	 */
	public static double getSmallestT(double[] tArray)
	{
		if(tArray == null)
		{
			return Double.NaN;
		}
		double smallestT = Double.MAX_VALUE;
		for(int i = 0; i < tArray.length; i++)
		{
			if(!Double.isNaN(tArray[i]) && tArray[i] > Constants.POSITIVE_ZERO && tArray[i] < smallestT)
			{
				smallestT = tArray[i];
			}
		}
		if(smallestT == Double.MAX_VALUE)
		{
			return Double.NaN;
		}
		return smallestT;
	}

	public static boolean doubleEqual(double num1, double num2, double tolerance)
	{
		if(num1 + tolerance > num2 && num1 - tolerance < num2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This function determines whether or not the incoming hit t values are still hits base on whether or not they occur past the
	 * solid length. For instance a point my hit an infinite cylinder, but it will be past the length, or before the base point, so
	 * it should count as a miss.
	 * @param incomingHitTs The default hit t values. These must be valid hits.
	 * @param solidDirection The direction vector of the solid.
	 * @param basePoint The point on the direction vector.
	 * @param length The length of the solid.
	 * @param r The ray.
	 * @return The correct t values, or NaN if there is a miss.
	 * @throws Exception 
	 */
	public static double[] getHitTsByLimitingLength(double[] incomingHitTs, Vector solidDirection, Point basePoint,
													 double length, Ray r, Util ops) throws RaytracerException
	{
		double[] hitTs = new double[incomingHitTs.length];
		for(int i = 0; i < incomingHitTs.length; i++)
		{
			if(Double.isNaN(incomingHitTs[i]))
			{
				throw new RaytracerException("Hit t value is NaN, these values must be valid hits.");
			}
			Point hitP = ops.getP(incomingHitTs[i], r);
			
			Vector vectorToHitPointFromBase = hitP.minus(basePoint);
			Vector vectorToHitPointFromBaseNormalized = vectorToHitPointFromBase.normalizeReturn(); 
			
			//if the dot product is < 0, then the hit occurs behind the base point, so it is a miss
			if(solidDirection.dot(vectorToHitPointFromBaseNormalized) < 0)
			{
				hitTs[i] = Double.NaN;
			}//if
			else
			{
				/*
				Length is not simply the distance between the hitP and basePoint, it is the distance along the solidDirection
				vector. We will set up a triangle having points basePoint and hitP. The angle between solidDirection and 
				vectorToHitPointFromBase is alpha. The distance along the solidDirection vector of the point is:
				cos(alpha) * vectorToHitPointFromBase.magnitude()
				by the properties of cos. 
				*/
				double solidDirectionLength = solidDirection.dot(vectorToHitPointFromBaseNormalized) * 
											  vectorToHitPointFromBase.magnitude();
				if(solidDirectionLength > length)
				{
					hitTs[i] = Double.NaN;
				}//if
				else
				{
					//simply copy over the hitT
					hitTs[i] = incomingHitTs[i];
				}
			}
		}
		
		return hitTs;
	}
	
	public static int[] getCircleUVImageMapping(Point p, Point center, double radius, int w, int h)
	{
		double theta;
		double phi;
	
		theta = Math.acos((p.y - center.y) / radius);
		phi = Math.atan2(p.z - center.z, p.x - center.x);
		if (phi < 0.0)
		{
			phi += 2 * Math.PI;
		}// if
	
		double u = phi / (2 * Math.PI);
		double v = (Math.PI - theta) / Math.PI;
	
		// System.out.println("u: " + u);
		// System.out.println("v: " + v);
	
		// u,v is in the unit square, so we need to convert them to the image
		// coordinates
		int uFinal = (int) ((int) w * u);
		int vFinal = (int) ((int) h * v);
		
		int[] retArray = new int[2];
		retArray[0] = uFinal;
		retArray[1] = vFinal;
		
		return retArray;
	}
	
	public static int[][][] readImage(String filePath)
	{
		int[][][] image;
		BufferedImage fileImage = null;
		try
		{
			File yonFile = new File(filePath);
			fileImage = ImageIO.read(yonFile);
		} catch (IOException e)
		{
			System.err.println("File path: " + filePath);
			e.printStackTrace();
		}
		int w = fileImage.getWidth();
		int h = fileImage.getHeight();
		image = new int[w][h][3];
		int pixel;
		for (int t = 0; t < h; t++)
		{
			for (int i = 0; i < w; i++)
			{
				pixel = fileImage.getRGB(i, t);
				image[i][t][0] = (pixel >> 16) & 0xff;
				image[i][t][1] = (pixel >> 8) & 0xff;
				image[i][t][2] = pixel & 0xff;
			}
		}
		
		return image;
	}
}
