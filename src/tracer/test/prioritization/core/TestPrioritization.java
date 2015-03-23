package tracer.test.prioritization.core;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tracer.coverage.io.TracerUtils;
import tracer.faulttracer.utils.ECGIOUtils;
import tracer.faulttracer.utils.EditsIOUtils;
import tracer.test.prioritization.io.PrioritizationIOUtils;
import tracer.test.selection.core.SelectionProperties;

public class TestPrioritization {
	public static void main(String[] args) throws IOException {
		Map<String, Map<String, Integer>> trace = TracerUtils
				.loadMethodTracesFromDirectory();
		List<String> edits = EditsIOUtils
				.getAtomicChanges(SelectionProperties.PROJECT_NEW_VERSION
						+ File.separator
						+ SelectionProperties.FAULTTRACER_EDIT_FILE);
		List<String> selected_tests = additionalPrioritize(trace, edits);
		PrioritizationIOUtils.writePrioritizedTests(selected_tests);
		int orgSize=trace.size();
		int selSize=selected_tests.size();
		System.out.println("************************************************************");
		System.out.println(selSize+" ("+(formatDouble(selSize*100/orgSize))+"%) affected tests were selected and prioritized from the original "+orgSize+ " tests.");
		System.out.println("************************************************************");
		
	}

	public static List<String> additionalPrioritize(
			Map<String, Map<String, Integer>> test_trace, List<String> changes)
			throws IOException {
		Map<String, Set<String>> map = getRel(test_trace, changes);
		List<String> ordered = new ArrayList<String>();
		while (!map.isEmpty()) {
			int maxSize = -1;
			String selectedTest = "";
			for (String s : map.keySet()) {
				int size = map.get(s).size();
				if (maxSize < size) {
					maxSize = size;
					selectedTest = s;
				}
			}
			Set<String> covChanges = map.get(selectedTest);
			map.remove(selectedTest);
			ordered.add(selectedTest);
			if (map.isEmpty())
				break;
			for (String key : map.keySet()) {
				// System.out.println(key+": "+map.get(key));
				if (map.get(key).size() > 0)
					map.get(key).removeAll(covChanges);
			}
		}

		return ordered;
	}

	public static Map<String, Set<String>> getRel(
			Map<String, Map<String, Integer>> test_trace, List<String> changes)
			throws IOException {
		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		// List<String> selected = new ArrayList<String>();
		for (String test : test_trace.keySet()) {
			String testName = ECGIOUtils.stripOutCome(test);
			for (String line : test_trace.get(test).keySet()) {
				if (!line.contains("-")) {
					if (!line.startsWith("<class ")) {
						if (changes.contains(line)) {
							if (!res.containsKey(testName)) {
								res.put(testName, new HashSet<String>());
							}
							res.get(testName).add(line);
							// break;
						}
					} else {
						String receiver = getReceiver(line);
						String method = getMethod(line);
						String change = receiver + "." + method;
						if (changes.contains(change)
								|| changes.contains(line.substring(line
										.indexOf(">") + 1))) {
							if (!res.containsKey(testName)) {
								res.put(testName, new HashSet<String>());
							}
							if (changes.contains(change))
								res.get(testName).add(change);
							else
								res.get(testName).add(
										line.substring(line.indexOf(">") + 1));
						}
					}
				} else if (line.startsWith("<FR>")) {
					String field = line.substring(line.indexOf("-") + 1);
					if (changes.contains(field)) {
						if (!res.containsKey(testName)) {
							res.put(testName, new HashSet<String>());
						}
						res.get(testName).add(field);
					}

				} else if (line.startsWith("<FW>")) {

				}
			}

		}
		return res;
	}
	public static String formatDouble(double d){
		NumberFormat format=new DecimalFormat("##.00");
		return format.format(d);
	}

	public static String getReceiver(String in) {
		int e = in.indexOf(">");
		int s = 7;
		return in.substring(s, e);
	}

	public static String getMethod(String in) {
		int s = in.lastIndexOf(".");
		return in.substring(s + 1);
	}
}
