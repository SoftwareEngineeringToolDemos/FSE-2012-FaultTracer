package tracer.coverage.junit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

public final class JUnitTestListener implements TestListener {

	private String message;

	public void addError(Test test, Throwable t) {
		message = "Error: " + test + " - " + t + " - " + getStackTrace(t);
	}

	public void addFailure(Test test, AssertionFailedError t) {
		message = "Failure: " + test + " - " + t + " - " + getStackTrace(t);
	}

	public static String getStackTrace(Throwable t) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		t.printStackTrace(printWriter);
		return result.toString();
	}

	public void endTest(Test test) {
	}

	public void startTest(Test test) {
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}

