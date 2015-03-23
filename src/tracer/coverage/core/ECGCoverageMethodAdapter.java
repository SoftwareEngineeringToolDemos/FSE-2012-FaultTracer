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

public class ECGCoverageMethodAdapter extends MethodAdapter {
	Logger logger = Logger.getLogger(ECGCoverageMethodAdapter.class);
	String name;
	int access;

	public ECGCoverageMethodAdapter(MethodVisitor m, int access, String n) {
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
			if (!name.contains("<init>")
					&& (access == 0 || access == ACC_PUBLIC
							|| access == ACC_PRIVATE || access == ACC_PROTECTED)) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object",
						"getClass", "()Ljava/lang/Class;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class",
						"toString", "()Ljava/lang/String;");
			} else {
				mv.visitLdcInsn("");
			}
			mv.visitLdcInsn(name);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					Properties.TRACER_CLASS_NAME, "logMethodInfo",
					"(Ljava/lang/String;Ljava/lang/String;)V");

			if (Properties.TRACE_CALL_REL) {
				mv.visitMethodInsn(Opcodes.INVOKESTATIC,
						Properties.TRACER_CLASS_NAME, "getInstance", "()L"
								+ Properties.TRACER_CLASS_NAME + ";");
				mv.visitMethodInsn(Opcodes.INVOKESTATIC,
						Properties.TRACER_CLASS_NAME, "stack_top",
						"()Ljava/lang/String;");
				mv.visitLdcInsn(name);
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
						Properties.TRACER_CLASS_NAME, "logCallRelationInfo",
						"(Ljava/lang/String;Ljava/lang/String;)V");

				mv.visitLdcInsn(name);
				mv.visitMethodInsn(Opcodes.INVOKESTATIC,
						Properties.TRACER_CLASS_NAME, "stack_push",
						"(Ljava/lang/String;)V");
			}
		}
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {

		/*
		 * if (Properties.TRACE_CALL_REL) {
		 * 
		 * }
		 */
		mv.visitMethodInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
		if (Properties.TRACE_BYTECODE && !name.contains("$")
				&& Properties.INSTRUMENT_FIELD) {
			String field = "";
			if (opcode == Opcodes.PUTFIELD || opcode == Opcodes.PUTSTATIC)
				field = "<FW>" + this.name + "-" + owner + ":" + name + ":"
						+ desc;
			else
				field = "<FR>" + this.name + "-" + owner + ":" + name + ":"
						+ desc;
			mv.visitMethodInsn(Opcodes.INVOKESTATIC,
					Properties.TRACER_CLASS_NAME, "getInstance", "()L"
							+ Properties.TRACER_CLASS_NAME + ";");
			mv.visitLdcInsn("");
			mv.visitLdcInsn(field);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					Properties.TRACER_CLASS_NAME, "logMethodInfo",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
		mv.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitInsn(int opcode) {
		if (Properties.TRACE_BYTECODE&&Properties.TRACE_CALL_REL) {
			if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
				this.visitMethodInsn(Opcodes.INVOKESTATIC,
						Properties.TRACER_CLASS_NAME, "stack_pop", "()V");
			}
		}
		mv.visitInsn(opcode);
	}

	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(maxStack + 4, maxLocals);
	}

}
