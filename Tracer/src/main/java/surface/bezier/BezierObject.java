package surface.bezier;

import java.util.ArrayList;

import etc.Color;
import etc.Effects;
import etc.HitData;
import etc.RaytracerException;
import scene.ray.Ray;
import surface.Surface;
import util.Constants;
import util.UtilImpl;
import math.Point;
import math.Vector;
import math.simplex.Simplex;
import math.simplex.factory.ParametricSimplexFactoryImpl;
import math.simplex.factory.SimplexFactory;

public class BezierObject extends Surface
{
	private ArrayList<BezierPatch> patches;
	private SimplexFactory simplexFactory;
	
	public BezierObject(ArrayList<BezierPatch> incomingPatches)
	{
		cR = new Color(5.0,0.0,0.0);
		cL = new Color(0.4,0.4,0.4);
		cA = Constants.cA;
		patches = incomingPatches;
		effects = new Effects();
		effects.setLambertian(true);
		ops = new UtilImpl();
		simplexFactory = new ParametricSimplexFactoryImpl();
	}

	@Override
	public SurfaceType getType()
	{
		return SurfaceType.Bezier;
	}

	@Override
	public ArrayList<HitData> getHitData(Ray r) throws RaytracerException
	{
		ArrayList<HitData> ret = new ArrayList<HitData>();
		for(BezierPatch patch: patches)
		{
			ArrayList<HitData> subDivided = subDivide(r, patch);
			ret.addAll(subDivided);
		}
		return ret;
	}
	
	private ArrayList<HitData> subDivide(Ray r, BezierPatch patch)
	{
		Simplex simplex = simplexFactory.getSimplex(patch.getA(), patch.getB(), patch.getC());
		HitData hit = getHitData(r, simplex);
		if(!hit.isHit())
		{
			return new ArrayList<HitData>();
		}
		if(isPoint(patch))
		{
			ArrayList<HitData> hits = new ArrayList<HitData>();
			hits.add(hit);
			return hits;
		}
		ArrayList<HitData> hits = new ArrayList<HitData>();
		/* I will split up the triangles according to my cool little picture below...
		 * c
		 * |\
		 * | \
		 * |  \
		 * |   \
		 * | T4 \
		 * |_____\
		 * |a'   |\c'
		 * |\ T2 | \
		 * | \   |  \
		 * |  \  |   \
		 * |   \ |    \
		 * | T1 \|  T3 \
		 * -------------\
		 * a     b'      b
		 */
		Point aPrime = patch.getA().add(patch.getC()).scaledReturn(0.5);
		Point bPrime = patch.getB().add(patch.getA()).scaledReturn(0.5);
		Point cPrime = patch.getC().add(patch.getB()).scaledReturn(0.5);
		
		BezierPatch t1 = new BezierPatch(patch.getA(), aPrime, bPrime);
		hits.addAll(subDivide(r, t1));
		BezierPatch t2 = new BezierPatch(aPrime, bPrime, cPrime);
		hits.addAll(subDivide(r, t2));
		BezierPatch t3 = new BezierPatch(patch.getB(), bPrime, cPrime);
		hits.addAll(subDivide(r, t3));
		BezierPatch t4 = new BezierPatch(aPrime, cPrime, patch.getC());
		hits.addAll(subDivide(r, t4));
		return hits;
	}
	
	private HitData getHitData(Ray r, Simplex simplex)
	{
		return simplex.getHitData(r, this);
	}
	
	private boolean isPoint(BezierPatch patch)
	{
		//if the cross section of the triangle is less than some small value this is a point.
		Point ABPrime = patch.getA().add(patch.getB()).scaledReturn(0.5);
		Vector crossSection = patch.getC().minus(ABPrime);
		return crossSection.magnitude() < 0.003;
	}
}
