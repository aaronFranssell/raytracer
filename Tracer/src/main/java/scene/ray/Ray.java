package scene.ray;

import math.Point;
import math.Vector;

public class Ray
{
	private Point eye;
	private Vector d;

	public Ray(Vector incomingD, Point incomingEye)
	{
		d = incomingD;
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
