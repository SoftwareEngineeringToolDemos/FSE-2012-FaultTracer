package tracer.differencing.core.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import tracer.differencing.core.data.AtomicChange;
import tracer.differencing.core.data.Constants;
import tracer.differencing.core.data.GlobalData;
import tracer.utils.GetNames;

public class SplitRemoveChangeVisitor implements ISplitChangeVisitor {

	public void SplitChangeVisitor() {
	}

	@Override
	public void visit(IPackageFragmentRoot root) {

		List<IPackageFragment> pkgs = new ArrayList<IPackageFragment>();
		try {
			for (IJavaElement elem : root.getChildren()) {
				if (elem instanceof IPackageFragment) {
					pkgs.add((IPackageFragment) elem);
				}
			}
			for (IPackageFragment pkg : pkgs) {
				visit(pkg);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void visit(IPackageFragment pkg) {

		try {
			ICompilationUnit[] units = pkg.getCompilationUnits();
			for (ICompilationUnit unit : units) {

				visit(unit, buildAST(unit));
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void visit(ICompilationUnit unit, CompilationUnit unode) {

		List<TypeDeclaration> typeNodes = DiffVisitor.getAllTypes(unode);
		IType[] types = DiffVisitor.getAllTypes(unit);
		for (IType type : types) {
			if (!type.getElementName().startsWith("Test")) {
				try {
					if (type.isEnum()||type.isAnnotation())
						continue;
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
				TypeDeclaration tnode = null;
				for (TypeDeclaration tn : typeNodes) {
					if (tn.getName().toString()
							.equals(type.getElementName())) {
						tnode = tn;
						break;
					}
				}
				visit(type, tnode);
			}
		}
	}

	@Override
	public void visit(IType type, TypeDeclaration tnode) {

		try {
			IMethod[] methods;
			methods = type.getMethods();
			for (IMethod method : methods)
				visit(method, findMethodDeclaration(tnode, method));

			IField[] fields = type.getFields();
			for (IField field : fields)
				visit(field);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void visit(IMethod method, MethodDeclaration mnode) {
		Block b = mnode.getBody();
		if (b == null) {
			AtomicChange c = new AtomicChange(Constants.DM, method);
			GlobalData.atomicData.add(c);
			return;
		}
		MethodBodyVisitor v = new MethodBodyVisitor(method);
		b.accept(v);

		AtomicChange c = new AtomicChange(Constants.DM, method);
		c.mCallees = v.mCallees;
		c.fCallees = v.fCallees;

		GlobalData.atomicData.add(c);
		findLCChanges(method.getDeclaringType(), method, "DM");

	}

	@Override
	public void visit(IField field) {
		AtomicChange c = new AtomicChange(Constants.DF, field);
		GlobalData.atomicData.add(c);

		findLCFChanges(field.getDeclaringType(), field, "DF");
	}

	public CompilationUnit buildAST(ICompilationUnit unit) {
		try {
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(unit);
			parser.setResolveBindings(true);
			return (CompilationUnit) parser.createAST(null);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
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

	public void findLCChanges(IType type, IMethod method, String kind) {
		try {
			ITypeHierarchy typeHierarchy = type.newTypeHierarchy(null);

			IType[] superTypes = typeHierarchy.getAllSuperclasses(type);
			boolean isOverride = false;
			for (IType supType : superTypes) {
				if (supType.isClass() && containsMethod(supType, method)) {
					isOverride = true;
					break;
				}
			}
			if (!isOverride)
				return;
			addLCChange(type, method, kind);

			IType[] subTypes = typeHierarchy.getSubtypes(type);
			List<IType> worklist = new ArrayList<IType>();
			for (IType subType : subTypes) {
				worklist.add(subType);
			}
			while (!worklist.isEmpty()) {
				IType cur = worklist.remove(0);
				if (!containsMethod(cur, method)) {
					addLCChange(cur, method, kind);
					IType[] subts = typeHierarchy.getSubtypes(cur);
					for (IType subt : subts)
						worklist.add(subt);
				}
			}

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addLCChange(IType type, IMethod method, String kind) throws JavaModelException {
		AtomicChange c = new AtomicChange(Constants.LC, null);
		c.LCName = type.getFullyQualifiedName() + "."
				+ method.getElementName() + ":" + method.getSignature();
		boolean exist = false;
		for (AtomicChange ac : GlobalData.atomicData) {
			if (ac.toString().equals(c.toString())) {
				c = ac;
				exist = true;
				break;
			}
		}
		if (!exist)
			GlobalData.atomicData.add(c);
		c.dependOns.add(kind + ":" + GetNames.getMethodName(method));
	}

	public boolean containsMethod(IType type, IMethod method) {
		IMethod[] meths = type.findMethods(method);

		if (meths != null)
			return true;
		else
			return false;
	}

	public void findLCFChanges(IType type, IField field, String kind) {
		try {
			ITypeHierarchy typeHierarchy = type.newTypeHierarchy(null);

			IType[] superTypes = typeHierarchy.getAllSuperclasses(type);
			boolean isHide = false;
			for (IType supType : superTypes) {
				if (containsField(supType, field)) {
					isHide = true;
					break;
				}
			}
			if (!isHide)
				return;
			addLCFChange(type, field,kind);

			IType[] subTypes = typeHierarchy.getSubtypes(type);
			List<IType> worklist = new ArrayList<IType>();
			for (IType subType : subTypes) {
				worklist.add(subType);
			}
			while (!worklist.isEmpty()) {
				IType cur = worklist.remove(0);
				if (!containsField(cur, field)) {
					addLCFChange(cur, field,kind);
					IType[] subts = typeHierarchy.getSubtypes(cur);
					for (IType subt : subts)
						worklist.add(subt);
				}
			}

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void addLCFChange(IType type, IField field, String kind) throws JavaModelException {
		AtomicChange c = new AtomicChange(Constants.LCF, null);
		c.LCFName = type.getFullyQualifiedName() + "."
				+ field.getElementName() + ":" + field.getTypeSignature();
		boolean exist = false;
		for (AtomicChange ac : GlobalData.atomicData) {
			if (ac.toString().equals(c.toString())) {
				c = ac;
				exist = true;
				break;
			}
		}
		if (!exist)
			GlobalData.atomicData.add(c);
		c.dependOns.add(kind + ":" + GetNames.getFieldName(field));
	}
	public boolean containsField(IType type, IField field)
			throws JavaModelException {
		IField[] fields = type.getFields();
		for (IField f : fields) {
			if (f.getElementName().equals(field.getElementName()))
				return true;
		}
		return false;
	}

}
