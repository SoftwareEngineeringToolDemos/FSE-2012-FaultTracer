package tracer.coverage.junit;


import static org.objectweb.asm.Opcodes.*;

import org.apache.log4j.Logger;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TransformTestSuiteMethodAdapter extends MethodAdapter {

	private static Logger logger = Logger
			.getLogger(TransformTestSuiteMethodAdapter.class);

	private String targetClass;

	private String transformMethod;

	private String transformMethodSignature;

	public TransformTestSuiteMethodAdapter(MethodVisitor mv,
			String targetClass, String integrationMethod,
			String integrationMethodSignature) {
		super(mv);
		this.targetClass = targetClass;
		this.transformMethod = integrationMethod;
		this.transformMethodSignature = integrationMethodSignature;
	}

	@Override
	public void visitInsn(int opcode) {
		if (opcode == Opcodes.ARETURN) {
			logger.info("Transforming Testsuite+ " + targetClass  + "    "  + transformMethod);
			mv.visitMethodInsn(INVOKESTATIC, targetClass, transformMethod,
					transformMethodSignature);

		}

		mv.visitInsn(opcode);
	}

}
