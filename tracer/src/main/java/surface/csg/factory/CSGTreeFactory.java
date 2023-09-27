package surface.csg.factory;

import etc.RaytracerException;
import surface.csg.tree.CSGTree;

public interface CSGTreeFactory {
	CSGTree createTree() throws RaytracerException;
}
