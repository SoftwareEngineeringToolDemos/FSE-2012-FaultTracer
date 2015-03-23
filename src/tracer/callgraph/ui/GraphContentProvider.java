package tracer.callgraph.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

public class GraphContentProvider extends ArrayContentProvider implements
		IGraphEntityContentProvider {

	@Override
	public Object[] getConnectedTo(Object entity) {
		if (entity instanceof GraphNode) {
			GraphNode input = (GraphNode) entity;
			return input.getCallees().toArray();
		}
		throw new RuntimeException("Type not supported");
	}

}
