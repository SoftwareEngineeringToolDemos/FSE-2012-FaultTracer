package tracer.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import tracer.differencing.core.data.AtomicChange;
import tracer.differencing.core.data.DependentGraphConstructor;
import tracer.differencing.core.data.GlobalData;
import tracer.differencing.core.diff.DiffVisitor;
import tracer.differencing.core.pairs.ProjectPair;
import tracer.faulttracer.utils.FaultTracerProperties;

public class DifferencingPopupAction implements IObjectActionDelegate {

	private ISelection selection;
	public static String dir = "";

	@Override
	public void run(IAction action) {

		if (!(this.selection instanceof IStructuredSelection)) {
			System.out.println("select projects in order to difference!");
			return;
		}

		IStructuredSelection structuredSelection = (IStructuredSelection) this.selection;
		List list = structuredSelection.toList();
		int num = list.size();
		for (int it = 0; it < num - 1; it++) {
			GlobalData.data.clear();
			GlobalData.atomicData.clear();
			GlobalData.overridden_rel.clear();

			IJavaProject proj1 = (IJavaProject) list.get(it);
			IJavaProject proj2 = (IJavaProject) list.get(it + 1);

			// IJavaProject proj1 = (IJavaProject) list.get(1);
			// IJavaProject proj2 = (IJavaProject) list.get(0);
			GlobalData.proj1 = proj1;
			GlobalData.proj2 = proj2;

			GlobalData.projNames.add(proj1.getElementName());
			GlobalData.projNames.add(proj2.getElementName());

			ProjectPair projPair = new ProjectPair(proj1, proj2);

			// RefactoringInferenceVisitor diff = new
			// RefactoringInferenceVisitor();
			DiffVisitor diff = new DiffVisitor();

			try {
				projPair.accept(diff);
			} catch (JavaModelException e) {

				System.out.println("JavaModel Exception!");

				e.printStackTrace();
			}
			GlobalData.workspace_path = Platform.getLocation().toString();
			dir = GlobalData.workspace_path + File.separator
					+ proj2.getElementName() + File.separator;
			File tracer_dir = new File(dir + FaultTracerProperties.TRACER_DIR);
			if (!tracer_dir.exists())
				tracer_dir.mkdir();
			serializeAtomicChanges();
			//serializeAdditionMapping();
			//serializeFieldScope(proj1);
		}
	}
/*
	private void serializeAdditionMapping() {
		try {
			String addMapPath = dir + FaultTracerProperties.TRACER_DIR
					+ File.separator + FiFlProperties.ADD_MAPPING;

			BufferedWriter writer = new BufferedWriter(new FileWriter(
					addMapPath));
			Map<String, String> overrideInfo = ConstructOverridingInfo
					.construct(GlobalData.atomicData);
			for (String key : overrideInfo.keySet()) {
				writer.write(key + "-" + overrideInfo.get(key) + "\n");
			}
			System.out.println("# Overrides:" + overrideInfo.size());
			writer.flush();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private void serializeAtomicChanges() {
		try {
			String atomicDiffPath = dir + FaultTracerProperties.TRACER_DIR
					+ File.separator + FaultTracerProperties.PROGRAM_EDIT_FILE;

			BufferedWriter writer = new BufferedWriter(new FileWriter(
					atomicDiffPath));

			List<AtomicChange> res = DependentGraphConstructor
					.constructDependenceGraph(GlobalData.atomicData);

			for (AtomicChange c : res) {
				StringBuilder out = new StringBuilder();
				out.append(c + "=>");
				for (String s : c.dependOns)
					out.append(s + ", ");
				System.out.println(out);
				writer.write(out.toString());
				writer.newLine();
			}
			writer.flush();
			writer.close();
			System.out.println("# AtomicChanges:" + res.size());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*public void serializeFieldScope(IJavaProject proj) {
		GlobalData.field_scope.clear();
		FieldScopeVisitor fieldVisitor = new FieldScopeVisitor();

		try {
			fieldVisitor.visit(proj);
		} catch (JavaModelException e) {
			System.out.println("JavaModel Exception!");
			e.printStackTrace();
		}
		String space = Platform.getLocation().toString();
		try {
			String fieldScopePath = dir + FaultTracerProperties.TRACER_DIR
			+ File.separator + FiFlProperties.FIELD_SCOPE;
			//fieldScopePath = fieldScopePath.replace("/", "\\");
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					fieldScopePath));
			for (String field : GlobalData.field_scope.keySet()) {
				String toWrite=field+"-";
				for(String line:GlobalData.field_scope.get(field))
					toWrite+=line+",";
				writer.write(toWrite+ "\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	public String getDirName(String proj) {
		return proj.substring(0, proj.length() - 3);
	}

}
