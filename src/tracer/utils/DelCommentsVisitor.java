package tracer.utils;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Javadoc;

public class DelCommentsVisitor extends ASTVisitor {

	@Override
	public boolean visit(Javadoc node) {
		node.delete();
		return super.visit(node);
	}

}
