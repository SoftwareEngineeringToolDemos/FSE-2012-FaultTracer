package tracer.differencing.core.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class GetAllITypeVisitor {

	public List<IType> types=new ArrayList<IType>();
	public void visit(ICompilationUnit unit){
		try {
			for(IType type:unit.getAllTypes()){
				visit(type);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void visit(IType type) throws JavaModelException{
		types.add(type);
		for(IMethod method:type.getMethods()){
			visit(method);
		}
	}
	public void visit(IMethod method) throws JavaModelException{
		IJavaElement[] elems=method.getChildren();
		for(IJavaElement elem:elems){
			if(elem instanceof IType)
				visit((IType)elem);
		}
	}
}
