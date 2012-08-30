package surface.csg.factory;

import surface.csg.tree.CSGTree;
import etc.RaytracerException;

public interface CSGTreeFactory
{
	CSGTree createTree() throws RaytracerException;
}
