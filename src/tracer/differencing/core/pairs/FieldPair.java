package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import tracer.differencing.core.diff.IDiffVisitor;


public class FieldPair implements IVisitable {

	public IField elem1;
	public IField elem2;
	public FieldDeclaration fnode1;
	public FieldDeclaration fnode2;

	public FieldPair(IField elem1, IField elem2, FieldDeclaration fnode1,
			FieldDeclaration fnode2) {
		this.elem1 = elem1;
		this.elem2 = elem2;
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
