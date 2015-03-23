package tracer.test.selection.core;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tracer.coverage.io.TracerUtils;
import tracer.faulttracer.utils.ECGIOUtils;
import tracer.faulttracer.utils.EditsIOUtils;
import tracer.test.selection.io.SelectionIOUtils;

public class TestSelection {

	public static void main(String[] args) throws IOException {
		Map<String, Map<String, Integer>> trace = TracerUtils
				.loadMethodTracesFromDirectory();
		List<String> edits = EditsIOUtils
				.getAtomicChanges(SelectionProperties.PROJECT_NEW_VERSION
						+ File.separator
						+ SelectionProperties.FAULTTRACER_EDIT_FILE);
		List<String> selected_tests = selection(trace, edits);
		SelectionIOUtils.writeSelectedTests(selected_tests);
	}

	public static List<String> selection(
			Map<String, Map<String, Integer>> test_trace, List<String> changes)
			throws IOException {
		List<String> selected = new ArrayList<String>();
		for (String test : test_trace.keySet()) {
			String testName = ECGIOUtils.stripOutCome(test);
			for (String line : test_trace.get(test).keySet()) {
				if (!line.contains("-")) {
					if (!line.startsWith("<class ")) {
						if (changes.contains(line)) {
							selected.add(testName);
							break;
						}
					} else {
						String receiver = getReceiver(line);
						String method = getMethod(line);
						String change = receiver + "." + method;
						if (changes.contains(change)
								|| changes.contains(line.substring(line
										.indexOf(">") + 1))) {
							selected.add(testName);
							break;
						}
					}
				} else if (line.startsWith("<FR>")) {
					String field = line.substring(line.indexOf("-") + 1);
					if (changes.contains(field)) {
						selected.add(testName);
						break;
					}

				} else if (line.startsWith("<FW>")) {
					// don't need to do anything yet
				}
			}

		}
		// System.out.println(selected.size());
		// list = selected;
		/*
		 * for (String s : selected) { System.out.println(s); }
		 */
		int orgSize = test_trace.size();
		int selSize = selected.size();
		System.out
				.println("************************************************************");
		System.out.println(selSize + " ("
				+ (formatDouble(selSize * 100 / orgSize))
				+ "%) affected tests were selected from the original "
				+ orgSize + " tests.");
		System.out
				.println("************************************************************");
		return selected;
	}

	public static Map<String, List<String>> selectionWithEditCov(
			Map<String, Map<String, Integer>> test_trace, List<String> changes)
			throws IOException {
		Map<String, List<String>> selected = new HashMap<String, List<String>>();
		for (String test : test_trace.keySet()) {
			String testName = ECGIOUtils.stripOutCome(test);
			for (String line : test_trace.get(test).keySet()) {
				if (!line.contains("-")) {
					if (!line.startsWith("<class ")) {
						for (String typechange : changes) {
							String change = typechange.substring(typechange
									.indexOf(":") + 1);
							if (change.equals(line)) {
								if (!selected.containsKey(testName))
									selected.put(testName,
											new ArrayList<String>());
								selected.get(testName).add(typechange);
								// continue;
							}
						}

					} else {
						String receiver = getReceiver(line);
						String method = getMethod(line);
						String methodchange = receiver + "." + method;

						// added to record the detailed edit test map info

						for (String typechange : changes) {
							String change = typechange.substring(typechange
									.indexOf(":") + 1);
							if (change.equals(methodchange)
									|| change.equals(line.substring(line
											.indexOf(">") + 1))) {
								if (!selected.containsKey(testName))
									selected.put(testName,
											new ArrayList<String>());
								selected.get(testName).add(typechange);
								// continue;
							}

						}
					}
				} else if (line.startsWith("<FR>")) {
					String field = line.substring(line.indexOf("-") + 1);
					for (String typechange : changes) {
						String change = typechange.substring(typechange
								.indexOf(":") + 1);
						if (change.equals(field)) {
							if (!selected.containsKey(testName))
								selected.put(testName, new ArrayList<String>());
							selected.get(testName).add(typechange);
						}
					}

				} else if (line.startsWith("<FW>")) {
					// don't need to do anything yet
				}
			}

		}
		// System.out.println(selected.size());
		// list = selected;
		/*
		 * for (String s : selected) { System.out.println(s); }
		 */
		int orgSize = test_trace.size();
		int selSize = selected.size();
//		for(String test:selected.keySet()){
//			System.out.println(selected.get(test));
//		}
		System.out
				.println("************************************************************");
		System.out.println(selSize + " ("
				+ (formatDouble(selSize * 100 / orgSize))
				+ "%) affected tests were selected from the original "
				+ orgSize + " tests.");
		System.out
				.println("************************************************************");
		return selected;
	}

	public static String formatDouble(double d) {
		NumberFormat format = new DecimalFormat("##.00");
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
