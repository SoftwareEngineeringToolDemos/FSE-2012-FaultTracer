package tracer.differencing.core.diff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import tracer.Configuration;
import tracer.differencing.core.data.GlobalData;
import tracer.utils.GetNames;
import tracer.utils.HidingFinder;
import tracer.utils.OverridingFinder;

public class MethodBodyVisitor extends ASTVisitor {
	// static boolean considerOverriden = false;
	IMethod iMethod;
	public HashMap<String, List<String>> mCallees;
	public HashMap<String, List<String>> fCallees;

	String debug = "";

	public MethodBodyVisitor(IMethod iMethod) {
		this.iMethod = iMethod;
		mCallees = new HashMap<String, List<String>>();
		fCallees = new HashMap<String, List<String>>();
//		if (GetNames.getMethodName(iMethod).equals(debug)) {
//			System.out.println("<<nimei>>" + GetNames.getMethodName(iMethod));
//		}
	}

	@Override
	public boolean visit(AnonymousClassDeclaration type) {
		return false;
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		if (node.resolveConstructorBinding() == null) {
			return false;
		}
		IMethod callee = (IMethod) node.resolveConstructorBinding()
				.getJavaElement();
		// if (callee != null
		// && GetNames
		// .getMethodName(iMethod)
		// .equals(debug)) {
		// System.out.println("<<>>" + GetNames.getMethodName(callee));
		// } else {
		// System.out.println("unresolved1!");
		// }
		if (callee == null
				|| GetNames.getMethodName(iMethod).equals(
						GetNames.getMethodName(callee)))
			return true;
		if (GlobalData.projNames.contains(callee.getPath().segment(0))) {
			if (!Configuration.considerOverriden) {
				mCallees.put(GetNames.getMethodName(callee),
						new ArrayList<String>());
			} else {
				List<IMethod> methods = OverridingFinder
						.findOverridenMethods(callee);
				ArrayList<String> overridden = new ArrayList<String>();
				for (IMethod method : methods) {
					overridden.add(GetNames.getMethodName(method));
				}
				mCallees.put(GetNames.getMethodName(callee), overridden);
			}
			// System.out.println("SuperConstructorInvocation:" +
			// GetNames.getMethodName(callee));
		}
		return true;
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		if (node.resolveMethodBinding() == null) {
			return false;
		}
		IMethod callee = (IMethod) node.resolveMethodBinding().getJavaElement();
		// if (callee != null
		// && GetNames
		// .getMethodName(iMethod)
		// .equals(debug)) {
		// System.out.println("<<>>" + GetNames.getMethodName(callee));
		// } else {
		// System.out.println("unresolved2!");
		// }
		if (callee == null
				|| GetNames.getMethodName(iMethod).equals(
						GetNames.getMethodName(callee)))
			return true;
		if (GlobalData.projNames.contains(callee.getPath().segment(0))) {
			if (!Configuration.considerOverriden) {
				mCallees.put(GetNames.getMethodName(callee),
						new ArrayList<String>());
			} else {
				List<IMethod> methods = OverridingFinder
						.findOverridenMethods(callee);
				ArrayList<String> overridden = new ArrayList<String>();
				for (IMethod method : methods) {
					overridden.add(GetNames.getMethodName(method));
				}
				mCallees.put(GetNames.getMethodName(callee), overridden);
			}
			// System.out.println("SuperMethodInvocation:"+
			// GetNames.getMethodName(callee));
		}
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {

		if (node.resolveConstructorBinding() == null) {
			return false;
		}
		IMethod callee = (IMethod) node.resolveConstructorBinding()
				.getJavaElement();
		// if (callee != null
		// && GetNames
		// .getMethodName(iMethod)
		// .equals(debug)) {
		// System.out.println("<<>>" + GetNames.getMethodName(callee));
		// } else {
		// System.out.println("unresolved3!");
		// }
		if (callee == null
				|| GetNames.getMethodName(iMethod).equals(
						GetNames.getMethodName(callee)))
			return true;
		if (GlobalData.projNames.contains(callee.getPath().segment(0))) {
			if (!Configuration.considerOverriden) {
				mCallees.put(GetNames.getMethodName(callee),
						new ArrayList<String>());
			} else {
				List<IMethod> methods = OverridingFinder
						.findOverridenMethods(callee);
				ArrayList<String> overridden = new ArrayList<String>();
				for (IMethod method : methods) {
					overridden.add(GetNames.getMethodName(method));
				}
				mCallees.put(GetNames.getMethodName(callee), overridden);
			}

		}
		return true;
	}

	@Override
	public boolean visit(ConstructorInvocation node) {

		if (node.resolveConstructorBinding() == null) {
			return false;
		}
		IMethod callee = (IMethod) node.resolveConstructorBinding()
				.getJavaElement();
		// if (callee != null
		// && GetNames
		// .getMethodName(iMethod)
		// .equals(debug)) {
		// System.out.println("<<>>" + GetNames.getMethodName(callee));
		// } else {
		// System.out.println("unresolved4!");
		// }
		if (callee == null
				|| GetNames.getMethodName(iMethod).equals(
						GetNames.getMethodName(callee)))
			return true;
		if (GlobalData.projNames.contains(callee.getPath().segment(0))) {
			if (!Configuration.considerOverriden) {
				mCallees.put(GetNames.getMethodName(callee),
						new ArrayList<String>());
			} else {
				List<IMethod> methods = OverridingFinder
						.findOverridenMethods(callee);
				ArrayList<String> overridden = new ArrayList<String>();
				for (IMethod method : methods) {
					overridden.add(GetNames.getMethodName(method));
				}
				mCallees.put(GetNames.getMethodName(callee), overridden);
			}
			// System.out.println("ConstructorInvocation:"+
			// GetNames.getMethodName(callee));
		}
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		if (node.resolveMethodBinding() == null) {
			return false;
		}
		IMethod callee = (IMethod) node.resolveMethodBinding().getJavaElement();
		// if (callee != null
		// && GetNames
		// .getMethodName(iMethod)
		// .equals(debug)) {
		// System.out.println("<<>>" + GetNames.getMethodName(callee));
		// } else {
		// System.out.println("unresolved5!");
		// }
		if (callee == null
				|| GetNames.getMethodName(iMethod).equals(
						GetNames.getMethodName(callee)))
			return true;
		if (GlobalData.projNames.contains(callee.getPath().segment(0))) {
			if (!Configuration.considerOverriden) {
				mCallees.put(GetNames.getMethodName(callee),
						new ArrayList<String>());
			} else {
				List<IMethod> methods = OverridingFinder
						.findOverridenMethods(callee);
				List<String> overridden = new ArrayList<String>();
				for (IMethod method : methods) {
					overridden.add(GetNames.getMethodName(method));
				}
				mCallees.put(GetNames.getMethodName(callee), overridden);
			}
			// System.out.println("MethodInvocation:"+
			// GetNames.getMethodName(callee));
		}
		return true;
	}

	/*
	 * 
	 * @Override public boolean visit(SuperFieldAccess node) { IVariableBinding
	 * bind = node.resolveFieldBinding(); if (bind.isField()) { IField callee =
	 * (IField) bind.getJavaElement(); if
	 * (GlobalData.projNames.contains(callee.getPath().segment(0))) {
	 * System.out.println("SupperFieldAccess:" + GetNames.getFieldName(callee));
	 * } } return true; }
	 * 
	 * @Override public boolean visit(FieldAccess node) { IVariableBinding bind
	 * = node.resolveFieldBinding(); if (bind.isField()) { IField callee =
	 * (IField) bind.getJavaElement(); if
	 * (GlobalData.projNames.contains(callee.getPath().segment(0))) {
	 * System.out.println("FieldAccess:" + GetNames.getFieldName(callee)); } }
	 * return true; }
	 * 
	 * @Override public boolean visit(QualifiedName node) { IBinding bind =
	 * node.resolveBinding(); if (bind instanceof IVariableBinding) {
	 * IVariableBinding ibind = (IVariableBinding) bind; IField callee =
	 * (IField) ibind.getJavaElement(); if (callee != null &&
	 * GlobalData.projNames.contains(callee.getPath() .segment(0))) {
	 * System.out.println("QualifiedNameAccess:" +
	 * GetNames.getFieldName(callee)); } } return true; }
	 */

	@Override
	public boolean visit(SimpleName node) {
		if (node.resolveBinding() == null)
			return false;
		IBinding bind = node.resolveBinding();
		if (bind instanceof IVariableBinding) {
			IVariableBinding ibind = (IVariableBinding) bind;
			if (ibind.isField()) {
				IField callee = (IField) ibind.getJavaElement();
				if (callee != null
						&& GlobalData.projNames.contains(callee.getPath()
								.segment(0))) {
					if (!Configuration.considerOverriden) {
						fCallees.put(GetNames.getFieldName(callee),
								new ArrayList<String>());
					} else {
						List<IField> fields = HidingFinder
								.findHidenFields(callee);
						ArrayList<String> hidden = new ArrayList<String>();
						for (IField field : fields) {
							hidden.add(GetNames.getFieldName(field));
						}
						fCallees.put(GetNames.getFieldName(callee), hidden);
					}
					// System.out.println("FieldAccess:"+
					// GetNames.getFieldName(callee));
				}
			}
		}
		return true;

	}

}
