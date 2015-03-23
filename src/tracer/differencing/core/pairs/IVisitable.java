package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.JavaModelException;

import tracer.differencing.core.diff.IDiffVisitor;



public interface IVisitable {

	public void accept(IDiffVisitor visitor) throws JavaModelException;

	//void accept(IRefactoringInferenceVisitor visitor) throws JavaModelException;
}
