package tracer.coverage.junit;


public class SingleTestResult {

	/**
	 * Enumeration that signals if a test case passed, failed or caused an
	 * error.
	 */
	public enum TestOutcome {
		PASS, FAIL, ERROR
	};

	/**
	 * The outcome of this test.
	 */
	public TestOutcome outcome;

	/**
	 * The passing/failing message of this test.
	 */
	public TestMessage testMessage;

	/**
	 * Creates a new SingleTestResult with given parameters.
	 * 
	 * @param testCaseName
	 *            the name of the test case
	 * @param message
	 *            the message for this test
	 * @param testOutcome
	 *            the outcome of the test
	 * @param duration
	 *            the time the test took
	 */
	public SingleTestResult(String testCaseName, String message, TestOutcome testOutcome,
			long duration) {
		super();
		this.outcome = testOutcome;
		this.testMessage = new TestMessage(testCaseName, message, duration);
	}

	/**
	 * Return the duration of this test.
	 *
	 * @return the duration of this test
	 */
	public long getDuration() {
		return testMessage.getDuration();
	}

	/**
	 * Return true, if the test passed.
	 *
	 * @return true, if the test passed
	 */
	public boolean hasPassed() {
		return outcome == TestOutcome.PASS;
	}

	/**
	 * Return the test message of this test.
	 *
	 * @return the test message of this test
	 */
	public TestMessage getTestMessage() {
		return testMessage;
	}

	@Override
	public String toString() {
		return outcome + "  " + testMessage.toString();
	}

	public TestOutcome getOutcome() {
		return outcome;
	}
}
