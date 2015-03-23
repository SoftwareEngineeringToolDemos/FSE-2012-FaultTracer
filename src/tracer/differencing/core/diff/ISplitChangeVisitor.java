package tracer.differencing.core.diff;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public interface ISplitChangeVisitor {
	public void visit(IPackageFragmentRoot root);

	public void visit(IPackageFragment pkg);

	public void visit(ICompilationUnit unit, CompilationUnit unode);

	public void visit(IType type, TypeDeclaration tnode);

	public void visit(IMethod method, MethodDeclaration mnode);

	public void visit(IField field);

}
