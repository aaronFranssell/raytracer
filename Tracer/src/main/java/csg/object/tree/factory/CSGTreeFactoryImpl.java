package csg.object.tree.factory;

import csg.object.tree.CSGTree;
import etc.RaytracerException;

public class CSGTreeFactoryImpl implements CSGTreeFactory
{
	@Override
	public CSGTree createTree() throws RaytracerException
	{
		return new CSGTree();
	}

}
