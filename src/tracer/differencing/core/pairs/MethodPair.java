package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import tracer.differencing.core.diff.IDiffVisitor;


public class MethodPair implements IVisitable {

	public IMethod elem1;
	public IMethod elem2;

	public MethodDeclaration mnode1;
	public MethodDeclaration mnode2;
	
	public MethodDeclaration umnode1;
	public MethodDeclaration umnode2;

	public MethodPair(IMethod elem1, IMethod elem2, MethodDeclaration mnode1,
			MethodDeclaration mnode2, MethodDeclaration umnode1,
			MethodDeclaration umnode2) {
		this.elem1 = elem1;
		this.elem2 = elem2;
		this.mnode1 = mnode1;
		this.mnode2 = mnode2;
		this.umnode1 = umnode1;
		this.umnode2 = umnode2;
	}

	@Override
	public void accept(IDiffVisitor visitor) throws JavaModelException {
		visitor.visit(this);

	}
//	@Override
//	public void accept(IRefactoringInferenceVisitor visitor) throws JavaModelException {
//		visitor.visit(this);
//	}

}
