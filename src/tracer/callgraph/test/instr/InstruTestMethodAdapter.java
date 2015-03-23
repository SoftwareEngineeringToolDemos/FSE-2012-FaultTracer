package tracer.callgraph.test.instr;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import tracer.Configuration;
import tracer.io.ToFile;

@Deprecated
public class InstruTestMethodAdapter extends MethodAdapter {

	String name;

	public InstruTestMethodAdapter(MethodVisitor m, String n) {
		super(m);
		name = n;
	}

	@Override
	public void visitCode() {
		System.out.println("testname:" + name);
		mv.visitCode();
		mv.visitLdcInsn("<TEST>" + name);
		mv.visitLdcInsn(ToFile.path);
		mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "write",
				"(Ljava/lang/String;Ljava/lang/String;)V");

	}

	@Override
	public void visitVarInsn(int opcode, int var) {

		mv.visitVarInsn(opcode, var);
	}

//	@Override
//	public void visitMethodInsn(int opcode, String owner, String name,
//			String desc) {
//
//		if(Configuration.traceCallRelation){
//		String toWrite = this.name + "-" + owner + ":" + name + ":" + desc;
//		mv.visitLdcInsn(toWrite);
//		mv.visitLdcInsn(ToFile.path);
//		mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "write",
//				"(Ljava/lang/String;Ljava/lang/String;)V");
//		}
//		mv.visitMethodInsn(opcode, owner, name, desc);
//	}

//	@Override
//	public void visitFieldInsn(int opcode, String owner, String name,
//			String desc) {
//		if (!name.contains("$")&&Configuration.traceField) {
//			String toWrite = "";
//			if (opcode == Opcodes.PUTFIELD || opcode == Opcodes.PUTSTATIC)
//				toWrite = "<FW>" + this.name + "-" + owner + ":" + name + ":"
//						+ desc;
//			else
//				toWrite = "<FR>" + this.name + "-" + owner + ":" + name + ":"
//						+ desc;
//
//			mv.visitLdcInsn(toWrite);
//			mv.visitLdcInsn(ToFile.path);
//			mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "write",
//					"(Ljava/lang/String;Ljava/lang/String;)V");
//		}
//		mv.visitFieldInsn(opcode, owner, name, desc);
//	}

	@Override
	public void visitInsn(int opcode) {
		if ((opcode >= IRETURN && opcode <= RETURN)) {
			String toWrite = "<TESTPASS>";
			mv.visitLdcInsn(toWrite);
			mv.visitLdcInsn(ToFile.path);
			mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "write",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
		mv.visitInsn(opcode);
	}

	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(maxStack + 4, maxLocals);
	}

}
