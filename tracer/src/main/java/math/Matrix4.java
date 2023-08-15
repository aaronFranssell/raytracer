package math;

public class Matrix4
{
	private double[][] matrix = new double[4][4];
	
	public static Matrix4 getIdentity()
	{
		return new Matrix4(1.0,0.0,0.0,0.0,
						   0.0,1.0,0.0,0.0,
						   0.0,0.0,1.0,0.0,
						   0.0,0.0,0.0,1.0);
	}
	
	public Matrix4(double  i1, double  i2, double  i3, double  i4, 
				   double  i5, double  i6, double  i7, double  i8, 
				   double  i9, double i10, double i11, double i12,
				   double i13, double i14, double i15, double i16)
	{
		matrix[0][0] = i1; matrix[0][1] = i2; matrix[0][2] = i3; matrix[0][3] = i4; 
		matrix[1][0] = i5; matrix[1][1] = i6; matrix[1][2] = i7; matrix[1][3] = i8; 
		matrix[2][0] = i9; matrix[2][1] =i10; matrix[2][2] =i11; matrix[2][3] =i12;
		matrix[3][0] =i13; matrix[3][1] =i14; matrix[3][2] =i15; matrix[3][3] =i16;
	}
	
	public Matrix4(double[][] incomingMatrix)
	{
		matrix = incomingMatrix;
	}
	
	public Matrix4 multiply(Matrix4 rightMatrix)
	{
		double[][] right = rightMatrix.getMatrix();
		double[][] retMatrix = new double[4][4];
		for(int i = 0; i < 4; i++)
		{
			for(int u = 0; u < 4; u++)
			{
				double smallSum = 0.0;
				for(int a = 0; a < 4; a++)
				{
					smallSum += matrix[i][a] * right[a][u];
				}
				retMatrix[i][u] = smallSum;
			}
		}
		return new Matrix4(retMatrix);
	}
	
	private double det()
	{
		double retVal = 0;
		double[] partialDet = new double[matrix.length];
		int partialDetIndex = 0;
		int row = 0;
		for(int i = 0; i < matrix.length; i++)
		{
			double outside = matrix[row][i];
			outside = i%2 == 1 ? outside * -1 : outside;
			double[][] matrix3 = new double[3][3];
			int m3row = 0;
			int m3col = 0;
			for(int m = 0; m < matrix[i].length; m++)
			{
				m3col = 0;
				//if the current row in the matrix is the outer part of the determinant
				if(m != row)
				{
					for(int e = 0; e < 4; e++)
					{
						if(e != i)
						{
							matrix3[m3row][m3col] = matrix[m][e];
							m3col++;
						}
					}
					m3row++;
				}
			}
			Matrix3 m = new Matrix3(matrix3);
			partialDet[partialDetIndex] = outside * m.det();
			partialDetIndex++;
		}
		for(int a = 0; a < partialDet.length; a++)
		{
			retVal += partialDet[a];
		}
		return retVal;
	}
	
	private Matrix4 getMinors()
	{
		double[][] retMatrix = new double[4][4];
		for(int i = 0; i < 4; i++)
		{
			for(int u = 0; u < 4; u++)
			{
				double[][] matrix3 = new double[3][3];
				int m3row = 0;
				int m3col = 0;
				for(int a = 0; a < 4; a++)
				{
					m3col = 0;
					//if the current row in the matrix is the outer part of the determinant
					if(a != i)
					{
						for(int e = 0; e < 4; e++)
						{
							if(e != u)
							{
								matrix3[m3row][m3col] = matrix[a][e];
								m3col++;
							}
						}
						m3row++;
					}
				}
				Matrix3 m = new Matrix3(matrix3);
				retMatrix[i][u] = (i+u)%2 == 0 ? m.det():m.det()*-1;
			}
		}
		return new Matrix4(retMatrix);
	}
	
	private Matrix4 transpose()
	{
		double[][] retMatrix = new double[4][4];
		for(int i = 0; i < 4; i++)
		{
			for(int u = 0; u < 4; u++)
			{
				retMatrix[u][i] = matrix[i][u];
			}
		}
		return new Matrix4(retMatrix);
	}
	
	private Matrix4 getAdjoint()
	{
		Matrix4 minorMatrix = getMinors();
		return minorMatrix.transpose();
	}
	
	public String toString()
	{
		String returnString = "\n";
		for(int i = 0; i < 4; i++)
		{
			for(int u = 0; u < 4; u++)
			{
				returnString += matrix[i][u] + ", ";
			}//for
			if(i != 3)
			{
				returnString += "\n";
			}
		}
		return returnString;
	}
	
	public Matrix4 multiplyBy(double mulNum)
	{
		double[][] retMatrix = new double[4][4];
		for(int i = 0; i < 4; i++)
		{
			for(int u = 0; u < 4; u++)
			{
				retMatrix[i][u] = matrix[i][u] * mulNum;
			}
		}
		return new Matrix4(retMatrix);
	}
	
	public Matrix4 getInverse()
	{
		Matrix4 adjoint = getAdjoint();
		double determinant = det();
		adjoint = adjoint.multiplyBy(1/determinant);
		return adjoint;
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}
	
	public Vector multiplyHomogeneousCoordinates(Vector vec)
	{
		Vector retVec = new Vector(0.0,0.0,0.0);
		//vector is assumed to be of the form [x,y,z,0] so the last row can be left off when multiplying by the 4x4 matrix.
		retVec.x = matrix[0][0] * vec.x + matrix[0][1] * vec.y + matrix[0][2] * vec.z;
		retVec.y = matrix[1][0] * vec.x + matrix[1][1] * vec.y + matrix[1][2] * vec.z;
		retVec.z = matrix[2][0] * vec.x + matrix[2][1] * vec.y + matrix[2][2] * vec.z;
		return retVec;
	}
	
	public Point multiplyHomogeneousCoordinates(Point point)
	{
		Point newPoint = new Point(0.0,0.0,0.0);
		//points have the final "1" homogenous coordinate. I don't need to multiply out the final row, since that
		//will just preserve the implicit homogenous coordinate
		newPoint.x = matrix[0][0] * point.x + matrix[0][1] * point.y + matrix[0][2] * point.z + matrix[0][3] * 1;
		newPoint.y = matrix[1][0] * point.x + matrix[1][1] * point.y + matrix[1][2] * point.z + matrix[1][3] * 1;
		newPoint.z = matrix[2][0] * point.x + matrix[2][1] * point.y + matrix[2][2] * point.z + matrix[2][3] * 1;
		return newPoint;
	}
	
	public Matrix4 add(Matrix4 otherMatrix)
	{
		double[][] other = otherMatrix.getMatrix();
		return new 
		Matrix4(matrix[0][0]+other[0][0],matrix[0][1]+other[0][1],matrix[0][2]+other[0][2],matrix[0][3]+ other[0][3],
				matrix[1][0]+other[1][0],matrix[1][1]+other[1][1],matrix[1][2]+other[1][2],matrix[1][3]+ other[1][3],
				matrix[2][0]+other[2][0],matrix[2][1]+other[2][1],matrix[2][2]+other[2][2],matrix[2][3]+ other[2][3],
				matrix[3][0]+other[3][0],matrix[3][1]+other[3][1],matrix[3][2]+other[3][2],matrix[3][3]+ other[3][3]);
	}
}
