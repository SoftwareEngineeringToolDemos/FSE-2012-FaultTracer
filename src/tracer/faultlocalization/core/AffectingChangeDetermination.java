package tracer.faultlocalization.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import tracer.coverage.core.Properties;
import tracer.coverage.io.TracerUtils;
import tracer.faultlocalization.io.LocalizationIOUtils;
import tracer.faulttracer.utils.ECGIOUtils;
import tracer.faulttracer.utils.EditsIOUtils;
import tracer.test.selection.core.SelectionProperties;
import tracer.test.selection.io.SelectionIOUtils;

public class AffectingChangeDetermination {

	public static void main(String[] args) throws IOException {
		Map<String, Map<String, Integer>> trace = TracerUtils
				.loadMethodTracesFromDirectory();
		Map<String, Set<String>> edit_relation = EditsIOUtils
				.getTypedAtomicChangesWithRelation(SelectionProperties.FAULTTRACER_EDIT_FILE);
		List<String> affected_tests = SelectionIOUtils.loadSelectedTests();
		Map<String, Set<String>> affecting_changes = FaultLocalization.getAffectingChanges(
				affected_tests, trace, edit_relation);
		File dir=new File(LocalizationProperties.AFFECTCHANGE_DIR);
		dir.mkdir();
		for (String test : affecting_changes.keySet()) {
			String testName=ECGIOUtils.stripOutCome(test);
			PriorityQueue<RankingEdit> priority_queue=new PriorityQueue<RankingEdit>(10, new RankingEditComparator());
			for(String edit: affecting_changes.get(test)){
				priority_queue.add(new RankingEdit(edit, 0, 0));
			}
			LocalizationIOUtils.writeRankedEditsToFile(testName, priority_queue, dir);
		}
	}

	
}
