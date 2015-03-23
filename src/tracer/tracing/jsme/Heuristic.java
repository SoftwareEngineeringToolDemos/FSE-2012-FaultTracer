package tracer.tracing.jsme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Heuristic {
	HashMap<String, String> depend;

	public Heuristic(String diff) throws IOException {
		depend = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		while (line != null) {
			String[] items = line.split("=>");
			if (items.length == 1)
				depend.put(items[0], "");
			else
				depend.put(items[0], items[1]);
			line = reader.readLine();
		}
		reader.close();
	}

	public int getHeuristicValue(String change) {
		HashSet<String> dependees = new HashSet<String>();
		List<String> worklist = new ArrayList<String>();
		worklist.add(change);
		while (!worklist.isEmpty()) {
			String cur = worklist.remove(0);
			if (!depend.containsKey(cur))
				return 0;
			String con = depend.get(cur);
			if (con != null && !con.equals("")) {
				String[] items = con.split(",");
				for (String item : items) {
					if (!dependees.contains(item)) {
						dependees.add(item);
						worklist.add(item);
					}
				}
			}
		}
		return dependees.size();

	}

}
