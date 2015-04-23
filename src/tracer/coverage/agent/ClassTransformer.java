package tracer.coverage.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import tracer.coverage.core.CoverageClassAdapter;
import tracer.coverage.core.Properties;
import tracer.coverage.junit.*;

public class ClassTransformer implements ClassFileTransformer {

	private static Logger logger = Logger.getLogger(ClassTransformer.class);

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		//System.out.println("ClassName: "+className+" "+MyAgent.excluded.size());
		try {
			if (className == null) {
				return classfileBuffer;
			}
			if (loader != ClassLoader.getSystemClassLoader()) {
				return classfileBuffer;
			}

			String classNameWithDots = className.replace('/', '.');
			
			// Do not transfer JUnit test suite anymore, now directly run the JUnit4FaultTracerTestSuite
			if (false&&compareWithSuiteProperty(classNameWithDots)) {
				logger.debug("transformer for test suite class: " + className);
				byte[] result = classfileBuffer;
				ClassReader reader = new ClassReader(classfileBuffer);
				ClassWriter writer = new ClassWriter(
						org.objectweb.asm.ClassWriter.COMPUTE_MAXS);
				ClassVisitor cv = writer;
				cv = new TransformTestSuiteClassAdapter(cv,
						"tracer/coverage/junit/FaultTracerTestSuite",
						"toFaultTracerTestSuite",
						"(Ljunit/framework/TestSuite;)Ltracer/coverage/junit/FaultTracerTestSuite;");
				reader.accept(cv, ClassReader.SKIP_FRAMES);
				result = writer.toByteArray();
				return result;
			}

			// whitelist - only trace packages of that domain

			if (!className.startsWith(Properties.PROJECT_PREFIX.replace('.',
					'/'))) {
				return classfileBuffer;
			}

			// exclude test classes
			if (MyAgent.excluded.contains(classNameWithDots)) {
				return classfileBuffer;
			}

			logger.debug("transformer for class: " + className);

			byte[] result = classfileBuffer;
			ClassReader reader = new ClassReader(classfileBuffer);
			ClassWriter writer = new ClassWriter(
					org.objectweb.asm.ClassWriter.COMPUTE_MAXS);
			ClassVisitor cv = writer;

			cv = new CoverageClassAdapter(cv);
			reader.accept(cv, ClassReader.SKIP_FRAMES);
			result = writer.toByteArray();
			return result;

		} catch (Throwable t) {
			t.printStackTrace();
			String message = "Exception thrown during instrumentation";
			logger.error(message, t);
			System.err.println(message);
			System.exit(1);
		}
		throw new RuntimeException("Should not be reached");
	}

	private static boolean compareWithSuiteProperty(String classNameWithDots) {
		boolean result = false;
		String testSuiteName = Properties.TEST_SUITE;
		if (testSuiteName != null && classNameWithDots.contains(testSuiteName)) {
			result = true;
		}
		return result;
	}

}
