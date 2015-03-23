package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import tracer.differencing.core.diff.IDiffVisitor;




public class ProjectPair implements IVisitable{
	
	public IJavaProject elem1;
	public IJavaProject elem2;
	
	public ProjectPair(IJavaProject elem1, IJavaProject elem2){
		this.elem1=elem1;
		this.elem2=elem2;
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
