package tracer.callgraph.ui;

import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;

import tracer.coverage.io.TracerUtils;

public class CallGraphActionGroup extends ActionGroup {
	public CallGraphView viewer;

	public CallGraphActionGroup(CallGraphView viewer) {
		this.viewer = viewer;
	}

	public void fillActionBars(IActionBars actionBars) {
		if (actionBars == null)
			return;
		IToolBarManager toolBar = actionBars.getToolBarManager();
		toolBar.add(new ChooseTestAction());
		toolBar.add(new oldECGAction());
		toolBar.add(new newECGAction());
		
	}

	public void fillContextMenu(IMenuManager menu) {
		if (menu == null)
			return;
		menu.add(new ChooseTestAction());
		menu.add(new oldECGAction());
		menu.add(new newECGAction());
		
	}

	private class oldECGAction extends Action {
		public oldECGAction() {
			setText("Old ECG");
		}

		public void run() {
			viewer.setInput(true);
		}
	}

	private class newECGAction extends Action {
		public newECGAction() {
			setText("New ECG");
		}

		public void run() {
			viewer.setInput(false);
		}
	}

	private class ChooseTestAction extends Action {
		public ChooseTestAction() {
			setText("Choose Test");
		}
		public void run() {
			Shell shell = new Shell();
			ChooseTestView dialog = new ChooseTestView(shell, true);
			Map<String, Map<String, Integer>> test_trace = TracerUtils
					.loadMethodTracesFromDirectoryForOldVersion();
			dialog.initialize(test_trace.keySet());
			dialog.setInitialPattern("");
			dialog.open();
			Object sel = dialog.getFirstResult();
			if (sel instanceof String) {
				String test = (String) sel;
				viewer.chosenTest = test;
				viewer.setInput(true);
			}
		}
	}

}
