package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import tracer.differencing.core.diff.IDiffVisitor;




public class PkgPair implements IVisitable{
	
	public IPackageFragment elem1;
	public IPackageFragment elem2;
	
	public PkgPair(IPackageFragment elem1, IPackageFragment elem2){
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
