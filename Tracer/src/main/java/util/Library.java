package util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;



public class Library 
{
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
}
