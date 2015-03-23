package tracer.tracing.jsme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Jaccard {

	public static void faultlocalization(String diff,
			List<String> failed_tests, int failednum, int passnum,
			HashMap<String, HashSet<String>> test_affchgs,
			HashMap<String, HashSet<String>> changeMap, String rank,
			List<String> affected_tests) throws IOException {
		if (test_affchgs == null)
			return;
		HashMap<String, Double> change_vals = new HashMap<String, Double>();
		for (String change : changeMap.keySet()) {
			double susval = 0.0;
			double all_fail_num = failednum;
			double all_pass_num = passnum;
			double exe_fail_num = 0;
			double exe_pass_num = 0;
			if (all_fail_num == 0 || all_pass_num == 0)
				susval = 0;
			else {
				for (String test : affected_tests) {
					HashSet<String> set = test_affchgs.get(test);
					if (set != null && set.contains(change)) {
						if (failed_tests.contains(test))
							exe_fail_num++;
						else
							exe_pass_num++;
					}
				}
				susval = exe_fail_num / (all_fail_num + exe_pass_num);
			}
			change_vals.put(change, susval);
		}

		Heuristic heu = new Heuristic(diff);
		BufferedWriter writer = new BufferedWriter(new FileWriter(rank));
		for (String s : test_affchgs.keySet()) {
			if (!failed_tests.contains(s))
				continue;

			HashSet<String> chgs = test_affchgs.get(s);

			int chg_num = chgs.size();
			String[] names = new String[chg_num];
			double[] values = new double[chg_num];
			int[] heuValues = new int[chg_num];
			int i = 0;
			for (String chg : chgs) {
				names[i] = chg;
				values[i] = change_vals.get(chg);
				heuValues[i] = heu.getHeuristicValue(chg);
				i++;
			}
			sort(names, values, heuValues, 0, chg_num - 1);
			for (i = 0; i < chg_num; i++) {
				writer.write(names[i] + ": " + values[i] + " - " + heuValues[i]
						+ "\n");
			}
		}
		writer.flush();
		writer.close();

	}

	public static void sort(String[] names, double[] values, int[] heuValues,
			int start, int end) {
		if (start >= end)
			return;
		int pivot = partition(names, values, heuValues, start, end);
		sort(names, values, heuValues, start, pivot - 1);
		sort(names, values, heuValues, pivot + 1, end);
	}

	public static int partition(String[] names, double[] values,
			int[] heuValues, int start, int end) {
		double key = values[start];
		double heuKey = heuValues[start];
		int i = start, j = end;
		while (i < j) {
			while (i < j
					&& (values[j] < key || (values[j] == key && heuValues[j] <= heuKey)))
				j--;
			if (i >= j)
				break;
			String stemp = names[j];
			names[j] = names[i];
			names[i] = stemp;

			double vtemp = values[j];
			values[j] = values[i];
			values[i] = vtemp;

			int htemp = heuValues[j];
			heuValues[j] = heuValues[i];
			heuValues[i] = htemp;

			i++;

			while (i < j
					&& (values[i] > key || (values[i] == key && heuValues[i] >= heuKey)))
				i++;
			if (i >= j)
				break;
			stemp = names[j];
			names[j] = names[i];
			names[i] = stemp;

			vtemp = values[j];
			values[j] = values[i];
			values[i] = vtemp;

			htemp = heuValues[j];
			heuValues[j] = heuValues[i];
			heuValues[i] = htemp;

			j--;

		}
		return i;

	}

	public static double localization(String coverage, String diff,
			List<String> list) throws IOException {
		double res = 0.0;
		double count = 0.0;
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		List<String> changes = FaultTracer.getTypedAtomicChanges(diff);
		String line = reader.readLine();
		double sum = 0.0;
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = FaultTracer.dropTag(line);
				if (list.contains(testName)) {
					// System.out.println("testName:"+testName);
					count++;
					HashSet<String> affectingChanges = new HashSet<String>();
					line = reader.readLine();
					while (line != null && !line.startsWith("<TEST>")) {
						if (!line.contains("-")) {
							if (!line.startsWith("<class")) {
								for (String change : changes) {
									if (change.contains(line)) {
										affectingChanges.add(change);
									}
								}

							} else {
								String receiver = FaultTracer.getReceiver(line);
								String meth = FaultTracer.getMethod(line);
								String method = receiver + "." + meth;
								for (String change : changes) {
									if (change.contains(method)) {
										affectingChanges.add(change);
									}
								}
							}
						} else if (line.startsWith("<FR>")) {
							String field = line
									.substring(line.indexOf("-") + 1);
							for (String change : changes) {
								if (change.contains(field))
									affectingChanges.add(change);
							}

						} else if (line.startsWith("<FW>")) {

						}
						line = reader.readLine();
					}
					sum += affectingChanges.size();
					// System.out.println("test:"+testName+"-"+affectingChanges.size());
				} else
					line = reader.readLine();

			} else
				line = reader.readLine();
		}
		res = sum / count;
		return res;
	}
}
