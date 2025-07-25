package math;

import util.Constants;
import util.UtilImpl;

public class UVWFactory {

	public UVW createUVW(Vector up, Vector gaze) throws Exception {
		Vector w = gaze.copy();
		w = w.normalizeReturn();
		Vector t = up.copy();
		t = t.normalizeReturn();
		Vector wCrossT = w.cross(t);
		if (UtilImpl.doubleEqual(wCrossT.magnitude(), 0.0, Constants.POSITIVE_ZERO)) {
			throw new Exception(
					"Can't instantiate UVW basis, the gaze and up vector are pointing in the same direction!");
		}
		wCrossT = wCrossT.normalizeReturn();
		Vector u = wCrossT;
		Vector v = u.cross(w);
		return new UVW(u, v, w);
	}
}
