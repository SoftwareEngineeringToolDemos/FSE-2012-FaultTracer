package tracer.coverage.io;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import tracer.coverage.core.Properties;

public class StatementTraceLoader {
	
	public static void main(String[] args) throws IOException{
		Map<String, Map<String, Integer>> trace=TracerUtils.loadMethodTracesFromDirectory(Properties.TRACER_STAT_FILES);
		for(String test:trace.keySet()){
			System.out.println("<Test>"+test);
			for(String elem:trace.get(test).keySet()){
				System.out.println(elem+"-"+trace.get(test).get(elem));
			}
		}
		CoverageSerializer.serialize("statement-coverage", trace);
	}

}
