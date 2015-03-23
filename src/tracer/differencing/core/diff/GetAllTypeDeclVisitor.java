package tracer.differencing.core.diff;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class GetAllTypeDeclVisitor extends ASTVisitor {
	public Set<TypeDeclaration> types=new HashSet<TypeDeclaration>();
	//public Set<AnonymousClassDeclaration> atypes=new HashSet<AnonymousClassDeclaration>();
	public boolean visit(TypeDeclaration node){
		types.add(node);
		return true;
	}
//	public boolean visit(AnonymousClassDeclaration node) {
//		atypes.add(node);
//		return true;
//	}
}
