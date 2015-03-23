package tracer.callgraph.instr;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import tracer.Configuration;
import tracer.io.ToFile;

@Deprecated
public class InstruMethodAdapter extends MethodAdapter {

	String name;
	int access;

	public InstruMethodAdapter(MethodVisitor m, int access, String n) {
		super(m);
		name = n;
		this.access = access;
	}

	@Override
	public void visitCode() {
		System.out.println("name:" + name);
		mv.visitCode();

		if (!name.contains("<init>")
				&& (access == 0 || access == ACC_PUBLIC
						|| access == ACC_PRIVATE || access == ACC_PROTECTED)) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass",
					"()Ljava/lang/Class;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "toString",
					"()Ljava/lang/String;");
			mv.visitLdcInsn(ToFile.path);
			mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "writeTag",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
		mv.visitLdcInsn(name);
		mv.visitLdcInsn(ToFile.path);
		mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "write",
				"(Ljava/lang/String;Ljava/lang/String;)V");
	}

	@Override
	public void visitVarInsn(int opcode, int var) {

		mv.visitVarInsn(opcode, var);

	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		if(Configuration.traceCallRelation){
		String toWrite = this.name + "-" + owner + ":" + name + ":" + desc;
		mv.visitLdcInsn(toWrite);
		mv.visitLdcInsn(ToFile.path);
		mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "write",
				"(Ljava/lang/String;Ljava/lang/String;)V");
		}
		mv.visitMethodInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
		if (!name.contains("$")&&Configuration.traceField) {
			String toWrite = "";
			if (opcode == Opcodes.PUTFIELD || opcode == Opcodes.PUTSTATIC)
				toWrite = "<FW>" + this.name + "-" + owner + ":" + name + ":"
						+ desc;
			else
				toWrite = "<FR>" + this.name + "-" + owner + ":" + name + ":"
						+ desc;

			mv.visitLdcInsn(toWrite);
			mv.visitLdcInsn(ToFile.path);
			mv.visitMethodInsn(INVOKESTATIC, "tracer/io/ToFile", "write",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
		mv.visitFieldInsn(opcode, owner, name, desc);
	}

	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(maxStack + 4, maxLocals);
	}

}
