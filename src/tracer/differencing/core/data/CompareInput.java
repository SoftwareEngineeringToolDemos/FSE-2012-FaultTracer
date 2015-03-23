package tracer.differencing.core.data;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.IProgressMonitor;

public class CompareInput extends CompareEditorInput {
	String olds;
	String news;

	public CompareInput(String olds, String news) {
		super(new CompareConfiguration());
		this.olds = olds;
		this.news = news;
	}

	protected Object prepareInput(IProgressMonitor pm) {
		CompareItem left = new CompareItem("Before", olds);
		CompareItem right = new CompareItem("After", news);
		return new DiffNode(left,
				right);
	}
}
