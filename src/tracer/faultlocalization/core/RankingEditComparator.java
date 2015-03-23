package tracer.faultlocalization.core;

import java.util.Comparator;

public class RankingEditComparator implements Comparator<RankingEdit> {

	@Override
	public int compare(RankingEdit e1, RankingEdit e2) {
		if (e1.getSusp_val() > e2.getSusp_val())
			return -1;
		else if (e1.getSusp_val() == e2.getSusp_val()) {
			if (e1.getHeu_val() > e2.getHeu_val())
				return -1;
			else if (e1.getHeu_val() == e2.getHeu_val())
				return 0;
			else
				return 1;
		} else
			return 1;
	}

}
