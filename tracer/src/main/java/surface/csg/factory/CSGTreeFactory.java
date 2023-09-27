package surface.csg.factory;

import surface.csg.tree.CSGTree;

public interface CSGTreeFactory {
	CSGTree createTree() throws Exception;
}
