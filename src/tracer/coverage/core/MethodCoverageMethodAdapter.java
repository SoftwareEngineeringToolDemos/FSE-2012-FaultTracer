package tracer.coverage.core;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.ATHROW;

import org.apache.log4j.Logger;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodCoverageMethodAdapter extends MethodAdapter {
	Logger logger = Logger.getLogger(MethodCoverageMethodAdapter.class);
	String name;
	int access;

	public MethodCoverageMethodAdapter(MethodVisitor m, int access, String n) {
		super(m);
		name = n;
		this.access = access;
	}

	@Override
	public void visitCode() {
		logger.debug("name:" + name);
		mv.visitCode();
		if (Properties.TRACE_BYTECODE) {
			this.visitMethodInsn(Opcodes.INVOKESTATIC,
					Properties.TRACER_CLASS_NAME, "getInstance", "()L"
							+ Properties.TRACER_CLASS_NAME + ";");
			mv.visitLdcInsn("");
			mv.visitLdcInsn(name);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					Properties.TRACER_CLASS_NAME, "logMethodInfo",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
	}

	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(maxStack + 4, maxLocals);
	}

}
