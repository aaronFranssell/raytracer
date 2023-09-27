package surface.csg.factory;

import etc.RaytracerException;
import surface.csg.tree.CSGTree;

public class CSGTreeFactoryImpl implements CSGTreeFactory {
	@Override
	public CSGTree createTree() throws RaytracerException {
		return new CSGTree();
	}
}
