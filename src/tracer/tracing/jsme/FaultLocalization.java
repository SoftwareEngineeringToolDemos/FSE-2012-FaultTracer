package tracer.tracing.jsme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import tracer.tracing.jsme.heuristic.CallHierarchyConstructor;
import tracer.tracing.jsme.heuristic.CallRelationGetter;

public class FaultLocalization {
	static List<String> clist = new ArrayList<String>();
	static List<String> flist = new ArrayList<String>();
	static int failednum = 0;
	static int passnum = 0;

	// static HashMap<String, HashSet<String>> typed_changes;
	// static List<String> failed_tests;

	public static void main(String[] args) throws IOException {
		String[] subs = { "jtopas", "xmlsec", "jmeter", "ant" };
		int[] vs = { 4, 4, 6, 9 };

		for (int i = 0; i < 4; i++) {
			trace(subs[i], vs[i]);
		}
	}

	public static void trace(String s, int v) throws IOException {
		String subject = "G:\\FSE_withLCF\\" + s + "\\";

		int vers = v;
		for (int i = 0; i < vers - 1; i++) {
			clist.clear();
			flist.clear();

			String oldCov = subject + "log" + i + i;
			String newCov = subject + "log" + (i + 1) + (i);
			String callGraph = subject + "CallGraph" + (i + 1) + (i);
			String diff = subject + s + i + ".0" + "-" + s + (i + 1)
					+ ".0.adiff";
			String fault_loc = subject + "SeededFaultLoc" + i + (i + 1);
			String rank_tar = subject + "tarantula_ranking" + i + (i + 1);
			String rank_jac = subject + "jaccard_ranking" + i + (i + 1);
			String rank_sbi = subject + "SBI_ranking" + i + (i + 1);
			String rank_och = subject + "ochiai_ranking" + i + (i + 1);
			String rank_heu = subject + "heuristic_ranking" + i + (i + 1);

			List<String> changes = FaultTracer.getAtomicChanges(diff);
			HashMap<String, HashSet<String>> changeRel = FaultTracer
					.getTypedAtomicChanges2(diff);
			List<String> typed_changes = FaultTracer
					.getTypedAtomicChanges(diff);
			HashMap<String, HashSet<String>> test_trace = readTestTrace(newCov);
			HashMap<String, HashSet<String>> fault_tests = readSeededFaults(fault_loc);
			 CallRelationGetter.initialize(callGraph);

			for (String fault : fault_tests.keySet()) {
				//add the seeded change
				boolean added = false;
				String typed_fault = fault;
				String untyped_fault = fault.substring(fault.indexOf(":") + 1);
				if (!changes.contains(untyped_fault)) {
					added = true;
					changes.add(untyped_fault);
					changeRel.put(fault, new HashSet<String>());
					typed_changes.add(fault);
				} else {
					for (String chg : changeRel.keySet()) {
						if (!chg.startsWith("LC")
								&& chg.contains(untyped_fault)) {
							typed_fault = chg;
							break;
						}
					}
				}

				//do fault localization
				List<String> failed_tests = new ArrayList<String>();
				failed_tests.addAll(fault_tests.get(fault));
				List<String> affected_tests = FaultTracer.selection(oldCov,
						changes);
				HashMap<String, HashSet<String>> test_affchgs = initialization(
						test_trace, changeRel, affected_tests, failed_tests);

				Jaccard.faultlocalization(diff, failed_tests, failednum,
						passnum, test_affchgs, changeRel, rank_jac,
						affected_tests);
				SBI.faultlocalization(diff, failed_tests, failednum, passnum,
						test_affchgs, changeRel, rank_sbi, affected_tests);
				Tarantula.faultlocalization(diff, failed_tests, failednum,
						passnum, test_affchgs, changeRel, rank_tar,
						affected_tests);
				Ochiai.faultlocalization(diff, failed_tests, failednum,
						passnum, test_affchgs, changeRel, rank_och,
						affected_tests);
				HeuristicRank.faultlocalization(callGraph, typed_changes,
						failed_tests, failednum, passnum, test_affchgs,
						changeRel, rank_heu, affected_tests);
				HashMap<String, List<String>> test_fault = new HashMap<String, List<String>>();
				for (String test : failed_tests) {
					List<String> list = new ArrayList<String>();
					list.add(typed_fault);
					test_fault.put(test, list);
				}
				SeededDebug.debugCM(test_fault, rank_tar, rank_sbi, rank_jac,
						rank_och, rank_heu);

				//remove the seeded change
				if (added) {
					changes.remove(untyped_fault);
					changeRel.remove(fault);
					typed_changes.remove(fault);
				}
			}
		}

	}

	public static HashMap<String, HashSet<String>> initialization(
			HashMap<String, HashSet<String>> test_trace,
			HashMap<String, HashSet<String>> typed_changes,
			List<String> affected_tests, List<String> failed_tests)
			throws IOException {
		HashMap<String, HashSet<String>> aff_test_trace = new HashMap<String, HashSet<String>>();
		for (String test : affected_tests) {
			aff_test_trace.put(test, test_trace.get(test));
		}
		HashMap<String, HashSet<String>> test_affchgs = FaultTracer
				.determineAffectingChanges(aff_test_trace, typed_changes);
		int failednum = failed_tests.size();
		int passnum = 0;
		for (String s : test_affchgs.keySet()) {
			if (failed_tests.contains(s))
				failednum++;
			else
				passnum++;
		}

		FaultLocalization.passnum = passnum;
		FaultLocalization.failednum = failednum;
		if (failednum == 0)
			return null;
		return test_affchgs;
	}

	public static HashMap<String, HashSet<String>> readTestTrace(String path)
			throws IOException {
		HashMap<String, HashSet<String>> tests = new HashMap<String, HashSet<String>>();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = FaultTracer.dropTag(line);
				HashSet<String> con = new HashSet<String>();
				line = reader.readLine();
				while (line != null && !line.startsWith("<TEST>")) {
					con.add(line);
					line = reader.readLine();
				}
				tests.put(testName, con);
			} else
				line = reader.readLine();
		}
		reader.close();
		return tests;
	}

	public static HashMap<String, HashSet<String>> readSeededFaults(
			String fault_loc) throws IOException {
		HashMap<String, HashSet<String>> fault_tests = new HashMap<String, HashSet<String>>();
		File file = new File(fault_loc);
		if (!file.exists())
			return fault_tests;
		BufferedReader reader = new BufferedReader(new FileReader(fault_loc));
		String line = reader.readLine();
		while (line != null) {
			if (line.length() > 1 && !line.startsWith("<TEST>")) {
				HashSet<String> tests = new HashSet<String>();
				String fault = line;
				line = reader.readLine();
				while (line != null && line.startsWith("<TEST>")) {
					tests.add(line.replace("<TEST>", ""));
					line = reader.readLine();
				}
				fault_tests.put(fault, tests);
			} else
				line = reader.readLine();
		}
		return fault_tests;
	}

	public static String compare(List<String> chianti, List<String> faultTracer) {
		List<String> c = new ArrayList<String>();
		List<String> f = new ArrayList<String>();
		for (String s : chianti) {
			c.add(s);
		}
		for (String s : faultTracer) {
			f.add(s);
		}
		for (String s : faultTracer) {
			if (c.contains(s))
				c.remove(s);
		}
		for (String s : chianti) {
			if (f.contains(s))
				f.remove(s);
		}

		clist = c;
		flist = f;
		return c.size() + "\t" + f.size();

	}

	public static List<String> findPassTest(String cov) throws IOException {
		List<String> res = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(cov));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {

				String test = line.substring(line.indexOf(">") + 1);

				line = reader.readLine();
				while (line != null && !line.startsWith("<TEST>")) {
					if (line.startsWith("<TESTPASS>")) {
						// if (test.substring(test.lastIndexOf(".") + 1)
						// .startsWith("test"))
						res.add(test);
						break;
					}
					line = reader.readLine();
				}
			} else
				line = reader.readLine();
		}

		return res;
	}
}
