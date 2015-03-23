package tracer.differencing.ui;

import org.eclipse.jface.viewers.ILabelProvider;

public interface IChangeLabelProvider extends ILabelProvider {

	public Object[] getInterestingEdges();
}
