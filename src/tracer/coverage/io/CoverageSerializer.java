package tracer.coverage.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import tracer.faulttracer.utils.FaultTracerProperties;

public class CoverageSerializer {
	public static void serialize(String type,
			Map<String, Map<String, Integer>> trace) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				FaultTracerProperties.TRACER_DIR + File.separator + type
						+ ".dat"));
		for (String test : trace.keySet()) {
			writer.write(test + " ");
			for (String elem : trace.get(test).keySet()) {
				writer.write(elem + "-" + trace.get(test).get(elem) + " ");
			}
			writer.write("\n");
		}
		writer.flush();
		writer.close();
	}
}
