package tracer.callgraph.instr;

import java.io.File;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;

@Deprecated
public interface IVisitor {

	public void visit(IJavaProject proj);

	public void visit(IPackageFragmentRoot root);

	public void visit(IPackageFragment pkg);

	public void visit(File file);

	void visit(ICompilationUnit unit);

}
