package tracer.differencing.core.diff;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import tracer.differencing.core.data.AtomicChange;
import tracer.differencing.core.data.Constants;
import tracer.differencing.core.data.GlobalData;
import tracer.differencing.core.pairs.ComUnitPair;
import tracer.differencing.core.pairs.FieldPair;
import tracer.differencing.core.pairs.MethodPair;
import tracer.differencing.core.pairs.PkgPair;
import tracer.differencing.core.pairs.PkgRootPair;
import tracer.differencing.core.pairs.ProjectPair;
import tracer.differencing.core.pairs.TypePair;
import tracer.utils.FilterWhiteSpaces;
import tracer.utils.GetNames;

public class DiffVisitor implements IDiffVisitor, Constants {
	public static List<IJavaElement> added = new ArrayList<IJavaElement>();
	public static List<IJavaElement> match1 = new ArrayList<IJavaElement>();
	public static List<IJavaElement> match2 = new ArrayList<IJavaElement>();
	public static List<IJavaElement> deleted = new ArrayList<IJavaElement>();

	public void visit(ProjectPair pair) throws JavaModelException {
		clear();
		IJavaProject proj1 = pair.elem1;
		IJavaProject proj2 = pair.elem2;

		IPackageFragmentRoot[] roots1 = proj1.getPackageFragmentRoots();
		IPackageFragmentRoot[] roots2 = proj2.getPackageFragmentRoots();

		List<IPackageFragmentRoot> list1 = new ArrayList<IPackageFragmentRoot>();
		List<IPackageFragmentRoot> list2 = new ArrayList<IPackageFragmentRoot>();

		for (IPackageFragmentRoot root : roots1) {
			if (!root.isArchive())
				list1.add(root);
		}
		for (IPackageFragmentRoot root : roots2) {
			if (!root.isArchive())
				list2.add(root);
		}

		IPackageFragmentRoot[] rs1 = new IPackageFragmentRoot[list1.size()];
		for (int i = 0; i < rs1.length; i++)
			rs1[i] = list1.get(i);

		IPackageFragmentRoot[] rs2 = new IPackageFragmentRoot[list2.size()];
		for (int i = 0; i < rs2.length; i++)
			rs2[i] = list2.get(i);

		compare(rs1, rs2);
		for (IJavaElement elem : added) {
			AtomicChange c = new AtomicChange(APR, elem);
			GlobalData.data.add(c);
			SplitAddChangeVisitor v = new SplitAddChangeVisitor();
			v.visit((IPackageFragmentRoot) elem);
		}
		for (IJavaElement elem : deleted) {
			AtomicChange c = new AtomicChange(DPR, elem);
			GlobalData.data.add(c);
			SplitRemoveChangeVisitor v = new SplitRemoveChangeVisitor();
			v.visit((IPackageFragmentRoot) elem);
		}
		List<PkgRootPair> workList = new ArrayList<PkgRootPair>();
		for (int i = 0; i < match1.size(); i++) {
			PkgRootPair pkgRootPair = new PkgRootPair(
					(IPackageFragmentRoot) match1.get(i),
					(IPackageFragmentRoot) match2.get(i));
			workList.add(pkgRootPair);
		}
		for (PkgRootPair pkgRootPair : workList) {
			pkgRootPair.accept(this);
		}

	}

	public void visit(PkgRootPair pair) throws JavaModelException {
		clear();
		IPackageFragmentRoot proot1 = pair.elem1;
		IPackageFragmentRoot proot2 = pair.elem2;

		List<IPackageFragment> l1 = new ArrayList<IPackageFragment>();
		List<IPackageFragment> l2 = new ArrayList<IPackageFragment>();
		for (IJavaElement elem : proot1.getChildren()) {
			if (elem instanceof IPackageFragment) {
				l1.add((IPackageFragment) elem);
			}
		}
		for (IJavaElement elem : proot2.getChildren()) {
			if (elem instanceof IPackageFragment) {
				l2.add((IPackageFragment) elem);
			}
		}
		IPackageFragment[] pkgs1 = new IPackageFragment[l1.size()];
		IPackageFragment[] pkgs2 = new IPackageFragment[l2.size()];
		for (int i = 0; i < l1.size(); i++)
			pkgs1[i] = l1.get(i);
		for (int i = 0; i < l2.size(); i++)
			pkgs2[i] = l2.get(i);

		compare(pkgs1, pkgs2);
		for (IJavaElement elem : added) {
			AtomicChange c = new AtomicChange(AP, elem);
			GlobalData.data.add(c);
			SplitAddChangeVisitor v = new SplitAddChangeVisitor();
			v.visit((IPackageFragment) elem);
		}
		for (IJavaElement elem : deleted) {
			AtomicChange c = new AtomicChange(DP, elem);
			GlobalData.data.add(c);
			SplitRemoveChangeVisitor v = new SplitRemoveChangeVisitor();
			v.visit((IPackageFragment) elem);
		}
		List<PkgPair> workList = new ArrayList<PkgPair>();
		for (int i = 0; i < match1.size(); i++) {
			PkgPair pkgPair = new PkgPair((IPackageFragment) match1.get(i),
					(IPackageFragment) match2.get(i));
			workList.add(pkgPair);
		}
		for (PkgPair pkgPair : workList) {
			pkgPair.accept(this);
		}
	}

	public void visit(PkgPair pair) throws JavaModelException {
		clear();
		IPackageFragment pkg1 = pair.elem1;
		IPackageFragment pkg2 = pair.elem2;

		ICompilationUnit[] comUnits1 = pkg1.getCompilationUnits();
		ICompilationUnit[] comUnits2 = pkg2.getCompilationUnits();

		compare(comUnits1, comUnits2);
		for (IJavaElement elem : added) {
			AtomicChange c = new AtomicChange(AC, elem);
			GlobalData.data.add(c);
			SplitAddChangeVisitor v = new SplitAddChangeVisitor();
			v.visit((ICompilationUnit) elem,
					v.buildAST((ICompilationUnit) elem));
		}
		for (IJavaElement elem : deleted) {
			AtomicChange c = new AtomicChange(DC, elem);
			GlobalData.data.add(c);
			SplitRemoveChangeVisitor v = new SplitRemoveChangeVisitor();
			v.visit((ICompilationUnit) elem,
					v.buildAST((ICompilationUnit) elem));
		}
		List<ComUnitPair> workList = new ArrayList<ComUnitPair>();
		for (int i = 0; i < match1.size(); i++) {
			ComUnitPair comUnitPair = new ComUnitPair(
					(ICompilationUnit) match1.get(i),
					(ICompilationUnit) match2.get(i));
			workList.add(comUnitPair);
		}
		for (ComUnitPair comUnitPair : workList) {
			comUnitPair.accept(this);
		}

	}

	public void visit(ComUnitPair pair) throws JavaModelException {
		clear();
		ICompilationUnit comUnit1 = pair.elem1;
		ICompilationUnit comUnit2 = pair.elem2;
		CompilationUnit unitNode1 = pair.unode1;
		CompilationUnit unitNode2 = pair.unode2;

		CompilationUnit formatNode1 = pair.fnode1;
		CompilationUnit formatNode2 = pair.fnode2;

		IType[] types1 = getAllTypes(comUnit1);
		IType[] types2 = getAllTypes(comUnit2);

//		if (types1[0].getFullyQualifiedName().equals(
//				"org.apache.jmeter.visualizers.GraphAccum")) {
//			int debug = 1;
//			debug = debug + 1;
//		}

		List<TypeDeclaration> typeNodes1 = getAllTypes(unitNode1);
		List<TypeDeclaration> typeNodes2 = getAllTypes(unitNode2);

		List<TypeDeclaration> ftypeNodes1 = getAllTypes(formatNode1);
		List<TypeDeclaration> ftypeNodes2 = getAllTypes(formatNode2);

		compare(types1, types2);
		for (IJavaElement elem : added) {
			if (!elem.getElementName().startsWith("Test")) {
				if (((IType) elem).isEnum())
					continue;
				AtomicChange c = new AtomicChange(AT, elem);
				GlobalData.data.add(c);
				SplitAddChangeVisitor v = new SplitAddChangeVisitor();
				TypeDeclaration tnode = null;
				for (TypeDeclaration tn : typeNodes2) {
					if (tn.getName().toString().equals(elem.getElementName())) {
						tnode = tn;
						break;
					}
				}
				v.visit((IType) elem, tnode);
			}
		}
		for (IJavaElement elem : deleted) {
			if (!elem.getElementName().startsWith("Test")) {
				if (((IType) elem).isEnum())
					continue;
				AtomicChange c = new AtomicChange(DT, elem);
				GlobalData.data.add(c);
				SplitRemoveChangeVisitor v = new SplitRemoveChangeVisitor();
				TypeDeclaration tnode = null;
				for (TypeDeclaration tn : typeNodes1) {// BUGFIX: for
														// (TypeDeclaration tn :
														// typeNodes2) {
					if (tn.getName().toString().equals(elem.getElementName())) {
						tnode = tn;
						break;
					}
				}
				v.visit((IType) elem, tnode);
			}
		}

		List<TypePair> workList = new ArrayList<TypePair>();
		for (int i = 0; i < match1.size(); i++) {
			IType t1 = (IType) match1.get(i);
			IType t2 = (IType) match2.get(i);
			if (t1.isEnum()||t2.isEnum()||t1.isAnnotation()||t2.isAnnotation())
				continue;

			if (!t1.getElementName().startsWith("Test")) {
				TypeDeclaration node1 = null;
				TypeDeclaration node2 = null;
				TypeDeclaration fnode1 = null;
				TypeDeclaration fnode2 = null;
				for (TypeDeclaration tn1 : typeNodes1) {
					if (tn1.getName().toString().equals(t1.getElementName())) {
						node1 = tn1;
						break;
					}
				}
				for (TypeDeclaration tn2 : typeNodes2) {
					if (tn2.getName().toString().equals(t2.getElementName())) {
						node2 = tn2;
						break;
					}
				}

				for (TypeDeclaration tn1 : ftypeNodes1) {
					if (tn1.getName().toString().equals(t1.getElementName())) {
						fnode1 = tn1;
						break;
					}
				}
				for (TypeDeclaration tn2 : ftypeNodes2) {
					if (tn2.getName().toString().equals(t2.getElementName())) {
						fnode2 = tn2;
						break;
					}
				}
				TypePair typePair = new TypePair(t1, t2, node1, node2, fnode1,
						fnode2);
				workList.add(typePair);
			}
		}
		for (TypePair typePair : workList) {
			typePair.accept(this);
		}

	}

	public void visit(TypePair pair) throws JavaModelException {

		IType type1 = pair.elem1;
		IType type2 = pair.elem2;
		TypeDeclaration node1 = pair.node1;
		TypeDeclaration node2 = pair.node2;

		TypeDeclaration ftnode1 = pair.fnode1;
		TypeDeclaration ftnode2 = pair.fnode2;

		clear();
		IMethod[] meths1 = type1.getMethods();
		IMethod[] meths2 = type2.getMethods();
		compare(meths1, meths2);

		for (IJavaElement elem : added) {
			MethodDeclaration mnode2 = findMethodDeclaration(node2,
					(IMethod) elem);
			Block b2 = mnode2.getBody();
			if (b2 == null) {
				AtomicChange c = new AtomicChange(Constants.AM, (IMethod) elem);
				GlobalData.data.add(c);
				GlobalData.atomicData.add(c);
			} else {
				MethodBodyVisitor v = new MethodBodyVisitor((IMethod) elem);
				b2.accept(v);
				AtomicChange c = new AtomicChange(AM, elem);
				GlobalData.data.add(c);
				c.fCallees = v.fCallees;
				c.mCallees = v.mCallees;
				GlobalData.atomicData.add(c);

				findLCChanges(type2, (IMethod) elem, "AM");
			}
		}
		for (IJavaElement elem : deleted) {
			MethodDeclaration mnode1 = findMethodDeclaration(node1,
					(IMethod) elem);
			Block b1 = mnode1.getBody();
			if (b1 == null) {
				AtomicChange c = new AtomicChange(Constants.DM, (IMethod) elem);
				GlobalData.data.add(c);
				GlobalData.atomicData.add(c);
			} else {
				MethodBodyVisitor v = new MethodBodyVisitor((IMethod) elem);
				b1.accept(v);
				AtomicChange c = new AtomicChange(DM, elem);
				GlobalData.data.add(c);
				c.fCallees = v.fCallees;
				c.mCallees = v.mCallees;
				GlobalData.atomicData.add(c);

				findLCChanges(type1, (IMethod) elem, "DM");
			}
		}
		List<MethodPair> workList = new ArrayList<MethodPair>();
		for (int i = 0; i < match1.size(); i++) {
			//System.out.println(type1.getFullyQualifiedName()+" >> "+match1.get(i).getElementName());
			MethodDeclaration mnode1 = findMethodDeclaration(node1, ftnode1,
					(IMethod) match1.get(i));
			MethodDeclaration mnode2 = findMethodDeclaration(node2, ftnode2,
					(IMethod) match2.get(i));

			MethodDeclaration umnode1 = findMethodDeclaration(node1,
					(IMethod) match1.get(i));
			MethodDeclaration umnode2 = findMethodDeclaration(node2,
					(IMethod) match2.get(i));

			MethodPair methodPair = new MethodPair((IMethod) match1.get(i),
					(IMethod) match2.get(i), mnode1, mnode2, umnode1, umnode2);
			workList.add(methodPair);
		}
		for (MethodPair methodPair : workList) {
			methodPair.accept(this);
		}

		clear();

		IField[] fields1 = type1.getFields();
		IField[] fields2 = type2.getFields();
		compare(fields1, fields2);
		for (IJavaElement elem : added) {
			AtomicChange c = new AtomicChange(AF, elem);
			GlobalData.data.add(c);
			GlobalData.atomicData.add(c);

			findLCFChanges(type1, (IField) elem, "AF");
		}
		for (IJavaElement elem : deleted) {
			AtomicChange c = new AtomicChange(DF, elem);
			GlobalData.data.add(c);
			GlobalData.atomicData.add(c);

			findLCFChanges(type2, (IField) elem, "DF");
		}
		List<FieldPair> worklist = new ArrayList<FieldPair>();
		for (int i = 0; i < match1.size(); i++) {
			FieldDeclaration fnode1 = findFieldDeclaration(node1, ftnode1,
					(IField) match1.get(i));
			FieldDeclaration fnode2 = findFieldDeclaration(node2, ftnode2,
					(IField) match2.get(i));
			FieldPair fieldPair = new FieldPair((IField) match1.get(i),
					(IField) match2.get(i), fnode1, fnode2);
			worklist.add(fieldPair);
		}
		for (FieldPair fieldPair : worklist) {
			fieldPair.accept(this);
		}
	}

	public void visit(MethodPair pair) throws JavaModelException {
		clear();
		IMethod meth1 = pair.elem1;
		IMethod meth2 = pair.elem2;

		MethodDeclaration mnode1 = pair.mnode1;
		MethodDeclaration mnode2 = pair.mnode2;

		MethodDeclaration umnode1 = pair.umnode1;
		MethodDeclaration umnode2 = pair.umnode2;

		String con1 = mnode1.toString();
		String con2 = mnode2.toString();

		String filtered1 = FilterWhiteSpaces.dropWhiteSpaces(con1);// BUGFIX:
																	// without
																	// comment
																	// filering

		String filtered2 = FilterWhiteSpaces.dropWhiteSpaces(con2);

		if (!filtered1.equals(filtered2)) {

			Block b1 = umnode1.getBody();
			Block b2 = umnode2.getBody();
			if (b1 == null || b2 == null) {
				AtomicChange c = new AtomicChange(Constants.CM, meth1, meth2,
						con1, con2);
				GlobalData.data.add(c);
				GlobalData.atomicData.add(c);
				return;
			}
			MethodBodyVisitor v1 = new MethodBodyVisitor(meth1);
			// PrintNodeVisitor v1=new PrintNodeVisitor(System.out);
			MethodBodyVisitor v2 = new MethodBodyVisitor(meth2);
			b1.accept(v1);
			b2.accept(v2);
			AtomicChange c = new AtomicChange(CM, meth1, meth2, con1, con2);
			GlobalData.data.add(c);
			for (String fCallee : v1.fCallees.keySet()) {
				c.fCallees.put(fCallee, v1.fCallees.get(fCallee));
			}
			for (String fCallee : v2.fCallees.keySet()) {
				c.fCallees.put(fCallee, v2.fCallees.get(fCallee));
			}
			for (String mCallee : v1.mCallees.keySet()) {
				c.mCallees.put(mCallee, v1.mCallees.get(mCallee));
			}
			for (String mCallee : v2.mCallees.keySet()) {
				c.mCallees.put(mCallee, v2.mCallees.get(mCallee));
			}
			GlobalData.atomicData.add(c);
		}
	}

	public void visit(FieldPair pair) throws JavaModelException {
		clear();
		IField field1 = pair.elem1;
		IField field2 = pair.elem2;

		String con1 = pair.fnode1.toString();
		// System.out.println("elem1: " + GetNames.getFieldName(field1));

		String con2 = pair.fnode2.toString();
		// System.out.println("elem2: " + GetNames.getFieldName(field2));
		String filtered1 = FilterWhiteSpaces.dropWhiteSpaces(con1);// BUGFIX:
																	// without
																	// comment
																	// filering

		String filtered2 = FilterWhiteSpaces.dropWhiteSpaces(con2);

		if (!filtered1.equals(filtered2)) {
			AtomicChange c;
			if (con1.contains("static "))
				c = new AtomicChange(CSFI, field2);
			else
				c = new AtomicChange(CFI, field2);
			GlobalData.data.add(c);
			GlobalData.atomicData.add(c);
		}

	}

	public static void compare(IJavaElement[] elems1, IJavaElement[] elems2)
			throws JavaModelException {
		for (IJavaElement elem1 : elems1) {
			boolean matching = false;
			for (IJavaElement elem2 : elems2) {
				String n1 = "";
				String n2 = "";

				if (elem1 instanceof IMethod) {
					IMethod m1 = (IMethod) elem1;
					IMethod m2 = (IMethod) elem2;
					n1 = m1.getPath().removeFirstSegments(1) + "-"
							+ m1.getElementName() + ":" + m1.getSignature();
					n2 = m2.getPath().removeFirstSegments(1) + "-"
							+ m2.getElementName() + ":" + m2.getSignature();
				} else {

					n1 = elem1.getPath().removeFirstSegments(1) + "-"
							+ elem1.getElementName();
					n2 = elem2.getPath().removeFirstSegments(1) + "-"
							+ elem2.getElementName();
				}
				if (n1.equals(n2)) {
					match1.add(elem1);
					match2.add(elem2);
					matching = true;
					break;
				}
			}
			if (!matching) {
				deleted.add(elem1);
			}
		}
		for (IJavaElement elem2 : elems2) {
			if (!(match2.contains(elem2)))
				added.add(elem2);
		}

	}

	public static void clear() {
		added.clear();
		deleted.clear();
		match1.clear();
		match2.clear();

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

	public static IType[] getAllTypes(ICompilationUnit unit){
//		GetAllITypeVisitor visitor = new GetAllITypeVisitor();
//		visitor.visit(unit);
//		IType[] types = new IType[visitor.types.size()];
//		for (int i = 0; i < types.length; i++)
//			types[i] = visitor.types.get(i);
		IType[] types=null;
		try {
			types=unit.getAllTypes();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return types;
	}

	public CompilationUnit getCu(TypeDeclaration type) {
		ASTNode node = type.getParent();
		while (!(node instanceof CompilationUnit)) {
			node = node.getParent();
		}
		return (CompilationUnit) node;

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

	private FieldDeclaration findFieldDeclaration(TypeDeclaration type,
			IField field) throws JavaModelException {

		ISourceRange sourceRange = field.getSourceRange();
		FieldDeclaration[] fnodes = type.getFields();
		for (FieldDeclaration fnode : fnodes) {
			if ((sourceRange.getOffset() <= fnode.getStartPosition())
					&& (sourceRange.getLength() >= fnode.getLength()))
				return fnode;
		}
		return null;

	}

	private FieldDeclaration findFieldDeclaration(TypeDeclaration type,
			TypeDeclaration ftype, IField field) throws JavaModelException {

		ISourceRange sourceRange = field.getSourceRange();
		FieldDeclaration[] fnodes = type.getFields();
		FieldDeclaration[] ffnodes = ftype.getFields();

		for (int i = 0; i < fnodes.length; i++) {
			FieldDeclaration fnode = fnodes[i];
			if ((sourceRange.getOffset() <= fnode.getStartPosition())
					&& (sourceRange.getLength() >= fnode.getLength()))
				return ffnodes[i];
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
			addLCFChange(type, field, kind);

			IType[] subTypes = typeHierarchy.getSubtypes(type);
			List<IType> worklist = new ArrayList<IType>();
			for (IType subType : subTypes) {
				worklist.add(subType);
			}
			while (!worklist.isEmpty()) {
				IType cur = worklist.remove(0);
				if (!containsField(cur, field)) {
					addLCFChange(cur, field, kind);
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

	public void addLCChange(IType type, IMethod method, String kind)
			throws JavaModelException {
		AtomicChange c = new AtomicChange(Constants.LC, null);
		c.LCName = type.getFullyQualifiedName() + "." + method.getElementName()
				+ ":" + method.getSignature();
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

	public void addLCFChange(IType type, IField field, String kind)
			throws JavaModelException {
		AtomicChange c = new AtomicChange(Constants.LCF, null);
		c.LCFName = type.getFullyQualifiedName() + "." + field.getElementName()
				+ ":" + field.getTypeSignature();
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

	public boolean containsMethod(IType type, IMethod method) {
		IMethod[] meths = type.findMethods(method);

		if (meths != null)
			return true;
		else
			return false;
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
