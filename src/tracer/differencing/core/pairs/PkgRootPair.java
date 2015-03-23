package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import tracer.differencing.core.diff.IDiffVisitor;




public class PkgRootPair implements IVisitable{
	
	public IPackageFragmentRoot elem1;
	public IPackageFragmentRoot elem2;
	
	public PkgRootPair(IPackageFragmentRoot elem1, IPackageFragmentRoot elem2){
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
