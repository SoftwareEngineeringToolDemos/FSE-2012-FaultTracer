package tracer.coverage.core;

public interface Listener {
	public void start();

	public void end();

	public void testStart(String testName);

	public void testEnd(String testName);
}
