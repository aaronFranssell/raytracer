package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

import etc.Color;
import etc.HitData;
import etc.RaytracerException;
import etc.Refractive;
import scene.Scene;
import scene.pixel.ScenePixel;
import scene.ray.Ray;
import scene.ray.RayFactory;
import scene.ray.RayFactoryImpl;
import surface.Surface;
import math.Point;
import math.Vector;
public class UtilImpl implements Util
{
	private RayFactory rayFactory; 
	
	public UtilImpl()
	{
		this(new RayFactoryImpl());
	}
	
	public UtilImpl(RayFactory incomingRayFactory)
	{
		rayFactory = incomingRayFactory;
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
	
	@Override
	public double[] sort(double[] incomingTArray)
	{
		double[] temp = new double[incomingTArray.length];
		for(int i = 0; i < incomingTArray.length; i++)
		{
			temp[i] = incomingTArray[i];
		}
		if(temp.length == 1)
		{
			return temp;
		}
		boolean switchMade;
		do
		{
			switchMade = false;
			for(int i = 0; i < temp.length-1; i++)
			{
				if(temp[i] > temp[i + 1])
				{
					switchMade = true;
					double tempVal = temp[i + 1];
					temp[i + 1] = temp[i];
					temp[i] = tempVal;
				}
			}
		}
		while(switchMade);
		return temp;
	}

	@Override
	public Point getP(double smallestT, Ray r)
	{
		return new Point(r.getEye().x + smallestT * r.getD().x,
				r.getEye().y + smallestT * r.getD().y,
				r.getEye().z + smallestT * r.getD().z);
	}

	@Override
	public Ray getRefractedRay(Vector v, double originalN, double newN,	HitData hitData)
	{
		Vector normal = hitData.getNormal();
		//may need to reverse normal. Normally it points outwards, but if there is a refracted ray 
		//shooting through the sphere the normal should point inwards. We can test this by seeing 
		//if ((reversed d) * n) is greater than 0. If the cos is < 0, we need to reverse the normal.
		if(normal.dot(v) > 0)
		{
			normal = normal.scaleReturn(-1.0);
		}
		double c1 = normal.dot(v) * -1;
		double n = originalN/newN;
		double val = 1 - (n*n) * (1 - c1*c1);
		if(val < 0.0)
		{
			return null;
		}
		double c2 = FastMath.sqrt(val);

		Vector refracted = v.scaleReturn(n).add(normal.scaleReturn(n * c1 - c2));
		refracted = refracted.normalizeReturn();
		return rayFactory.createRay(refracted, hitData.getP());
	}

	@Override
	public Ray getReflectedRay(Ray r, Point p, Vector n)
	{
		/*V is the ray direction, N is the surface normal
		c1 = -dot_product( N, V )
		Rl = V + (2 * N * c1 )
		 */
		double c1 = - n.dot(r.getD());
		Vector reflectedRay = r.getD().add(n.scaleReturn(2*c1));
		reflectedRay = reflectedRay.normalizeReturn();
		return rayFactory.createRay(reflectedRay, p);
	}

	@Override
	public Color clamp(Color c)
	{
		Color ret = new Color(c.red, c.green, c.blue);
		if(ret.red > 1.0)
		{
			ret.red = 1.0;
		}
		if(ret.green > 1.0)
		{
			ret.green = 1.0;
		}
		if(ret.blue > 1.0)
		{
			ret.blue = 1.0;
		}
		return ret;
	}

	@Override
	public Color getReflectedColor(Ray r, int currentDepth, HitData hit, Surface currSurface, ScenePixel pixel) throws RaytracerException
	{
		//r.getD().dot... code is for when the ray might be refracted inside of a surface.
		//if the ray's dot product is > 0 then the ray is inside of a surface and does not need to
		//be reflected
		if(!currSurface.getEffects().isReflective() || r.getD().dot(hit.getNormal()) >= 0)
		{
			return new Color(0.0,0.0,0.0);
		}
		Ray newRay = getReflectedRay(r, hit.getP(), hit.getNormal());
		Color reflectReturnColor = pixel.getPixelColor(newRay, currentDepth + 1);
		reflectReturnColor = clamp(reflectReturnColor);
		return reflectReturnColor;
	}
	
	@Override
	public Color getRefractedColor(Ray r, int currentDepth, HitData hit, Surface currSurface, ScenePixel pixel) throws RaytracerException
	{
		if(currSurface.getEffects().getRefractive() == null)
		{
			return new Color(0.0,0.0,0.0);
		}
		Refractive refractive = currSurface.getEffects().getRefractive();
		Ray newRay = getRefractedRay(r.getD(), refractive.getN(),refractive.getnT(), hit);
		//total internal reflection
		if(newRay == null)
		{
			return new Color(0.0,0.0,0.0);
		}
		Color refractReturnColor = pixel.getPixelColor(newRay, currentDepth + 1);
		refractReturnColor = clamp(refractReturnColor);
		return refractReturnColor;
	}

	@Override
	public boolean isInShadow(Scene scene, Point light, HitData hitData) throws RaytracerException
	{
		Vector d = light.minus(hitData.getP());
		double distanceToLight = d.magnitude();
		d = d.normalizeReturn();
		
		Ray rayShotToLight = rayFactory.createRay(d, hitData.getP());
		
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

	@Override
	public Color getColorLambertian(Color cR, Color cA, Color cL, Vector n,	Point light, Point p, boolean inShadow)
	{
		Vector pointToLight = light.minus(p).normalizeReturn();
		n = n.normalizeReturn();
		
		double nDotL = n.dot(pointToLight);
		
		Color RGBValue = new Color(0.0,0.0,0.0);
		if(inShadow)
		{
			RGBValue.red = cR.red*cA.red;
			RGBValue.green = cR.green*cA.green;
			RGBValue.blue = cR.blue*cA.blue;
		}
		else
		{
			RGBValue.red = cR.red*(cA.red + cL.red*FastMath.max(0,nDotL));
			RGBValue.green = cR.green*(cA.green + cL.green*FastMath.max(0,nDotL));
			RGBValue.blue = cR.blue*(cA.blue + cL.blue*FastMath.max(0,nDotL));
		}
		return RGBValue;
	}

	@Override
	public Color getColorPhong(Color cR, Color cA, Color cL, Vector n, Point light, Point eye, int exponent, Point p, boolean inShadow)
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
		double phongHighlight = FastMath.max(0,n.dot(h));
		
		Color phong = new Color(0.0,0.0,0.0);
		phong.red = cL.red * FastMath.pow(phongHighlight,exponent);
		phong.green = cL.green * FastMath.pow(phongHighlight,exponent);
		phong.blue = cL.blue * FastMath.pow(phongHighlight,exponent);
		
		RGBValue.red += cR.red*phong.red;
		RGBValue.green += cR.green*phong.green;
		RGBValue.blue += cR.blue*phong.blue;
		
		return RGBValue;
	}

	@Override
	public double[] solveQuadratic(double a, double b, double c)
	{
		double[] retArray = new double[2];
		double discriminant = b*b - 4*a*c;
		if(discriminant < 0)
		{
			retArray[0] = Double.NaN;
			retArray[1] = Double.NaN;
			return retArray;
		}
		retArray[0] = ((b*-1) + FastMath.pow(discriminant,0.5))/(2*a);
		retArray[1] = ((b*-1) - FastMath.pow(discriminant,0.5))/(2*a);
		return retArray;
	}

	@Override
	public boolean hasHits(double[] hits)
	{
		for(int i = 0; i < hits.length; i++)
		{
			if(Double.isNaN(hits[i]))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int[][][] readImage(String filePath) throws IOException
	{
		int[][][] image;
		BufferedImage fileImage = null;
		File yonFile = new File(filePath);
		fileImage = ImageIO.read(yonFile);
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
	
	@Override
	public double[] solveQuartic(double A,double B,double C,double D,double E)
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
					if(UtilImpl.doubleEqual(root.getImaginary(), 0.0, Constants.POSITIVE_ZERO))
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
		}
		Complex P = new Complex((-Math.pow(alpha,2)/12 - gamma), 0.0);
		Complex Q = new Complex(((-1)*Math.pow(alpha,3)/108) + (alpha*gamma/3) - (Math.pow(beta,2)/8), 0.0);
		Complex R;

		R = getR(P, Q);

		List<Complex> UList = R.nthRoot(3);
		//according to wikipedia, U has: 3 complex roots, any one of them will do
		Complex U = UList.get(0);
		Complex y;

		Complex minus5AlphaOver6 = new Complex((-5 * alpha)/6, 0.0);
		//if U == 0...
		if(UtilImpl.doubleEqual(U.getReal(), 0.0, Constants.POSITIVE_ZERO) && UtilImpl.doubleEqual(U.getImaginary(), 0.0, Constants.POSITIVE_ZERO))
		{
			//which root is the correct one to take? Wikipedia doesn't say...
			Complex whichRoot = Q.nthRoot(3).get(0);
			y = minus5AlphaOver6.add(U).subtract(whichRoot);
		}
		else
		{
			Complex threeU = new Complex(U.getReal() * 3, U.getImaginary() * 3);
			y = minus5AlphaOver6.add(U).subtract(P.divide(threeU));
		}
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
				if(UtilImpl.doubleEqual(x.getImaginary(), 0.0, Constants.POSITIVE_ZERO))
				{
					returnTs[returnTIndex] = x.getReal();
				}
				else
				{
					returnTs[returnTIndex] = Double.NaN;
				}
				returnTIndex++;
			}
		}
		return returnTs;
	}

	private Complex getR(Complex P, Complex Q)
	{
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
}
