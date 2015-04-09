package tracer.coverage.core;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import tracer.coverage.junit.TestDetector;

public class CoverageClassAdapter extends ClassAdapter {
	String name;

	public CoverageClassAdapter(ClassVisitor cv) {
		super(cv);
	}

	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName,
			final String[] interfaces) {
		this.name = name;
		cv.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
				exceptions);
		// if(TestDetector.isTestMethod(name))
		// return new CoverageTestMethodAdapter(mv, access,this.name+":"+name +
		// ":" + desc);
		if (Properties.METHOD_COV)
			return new MethodCoverageMethodAdapter(mv, access, this.name + ":"
					+ name + ":" + desc);
		else if (Properties.STATEMENT_COV)
			return new StatementCoverageMethodAdapter(mv, access, this.name
					+ ":" + name + ":" + desc);
		else if (Properties.BRANCH_COV)
			return new BranchCoverageMethodAdapter(mv, access, this.name + ":"
					+ name + ":" + desc);
		return new ECGCoverageMethodAdapter(mv, access, this.name + ":" + name
				+ ":" + desc);
	}

}
