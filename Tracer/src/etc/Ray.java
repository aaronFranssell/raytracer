package etc;

import math.Point;
import math.Vector;



public class Ray
{
	private boolean shootingToLight;
	
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

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
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

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public double getL() {
		return l;
	}

	public void setL(double l) {
		this.l = l;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public Vector getS() {
		return s;
	}

	public void setS(Vector s) {
		this.s = s;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public Vector getU() {
		return u;
	}

	public void setU(Vector u) {
		this.u = u;
	}

	public Vector getUvwS() {
		return uvwS;
	}

	public void setUvwS(Vector uvwS) {
		this.uvwS = uvwS;
	}

	public Vector getV() {
		return v;
	}

	public void setV(Vector v) {
		this.v = v;
	}

	public Vector getW() {
		return w;
	}

	public void setW(Vector w) {
		this.w = w;
	}

	public boolean isShootingToLight()
	{
		return shootingToLight;
	}

	public void setShootingToLight(boolean shootingToLight)
	{
		this.shootingToLight = shootingToLight;
	}
}
