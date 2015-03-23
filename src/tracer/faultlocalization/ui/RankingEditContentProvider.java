package tracer.faultlocalization.ui;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import tracer.differencing.core.data.GlobalData;
import tracer.faultlocalization.core.LocalizationProperties;
import tracer.faultlocalization.core.RankingEdit;
import tracer.faultlocalization.io.LocalizationIOUtils;

public class RankingEditContentProvider implements IStructuredContentProvider {

	public static Map<String, List<RankingEdit>> unranked_test_edits;
	public static Map<String, List<RankingEdit>> tarantula_test_edits;
	public static Map<String, List<RankingEdit>> sbi_test_edits;
	public static Map<String, List<RankingEdit>> jaccard_test_edits;
	public static Map<String, List<RankingEdit>> ochiai_test_edits;
	public static String selected_test;
	public static char selected_tech;

	public RankingEditContentProvider() {
		super();
	}
	public static void initialize(){
		File unranked_file = new File(GlobalData.workspace_path
				+ File.separator + GlobalData.proj2.getElementName()
				+ File.separator + LocalizationProperties.AFFECTCHANGE_DIR);

		File t_file = new File(GlobalData.workspace_path + File.separator
				+ GlobalData.proj2.getElementName() + File.separator
				+ LocalizationProperties.TARANTULA_DIR);

		File s_file = new File(GlobalData.workspace_path + File.separator
				+ GlobalData.proj2.getElementName() + File.separator
				+ LocalizationProperties.SBI_DIR);

		File j_file = new File(GlobalData.workspace_path + File.separator
				+ GlobalData.proj2.getElementName() + File.separator
				+ LocalizationProperties.JACCARD_DIR);

		File o_file = new File(GlobalData.workspace_path + File.separator
				+ GlobalData.proj2.getElementName() + File.separator
				+ LocalizationProperties.OCHIAI_DIR);

		unranked_test_edits = LocalizationIOUtils
				.loadRankedEditsFromDir(unranked_file);
		if (t_file.exists())
			tarantula_test_edits = LocalizationIOUtils
					.loadRankedEditsFromDir(t_file);
		if (s_file.exists())
			sbi_test_edits = LocalizationIOUtils.loadRankedEditsFromDir(s_file);
		if (j_file.exists())
			jaccard_test_edits = LocalizationIOUtils
					.loadRankedEditsFromDir(j_file);
		if (o_file.exists())
			ochiai_test_edits = LocalizationIOUtils
					.loadRankedEditsFromDir(o_file);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			List<?> input = (List<?>) inputElement;
			return input.toArray();
		}
		return new Object[0];
	}

	public static List<RankingEdit> getRankingEdits() {
		switch (selected_tech) {
		case 'T':
			return tarantula_test_edits.get(selected_test);
		case 'S':
			return sbi_test_edits.get(selected_test);
		case 'J':
			return jaccard_test_edits.get(selected_test);
		case 'O':
			return ochiai_test_edits.get(selected_test);
		default:
			return unranked_test_edits.get(selected_test);
		}
	}
}
