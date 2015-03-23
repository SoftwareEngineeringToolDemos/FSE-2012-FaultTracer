package tracer.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class HidingFinder {

	public static List<IField> findHidenFields(IField field) {
		String fname = field.getElementName();
		IType type = field.getDeclaringType();
		List<IField> list = new ArrayList<IField>();
		try {
			ITypeHierarchy hier = type.newSupertypeHierarchy(null);
			IType[] supers = hier.getAllSuperclasses(type);
			for (IType s : supers) {
				// if(!s.isInterface())
				IField[] fs = s.getFields();
				for (IField f : fs) {
					if (f.getElementName().equals(fname)) {
						list.add(f);
						break;
					}
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}
