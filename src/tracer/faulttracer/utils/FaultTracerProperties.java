package tracer.faulttracer.utils;

import java.io.File;

public class FaultTracerProperties {
	public static boolean CONSIDER_LCF = true;
	public static final String TRACER_DIR = "faulttracer-files";
	public static final String PROGRAM_EDIT_FILE="program-edits";
	public static final String FAILURES="test-failures";
	public static final String REDUCE_TEST_FILE=FaultTracerProperties.TRACER_DIR+File.separator+"reduced-tests.gz";
}
