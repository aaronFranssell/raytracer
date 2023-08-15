package surface.csg.factory;

import surface.csg.tree.CSGTree;
import etc.RaytracerException;

public class CSGTreeFactoryImpl implements CSGTreeFactory
{
	@Override
	public CSGTree createTree() throws RaytracerException
	{
		return new CSGTree();
	}

}
