package tracer.coverage.junit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.internal.runners.SuiteMethod;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import tracer.coverage.core.ECGCoverageListener;
import tracer.coverage.core.Listener;
import tracer.coverage.core.Properties;

public class JUnit4TestDriver {

	static Logger logger = Logger.getLogger(JUnit4TestDriver.class);
	LinkedList<Listener> listeners = new LinkedList<Listener>();
	private Map<String, Description> allTests;
	public static boolean pass = true;
	private Runner masterRunner;
	RunNotifier notifier = new RunNotifier();
	public String testSuite;

	public JUnit4TestDriver(String suite) {
		testSuite = suite;

		System.out.println(">>" + testSuite);
		masterRunner = null;
		Throwable t = null;
		try {
			masterRunner = getRunner(testSuite);
		} catch (ClassNotFoundException e) {
			t = e;
		} catch (InitializationError e) {
			t = e;
		} finally {
			if (t != null) {
				String message = "Could not initialize tests suite for:"
						+ suite;
				logger.warn(message, t);
				throw new RuntimeException(message, t);
			}
		}
		allTests = getTests(masterRunner);
	}

	public final void run() {
		listeners.add(new ECGCoverageListener());
		try {
			runInstrumentedTests();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, Description> getTests(Runner r) {
		Map<String, Description> testMap = new HashMap<String, Description>();
		List<Description> descs = new ArrayList<Description>();
		Description description = r.getDescription();
		logger.debug(description);
		descs.add(description);
		while (descs.size() > 0) {
			Description d = descs.remove(0);
			ArrayList<Description> children = d.getChildren();
			if (children != null && children.size() > 0) {
				descs.addAll(children);
			} else {
				String testName = getTestName(d);
				String insertTestName = testName;
				int count = 0;
				while (testMap.containsKey(insertTestName)) {
					count++;
					insertTestName = testName + "-instance-" + count;
				}
				logger.debug("Got test case: " + insertTestName + " Desc: " + d);
				testMap.put(insertTestName, d);
			}

		}
		return testMap;
	}

	private static String getTestName(Description d) {
		return d.getClassName() + "." + d.getMethodName();
	}

	protected List<String> getAllTests() {
		return new ArrayList<String>(allTests.keySet());
	}

	private void runInstrumentedTests() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("time.log"));
		logger.info("Running tests of project " + Properties.PROJECT_PREFIX);
		// addMutationTestListener(new AdabuListener());
		List<String> allTests = getAllTests();
		testsStart();
		int passed = 0, failed = 0, error = 0;
		// System.out.println("************************************************************");
		for (String testName : allTests) {
			pass = true;
			TestRunListener listener = new TestRunListener();

			testStart(testName);
			Description test = this.allTests.get(testName);
			long start = System.currentTimeMillis();
			runWithOutTimeout(test, listener);
			long end = System.currentTimeMillis();
			long timecost = end - start;
			pass = listener.getErrors().size() == 0
					&& listener.getFailures().size() == 0;
			testEnd(testName);
			if (listener.getErrors().size() != 0)
				error++;
			if (listener.getFailures().size() != 0)
				failed++;
			if (pass) {
				System.out.println("[Passed Test: " + timecost + "ms] "
						+ testName);
				writer.write("[Passed Test: " + timecost + "ms] " + testName
						+ "\n");

				passed++;
			} else {
				System.out.println("[Failed Test: " + timecost + "ms] "
						+ testName);
				writer.write("[Failed Test: " + timecost + "ms] " + testName
						+ "\n");

			}
		}
		System.out
				.println("************************************************************");
		System.out.println("FaultTracer tests run: " + (passed + failed)
				+ ", Failures: " + failed + ", Errors: " + error);
		System.out
				.println("************************************************************");
		testsEnd();
		writer.flush();
		writer.close();
	}

	protected long runWithOutTimeout(Description t, RunListener listener) {
		logger.debug("Start  test: " + t.toString());
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Runner r = null;
		try {
			r = getRunner(testSuite);
			Filter f = Filter.matchMethodDescription(t);
			((Filterable) r).filter(f);
			logger.debug(">>>Runner: " + r.toString() + "-" + r.testCount());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InitializationError e) {
			e.printStackTrace();
		} catch (NoTestsRemainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RunNotifier notifier = new RunNotifier();
		notifier.addListener(listener);
		r.run(notifier);

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
	private void testEnd(String testName) {
		for (Listener listener : listeners) {
			listener.testEnd(testName);
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

	public static Runner getRunner(String testSuite)
			throws ClassNotFoundException, InitializationError {
		Class<?> forName = null;
		Runner r = null;

		if (testSuite.contains(":")) {
			logger.debug("Using getClassesRunner to get Runner.");
			r = getClassesRunner(testSuite);
		} else {
			logger.info("Getting test suite for name: " + testSuite);
			forName = Class.forName(testSuite);
			try {
				Method suite = getSuiteMethod(forName);
				if (suite != null) {
					logger.debug("Using SuiteMethod to get Runner.");
					r = new SuiteMethod(forName);
				} else {
					logger.debug("Using AllDefaultPossibilitiesBuilder to get Runner");
					r = new AllDefaultPossibilitiesBuilder(true)
							.runnerForClass(forName);
				}
				if (r == null) {
					logger.debug("Using Suite to get Runner");
					r = new Suite(forName, new AllDefaultPossibilitiesBuilder(
							true));
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return r;
	}

	/*
	 * private static void runTest(final Description desc, RunListener
	 * runListener, Runner masterRunner, String testSuite) { try { // StopWatch
	 * stp = new StopWatch(); // stp.start(); Runner r = getRunner(testSuite);
	 * // long time1 = stp.getTime(); // logger.info("Time to get runner: " +
	 * time1); RunNotifier notifier = new RunNotifier();
	 * notifier.addListener(runListener); r.run(notifier);
	 * 
	 * } catch (ClassNotFoundException e) { throw new RuntimeException(e); }
	 * catch (InitializationError e) { throw new RuntimeException(e); } }
	 */

	public static Method getSuiteMethod(Class<?> forName) {
		Method[] methods = forName.getMethods();
		for (Method method : methods) {
			if (method.getName().equals("suite")
					&& method.getParameterTypes().length == 0) {
				return method;
			}
		}
		return null;
	}

	private static Runner getClassesRunner(String testClasses)
			throws ClassNotFoundException, InitializationError {
		Runner r;
		String[] split = testClasses.split(":");
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String className : split) {
			try {
				Class<?> clazz = Class.forName(className);
				classes.add(clazz);
			} catch (ClassNotFoundException e) {
				if (className.trim().length() == 0) {
					throw new RuntimeException("Classname with length 0.", e);
				}
				throw new RuntimeException("Class not found: " + className, e);
			} catch (VerifyError e) {
				throw new RuntimeException("Verrify error for " + className, e);
			}
		}
		r = new Suite(new AllDefaultPossibilitiesBuilder(true),
				classes.toArray(new Class[0]));
		return r;
	}

	/*
	 * private static Runner getMethodsRunner(String testMethods) throws
	 * ClassNotFoundException, InitializationError { String[] testMethodsSplit =
	 * testMethods.split(":"); final HashMap<String, String> methods =
	 * getMethodMap(testMethodsSplit); RunnerBuilder runnerBuilder = new
	 * RunnerBuilder() {
	 * 
	 * @Override public Runner runnerForClass(Class<?> testClass) throws
	 * Throwable { Request aClass = Request.aClass(testClass); final
	 * Collection<String> methodNames = methods.get(testClass .getName());
	 * Request filtered = aClass.filterWith(new Filter() {
	 * 
	 * @Override public String describe() { return "Javalanche test filter"; }
	 * 
	 * @Override public boolean shouldRun(Description description) { String name
	 * = description.getClassName() + "." + description.getMethodName();
	 * logger.debug("Testname: " + name); boolean var =
	 * methodNames.contains(name); return var; } }); return
	 * filtered.getRunner(); } }; Class<?>[] classes = getClasses(methods);
	 * return new Suite(runnerBuilder, classes); }
	 */

	private static Class<?>[] getClasses(final HashMap<String, String> methods)
			throws ClassNotFoundException {
		Set<String> keySet = methods.keySet();
		List<Class<?>> classes = new ArrayList<Class<?>>();

		for (String className : keySet) {
			classes.add(Class.forName(className));
		}
		return classes.toArray(new Class<?>[0]);
	}

	private static HashMap<String, String> getMethodMap(String[] testMethods) {
		final HashMap<String, String> methods = new HashMap<String, String>();
		for (String testMethod : testMethods) {
			if (!testMethod.trim().isEmpty()) {
				String testClass = getTestClass(testMethod);
				methods.put(testClass, testMethod);
			}
		}
		return methods;
	}

	static String getTestClass(String testMethod) {
		int lastIndexOf = testMethod.lastIndexOf('.');
		if (lastIndexOf < 0) {
			throw new RuntimeException("Did not find class name for test: "
					+ testMethod);
		}
		return testMethod.substring(0, lastIndexOf);
	}

	private static class TestRunListener extends RunListener {

		List<Failure> failures = new ArrayList<Failure>();
		List<Failure> errors = new ArrayList<Failure>();

		String message = null;

		@Override
		public void testFailure(Failure failure) throws Exception {
			// if(message != null){
			// logger.warn("Message exists " + message);
			// }
			Throwable e = failure.getException();
			logger.debug("Adding failure: " + failure
					+ "Exception of failure: " + failure.getException());
			// Junit4 does distinguish between failures and errors. Thus, the
			// type of the exception is checked.
			if (e instanceof AssertionError) {
				failures.add(failure);
			} else {
				errors.add(failure);
			}
			if (failure != null) {
				message = failure.getMessage();
				logger.debug("Setting failure message: " + message);

			}
		}

		public List<Failure> getFailures() {
			return failures;
		}

		public void addFailure(Description desc, Throwable t) {
			try {
				testFailure(new Failure(desc, t));
			} catch (Exception e) {
				throw new RuntimeException("Could not add test failure: " + e,
						e);
			}
		}

		public List<Failure> getErrors() {
			return errors;
		}

		public String getMessage() {
			return message;
		}

	}
}
