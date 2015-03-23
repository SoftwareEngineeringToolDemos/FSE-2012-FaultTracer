package tracer.tracing.jsme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import tracer.tracing.jsme.heuristic.CallHierarchyConstructor;
import tracer.tracing.jsme.heuristic.HeuristicComputation;



public class HeuristicRank {

	public static void faultlocalization(String callGraph, List<String> typed_changes, List<String> changed,
			int failednum, int passnum,
			HashMap<String, HashSet<String>> test_affchgs,
			HashMap<String, HashSet<String>> changeMap, String rank,
			List<String> sel)
			throws IOException {

		
		if(test_affchgs==null)return;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(rank));
		for (String s : test_affchgs.keySet()) {
			if (!changed.contains(s))
				continue;
			HashSet<String> chgs = test_affchgs.get(s);
			writer.write("<TEST>" + s + " -" + chgs.size() + "\n");
			String[] names = new String[chgs.size()];
			int[] values = new int[chgs.size()];
			int num = 0;
			for (String chg : chgs) {
				if (chg.startsWith("CM:")||chg.startsWith("AM:")) {
					names[num] = chg;
					values[num] = HeuristicComputation.compute(
							s,chg.substring(chg.indexOf(":") + 1), typed_changes);
					num++;
				}
			}
			sort(names, values, 0, num - 1);
			for (int i = 0; i < num; i++) {
				writer.write(names[i] + ": " + values[i] + "\n");
			}
		}
		writer.flush();
		writer.close();

	}

	public static void sort(String[] names, int[] values, int start, int end) {
		if (start >= end)
			return;
		int pivot = partition(names, values, start, end);
		sort(names, values, start, pivot - 1);
		sort(names, values, pivot + 1, end);
	}

	public static int partition(String[] names, int[] values, int start, int end) {
		int key = values[start];
		int i = start, j = end;
		while (i < j) {
			while (i < j && values[j] <= key)
				j--;
			if (i >= j)
				break;
			String stemp = names[j];
			names[j] = names[i];
			names[i] = stemp;

			int vtemp = values[j];
			values[j] = values[i];
			values[i] = vtemp;

			i++;

			while (i < j && values[i] >= key)
				i++;
			if (i >= j)
				break;
			stemp = names[j];
			names[j] = names[i];
			names[i] = stemp;

			vtemp = values[j];
			values[j] = values[i];
			values[i] = vtemp;

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
