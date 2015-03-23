package tracer.test.prioritization.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Parsers {
	public static String root = "D:\\eclipse3.7.2\\FSE-workspace\\oopsla-raw-data";

	public static Map<String, Set<String>> getEditMutantMap(String sub,
			String oldV, String newV) throws IOException {
		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		String path = root
				+ "\\step3-data\\mapping-between-edits-and-mutants\\" + sub
				+ oldV + "-" + sub + newV + "-edit-mutant-mapping.data";
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while (line != null) {
			String[] items = line.split("-");
			String[] mutants = items[1].split(",");
			Set<String> mset = new HashSet<String>();
			for (String mutant : mutants) {
				mset.add(mutant);
			}
			res.put(items[0], mset);
			line = reader.readLine();
		}
		reader.close();
		return res;
	}

	public static Map<String, Set<String>> getMutantTests(String sub,
			String oldV, String newV) throws IOException {
		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		String path = root + "\\step1-data\\mutant-correlation-with-tests\\"
				+ sub + oldV + "-" + sub + newV + "-mutant-killing.data";
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while (line != null) {
			String[] items = line.split("-");
			Set<String> tset = new HashSet<String>();
			if (items.length > 1) {
				String[] tests = items[1].split(",");
				for (String mutant : tests) {
					tset.add(mutant);
				}
			}
			res.put(items[0], tset);
			line = reader.readLine();
		}
		reader.close();
		return res;
	}

	public static HashMap<String, HashSet<String>> getFaultInfo(String sub,
			String oldV, String newV) throws IOException {
		HashMap<String, HashSet<String>> res = new HashMap<String, HashSet<String>>();
		String path = root + "\\step2-data\\fault-info\\" + sub + oldV + "-"
				+ sub + newV + "-fault-info.data";
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while (line != null) {
			String[] items = line.split("-");
			String test = items[0].replace("<TEST>", "");
			String[] faults = items[1].split(",");
			HashSet<String> set = new HashSet<String>();
			for (String fault : faults) {
				set.add(fault);
			}
			res.put(test, set);
			line = reader.readLine();
		}
		return res;
	}

}
