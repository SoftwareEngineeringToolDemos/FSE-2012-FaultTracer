package tracer.coverage.core;

import java.io.File;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

import tracer.coverage.io.Tracer;
import tracer.coverage.io.TracerUtils;

public class ECGCoverageListener implements Listener {
	public static Logger logger = Logger.getLogger(ECGCoverageListener.class);
	public static AtomicReference<ConcurrentMap<String, Integer>> methodCoverageMap = new AtomicReference<ConcurrentMap<String, Integer>>(
			new ConcurrentHashMap<String, Integer>());

	public static ConcurrentMap<String, Integer> getMethodCoverage() {
		return methodCoverageMap.get();
	}

	public ECGCoverageListener() {
		File dir = new File(Properties.TRACER_ECG_FILES);
		if (!dir.exists())
			dir.mkdir();
	}

	/**
	 * process before the coverage collection
	 * 
	 */
	public void start() {
		methodCoverageMap.get().clear();
	}

	/**
	 * process after the coverage collection
	 * 
	 */
	public void end() {
		methodCoverageMap.get().clear();
	}

	/**
	 * process before the execution of each test
	 * 
	 */
	public void testStart(String testName) {
		logger.debug("start test: "+testName);
		methodCoverageMap.get().clear();
		if (!Tracer.call_stack.isEmpty())
			Tracer.call_stack.clear();
		//Tracer.call_stack.push("StartFakeCaller");
	}

	/**
	 * process after the execution of each test
	 * 
	 */
	public void testEnd(String testName) {
		Tracer.getInstance().deactivateTrace();

		ConcurrentMap<String, Integer> methodMap = methodCoverageMap.get();
		methodCoverageMap.set(new ConcurrentHashMap<String, Integer>());
		TracerUtils.writeMethodTrace(methodMap, testName,
				getMethodCoverageFileName(testName));
		methodCoverageMap.get().clear();

		Tracer.getInstance().activateTrace();
	}

	static String sanitize(String name) {
		String result = name.replace(' ', '_');
		result = result.replace('/', '-');
		return result;
	}

	static String getMethodCoverageFileName(String testName) {
		String sanitizedName = sanitize(testName);
		String fileName = Properties.TRACER_ECG_FILES + "/" + sanitizedName
				+ ".gz";
		return fileName;
	}

}
