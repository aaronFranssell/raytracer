package scene.ray;

import math.Point;
import math.Vector;

public class Ray
{
	private Point eye;
	
	private Vector uvwS;
	
	private double l;
	private double r;
	private double b;
	private double t;
	
	private int i;
	private int j;
	
	private Vector u;
	private Vector v;
	private Vector w;
	
	private Vector s;
	private Vector d;
	
	public Ray(Point incomingEye, double incomingL, double incomingR, double incomingB, double incomingT,
			   int incomingNX, int incomingNY, int incomingI, int incomingJ,
			   Vector incomingU, Vector incomingV, Vector incomingW)
	{
		eye = new Point(incomingEye.x, incomingEye.y, incomingEye.z);
		
		l = incomingL;
		r = incomingR;
		b = incomingB;
		t = incomingT;
		
		i = incomingI;
		j = incomingJ;
		
		u = incomingU;
		v = incomingV;
		w = incomingW;
		
		uvwS = new Vector(l + (r - l) * (i + 0.5)/incomingNX,
						  b + (t - b) * (j + 0.5)/incomingNY,
						  1);
		
		s = new Vector(0,0,0);
		s.x = eye.x + uvwS.x*u.x + uvwS.y*v.x + uvwS.z*w.x;
		s.y = eye.y + uvwS.x*u.y + uvwS.y*v.y + uvwS.z*w.y;
		s.z = eye.z + uvwS.x*u.z + uvwS.y*v.z + uvwS.z*w.z;
		
		d = new Vector(s.x - eye.x,s.y - eye.y,s.z - eye.z);
		
		d.normalize();
		s.normalize();
	}
	
	public Ray(Vector incomingD, Point incomingEye)
	{
		d = incomingD;
		d.normalize();
		eye = incomingEye;
	}

	public Vector getD() {
		return d;
	}

	public void setD(Vector d) {
		this.d = d;
	}

	public Point getEye() {
		return eye;
	}

	public void setEye(Point eye) {
		this.eye = eye;
	}

}
