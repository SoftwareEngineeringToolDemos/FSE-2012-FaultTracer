package tracer.coverage.junit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

public final class TestResultListener implements TestListener {

	boolean pass = true;
	boolean error = false;
	boolean fail = false;

	public boolean getResult() {
		return pass;
	}

	public void reset() {
		pass = true;
		error = false;
		fail = false;
	}

	public void setResult(boolean pass) {
		this.pass = pass;
	}

	public void addError(Test test, Throwable t) {
		pass = false;
		error = true;
	}

	public void addFailure(Test test, AssertionFailedError t) {
		pass = false;
		fail = true;
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
		pass = true;
	}

}
