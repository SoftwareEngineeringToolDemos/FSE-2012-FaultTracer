package tracer.faultlocalization.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareUI;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import tracer.differencing.core.data.AtomicChange;
import tracer.differencing.core.data.CompareInput;
import tracer.differencing.core.data.GlobalData;
import tracer.faultlocalization.core.RankingEdit;
import tracer.test.selection.io.SelectionIOUtils;

public class TestEditMapping extends ViewPart implements ISelectionListener {
	public static TableViewer test;
	public static TableViewer edit;

	@Override
	public void createPartControl(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE
				| SWT.DOUBLE_BUFFERED);
		topComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		topComp.setLayout(new GridLayout(2, true));

		Composite t = new Composite(topComp, SWT.BORDER);
		Composite e = new Composite(topComp, SWT.BORDER);

		t.setLayout(new FillLayout());
		e.setLayout(new FillLayout());
		GridData data = new GridData(GridData.FILL_BOTH | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.DOUBLE_BUFFERED);
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		t.setLayoutData(data);
		e.setLayoutData(data);

		createTestList(t);
		createEditList(e);

		MappingViewActionGroup actionGroup = new MappingViewActionGroup();
		fillViewAction(actionGroup);
		fillViewMenu(actionGroup);

	}

	private void fillViewMenu(MappingViewActionGroup actionGroup) {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		actionGroup.fillContextMenu(manager);
	}

	private void fillViewAction(MappingViewActionGroup actionGroup) {
		IActionBars bars = getViewSite().getActionBars();
		actionGroup.fillActionBars(bars);
	}

	private void createTestList(Composite c) {
		test = new TableViewer(c, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.DOUBLE_BUFFERED);
		test.setContentProvider(new AffectedTestContentProvider());
		test.setLabelProvider(new AffectedTestLabelProvider());
		Table table = test.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		List<String> affected_tests = SelectionIOUtils
				.loadSelectedTestsForEclipse();
		test.setInput(affected_tests);
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(table, SWT.NONE).setText("AffectedTestName");

		test.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ISelection selected = event.getSelection();

				if (selected instanceof IStructuredSelection) {
					IStructuredSelection structSelection = (IStructuredSelection) selected;
					Object o = structSelection.getFirstElement();
					if (o instanceof String) {
						String test = (String) o;
						RankingEditContentProvider.selected_test = test;
						if (RankingEditContentProvider.tarantula_test_edits != null)
							RankingEditContentProvider.selected_tech = 'T';
						else
							RankingEditContentProvider.selected_tech = 'U';
						List<RankingEdit> list = RankingEditContentProvider
								.getRankingEdits();
						TestEditMapping.setEdits(list);
					}
				}
			}
		});

		// dndBuilder();
	}

	private void createEditList(Composite c) {
		edit = new TableViewer(c, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.DOUBLE_BUFFERED);
		edit.setContentProvider(new RankingEditContentProvider());
		RankingEditContentProvider.initialize();
		edit.setLabelProvider(new RankingEditLabelProvider());
		Table table = edit.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		edit.setInput(new ArrayList<RankingEdit>());
		TableLayout layout = new TableLayout();
		table.setLayout(layout);
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(table, SWT.NONE).setText("Type");
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(table, SWT.NONE).setText("Element");
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(table, SWT.NONE).setText("SuspVal");
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(table, SWT.NONE).setText("HeurVal");

		edit.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ISelection selected = event.getSelection();

				if (selected instanceof IStructuredSelection) {
					IStructuredSelection structSelection = (IStructuredSelection) selected;
					Object o = structSelection.getFirstElement();
					if (o instanceof RankingEdit) {
						RankingEdit ranking_edit = (RankingEdit) o;
						String edit = ranking_edit.getEdit();
						for (AtomicChange chg : GlobalData.atomicData) {
							if (chg.toString().equals(edit)) {
								try {
									IJavaProject proj1 = GlobalData.proj1;
									if (chg.getType().equals("CM")) {
										IJavaElement elem = chg
												.getJavaElement();

										IMethod meth2 = (IMethod) elem;

										IJavaElement oldElem = chg
												.getOldJavaElement();

										IMethod meth1 = (IMethod) oldElem;
										IEditorPart editor1 = JavaUI
												.openInEditor((IJavaElement) meth1);
										IEditorPart editor2 = JavaUI
												.openInEditor((IJavaElement) meth2);

										CompareUI
												.openCompareEditor(new CompareInput(
														chg.oldContent,
														chg.newContent));

									} else if (chg.getType().equals("CFI")
											|| chg.getType().equals("CSFI")) {
										IJavaElement elem = chg
												.getJavaElement();
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

									else if (!chg.getType().equals("LC")) {
										IJavaElement elem = chg
												.getJavaElement();
										IEditorPart editor = JavaUI
												.openInEditor((IJavaElement) elem);

									}
								} catch (PartInitException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JavaModelException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
					}
				}

			}
		});

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	public void dndBuilder() {
		Transfer[] type = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK
				| DND.DROP_NONE;
		final DragSource source = new DragSource(edit.getTable(), operations);
		source.setTransfer(type);
		final TableItem[] dragSourceItem = new TableItem[1];
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				TableItem[] selection = edit.getTable().getSelection();
				if (selection.length > 0) {
					event.doit = true;
					dragSourceItem[0] = selection[0];
				} else {
					event.doit = false;
				}
			};

			public void dragSetData(DragSourceEvent event) {
				event.data = dragSourceItem[0].getText();
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE) {
					dragSourceItem[0].dispose();
					dragSourceItem[0] = null;
				}
			}
		});

		DropTarget target = new DropTarget(edit.getTable(), operations);
		target.setTransfer(type);
		target.addDropListener(new DropTargetAdapter() {
			private TableItem initTableItem(TableItem sitem, TableItem ditem) {
				for (int i = 0; i < edit.getTable().getColumnCount(); i++) {
					ditem.setText(i, sitem.getText(i));
				}
				ditem.setData(sitem.getData());
				return ditem;
			}

			private boolean checkValidate(TableItem sitem, TableItem ditem) {
				if (ditem.equals(sitem))
					return false;
				return true;
			}

			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_NONE | DND.FEEDBACK_SCROLL;
				if (event.item != null) {
					TableItem item = (TableItem) event.item;
					if (!checkValidate(dragSourceItem[0], item)) {
						event.detail = DND.DROP_NONE;
						return;
					} else {
						event.detail = DND.DROP_MOVE;
					}
					Point pt = Display.getDefault().getShells()[0].getDisplay()
							.map(null, edit.getTable(), event.x, event.y);
					Rectangle bounds = item.getBounds();
					if (pt.y < bounds.y + bounds.height / 2) {
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y >= bounds.y + bounds.height / 2) {
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					}
				}
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (event.item == null) {
					TableItem item = new TableItem(edit.getTable(), SWT.NONE);
					item = initTableItem(dragSourceItem[0], item);
				} else {
					TableItem item = (TableItem) event.item;
					Point pt = Display.getDefault().getShells()[0].getDisplay()
							.map(null, edit.getTable(), event.x, event.y);
					Rectangle bounds = item.getBounds();
					TableItem[] items = edit.getTable().getItems();
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					if (pt.y < bounds.y + bounds.height / 2) {
						TableItem newItem = new TableItem(edit.getTable(),
								SWT.NONE, index);
						newItem = initTableItem(dragSourceItem[0], newItem);

					} else if (pt.y >= bounds.y + bounds.height / 2) {
						TableItem newItem = new TableItem(edit.getTable(),
								SWT.NONE, index + 1);
						newItem = initTableItem(dragSourceItem[0], newItem);
					}
				}

				edit.getTable().redraw();
			}
		});

	}

	public static void setEdits(List<RankingEdit> elements) {
		edit.setInput(elements);
		edit.refresh();
	}

}
