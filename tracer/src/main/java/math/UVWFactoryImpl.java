package math;

import util.Constants;
import util.UtilImpl;
import etc.RaytracerException;

public class UVWFactoryImpl implements UVWFactory
{

	@Override
	public UVW createUVW(Vector up, Vector gaze) throws RaytracerException
	{
		Vector w = gaze.copy();
		w = w.normalizeReturn();
		Vector t = up.copy();
		t = t.normalizeReturn();
		Vector wCrossT = w.cross(t);
		if(UtilImpl.doubleEqual(wCrossT.magnitude(), 0.0, Constants.POSITIVE_ZERO))
		{
			throw new RaytracerException("Can't instantiate UVW basis, the gaze and up vector are pointing in the same direction!");
		}
		wCrossT = wCrossT.normalizeReturn();
		Vector u = wCrossT;
		Vector v = u.cross(w);
		return new UVW(u,v,w);
	}

}
