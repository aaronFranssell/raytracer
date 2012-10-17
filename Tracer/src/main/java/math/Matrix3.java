package math;

public class Matrix3
{
	private double[][] matrix = new double[3][3];
	
	public Matrix3(double i1, double i2, double i3,
				   double i4, double i5, double i6,
				   double i7, double i8, double i9)
	{
		matrix[0][0] = i1; matrix[0][1] = i2; matrix[0][2] = i3;
		matrix[1][0] = i4; matrix[1][1] = i5; matrix[1][2] = i6;
		matrix[2][0] = i7; matrix[2][1] = i8; matrix[2][2] = i9;
	}
	
	public Matrix3(double[][] incomingMatrix)
	{
		matrix = incomingMatrix;
	}
	
	public String toString()
	{
		String returnString = "";
		for(int i = 0; i < 3; i++)
		{
			for(int u = 0; u < 3; u++)
			{
				returnString += matrix[i][u] + ", ";
			}
			returnString += "\n";
		}
		return returnString;
	}
	
	public double det()
	{
		double first;
		double second;
		double third;
		first = (matrix[0][0]*(matrix[1][1]*matrix[2][2] - matrix[1][2]*matrix[2][1]));
		second = -1 * (matrix[0][1]*(matrix[1][0]*matrix[2][2] - matrix[1][2] * matrix[2][0]));
		third = (matrix[0][2] * (matrix[1][0]*matrix[2][1] - matrix[1][1]*matrix[2][0]));
		return (first + second + third);
	}
}
