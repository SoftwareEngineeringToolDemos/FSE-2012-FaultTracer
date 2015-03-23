package tracer.coverage.junit;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class TestMessage implements Serializable {

	Logger logger = Logger.getLogger(TestMessage.class);
	private String testCaseName;

	private String message;

	private long duration;

	private static final int MAX_MESSAGE_LENGTH = 8;

	public TestMessage(TestMessage testMessage) {
		this.testCaseName = testMessage.testCaseName;
		this.message = testMessage.message;
		this.duration = 0;
		// this.duration = testMessage.duration;
	}

	public TestMessage(String testCaseName, String message, long duration) {
		super();
		this.testCaseName = testCaseName;
		if (message != null && message.length() > MAX_MESSAGE_LENGTH) {
			logger.info("Got long error message from test:  ("
					+ message.length() + ") " + testCaseName + "\n" + message);
			this.message = message.substring(0,
					Math.min(message.length(), MAX_MESSAGE_LENGTH));
		} else {
			this.message = message;
		}
		this.duration = duration;
	}

	public TestMessage() {
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
