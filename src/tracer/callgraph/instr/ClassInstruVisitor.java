package tracer.callgraph.instr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import tracer.io.ToFile;

@Deprecated
public class ClassInstruVisitor implements IVisitor {

	@Override
	public void visit(IJavaProject proj) {

		try {
			IPackageFragmentRoot[] roots = proj.getAllPackageFragmentRoots();
			for (IPackageFragmentRoot root : roots) {
				// System.out.println(root.getPath());
				if (root.getPath().segment(0).equals(proj.getElementName())) {
					this.visit(root);

				}
			}

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void visit(IPackageFragmentRoot root) {
		// System.out.println("root:" + root.getElementName());
		try {
			IJavaElement[] children = root.getChildren();
			for (IJavaElement elem : children) {
				if (elem instanceof IPackageFragment)
					this.visit((IPackageFragment) elem);
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
				this.visit(unit);
			}

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void visit(ICompilationUnit comUnit) {

		try {
			String proj = Platform.getLocation().toString();
			String outPath = comUnit.getJavaProject().getOutputLocation()
					.toString();

			ToFile.path = proj + "/"+comUnit.getJavaProject().getElementName()+"/FaultTracer_log";
			ToFile.path=ToFile.path.replace("/", "\\");
			//System.out.println("LogPath:" + ToFile.path);
			
			String pkgPath = proj + outPath + "/"
					+ comUnit.getParent().getElementName().replace(".", "/");
			pkgPath = pkgPath.replace("/", "\\");
			//System.out.println(pkgPath);
			String name = comUnit.getElementName().replace(".java", "");
			File file = new File(pkgPath);

			File[] cfiles = file.listFiles();

			for (File cfile : cfiles) {
				if (cfile.getName().startsWith(name + ".class")
						|| cfile.getName().startsWith(name + "$")) {
					visit(cfile);
				}
			}

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void visit(File file) {
		//System.out.println("class:" + file.getPath());
		try {
			FileInputStream in = new FileInputStream(file);
			byte[] b = new byte[(int) file.length()];
			in.read(b);
			in.close();
			ClassReader cr = new ClassReader(b);
			ClassWriter cw = new ClassWriter(0);
			InstruClassAdapter ia = new InstruClassAdapter(cw);
			cr.accept(ia, 0);

			FileOutputStream writer;

			writer = new FileOutputStream(file);

			writer.write(cw.toByteArray());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
