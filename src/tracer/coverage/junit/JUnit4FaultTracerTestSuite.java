package tracer.coverage.junit;

import junit.framework.TestResult;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import tracer.coverage.core.Properties;

@RunWith(JUnit4FaultTracerTestSuite.class)
public class JUnit4FaultTracerTestSuite extends Runner {

	private Runner r;

	public JUnit4FaultTracerTestSuite(Class<?> c) {
	}

	@Test
	public void testMethod() {
		System.out.println("FaultTracerTestSuite.testMethod()");
	}

	@Override
	public Description getDescription() {
		return getRunner().getDescription();
	}

	private Runner getRunner() {
		if (r == null) {
			try {
				r = JUnit4TestDriver.getRunner(Properties.TEST_SUITE);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InitializationError e) {
				e.printStackTrace();
			}
		}
		return r;
	}

	@Override
	public void run(RunNotifier notifier) {
		if (Properties.JUNIT4) {
			JUnit4TestDriver driver = new JUnit4TestDriver(Properties.TEST_SUITE);
			driver.run();
		} else {
			TestResult result=new TestResult();
			JUnitTestDriver driver = new JUnitTestDriver(Properties.TEST_SUITE);
			driver.run(result);
		}
	}

}
