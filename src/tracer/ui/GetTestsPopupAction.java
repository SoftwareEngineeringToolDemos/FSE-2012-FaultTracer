package tracer.ui;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class GetTestsPopupAction implements IObjectActionDelegate {
	private ISelection selection;

	@Override
	public void run(IAction action) {
		if (!(this.selection instanceof IStructuredSelection)) {
			System.out.println("select projects in order to get field scopes!");
			return;
		}

		IStructuredSelection structuredSelection = (IStructuredSelection) this.selection;
		List list = structuredSelection.toList();
		int num = list.size();
		for (int it = 0; it < num; it++) {
			try {
				IJavaElement elem = (IJavaElement) list.get(it);
				if (elem instanceof IJavaProject)
					visit((IJavaProject) elem);
				else if (elem instanceof IPackageFragmentRoot)
					visit((IPackageFragmentRoot) elem);
				else if (elem instanceof IPackageFragment)
					visit((IPackageFragment) elem);
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public void visit(IJavaProject proj) throws JavaModelException {

		IPackageFragmentRoot[] roots = proj.getPackageFragmentRoots();

		for (IPackageFragmentRoot root : roots)
			visit(root);
	}

	public void visit(IPackageFragmentRoot root) throws JavaModelException {
		for (IJavaElement elem : root.getChildren()) {
			if (elem instanceof IPackageFragment) {
				visit((IPackageFragment) elem);
			}
		}
	}

	public void visit(IPackageFragment pkg) throws JavaModelException {
		for (ICompilationUnit unit : pkg.getCompilationUnits())
			visit(unit);
	}

	public void visit(ICompilationUnit unit) throws JavaModelException {
		IType[] types = unit.getAllTypes();
		for (IType type : types) {
			if (!type.getFullyQualifiedName().contains("$"))
				System.out.println("result.addTestSuite("
						+ type.getFullyQualifiedName() + ".class);");
		}
	}

}
