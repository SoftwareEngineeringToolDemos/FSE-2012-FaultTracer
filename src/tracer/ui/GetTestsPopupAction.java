package tracer.ui;

import java.util.ArrayList;
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
	private List<IType> allTypes;

	@Override
	public void run(IAction action) {
		allTypes = new ArrayList<IType>();
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
		serialize();
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
				this.allTypes.add(type);
		}

	}

	public void serialize() {
		System.out
				.println(">>>>>>>>>>>>>>>>>>>>>>list of test classes:>>>>>>>>>>>>>>>>>>>>>>\n");
		for (IType type : allTypes) {
				System.out.println(type.getFullyQualifiedName());
		}
		StringBuilder sb = new StringBuilder();

		sb.append(">>>>>>>>>>>>>>>>>>>>>>JUnit3 format test suite:>>>>>>>>>>>>>>>>>>>>>>\n");
		sb.append("import junit.framework.Test;\n");
		sb.append("import junit.framework.TestCase;\n");
		sb.append("import junit.framework.TestSuite;\n");

		sb.append("public class AllTests extends TestCase {\n");

		sb.append("	public static Test suite() {\n");

		sb.append("		TestSuite result = new TestSuite(\"All Tests\");\n");
		for (IType type : allTypes) {
				sb.append("result.addTestSuite(" + type.getFullyQualifiedName()
						+ ".class);\n");
		}
		sb.append("return result;\n");
		sb.append("}\n");
		sb.append("}\n");
		sb.append("\n");
		sb.append(">>>>>>>>>>>>>>>>>>>>>>JUnit4 format test suite:>>>>>>>>>>>>>>>>>>>>>>\n");

		sb.append("import org.junit.runner.RunWith;\n");
		sb.append("import org.junit.runners.Suite;\n");

		sb.append("@RunWith(Suite.class)\n");
		sb.append("@Suite.SuiteClasses({");
		for (IType type : allTypes) {
				sb.append(type.getFullyQualifiedName() + ".class, ");
		}
		sb.append("})\n");
		sb.append("public class AllTests {}\n");
		System.out.println(sb);
	}

}
