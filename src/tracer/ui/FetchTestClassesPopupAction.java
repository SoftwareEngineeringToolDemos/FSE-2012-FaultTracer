package tracer.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Platform;
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

public class FetchTestClassesPopupAction implements IObjectActionDelegate {
	private ISelection selection;
	File file;

	@Override
	public void run(IAction action) {
		if (!(this.selection instanceof IStructuredSelection)) {
			System.out.println("select projects in order to get field scopes!");
			return;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) this.selection;
		List list = structuredSelection.toList();
		String workspace = Platform.getLocation().toString();
		String project = ((IJavaElement) list.get(0)).getJavaProject()
				.getElementName();
		file = new File(workspace + File.separator + project + File.separator
				+ "TestClasses.dat");

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
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(file, true));
				writer.write(type.getFullyQualifiedName() + "\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
