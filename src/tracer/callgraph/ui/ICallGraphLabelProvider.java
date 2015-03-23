package tracer.callgraph.ui;

import org.eclipse.jface.viewers.ILabelProvider;

public interface ICallGraphLabelProvider extends ILabelProvider {

	public Object[] getInterestingEdges();
}
