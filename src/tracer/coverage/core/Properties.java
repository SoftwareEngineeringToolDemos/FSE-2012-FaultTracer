package tracer.coverage.core;

import java.io.File;

import org.apache.log4j.Logger;

import tracer.coverage.junit.JUnitTestDriver;
import tracer.faulttracer.utils.FaultTracerProperties;

public class Properties {
	static Logger logger = Logger.getLogger(Properties.class);
	

	public static final String PROJECT_PREFIX_KEY = "faulttracer.package.prefix";
	public static final String TEST_SUITE_KEY = "faulttracer.test.suite";
	public static final String TRACE_BYTECODE_KEY = "faulttracer.trace";
	public static final String RUN_SELECTED_KEY = "faulttracer.select";
	public static final String RUN_PRIORITIZED_KEY = "faulttracer.prioritize";
	public static final String RUN_REDUCED_KEY = "faulttracer.reduce";
	public static final String METHOD_COV_KEY = "faulttracer.method.coverage";
	public static final String STATEMENT_COV_KEY = "faulttracer.statement.coverage";
	public static final String BRANCH_COV_KEY = "faulttracer.branch.coverage";

	public static final String JUNIT_LEVEL_KEY = "faulttracer.junit.level";

	public static String PROJECT_PREFIX = getProperty(PROJECT_PREFIX_KEY);
	public static String TEST_SUITE = getProperty(TEST_SUITE_KEY);
	public static final boolean TRACE_BYTECODE = getPropertyOrDefault(
			TRACE_BYTECODE_KEY, true);
	public static boolean RUN_SELECTED = getPropertyOrDefault(RUN_SELECTED_KEY,
			false);
	public static boolean RUN_PRIORITIZED = getPropertyOrDefault(
			RUN_PRIORITIZED_KEY, false);
	public static boolean RUN_REDUCED = getPropertyOrDefault(RUN_REDUCED_KEY,
			false);
	public static boolean METHOD_COV = getPropertyOrDefault(METHOD_COV_KEY,
			false);
	public static boolean STATEMENT_COV = getPropertyOrDefault(
			STATEMENT_COV_KEY, false);
	
	public static boolean BRANCH_COV = getPropertyOrDefault(
			BRANCH_COV_KEY, false);

	public static boolean JUNIT4 = getPropertyOrDefault(JUNIT_LEVEL_KEY, false);

	public static boolean INSTRUMENT_FIELD = true;
	public static boolean TRACE_CALL_REL = true;
	public static final String TRACER_CLASS_NAME = "tracer/coverage/io/Tracer";
	public static String TRACER_COV_DIR = FaultTracerProperties.TRACER_DIR
			+ File.separator + "ecg-coverage";
	public static String TEST_ID_FILE="test_id.dat";
	static {
		if (Properties.METHOD_COV)
			TRACER_COV_DIR = FaultTracerProperties.TRACER_DIR
					+ File.separator + "method-coverage";
		else if (Properties.STATEMENT_COV)
			TRACER_COV_DIR = FaultTracerProperties.TRACER_DIR
					+ File.separator + "statement-coverage";
		else if (Properties.BRANCH_COV)
			TRACER_COV_DIR = FaultTracerProperties.TRACER_DIR
					+ File.separator + "branch-coverage";
	}
	public static String TRACER_BRAN_FILES = FaultTracerProperties.TRACER_DIR
			+ File.separator + "branch-coverage";
	public static String TRACER_STAT_FILES = FaultTracerProperties.TRACER_DIR
			+ File.separator + "statement-coverage";
	public static String TRACER_METH_FILES = FaultTracerProperties.TRACER_DIR
			+ File.separator + "method-coverage";

	private static String getProperty(String key) {
		String result = null;
		if (System.getProperty(key) != null) {
			result = System.getProperty(key);
		}
		// no else if - property may also be null
		return result;
	}

	public static boolean getPropertyOrDefault(String key, boolean defaultValue) {
		String property = getProperty(key);
		logger.debug(key+"-"+property);
		if (property != null) {
			String propertyTrimmed = property.trim().toLowerCase();
			if (propertyTrimmed.equals("true") || propertyTrimmed.equals("yes")) {
				return true;
			} else if (propertyTrimmed.equals("false")
					|| propertyTrimmed.equals("no")) {
				return false;
			}
		}
		return defaultValue;
	}
}
