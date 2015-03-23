package tracer.faultlocalization.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import tracer.faultlocalization.io.LocalizationIOUtils;
import tracer.faulttracer.utils.ECGIOUtils;
import tracer.test.selection.core.SelectionProperties;
import tracer.tracing.Heuristic;

public class SBI {

	public static void faultlocalization(
			Map<String, Set<String>> affecting_changes) throws IOException {
		if (affecting_changes == null || affecting_changes.size() == 0)
			return;
		int all_pass_num = 0;
		int all_fail_num = 0;
		Map<String, RankingEdit> edit_pool = new HashMap<String, RankingEdit>();
		Heuristic heu = new Heuristic(SelectionProperties.FAULTTRACER_EDIT_FILE);
		for (String test : affecting_changes.keySet()) {
			boolean pass = ECGIOUtils.getOutCome(test);
			if (pass)
				all_pass_num++;
			else
				all_fail_num++;

			for (String edit : affecting_changes.get(test)) {
				if (!edit_pool.containsKey(edit)) {
					int heu_val = heu.getHeuristicValue(edit);
					RankingEdit rank_edit = new RankingEdit(edit, heu_val);
					edit_pool.put(edit, rank_edit);
				}
				if (pass)
					edit_pool.get(edit).increaseExePass();
				else
					edit_pool.get(edit).increaseExeFail();
			}
		}
		for (RankingEdit edit : edit_pool.values()) {
			edit.getSBIVal(all_pass_num, all_fail_num);
		}
		File dir=new File(LocalizationProperties.SBI_DIR);
		dir.mkdir();
		for (String test : affecting_changes.keySet()) {
			String testName=ECGIOUtils.stripOutCome(test);
			PriorityQueue<RankingEdit> priority_queue=new PriorityQueue<RankingEdit>(10, new RankingEditComparator());
			for(String edit: affecting_changes.get(test)){
				priority_queue.add(edit_pool.get(edit));
			}
			LocalizationIOUtils.writeRankedEditsToFile(testName, priority_queue, dir);
		}
	}
}
