package tracer.tracing.heuristic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import tracer.tracing.FaultTracer;

public class HeuristicComputation {

	public static int compute(String m, String diff) throws IOException {
		return computeAnscestorsAndDescendants(m)
				+ computeCalleesAndCallers(m, diff);
	}

	public static int computeAnscestorsAndDescendants(String m) {
		HashSet<String> anscestors = new HashSet<String>();
		HashSet<String> descendants = new HashSet<String>();
		HashSet<String> ads = new HashSet<String>();

		List<String> worklist = new ArrayList<String>();

		worklist.add(m);
		// compute descendants
		while (!worklist.isEmpty()) {
			String cur = worklist.remove(0);
			descendants.add(cur);
			for (String map : CallHierarchyConstructor.callmap) {
				if (map.startsWith(cur)) {
					String callee = map.substring(map.indexOf("-") + 1);
					if (!descendants.contains(callee)) {
						worklist.add(callee);
					}
				}
			}

		}
		// compute anscestors
		worklist.clear();
		worklist.add(m);
		while (!worklist.isEmpty()) {
			String cur = worklist.remove(0);
			anscestors.add(cur);
			for (String map : CallHierarchyConstructor.callmap) {
				if (map.endsWith(cur)) {
					String caller = map.substring(0, map.indexOf("-"));
					if (!anscestors.contains(caller)) {
						worklist.add(caller);
					}
				}
			}

		}

		ads.addAll(descendants);
		ads.addAll(anscestors);
		return ads.size();
	}

	public static int computeCalleesAndCallers(String m, String diff)
			throws IOException {
		HashSet<String> callees = new HashSet<String>();
		HashSet<String> callers = new HashSet<String>();
		HashSet<String> all = new HashSet<String>();

		// compute direct callees
		for (String map : CallHierarchyConstructor.callmap) {
			if (map.startsWith(m)) {
				callees.add(map.substring(map.indexOf("-") + 1));
			}
		}
		// compute direct callers
		for (String map : CallHierarchyConstructor.callmap) {
			if (map.endsWith(m)) {
				callers.add(map.substring(0, map.indexOf("-")));
			}
		}

		List<String> changes = FaultTracer.getTypedAtomicChanges(diff);

		for (String callee : callees) {
			if (changes.contains("CM:" + callee)||changes.contains("AM:" + callee)) {
				all.addAll(callees);
				break;
			}
		}

		for (String caller : callers) {
			if (changes.contains("CM:" + caller)||changes.contains("AM:" + caller)) {
				all.addAll(callers);
				break;
			}
		}
		return all.size();
	}

}
