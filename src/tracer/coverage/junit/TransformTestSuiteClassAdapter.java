package tracer.coverage.junit;
  
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TransformTestSuiteClassAdapter extends ClassAdapter {
	Logger logger=Logger.getLogger(TransformTestSuiteClassAdapter.class);
	
	String className;
	
	private String targetClass;

	private String transformMethod;
 
	private String transformMethodSignature;

	public TransformTestSuiteClassAdapter(ClassVisitor cv, String targetClass,
			String transformMethod, String transformSignature) {
		super(cv);
		this.targetClass = targetClass;
		this.transformMethod = transformMethod;
		this.transformMethodSignature = transformSignature;
	}
	
	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName,
			final String[] interfaces) {
		this.className=name;
		cv.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature,
				exceptions);
		//logger.debug("transform test suite temp: "+className+":"+name +":" + desc);
		if (name.equals("suite")) {
			logger.debug("transform test suite: "+className+":"+name +":" + desc);
			mv = new TransformTestSuiteMethodAdapter(mv, targetClass,
					transformMethod, transformMethodSignature);
		}
		return mv;
	}
}