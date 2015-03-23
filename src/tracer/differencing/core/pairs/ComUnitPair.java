package tracer.differencing.core.pairs;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import tracer.differencing.core.diff.IDiffVisitor;
import tracer.utils.DelCommentsVisitor;

public class ComUnitPair implements IVisitable {

	public ICompilationUnit elem1;
	public ICompilationUnit elem2;
	public CompilationUnit fnode1;
	public CompilationUnit fnode2;

	public CompilationUnit unode1;
	public CompilationUnit unode2;

	public ComUnitPair(ICompilationUnit elem1, ICompilationUnit elem2) {
		this.elem1 = elem1;
		this.elem2 = elem2;

		fnode1 = getFormattedASTNode(elem1);
		fnode2 = getFormattedASTNode(elem2);

		unode1 = getASTNode(elem1);
		unode2 = getASTNode(elem2);
	}

	@Override
	public void accept(IDiffVisitor visitor) throws JavaModelException {
		visitor.visit(this);
	}

//	@Override
//	public void accept(IRefactoringInferenceVisitor visitor)
//			throws JavaModelException {
//		visitor.visit(this);
//	}

	public CompilationUnit getFormattedASTNode(ICompilationUnit unit) {
		try {
			ASTParser parser = ASTParser.newParser(AST.JLS3);

			String content = null;
			try {
				content = format(unit.getSource());
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			parser.setSource(content.toCharArray());
		
			parser.setResolveBindings(true);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			cu.accept(new DelCommentsVisitor());
			return cu;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
	}

	public CompilationUnit getASTNode(ICompilationUnit unit) {
		try {
			ASTParser parser = ASTParser.newParser(AST.JLS3);

			parser.setSource(unit);
			parser.setResolveBindings(true);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			return cu;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String format(String content) {
		Document doc = new Document(content);
		CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(JavaCore
				.getOptions());
		TextEdit edits = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT,
				content, 0, content.length(), 0, null);
		try {
			if (edits != null)
				edits.apply(doc);
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return doc.get();

	}

}
