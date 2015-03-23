package tracer.differencing.ui.dseview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import tracer.differencing.core.data.CompareInput;
import tracer.differencing.core.data.GlobalData;
import tracer.utils.CreateType;

public class ChangeDependencyView extends ViewPart implements
		IZoomableWorkbenchPart {

	private Graph graph;
	private int layout = 1;
	private GraphViewer viewer;
	private Font searchFont;
	private GraphLabelProvider label;
	private GraphContentProvider content;
	private ModelConstructor model;
	private Action focusDialogAction;
	private Action springLayoutAction;
	private Action gridLayoutAction;
	private Action treeLayoutAction;
	private Action htreeLayoutAction;
	private Action radialLayoutAction;
	private Action applyAction;
	private Action unapplyAction;
	private Action searchAction;
	private Action deleteAction;
	private Action ignoreAction;
	private HashSet<GraphNode> deleteNodes=new HashSet<GraphNode> ();

	public void createPartControl(Composite parent) {

		viewer = new GraphViewer(parent, SWT.BORDER);
		this.content = new GraphContentProvider();
		viewer.setContentProvider(this.content);
		this.label = new GraphLabelProvider(this.viewer, null);
		// this.label = new SimpleLabelProvider();
		viewer.setLabelProvider(this.label);
		model = new ModelConstructor();
		viewer.setInput(model.getNodes());

		// viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		LayoutAlgorithm layout = setLayout();
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();
		makeActions();
		fillToolBar();
		hookContextMenu();
		hookDoubleClickAction();
		FontData fontData = Display.getCurrent().getSystemFont().getFontData()[0];
		fontData.height = 42;

		searchFont = new Font(Display.getCurrent(), fontData);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				Object selectedElement = ((IStructuredSelection) event
						.getSelection()).getFirstElement();
				if (selectedElement instanceof EntityConnectionData) {
					return;
				}
				ChangeDependencyView.this.selectionChanged(selectedElement);
			}
		});

		viewer.refresh();

	}

	private void selectionChanged(Object selectedItem) {
		label.setCurrentSelection(selectedItem);
		// viewer.update(label.getElements(currentNode), null);
	}

	private LayoutAlgorithm setLayout() {
		LayoutAlgorithm layout;
		// layout = new
		// SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;

	}

	private void fillToolBar() {
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(
				this);
		IActionBars bars = getViewSite().getActionBars();
		IMenuManager menuMgr = bars.getMenuManager();
		menuMgr.add(toolbarZoomContributionViewItem);
		IToolBarManager toolBar = bars.getToolBarManager();
		toolBar.add(deleteAction);
		toolBar.add(ignoreAction);
		//toolBar.add(applyAction);
		// menuMgr.add(treeLayoutAction);
		// menuMgr.add(springLayoutAction);
		// menuMgr.add(gridLayoutAction);
		// menuMgr.add(htreeLayoutAction);
		// menuMgr.add(radialLayoutAction);

	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void makeActions() {
		applyAction = new Action() {
			public void run() {
				HashSet<GraphNode> set = label.interestingDependencies;
				try {
					for (GraphNode node : set) {
						IJavaProject proj1 = GlobalData.proj1;
						if (node.getType().equals("AM")) {
							IJavaElement elem = node.getElem();
							IType type2 = null;

							if (elem instanceof IMethod) {
								IMethod meth2 = (IMethod) elem;
								type2 = meth2.getDeclaringType();
								IType type1 = proj1.findType(type2
										.getFullyQualifiedName());
								if (type1 == null) {
									IType parent2 = type2.getDeclaringType();
									Stack<IType> s = new Stack<IType>();
									while (parent2 != null) {
										s.push(parent2);
										parent2 = parent2.getDeclaringType();
									}
									while (!s.isEmpty()) {
										IType in_type2 = s.pop();
										IType in_type1 = proj1
												.findType(in_type2
														.getFullyQualifiedName());
										if (in_type1 == null) {
											in_type1 = CreateType.createType(
													proj1, in_type2);
										}
									}
									type1 = CreateType.createType(proj1, type2);
								}

								type1.createMethod(meth2.getSource(), null,
										true, null);

							}

						} else if (node.getType().equals("DM")) {
							IJavaElement elem = node.getElem();
							IType type1 = null;
							if (elem instanceof IMethod) {
								IMethod meth1 = (IMethod) elem;
								meth1.delete(true, null);
							}

						} else if (node.getType().equals("CM")) {
							IJavaElement elem = node.getElem();
							IType type2 = null;
							if (elem instanceof IMethod) {
								IMethod meth2 = (IMethod) elem;
								type2 = meth2.getDeclaringType();
								IType type1 = proj1.findType(type2
										.getFullyQualifiedName());
								if (type1 != null) {
									IMethod meth1 = type1.getMethod(
											meth2.getElementName(),
											meth2.getParameterTypes());
									meth1.delete(true, null);
									type1.createMethod(meth2.getSource(), null,
											true, null);
								}
							}

						} else if (node.getType().equals("AF")) {
							IJavaElement elem = node.getElem();
							IType type2 = null;
							if (elem instanceof IField) {
								IField f2 = (IField) elem;
								type2 = f2.getDeclaringType();
								IType type1 = proj1.findType(type2
										.getFullyQualifiedName());
								if (type1 == null) {
									IType parent2 = type2.getDeclaringType();
									Stack<IType> s = new Stack<IType>();
									while (parent2 != null) {
										s.push(parent2);
										parent2 = parent2.getDeclaringType();
									}
									while (!s.isEmpty()) {
										IType in_type2 = s.pop();
										IType in_type1 = proj1
												.findType(in_type2
														.getFullyQualifiedName());
										if (in_type1 == null) {
											in_type1 = CreateType.createType(
													proj1, in_type2);
										}
									}
									type1 = CreateType.createType(proj1, type2);
								}
								type1.createField(f2.getSource(), null, true,
										null);
							}

						} else if (node.getType().equals("DF")) {
							IJavaElement elem = node.getElem();
							if (elem instanceof IField) {
								IField f1 = (IField) elem;
								f1.delete(true, null);
							}

						} else if (node.getType().equals("CFI")
								|| node.getType().equals("CSFI")) {
							IJavaElement elem = node.getElem();
							IType type2 = null;
							if (elem instanceof IField) {
								IField f2 = (IField) elem;
								type2 = f2.getDeclaringType();
								IType type1 = proj1.findType(type2
										.getFullyQualifiedName());
								IField f1 = type1.getField(f2.getElementName());
								f1.delete(true, null);
								if (type1 != null)
									type1.createField(f2.getSource(), null,
											true, null);
							}

						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		applyAction.setText("Apply");
		applyAction
				.setToolTipText("Apply changes to construct intermediate version");

		deleteAction = new Action() {
			public void run() {
				GraphNode node = (GraphNode) label.getSelected();

				model.getNodes().remove(node);
				for (GraphNode n : model.getNodes()) {
					if (n.getDependants().contains(node))
						n.getDependants().remove(node);
				}
				viewer.setInput(model.getNodes());
				//viewer.refresh();

				String change = node.getName();
				String space = Platform.getLocation().toString();
				String diffPath = space + "/"
						+ GlobalData.proj1.getElementName() + "-"
						+ GlobalData.proj2.getElementName() + ".adiff";
				diffPath = diffPath.replace("/", "\\");
				try {
					BufferedReader reader = new BufferedReader(new FileReader(
							diffPath));
					String line = reader.readLine();
					StringBuilder content = new StringBuilder();
					while (line != null) {
						if (!line.contains(change)) {
							content.append(line + "\n");
						} else {
							if (!line.startsWith(change)) {
								String depender = line.substring(0,
										line.indexOf("=>"));
								String[] dependees = line.substring(
										line.indexOf("=>") + 2).split(", ");
								String filteredLine = depender + "=>";
								for (String dependee : dependees) {
									if (!dependee.equals(change))
										filteredLine = filteredLine + dependee
												+ ",";
								}
								content.append(filteredLine + "\n");
							}
						}
						line = reader.readLine();
					}
					reader.close();
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							diffPath));
					writer.write(content.toString());
					writer.flush();
					writer.close();
				} catch (IOException e) {
				}
			}
		};
		deleteAction.setText("Delete");
		deleteAction.setToolTipText("Delete");

		ignoreAction = new Action() {
			public void run() {
				GraphNode node = (GraphNode) label.getSelected();

				model.getNodes().remove(node);
				for (GraphNode n : model.getNodes()) {
					if (n.getDependants().contains(node))
						n.getDependants().remove(node);
				}
				viewer.setInput(model.getNodes());
				//viewer.refresh();

				
			}
		};
		ignoreAction.setText("Ignore");
		ignoreAction.setToolTipText("Ignore");

		
		treeLayoutAction = new Action() {
			public void run() {
				LayoutAlgorithm layout = new TreeLayoutAlgorithm(
						LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				viewer.setLayoutAlgorithm(layout, true);
				viewer.applyLayout();

			}
		};
		treeLayoutAction.setText("TreeLayout");
		treeLayoutAction.setToolTipText("TreeLayout");
		// treeLayoutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		springLayoutAction = new Action() {
			public void run() {
				LayoutAlgorithm layout = new SpringLayoutAlgorithm(
						LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				viewer.setLayoutAlgorithm(layout, true);
				viewer.applyLayout();

			}
		};
		springLayoutAction.setText("SpringLayout");
		springLayoutAction.setToolTipText("SpringLayout");
		// treeLayoutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		gridLayoutAction = new Action() {
			public void run() {
				LayoutAlgorithm layout = new GridLayoutAlgorithm(
						LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				viewer.setLayoutAlgorithm(layout, true);
				viewer.applyLayout();

			}
		};
		gridLayoutAction.setText("GridLayout");
		gridLayoutAction.setToolTipText("GridLayout");
		// treeLayoutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		htreeLayoutAction = new Action() {
			public void run() {
				LayoutAlgorithm layout = new HorizontalTreeLayoutAlgorithm(
						LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				viewer.setLayoutAlgorithm(layout, true);
				viewer.applyLayout();

			}
		};
		htreeLayoutAction.setText("HorizontalTreeLayout");
		htreeLayoutAction.setToolTipText("HorizontalTreeLayout");
		// treeLayoutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		radialLayoutAction = new Action() {
			public void run() {
				LayoutAlgorithm layout = new RadialLayoutAlgorithm(
						LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				viewer.setLayoutAlgorithm(layout, true);
				viewer.applyLayout();

			}
		};
		radialLayoutAction.setText("RadialLayout");
		radialLayoutAction.setToolTipText("RadialLayout");
		// treeLayoutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.add(treeLayoutAction);
		menuMgr.add(springLayoutAction);
		menuMgr.add(gridLayoutAction);
		menuMgr.add(htreeLayoutAction);
		menuMgr.add(radialLayoutAction);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
		getSite().setSelectionProvider(viewer);
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				try {
					if (obj instanceof GraphNode) {
						IJavaProject proj1 = GlobalData.proj1;
						GraphNode node = (GraphNode) obj;
						if (node.getType().equals("CM")) {
							IJavaElement elem = node.getElem();

							IMethod meth2 = (IMethod) elem;

							IJavaElement oldElem = node.getOldElem();

							IMethod meth1 = (IMethod) oldElem;
							IEditorPart editor1 = JavaUI
									.openInEditor((IJavaElement) meth1);
							IEditorPart editor2 = JavaUI
									.openInEditor((IJavaElement) meth2);

							CompareUI.openCompareEditor(new CompareInput(
									node.oldContent, node.newContent));

						} else if (node.getType().equals("CFI")
								|| node.getType().equals("CSFI")) {
							IJavaElement elem = node.getElem();
							IField field2 = (IField) elem;
							IType type2 = field2.getDeclaringType();
							IType type1 = proj1.findType(type2
									.getFullyQualifiedName());
							IField field1 = type1.getField(field2
									.getElementName());
							IEditorPart editor1 = JavaUI
									.openInEditor((IJavaElement) field1);
							IEditorPart editor2 = JavaUI
									.openInEditor((IJavaElement) field2);

						}

						else if (!node.getType().equals("LC")) {
							IJavaElement elem = node.getElem();

							IEditorPart editor = JavaUI
									.openInEditor((IJavaElement) elem);

						}
					}
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
