package tracer.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class OverridingFinder {

	public static List<IMethod> findOverridenMethods(IMethod method) {
		// String mname = GetNames.getShortMethodName(method);
		IType type = method.getDeclaringType();
		List<IMethod> list = new ArrayList<IMethod>();
		try {
			ITypeHierarchy hier = type.newSupertypeHierarchy(null);
			IType supType = hier.getSuperclass(type);
			String prefix="";
			if(type.getFullyQualifiedName().contains("."))
			prefix=type.getFullyQualifiedName().substring(0, type.getFullyQualifiedName().indexOf("."));
			while (supType != null) {
				if(!supType.getFullyQualifiedName().startsWith(prefix)){
					break;
				}
				IMethod[] ms = supType.findMethods(method);
				if (ms != null) {
					for (IMethod m : ms) {
						int flag = m.getFlags();
						if (!Flags.isAbstract(flag))//Bug fix: add the checking for abstract methods
							list.add(m);
					}
				}
				supType = hier.getSuperclass(supType);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}
