package tracer.coverage.junit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import tracer.coverage.core.ECGCoverageListener;
import tracer.coverage.core.Listener;
import tracer.coverage.core.Properties;
import tracer.test.prioritization.io.PrioritizationIOUtils;
import tracer.test.reduce.io.ReductionIOUtils;
import tracer.test.selection.io.SelectionIOUtils;

public class JUnitTestDriver {

	Logger logger = Logger.getLogger(JUnitTestDriver.class);
	LinkedList<Listener> listeners = new LinkedList<Listener>();
	private Map<String, Test> allTests;
	TestResult result;
	public static boolean pass = true;
	public static List<String> selectedTests;
	public static List<String> prioritizedTests;
	public static Map<String, Set<String>> reducedTests;

	// private List<SingleTestResult> resultsForMutation = new
	// ArrayList<SingleTestResult>();
	public JUnitTestDriver(TestSuite suite) {
		selectedTests = SelectionIOUtils.loadSelectedTests();
		// this.suite = suite;
		allTests = TestDetector.getAllTests(suite);

		Set<String> to_remove = new HashSet<String>();
		to_remove.addAll(allTests.keySet());
		if (selectedTests != null)
			to_remove.removeAll(selectedTests);
		if (Properties.RUN_SELECTED)
			for (String testName : to_remove) {
				allTests.remove(testName);
			}
	}

	public JUnitTestDriver(String testSuiteName) {
		// selectedTests.clear();
		// prioritizedTests.clear();
		// reducedTests.clear();
		// this.suite = suite;

		TestSuite suite = null;
		try {
			Class<?> suiteClass = Class.forName(testSuiteName);
			Method suiteMethod = JUnit4TestDriver.getSuiteMethod(suiteClass);
			suite = (TestSuite) suiteMethod.invoke(null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		allTests = TestDetector.getAllTests(suite);
		Set<String> to_remove = new HashSet<String>();
		to_remove.addAll(allTests.keySet());

		if (Properties.RUN_SELECTED) {
			selectedTests = SelectionIOUtils.loadSelectedTests();
			if (selectedTests != null) {
				to_remove.removeAll(selectedTests);

				for (String testName : to_remove) {
					allTests.remove(testName);
				}
			}
		} else if (Properties.RUN_PRIORITIZED) {
			prioritizedTests = PrioritizationIOUtils.loadPrioritizedTests();
			if (prioritizedTests != null) {
				to_remove.removeAll(prioritizedTests);

				for (String testName : to_remove) {
					allTests.remove(testName);
				}
			}
		} else if (Properties.RUN_REDUCED) {
			reducedTests = ReductionIOUtils.loadReducedTests();
			// System.out.println("Redundant test list");
			System.out
					.println("************************************************************");
			for (String key : reducedTests.keySet()) {
				Set<String> set = reducedTests.get(key);
				for (String s : set) {
					System.out.println("[Redundant Test] " + s
							+ " (redundant with " + key + ")");
				}
			}
			// System.out.println("************************************************************");
			if (reducedTests != null) {
				to_remove.removeAll(reducedTests.keySet());
				for (String testName : to_remove) {
					allTests.remove(testName);
				}
			}
		}
	}

	public final void run(TestResult result) {
		listeners.add(new ECGCoverageListener());
		try {
			runInstrumentedTests(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected List<String> getAllTests() {
		if (Properties.RUN_PRIORITIZED && prioritizedTests != null)
			return Collections.unmodifiableList(prioritizedTests);
		else
			return Collections.unmodifiableList(new ArrayList<String>(allTests
					.keySet()));
	}

	private void runInstrumentedTests(TestResult result) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("time.log"));
		// TestResult result = new TestResult();
		logger.info("Running tests of project " + Properties.PROJECT_PREFIX);
		// addMutationTestListener(new AdabuListener());
		this.result = result;
		List<String> allTests = getAllTests();
		TestResultListener listener = new TestResultListener();
		result.addListener(listener);
		testsStart();
		int passed = 0, failed = 0, error = 0;
		int test_id = 1;
		Map<Integer, String> test_mapping = new HashMap<Integer, String>();
	// System.out.println("************************************************************");
		for (String testName : allTests) {
			pass = true;
			test_mapping.put(test_id, testName);
			testStart(testName);
			Test test = this.allTests.get(testName);
			long start = System.currentTimeMillis();
			runWithOutTimeout(test, result);
			long end = System.currentTimeMillis();
			long timecost = end - start;
			pass = listener.getResult();
			testEnd(testName, test_id);
			test_id++;
			if (listener.error)
				error++;
			if (listener.fail)
				failed++;
			if (listener.pass)
				passed++;
			if (pass) {
				System.out.println("[Passed Test: " + timecost + "ms] "
						+ testName);
				writer.write("[Passed Test: " + timecost + "ms] " + testName
						+ "\n");

			} else {
				System.out.println("[Failed Test: " + timecost + "ms] "
						+ testName);
				writer.write("[Failed Test: " + timecost + "ms] " + testName
						+ "\n");

			}
			listener.reset();
		}
		System.out
				.println("************************************************************");
		System.out.println("FaultTracer tests run: "
				+ (passed + failed + error) + ", Failures: " + failed
				+ ", Errors: " + error);
		System.out
				.println("************************************************************");
		testsEnd();
		writer.flush();
		writer.close();
		JUnit4TestDriver.serializeTestId(test_mapping);
	}

	protected long runWithOutTimeout(Test t, TestResult result) {
		logger.debug("Start  test: ");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		t.run(result);
		stopWatch.stop();
		logger.debug("End timed test, it took " + stopWatch.getTime() + " ms");
		return stopWatch.getTime();
	}

	/**
	 * Inform all listeners that a test starts.
	 * 
	 * @param testName
	 *            the test that starts
	 */
	private void testEnd(String testName, int test_id) {
		for (Listener listener : listeners) {
			listener.testEnd(testName, test_id);
		}
	}

	/**
	 * Inform all listeners that a test has ended.
	 */
	private void testStart(String testName) {
		for (Listener listener : listeners) {
			listener.testStart(testName);
		}
	}

	/**
	 * Inform all listeners that the test process has started.
	 */
	private void testsStart() {
		for (Listener listener : listeners) {
			listener.start();
		}
	}

	/**
	 * Inform all listeners that the test process has finished.
	 */
	private void testsEnd() {
		for (Listener listener : listeners) {
			listener.end();
		}
		/*
		 * Set<Test> not_passed=new HashSet<Test>(); Enumeration<TestFailure>
		 * fails=result.failures(); while(fails.hasMoreElements()){ TestFailure
		 * fail=fails.nextElement(); Test t =fail.failedTest();
		 * not_passed.add(t); } Enumeration<TestFailure> errors=result.errors();
		 * while(errors.hasMoreElements()){ TestFailure
		 * error=errors.nextElement(); Test t=error.failedTest();
		 * not_passed.add(t); }
		 */
	}

	/*
	 * protected JUnitTestRunnable getTestRunnable(final String testName) {
	 * JUnitTestRunnable r = new JUnitTestRunnable(testName, allTests); return
	 * r; }
	 */

}
