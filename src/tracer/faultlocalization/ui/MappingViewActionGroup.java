package tracer.faultlocalization.ui;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;

import tracer.faultlocalization.core.RankingEdit;

public class MappingViewActionGroup extends ActionGroup {

	public void fillActionBars(IActionBars actionBars) {
		if (actionBars == null)
			return;
		IToolBarManager toolBar = actionBars.getToolBarManager();
		toolBar.add(new TarantulaAction());
		toolBar.add(new SBIAction());
		toolBar.add(new JaccardAction());
		toolBar.add(new OchiaiAction());
	}

	public void fillContextMenu(IMenuManager menu) {
		if (menu == null)
			return;
		menu.add(new TarantulaAction());
		menu.add(new SBIAction());
		menu.add(new JaccardAction());
		menu.add(new OchiaiAction());
	}

	private class TarantulaAction extends Action {
		public TarantulaAction() {
			setText("Tarantula");
		}

		public void run() {
			RankingEditContentProvider.selected_tech = 'T';
			if(RankingEditContentProvider.tarantula_test_edits==null)
				RankingEditContentProvider.initialize();
			List<RankingEdit> list = RankingEditContentProvider
					.getRankingEdits();
			TestEditMapping.setEdits(list);
		}
	}

	private class SBIAction extends Action {
		public SBIAction() {
			setText("SBI");
		}

		public void run() {
			RankingEditContentProvider.selected_tech = 'S';
			if(RankingEditContentProvider.sbi_test_edits==null)
				RankingEditContentProvider.initialize();
			List<RankingEdit> list = RankingEditContentProvider
					.getRankingEdits();
			TestEditMapping.setEdits(list);
		}
	}

	private class JaccardAction extends Action {
		public JaccardAction() {
			setText("Jaccard");
		}

		public void run() {
			RankingEditContentProvider.selected_tech = 'J';
			if(RankingEditContentProvider.jaccard_test_edits==null)
				RankingEditContentProvider.initialize();
			List<RankingEdit> list = RankingEditContentProvider
					.getRankingEdits();
			TestEditMapping.setEdits(list);
		}
	}

	private class OchiaiAction extends Action {
		public OchiaiAction() {
			setText("Ochiai");
		}

		public void run() {
			RankingEditContentProvider.selected_tech = 'O';
			if(RankingEditContentProvider.ochiai_test_edits==null)
				RankingEditContentProvider.initialize();
			List<RankingEdit> list = RankingEditContentProvider
					.getRankingEdits();
			TestEditMapping.setEdits(list);
		}
	}

}
