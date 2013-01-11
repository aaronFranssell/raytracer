package math;

import scene.ray.Ray;

public class BarycentricSimplex
{
	private Point a;
	private Point b;
	private Point c;

	public BarycentricSimplex(Point incomingA, Point incomingB, Point incomingC)
	{
		a = incomingA;
		b = incomingB;
		c = incomingC;
	}

	public double getT(Ray r)
	{
		Matrix3 A = new Matrix3(a.x - b.x, a.x - c.x, r.getD().x,
								a.y - b.y, a.y - c.y, r.getD().y,
								a.z - b.z, a.z - c.z, r.getD().z);
		double detA = A.det();
		Matrix3 gammaTop = new Matrix3(a.x - b.x, a.x - r.getEye().x, r.getD().x,
									   a.y - b.y, a.y - r.getEye().y, r.getD().y,
									   a.z - b.z, a.z - r.getEye().z, r.getD().z);
		double detGammaTop = gammaTop.det();
		double gamma = detGammaTop/detA;

		if(gamma < 0.0 || gamma > 1.0)
		{
			return Double.NaN;
		}
		Matrix3 betaTop = new Matrix3(a.x - r.getEye().x, a.x - c.x, r.getD().x,
									  a.y - r.getEye().y, a.y - c.y, r.getD().y,
									  a.z - r.getEye().z, a.z - c.z, r.getD().z);
		double detBetaTop = betaTop.det();

		double beta = detBetaTop/detA;
		if(beta < 0.0 || beta > 1.0 - gamma)
		{
			return Double.NaN;
		}
		Matrix3 tTop = new Matrix3(a.x - b.x, a.x - c.x, a.x - r.getEye().x,
								   a.y - b.y, a.y - c.y, a.y - r.getEye().y,
								   a.z - b.z, a.z - c.z, a.z - r.getEye().z);
		double detTTop = tTop.det();
		return detTTop/detA;
	}
}
