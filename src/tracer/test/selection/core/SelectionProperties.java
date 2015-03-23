package tracer.test.selection.core;

import java.io.File;

import tracer.faulttracer.utils.FaultTracerProperties;

public class SelectionProperties {

	public static final String PROJECT_OLD_VERSION_KEY = "faulttracer.old.version";
	public static final String PROJECT_NEW_VERSION_KEY = "faulttracer.new.version";

	public static final String FAULTTRACER_EDIT_FILE=FaultTracerProperties.TRACER_DIR+File.separator+FaultTracerProperties.PROGRAM_EDIT_FILE;
	
	public static String PROJECT_OLD_VERSION = getProperty(PROJECT_OLD_VERSION_KEY);
	public static String PROJECT_NEW_VERSION = getProperty(PROJECT_NEW_VERSION_KEY);

	
	public static String SELECT_TEST_FILE=FaultTracerProperties.TRACER_DIR+File.separator+"selected-tests.gz";
	public static String PRIORITIZE_TEST_FILE=FaultTracerProperties.TRACER_DIR+File.separator+"prioritized-tests.gz";

	private static String getProperty(String key) {
		String result = null;
		if (System.getProperty(key) != null) {
			result = System.getProperty(key);
		}
		// no else if - property may also be null
		return result;
	}

}
