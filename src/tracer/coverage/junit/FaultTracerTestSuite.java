package tracer.coverage.junit;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import tracer.coverage.core.Properties;

public class FaultTracerTestSuite extends TestSuite {

	private static Logger logger = Logger.getLogger(FaultTracerTestSuite.class);

	/**
	 * Creates a new FaultTracerTestSuite with the given name.
	 * 
	 * @param name
	 *            the name of the test suite
	 * 
	 */
	public FaultTracerTestSuite(String name) {
		super(name);
	}

	/**
	 * Creates a new FaultTracerTestSuite with no name.
	 */
	public FaultTracerTestSuite() {
		super();
	}

	/**
	 * Delegates the control to {@link JunitTestDriver}.
	 * 
	 * @see junit.framework.TestSuite#run(junit.framework.TestResult)
	 * 
	 */
	@Override
	public void run(TestResult result) {
		logger.debug("Running faulttracer test suite");
		System.out.println(">>>>>>>>>run faulttracer");

		if (Properties.JUNIT4) {
			JUnit4TestDriver driver = new JUnit4TestDriver(Properties.TEST_SUITE);
			driver.run();
		} else {
			JUnitTestDriver driver = new JUnitTestDriver(this);
			driver.run(result);
		}
	}

	/**
	 * Transforms a {@link TestSuite} to a FaultTracerTestSuite. This method is
	 * called by instrumented code to insert this class instead of the
	 * TestSuite.
	 * 
	 * @param testSuite
	 *            The original TestSuite.
	 * @return The FaultTracerTestSuite that contains the given TestSuite.
	 */
	public static FaultTracerTestSuite toFaultTracerTestSuite(
			TestSuite testSuite) {
		logger.info("Transforming TestSuite to enable FaultTracer coverage collection");
		FaultTracerTestSuite returnTestSuite = new FaultTracerTestSuite(
				testSuite.getName());
		returnTestSuite.addTest(testSuite);
		return returnTestSuite;
	}

}
