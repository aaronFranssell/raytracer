package csg.object.tree.factory;

import csg.object.tree.CSGTree;
import etc.RaytracerException;

public interface CSGTreeFactory
{
	CSGTree createTree() throws RaytracerException;
}
