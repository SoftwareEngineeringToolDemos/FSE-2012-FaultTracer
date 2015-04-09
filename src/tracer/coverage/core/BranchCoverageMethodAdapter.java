package tracer.coverage.core;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BranchCoverageMethodAdapter extends MethodAdapter {
	Logger logger = Logger.getLogger(BranchCoverageMethodAdapter.class);
	String name;
	int access;
	static int branchID = 0;// Global branch id
	static int switchLabelID = 0;// Global switch label id
	static int lineNum = -1;
	static Set<Label> switchLabels = new HashSet<Label>();

	public BranchCoverageMethodAdapter(MethodVisitor m, int access, String n) {
		super(m);
		name = n;
		this.access = access;
	}

	@Override
	public void visitCode() {
		mv.visitCode();
		switchLabels.clear();
	}

	@Override
	public void visitJumpInsn(int type, Label target) {
		if (type == org.objectweb.asm.Opcodes.GOTO) {
			mv.visitJumpInsn(type, target);
			return;
		}
		branchID++;
		if (Properties.TRACE_BYTECODE) {
			this.visitMethodInsn(Opcodes.INVOKESTATIC,
					Properties.TRACER_CLASS_NAME, "getInstance", "()L"
							+ Properties.TRACER_CLASS_NAME + ";");
			mv.visitLdcInsn("false");
			mv.visitLdcInsn(name + ":" + lineNum + ":" + branchID + "");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					Properties.TRACER_CLASS_NAME, "logBranchInfo",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
		mv.visitJumpInsn(type, target);
		if (Properties.TRACE_BYTECODE) {
			this.visitMethodInsn(Opcodes.INVOKESTATIC,
					Properties.TRACER_CLASS_NAME, "getInstance", "()L"
							+ Properties.TRACER_CLASS_NAME + ";");
			mv.visitLdcInsn("true");
			mv.visitLdcInsn(name + ":" + lineNum + ":" + branchID + "");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					Properties.TRACER_CLASS_NAME, "logBranchInfo",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
	}

	@Override
	public void visitTableSwitchInsn(int type, int arg2, Label defaultTarget,
			Label[] larray) {
		for (Label l : larray)
			switchLabels.add(l);
		switchLabels.remove(defaultTarget);
		mv.visitTableSwitchInsn(type, arg2, defaultTarget, larray);

	}

	@Override
	public void visitLookupSwitchInsn(Label defaultTarget, int[] iarray,
			Label[] larray) {
		for (Label l : larray)
			switchLabels.add(l);
		switchLabels.remove(defaultTarget);
		mv.visitLookupSwitchInsn(defaultTarget, iarray, larray);
	}

	@Override
	public void visitLabel(Label l) {
		mv.visitLabel(l);
		if (switchLabels.contains(l) && Properties.TRACE_BYTECODE) {
			switchLabelID++;
			this.visitMethodInsn(Opcodes.INVOKESTATIC,
					Properties.TRACER_CLASS_NAME, "getInstance", "()L"
							+ Properties.TRACER_CLASS_NAME + ";");
			mv.visitLdcInsn("");
			mv.visitLdcInsn(name + ":" + (lineNum + 1) + ":" + switchLabelID);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
					Properties.TRACER_CLASS_NAME, "logMethodInfo",
					"(Ljava/lang/String;Ljava/lang/String;)V");
		}
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		mv.visitLineNumber(line, start);
		lineNum = line;
	}

	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(maxStack + 4, maxLocals);
	}

}
