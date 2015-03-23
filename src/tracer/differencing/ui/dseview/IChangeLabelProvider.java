package tracer.differencing.ui.dseview;

import org.eclipse.jface.viewers.ILabelProvider;

public interface IChangeLabelProvider extends ILabelProvider {

	public Object[] getInterestingEdges();
}
