package tracer.tracing.jsme.heuristic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class CallRelationGetter {
	public static HashMap<String, HashSet<String>> test_callmap = new HashMap<String, HashSet<String>>();

	public static void initialize(String path) throws IOException {
		test_callmap.clear();
		BufferedReader reader = new BufferedReader(new FileReader(path + "New"));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String test = line.replace("<TEST>", "");
				line = reader.readLine();
				HashSet<String> rels = new HashSet<String>();
				while (line != null && !line.startsWith("<TEST>")) {
					rels.add(line);
				}
				test_callmap.put(test, rels);
			} else
				line = reader.readLine();
		}
	}

}
