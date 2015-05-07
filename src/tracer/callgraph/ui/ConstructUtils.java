package tracer.callgraph.ui;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tracer.coverage.io.TracerUtils;

public class ConstructUtils {

	public static Set<String> call_relation = new HashSet<String>();

	public static void initialize(String test, boolean before) throws IOException {
		call_relation.clear();
		Map<String, Map<String, Integer>> trace = null;
		if (before)
			trace = TracerUtils.loadMethodTracesFromDirectoryForOldVersion();
		else
			trace = TracerUtils.loadMethodTracesFromDirectoryForNewVersion();
		if (null == test) {
			Iterator<String> it = trace.keySet().iterator();
			test = it.next();
		}
		for (String line : trace.get(test).keySet()) {
			if (line.startsWith("<CALL>") || line.startsWith("<FR>")
					|| line.startsWith("<FW>")) {
				call_relation.add(line);
			}
		}
	}

}
