package tracer.callgraph.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
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
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import tracer.faultlocalization.ui.MappingViewActionGroup;

public class CallGraphView extends ViewPart implements IZoomableWorkbenchPart {

	private Graph graph;
	private int layout = 1;
	private GraphViewer viewer;
	private Font searchFont;
	private GraphLabelProvider label;
	private GraphContentProvider content;
	private Action focusDialogAction;
	private Action springLayoutAction;
	private Action gridLayoutAction;
	private Action treeLayoutAction;
	private Action htreeLayoutAction;
	private Action radialLayoutAction;
	
	public String chosenTest=null;

	public void createPartControl(Composite parent) {

		viewer = new GraphViewer(parent, SWT.BORDER);
		this.content = new GraphContentProvider();
		viewer.setContentProvider(this.content);
		this.label = new GraphLabelProvider(this.viewer, null);
		// this.label = new SimpleLabelProvider();
		viewer.setLabelProvider(this.label);
		ModelConstructor model = new ModelConstructor(chosenTest, true);
		viewer.setInput(model.getNodes());

		// viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		LayoutAlgorithm layout = setLayout();
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();
		fillToolBar();
		makeActions();
		hookContextMenu();
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
				CallGraphView.this.selectionChanged(selectedElement);
			}
		});

		viewer.refresh();
		CallGraphActionGroup actionGroup = new CallGraphActionGroup(this);
		fillViewAction(actionGroup);
		fillViewMenu(actionGroup);

	}

	private void fillViewMenu(CallGraphActionGroup actionGroup) {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		actionGroup.fillContextMenu(manager);
	}

	private void fillViewAction(CallGraphActionGroup actionGroup) {
		IActionBars bars = getViewSite().getActionBars();
		actionGroup.fillActionBars(bars);
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
		bars.getMenuManager().add(toolbarZoomContributionViewItem);

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

	public void setInput(boolean old) {
		ModelConstructor model = new ModelConstructor(chosenTest, old);
		viewer.setInput(model.getNodes());
		viewer.refresh();
	}

}
