package tracer.coverage.io;

import java.io.File;
import java.util.Map;

import tracer.coverage.core.Properties;

public class ECGTraceLoader {
	
	public static void main(String[] args){
		Map<String, Map<String, Integer>> trace=TracerUtils.loadMethodTracesFromDirectory(Properties.TRACER_ECG_FILES);
		for(String test:trace.keySet()){
			System.out.println("<Test>"+test);
			for(String elem:trace.get(test).keySet()){
				System.out.println(elem+trace.get(test).get(elem));
			}
		}
	}

}
