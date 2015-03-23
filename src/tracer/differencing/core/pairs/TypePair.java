package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import tracer.differencing.core.diff.IDiffVisitor;


public class TypePair implements IVisitable {

	public IType elem1;
	public IType elem2;
	public TypeDeclaration node1;
	public TypeDeclaration node2;
	public TypeDeclaration fnode1;
	public TypeDeclaration fnode2;

	public TypePair(IType elem1, IType elem2, TypeDeclaration node1,
			TypeDeclaration node2, TypeDeclaration fnode1,
			TypeDeclaration fnode2) {
		this.elem1 = elem1;
		this.elem2 = elem2;
		this.node1 = node1;
		this.node2 = node2;
		this.fnode1 = fnode1;
		this.fnode2 = fnode2;
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
