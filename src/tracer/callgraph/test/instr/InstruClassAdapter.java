package tracer.callgraph.test.instr;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

@Deprecated
public class InstruClassAdapter extends ClassAdapter {
	String name;

	public InstruClassAdapter(ClassVisitor cv) {
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
//		 if (name.startsWith("test") || name.startsWith("compare")
//		 || name.startsWith("check"))
if (name.startsWith("test"))
			return new InstruTestMethodAdapter(mv, this.name + ":" + name + ":"
					+ desc);
		else
			return mv;
//			return new InstruMethodAdapter(mv, access, this.name + ":" + name
//					+ ":" + desc);
	}

}
