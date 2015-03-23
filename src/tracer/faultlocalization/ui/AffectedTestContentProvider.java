package tracer.faultlocalization.ui;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import tracer.test.selection.io.SelectionIOUtils;

public class AffectedTestContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			List<?> input = (List<?>) inputElement;
			return input.toArray();
		}
		return new Object[0];
	}

}
