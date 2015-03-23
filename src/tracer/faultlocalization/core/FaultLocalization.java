package tracer.faultlocalization.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tracer.coverage.core.Properties;
import tracer.coverage.io.TracerUtils;
import tracer.faultlocalization.io.LocalizationIOUtils;
import tracer.faulttracer.utils.ECGIOUtils;
import tracer.faulttracer.utils.EditsIOUtils;
import tracer.test.selection.core.SelectionProperties;
import tracer.test.selection.io.SelectionIOUtils;

public class FaultLocalization {

	public static void main(String[] args) throws IOException {
		Map<String, Map<String, Integer>> trace = TracerUtils
				.loadMethodTracesFromDirectory();
		Map<String, Set<String>> edit_relation = EditsIOUtils
				.getTypedAtomicChangesWithRelation(SelectionProperties.FAULTTRACER_EDIT_FILE);
		List<String> affected_tests = SelectionIOUtils.loadSelectedTests();
		Map<String, Set<String>> affecting_changes = getAffectingChanges(
				affected_tests, trace, edit_relation);
		Tarantula.faultlocalization(affecting_changes);
		SBI.faultlocalization(affecting_changes);
		Jaccard.faultlocalization(affecting_changes);
		Ochiai.faultlocalization(affecting_changes);
	}

	public static Map<String, Set<String>> getAffectingChanges(
			List<String> affected_tests,
			Map<String, Map<String, Integer>> trace,
			Map<String, Set<String>> editRelation) {
		Map<String, Set<String>> test_affchgs = new HashMap<String, Set<String>>();
		for (String test : trace.keySet()) {
			String testName = ECGIOUtils.stripOutCome(test);
			if (!affected_tests.contains(testName))
				continue;
			Set<String> affectingChanges = new HashSet<String>();

			Set<String> cons = trace.get(test).keySet();
			for (String con : cons) {
				if (!con.contains("-")) {
					if (!con.startsWith("<class")) {
						for (String change : editRelation.keySet()) {
							if (change.contains(con)) {
								if (!change.startsWith("LC:")) {
									affectingChanges.add(change);
								} else {
									for (String s : editRelation.get(change)) {
										// System.out.println("haha");
										affectingChanges.add(s);
									}
								}
							}
						}

					} else {
						String smethod = con.substring(con.indexOf(">") + 1);
						String receiver = ECGIOUtils.getReceiver(con);
						String meth = ECGIOUtils.getMethod(con);
						String method = receiver + "." + meth;
						for (String change : editRelation.keySet()) {
							if (change.contains(method)) {
								// System.out.println("haha");
							} else if (change.contains(smethod)) {
								// System.out.println("xixi");
							}
							if (change.contains(method)
									|| change.contains(smethod)) {
								if (!change.startsWith("LC:")) {
									affectingChanges.add(change);
								} else {
									for (String s : editRelation.get(change)) {
										// System.out.println("haha");
										affectingChanges.add(s);
									}
								}
							}
						}
					}
				} else if (con.startsWith("<FR>")) {
					String field = con.substring(con.indexOf("-") + 1);
					for (String change : editRelation.keySet()) {
						if (change.contains(field)) {
							if (!change.startsWith("LCF:")) {
								affectingChanges.add(change);
							} else {
								for (String s : editRelation.get(change)) {
									affectingChanges.add(s);
								}
							}
						}
					}

				} else if (con.startsWith("<FW>")) {

				}
			}
			test_affchgs.put(test, affectingChanges);
		}
		return test_affchgs;
	}
}
