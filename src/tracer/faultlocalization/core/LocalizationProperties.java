package tracer.faultlocalization.core;

import java.io.File;

import tracer.faulttracer.utils.FaultTracerProperties;

public class LocalizationProperties {
	public static final String AFFECTCHANGE_DIR=FaultTracerProperties.TRACER_DIR+File.separator+"affecting-change-results";
	
	public static final String TARANTULA_DIR=FaultTracerProperties.TRACER_DIR+File.separator+"tarantula-fault-localization";
	public static final String SBI_DIR=FaultTracerProperties.TRACER_DIR+File.separator+"sbi-fault-localization";
	public static final String JACCARD_DIR=FaultTracerProperties.TRACER_DIR+File.separator+"jaccard-fault-localization";
	public static final String OCHIAI_DIR=FaultTracerProperties.TRACER_DIR+File.separator+"ochiai-fault-localization";
}
