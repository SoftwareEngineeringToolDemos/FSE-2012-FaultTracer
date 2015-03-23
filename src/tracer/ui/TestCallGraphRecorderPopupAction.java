package tracer.ui;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import tracer.callgraph.test.instr.ClassInstruVisitor;


public class TestCallGraphRecorderPopupAction implements IObjectActionDelegate {

	private ISelection selection;

	@Override
	public void run(IAction action) {

		if (!(this.selection instanceof IStructuredSelection)) {
			System.out
					.println("select java elements in order to record call graph!");
			return;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		List list = structuredSelection.toList();
		for (int i = 0; i < list.size(); i++) {

			IJavaElement elem = (IJavaElement) list.get(i);
			System.out.println("analyze:"+elem.getElementName());
			ClassInstruVisitor visitor = new ClassInstruVisitor();
			if (elem instanceof IJavaProject)
				visitor.visit((IJavaProject) elem);
			else if (elem instanceof IPackageFragmentRoot)
				visitor.visit((IPackageFragmentRoot) elem);
			else if (elem instanceof IPackageFragment)
				visitor.visit((IPackageFragment) elem);
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

}
