package tracer.differencing.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

public class GraphLabelProvider implements IChangeLabelProvider,
		IConnectionStyleProvider, IEntityStyleProvider {

	public Color BLANK = new Color(Display.getDefault(), 255, 255, 255);

	public Color LIGHT_BLUE = new Color(Display.getDefault(), 216, 228, 248);
	public Color DARK_BLUE = new Color(Display.getDefault(), 1, 70, 122);
	public Color GREY_BLUE = new Color(Display.getDefault(), 139, 150, 171);
	public Color LIGHT_BLUE_CYAN = new Color(Display.getDefault(), 213, 243,
			255);
	public Color LIGHT_YELLOW = new Color(Display.getDefault(), 255, 255, 206);
	public Color GRAY = new Color(Display.getDefault(), 128, 128, 128);
	public Color LIGHT_GRAY = new Color(Display.getDefault(), 220, 220, 220);
	public Color BLACK = new Color(Display.getDefault(), 0, 0, 0);
	public Color RED = new Color(Display.getDefault(), 255, 0, 0);
	public Color DARK_RED = new Color(Display.getDefault(), 127, 0, 0);
	public Color ORANGE = new Color(Display.getDefault(), 255, 196, 0);
	public Color YELLOW = new Color(Display.getDefault(), 255, 255, 0);
	public Color GREEN = new Color(Display.getDefault(), 0, 255, 0);
	public Color DARK_GREEN = new Color(Display.getDefault(), 0, 127, 0);
	public Color LIGHT_GREEN = new Color(Display.getDefault(), 96, 255, 96);
	public Color CYAN = new Color(Display.getDefault(), 0, 255, 255);
	public Color BLUE = new Color(Display.getDefault(), 0, 0, 255);
	public Color WHITE = new Color(Display.getDefault(), 255, 255, 255);
	public Color EDGE_WEIGHT_0 = new Color(Display.getDefault(), 192, 192, 255);
	public Color EDGE_WEIGHT_01 = new Color(Display.getDefault(), 64, 128, 225);
	public Color EDGE_WEIGHT_02 = new Color(Display.getDefault(), 32, 32, 128);
	public Color EDGE_WEIGHT_03 = new Color(Display.getDefault(), 0, 0, 128);
	public Color EDGE_DEFAULT = new Color(Display.getDefault(), 64, 64, 128);
	public Color EDGE_HIGHLIGHT = new Color(Display.getDefault(), 192, 32, 32);
	public Color DISABLED = new Color(Display.getDefault(), 230, 240, 250);

	public Color Green_AM = new Color(Display.getDefault(), 0, 255, 127);
	public Color Red_DM = new Color(Display.getDefault(), 176, 23, 31);
	public Color Orange_CM = new Color(Display.getDefault(), 255, 140, 0);
	public Color Blue_AF = new Color(Display.getDefault(), 113, 113, 198);
	public Color Pink_DF = new Color(Display.getDefault(), 240, 128, 128);
	public Color Tan_CFI = new Color(Display.getDefault(), 210, 180, 140);
	public Color Brick_CSFI = new Color(Display.getDefault(), 156, 102, 31);
	public Color Gray_LC = new Color(Display.getDefault(), 133, 133, 133);
	public Color Gray_LCF = new Color(Display.getDefault(), 222, 222, 222);

	private Object selected = null;
	public HashSet interestingRelationships = new HashSet();
	public HashSet interestingDependencies = new HashSet();
	public HashSet deleted = new HashSet();
	private Color disabledColor = null;
	private GraphViewer viewer;

	public GraphLabelProvider(GraphViewer viewer,
			GraphLabelProvider currentProvider) {
		this.viewer = viewer;

	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof GraphNode) {

			GraphNode myNode = (GraphNode) element;
			// System.out.println(myNode.getName());
			return nameReducer(myNode.getName());
		}
		if (element instanceof GraphEdge) {
			System.out.println("execute here!");
			GraphEdge myConnection = (GraphEdge) element;
			return myConnection.getName();
		}

		if (element instanceof EntityConnectionData) {
			return "";
		}
		throw new RuntimeException("Wrong type: "
				+ element.getClass().toString());
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBorderColor(Object entity) {
		if (this.selected != null) {
			if (entity == this.selected) {
				return BLACK;
			} else if (interestingDependencies.contains(entity)) {
				return BLACK;
			} else {
				return LIGHT_GRAY;
			}

		}

		return BLACK;
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBorderWidth(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getBackgroundColour(Object entity) {

		if (entity == this.selected) {
			return viewer.getGraphControl().DEFAULT_NODE_COLOR;
		} else if (deleted.contains(entity)) {
			return this.BLANK;
		} else if (interestingDependencies.contains(entity)) {
			return viewer.getGraphControl().HIGHLIGHT_ADJACENT_COLOR;
		} else {
			GraphNode n = (GraphNode) entity;
			if (n.getType().equals("AM"))
				return this.Green_AM;
			else if (n.getType().equals("DM"))
				return this.Red_DM;
			else if (n.getType().equals("CM"))
				return this.Orange_CM;
			else if (n.getType().equals("AF"))
				return this.Blue_AF;
			else if (n.getType().equals("DF"))
				return this.Pink_DF;
			else if (n.getType().equals("CFI"))
				return this.Tan_CFI;
			else if (n.getType().equals("CSFI"))
				return this.Brick_CSFI;
			else if (n.getType().equals("LC"))
				return this.Gray_LC;
			else if (n.getType().equals("LCF"))
				return this.Gray_LCF;

			return getDisabledColor();
		}
	}

	private Color getDisabledColor() {
		if (disabledColor == null) {
			disabledColor = new Color(Display.getDefault(), new RGB(225, 238,
					255));
		}
		return disabledColor;
	}

	@Override
	public Color getForegroundColour(Object entity) {
		if (this.selected != null) {
			if (entity == this.selected) {
				return BLACK;
			} else if (interestingDependencies.contains(entity)) {
				return BLACK;
			} else {
				return GRAY;
			}

		}
		return BLACK;
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getConnectionStyle(Object rel) {

		return ZestStyles.CONNECTIONS_DIRECTED;
	}

	@Override
	public Color getColor(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return DARK_RED;
		}
		return BLACK;
	}

	@Override
	public Color getHighlightColor(Object rel) {
		// TODO Auto-generated method stub
		return DARK_RED;
	}

	@Override
	public int getLineWidth(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return 1;
		}
		return 1;
	}

	@Override
	public IFigure getTooltip(Object entity) {
		// TODO Auto-generated method stub
		if (entity instanceof GraphNode) {
			GraphNode node = (GraphNode) entity;
			return new Label(node.getName());
		} else if (entity instanceof EntityConnectionData) {
			EntityConnectionData conn = (EntityConnectionData) entity;
			GraphNode caller = (GraphNode) conn.source;
			GraphNode callee = (GraphNode) conn.dest;
			return new Label(caller.getName() + "-" + callee.getName());
		}
		return null;
	}

	public Object[] getInterestingEdges() {
		return interestingRelationships.toArray();
	}

	public void setCurrentSelection(Object currentSelection) {

		for (Iterator iter = interestingRelationships.iterator(); iter
				.hasNext();) {
			EntityConnectionData entityConnectionData = (EntityConnectionData) iter
					.next();
			viewer.unReveal(entityConnectionData);
		}
		for (Iterator iter = interestingDependencies.iterator(); iter.hasNext();) {
			GraphNode node = (GraphNode) iter.next();
			viewer.unReveal(node);
		}
		this.selected = currentSelection;

		interestingRelationships = new HashSet();
		interestingDependencies = new HashSet();
		if (this.selected != null) {
			calculateInterestingRelations(interestingRelationships,
					interestingDependencies);
		}

		Object[] connections = viewer.getConnectionElements();
		Object[] nodes = viewer.getNodeElements();
		for (Iterator iter = interestingRelationships.iterator(); iter
				.hasNext();) {
			Object entityConnectionData = iter.next();
			viewer.reveal(entityConnectionData);
		}
		for (Iterator iter = interestingDependencies.iterator(); iter.hasNext();) {
			GraphNode node = (GraphNode) iter.next();
			viewer.reveal(node);
		}
		for (int i = 0; i < connections.length; i++) {
			viewer.update(connections[i], null);
		}
		for (int i = 0; i < nodes.length; i++) {
			viewer.update(nodes[i], null);
		}

	}

	void calculateInterestingRelations(HashSet rels, HashSet entities) {
		if (getSelected() != null) {
			GraphNode sel = (GraphNode) getSelected();
			List<GraphNode> worklist = new ArrayList<GraphNode>();
			worklist.add(sel);
			entities.add(sel);
			while (!worklist.isEmpty()) {
				GraphNode cur = worklist.remove(0);
				//System.out.println(cur.getType()+":"+cur.getName());
				for (GraphNode n : cur.getDependants()) {
					EntityConnectionData conData = new EntityConnectionData(
							cur, n);
					rels.add(conData);
					entities.add(n);
					worklist.add(n);
				}
			}

		}

	}

	public Object getSelected() {
		return this.selected;
	}

	public String nameReducer(String s) {

		// map.put("[I", "int[]");

		String type = s.substring(0, s.indexOf(":") + 1);
		String classPath = s.substring(0, s.lastIndexOf("."));
		String elem = s.substring(s.lastIndexOf(".") + 1);
		// System.out.println("elem:"+elem);
		String className;
		if (classPath.contains("."))
			className = classPath.substring(classPath.lastIndexOf(".") + 1);
		else
			className = classPath.substring(classPath.indexOf(":") + 1);
		// System.out.println(s+"@"+elem);
		String elemName = elem.substring(0, elem.indexOf(":"));
		String desc = elem.substring(elem.indexOf(":") + 1);
		String reducedElem = "";
		/*
		 * if (desc.startsWith("(")) { String paras = desc.substring(1,
		 * desc.indexOf(")")); reducedElem += "("; if (paras.contains(";")) {
		 * String[] paralist = paras.split(";"); for (String fakePara :
		 * paralist) { int i = 0; while (i < fakePara.length()) { // TODO } } }
		 * int i = 0; while (i < desc.length()) {
		 * 
		 * } } else {
		 * 
		 * }
		 */
		return type + " " + className + "." + elem;

	}
}
