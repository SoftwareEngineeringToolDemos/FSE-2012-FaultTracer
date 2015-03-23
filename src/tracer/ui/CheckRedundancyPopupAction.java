package tracer.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import tracer.differencing.core.diff.GetAllTypeDeclVisitor;
import tracer.test.reduce.io.ReductionIOUtils;
import tracer.utils.DelCommentsVisitor;

public class CheckRedundancyPopupAction implements IObjectActionDelegate {
	private ISelection selection;
	Map<String, MethodDeclaration> tests = new HashMap<String, MethodDeclaration>();
	static String pathRoot;

	@Override
	public void run(IAction action) {
		tests.clear();
		if (!(this.selection instanceof IStructuredSelection)) {
			System.out.println("test files!");
			return;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) this.selection;
		List list = structuredSelection.toList();
		String workspace = Platform.getLocation().toString();
		String project = ((IJavaElement) list.get(0)).getJavaProject()
				.getElementName();
		pathRoot = workspace + File.separator + project + File.separator;
		int num = list.size();
		for (int it = 0; it < num; it++) {
			try {
				IJavaElement elem = (IJavaElement) list.get(it);
				if (elem instanceof IJavaProject)
					visit((IJavaProject) elem);
				else if (elem instanceof IPackageFragmentRoot)
					visit((IPackageFragmentRoot) elem);
				else if (elem instanceof IPackageFragment)
					visit((IPackageFragment) elem);
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		CheckRedundantTests();
	}

	public void CheckRedundantTests() {
		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		List<String> list = new ArrayList<String>();
		list.addAll(tests.keySet());
		while (!list.isEmpty()) {
			String base = list.remove(0);
			res.put(base, new HashSet<String>());
			String baseContent = tests.get(base).toString();
			Set<String> redun = new HashSet<String>();
			for (String m : list) {
				String comparedContent = tests.get(m).toString();
				if (compareAST(baseContent, comparedContent)) {
					redun.add(m);
					res.get(base).add(m);
				}
			}
			list.removeAll(redun);
		}
		ReductionIOUtils.writeReducedTests(pathRoot, res);
		System.out.println("The remaining test list:");
		for (String s : res.keySet()) {
			System.out.println(s);
		}
		System.out.println("Tests are reduced from " + tests.size() + " to "
				+ res.size() + ".");
	}
	public boolean compareAST(String con1, String con2){
		
		String temp1=con1.substring(con1.indexOf("{"));
		String temp2=con2.substring(con2.indexOf("{"));
		System.out.println("content: "+temp1);
		return temp1.equals(temp2);
	}

	


	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	public void visit(IJavaProject proj) throws JavaModelException {

		IPackageFragmentRoot[] roots = proj.getPackageFragmentRoots();

		for (IPackageFragmentRoot root : roots)
			visit(root);
	}

	public void visit(IPackageFragmentRoot root) throws JavaModelException {
		for (IJavaElement elem : root.getChildren()) {
			if (elem instanceof IPackageFragment) {
				visit((IPackageFragment) elem);
			}
		}
	}

	public void visit(IPackageFragment pkg) throws JavaModelException {
		for (ICompilationUnit unit : pkg.getCompilationUnits())
			visit(unit);
	}

	public void visit(ICompilationUnit unit) throws JavaModelException {
		CompilationUnit funit = getFormattedASTNode(unit);
		CompilationUnit nunit = getASTNode(unit);

		IType[] types = unit.getAllTypes();
		List<TypeDeclaration> typeDecls = getAllTypes(funit);
		List<TypeDeclaration> typeDecls2 = getAllTypes(nunit);
		for (IType type : types) {
			TypeDeclaration node = null;
			TypeDeclaration node2 = null;
			for (TypeDeclaration tn1 : typeDecls) {
				if (tn1.getName().toString().equals(type.getElementName())) {
					node = tn1;
					break;
				}
			}
			for (TypeDeclaration tn2 : typeDecls2) {
				if (tn2.getName().toString().equals(type.getElementName())) {
					node2 = tn2;
					break;
				}
			}
			visit(type, node, node2);
		}
	}

	public void visit(IType type, TypeDeclaration typeDeclFormat,
			TypeDeclaration typeDecl) throws JavaModelException {
		for (IMethod m : type.getMethods()) {
			if (m.getElementName().startsWith("test")) {
				String methodName = type.getFullyQualifiedName() + "."
						+ m.getElementName();
				MethodDeclaration mnode = findMethodDeclaration(typeDecl,
						typeDeclFormat, m);
				tests.put(methodName, mnode);
				// System.out.println(methodName+": "+mnode);
			}
		}
	}

	private MethodDeclaration findMethodDeclaration(TypeDeclaration type,
			TypeDeclaration ftype, IMethod method) throws JavaModelException {

		ISourceRange sourceRange = method.getSourceRange();
		MethodDeclaration[] mnodes = type.getMethods();
		MethodDeclaration[] fmnodes = ftype.getMethods();

		for (int i = 0; i < mnodes.length; i++) {
			MethodDeclaration mnode = mnodes[i];
			if ((sourceRange.getOffset() <= mnode.getStartPosition())
					&& (sourceRange.getLength() >= mnode.getLength()))
				return fmnodes[i];
		}
		return null;
	}

	private MethodDeclaration findMethodDeclaration(TypeDeclaration type,
			IMethod method) throws JavaModelException {
		ISourceRange sourceRange = method.getSourceRange();
		MethodDeclaration[] mnodes = type.getMethods();
		for (MethodDeclaration mnode : mnodes) {

			if ((sourceRange.getOffset() <= mnode.getStartPosition())
					&& (sourceRange.getLength() >= mnode.getLength()))
				return mnode;
		}
		return null;
	}

	public static List<TypeDeclaration> getAllTypes(CompilationUnit unitNode) {
		List<TypeDeclaration> res = new ArrayList<TypeDeclaration>();
		//
		// List tlist = unitNode.types();
		// List worklist = new ArrayList();
		// worklist.addAll(tlist);
		// while (!worklist.isEmpty()) {
		// Object o = worklist.remove(0);
		// TypeDeclaration node = (TypeDeclaration) o;
		// res.add(node);
		// for (TypeDeclaration child : node.getTypes())
		// worklist.add(child);
		// }
		GetAllTypeDeclVisitor visitor = new GetAllTypeDeclVisitor();
		unitNode.accept(visitor);
		res.addAll(visitor.types);
		return res;
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
