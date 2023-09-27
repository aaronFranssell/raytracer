package surface.csg.factory;

import surface.csg.tree.CSGTree;

public class CSGTreeFactoryImpl implements CSGTreeFactory {
	@Override
	public CSGTree createTree() {
		return new CSGTree();
	}
}
