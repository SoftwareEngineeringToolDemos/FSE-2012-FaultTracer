package tracer.utils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

import tracer.differencing.core.data.GlobalData;

public class CreateType {
	static String projName1;
	static String projName2;

	public static IType createType(IJavaProject proj1, IType type2)
			throws CoreException {
		projName1 = proj1.getElementName();
		projName2 = type2.getJavaProject().getElementName();
		IType parent2 = type2.getDeclaringType();
		ICompilationUnit unit2 = type2.getCompilationUnit();
		if (parent2 != null) {
			IType parent1 = proj1.findType(parent2.getFullyQualifiedName());
			IType type1 = parent1.createType(type2.getSource(), null, true,
					null);
			IMethod[] methods = type1.getMethods();
			IField[] fields = type1.getFields();
			for (IMethod m : methods)
				m.delete(true, null);
			for (IField f : fields) {
				f.delete(true, null);
			}
			return type1;
		}
		IPath path1 = getPath(unit2.getPath());
		System.out.println(path1.isAbsolute() + ""
				+ unit2.getPath().makeRelative());

		IJavaElement elem1 = proj1.findElement(getPath(unit2.getPath()).makeRelative());
		if (elem1 != null) {
			ICompilationUnit unit1 = (ICompilationUnit) elem1;
			IType type1 = unit1.createType(type2.getSource(), null, true, null);
			IMethod[] methods = type1.getMethods();
			IField[] fields = type1.getFields();
			for (IMethod m : methods)
				m.delete(true, null);
			for (IField f : fields) {
				f.delete(true, null);
			}
			return type1;
		}

		IPackageFragment pkg2 = (IPackageFragment) unit2.getParent();
		IPackageFragment pkg1 = proj1.findPackageFragment(getPath(pkg2
				.getPath()));
		if (pkg1 != null) {
			ICompilationUnit unit1 = pkg1.createCompilationUnit(
					unit2.getElementName(), unit2.getSource(), true, null);
			IType[] types = unit1.getTypes();
			for (IType type : types) {
				if (!type.getElementName().equals(type2.getElementName()))
					type.delete(true, null);
			}
			IType type1 = unit1.getType(type2.getElementName());
			IMethod[] methods = type1.getMethods();
			IField[] fields = type1.getFields();
			for (IMethod m : methods)
				m.delete(true, null);
			for (IField f : fields) {
				f.delete(true, null);
			}
			return type1;
		}

		IPackageFragmentRoot pr2 = (IPackageFragmentRoot) pkg2.getParent();
		IPackageFragmentRoot pr1 = proj1.findPackageFragmentRoot(getPath(pr2
				.getPath()));
		if (pr1 == null) {
			IFolder folder = proj1.getResource().getProject()
					.getFolder(pr2.getElementName());
			if(!folder.exists())
			folder.create(true, true, null);
			pr1 = proj1.getPackageFragmentRoot(folder);
			
			IPackageFragmentRoot root = proj1.getPackageFragmentRoot(folder);
			IClasspathEntry[] oldEntries = proj1.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
			proj1.setRawClasspath(newEntries, null);

		}
		pkg1 = pr1.createPackageFragment(pkg2.getElementName(), true, null);
		ICompilationUnit unit1 = pkg1.createCompilationUnit(
				unit2.getElementName(), unit2.getSource(), true, null);
		IType[] types = unit1.getTypes();
		for (IType type : types) {
			if (!type.getElementName().equals(type2.getElementName()))
				type.delete(true, null);
		}
		IType type1 = unit1.getType(type2.getElementName());
		IMethod[] methods = type1.getMethods();
		IField[] fields = type1.getFields();
		for (IMethod m : methods)
			m.delete(true, null);
		for (IField f : fields) {
			f.delete(true, null);
		}
		return type1;
	}

	public static IPath getPath(IPath path2) {
		IPath proj1 = GlobalData.proj1.getPath();
		IPath path1 = proj1.append(path2.removeFirstSegments(1));
		return path1;
	}
}
