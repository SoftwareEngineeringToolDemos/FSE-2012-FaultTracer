package tracer.coverage.junit;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestResult;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import tracer.coverage.junit.SingleTestResult.TestOutcome;

@Deprecated
public class JUnitTestRunnable implements Runnable{
	
	Logger logger=Logger.getLogger(JUnitTestRunnable.class);
	boolean finished = false;

	TestResult result = new TestResult();

	private JUnitTestListener listener = new JUnitTestListener();

	private boolean failed = false;

	private StopWatch stopWatch = new StopWatch();

	private String testName;
	
	private Map<String, Test> allTests;
	
	public JUnitTestRunnable(String testName, TestResult result, Map<String, Test> allTests){
		this.testName=testName;
		this.allTests=allTests;
		this.result=result;
	}
	
	public void run() {
		try {
			stopWatch.start();
			Test actualtest = allTests.get(testName);
			if (actualtest == null) {
				String message = "Test not found in: " + testName
						+ "\n All Tests:" + allTests;
				logger.warn(message);
				System.exit(0);
			}
			result.addListener(listener);
			actualtest.run(result);
		} catch (Exception e) {
			logger.error("Cought exception from test " + e
					+ " Message " + e.getMessage());
		} finally {
			stopWatch.stop();
			finished = true;
		}
	}

	public synchronized boolean hasFinished() {
		return finished;
	}

	public SingleTestResult getResult() {

		String message = listener.getMessage();
		if (message == null) {
			message = "";
		}

		TestOutcome outcome = TestOutcome.PASS;
		if (result.failureCount() > 0) {
			outcome = TestOutcome.FAIL;
		} else if (result.errorCount() > 0) {
			outcome = TestOutcome.ERROR;
		}
		SingleTestResult res = new SingleTestResult(testName, message,
				outcome, stopWatch.getTime());
		return res;
	}

	public void setFailed(String message) {
		Exception e = new Exception(message);
		result.addError(allTests.get(testName), e);
	}
}
