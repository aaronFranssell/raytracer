package math;


public class UVW
{
	private Vector  u,v,w;
	
	public UVW(Vector incomingU, Vector incomingV, Vector incomingW)
	{
		u = incomingU;
		v = incomingV;
		w = incomingW;
	}
	
	public UVW(Vector up, Vector gaze)
	{
		
		w = new Vector(gaze.x,gaze.y,gaze.z);
		w.normalize();
		Vector t = new Vector(up.x, up.y, up.z);
		//i know my textbook says t cross w, but the u vector is pointing the wrong way!
		Vector wCrossT = w.cross(t);
		wCrossT.normalize();
		u = wCrossT;
		//i know my textbook says w cross u, but the v vector is pointing the wrong way!
		v = u.cross(w);
	}
	public Vector getU() {
		return u;
	}
	public void setU(Vector u) {
		this.u = u;
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
}
