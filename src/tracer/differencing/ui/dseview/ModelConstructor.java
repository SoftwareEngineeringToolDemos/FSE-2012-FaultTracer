package tracer.differencing.ui.dseview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tracer.differencing.core.data.AtomicChange;
import tracer.differencing.core.data.GlobalData;

public class ModelConstructor {
	private List<GraphNode> nodes;
	private List<GraphEdge> edges;
	private HashMap<String, GraphNode> nodeNaming;
	private HashMap<String, GraphEdge> edgeNaming;

	public List<GraphNode> getNodes() {
		return nodes;
	}

	public List<GraphEdge> getEdges() {
		return edges;
	}

	public ModelConstructor() {
		nodes = new ArrayList<GraphNode>();
		edges = new ArrayList<GraphEdge>();
		nodeNaming = new HashMap<String, GraphNode>();
		edgeNaming = new HashMap<String, GraphEdge>();
		List<AtomicChange> worklist = GlobalData.atomicData;

		for (AtomicChange c : worklist) {
			GraphNode node = null;
			if (c.getType().equals("CM")) {
				node = new GraphNode(c.toString(), c.getType(),
						c.getOldJavaElement(), c.getJavaElement(),
						c.oldContent, c.newContent);
				nodes.add(node);
			} else if (!c.getType().equals("LC") && !c.getType().equals("LCF"))
				node = new GraphNode(c.toString(), c.getType(),
						c.getJavaElement());

			nodeNaming.put(c.toString(), node);
		}

		for (AtomicChange c : worklist) {
			GraphNode node = nodeNaming.get(c.toString());
			if (c.getType().equals("CM")) {
				for (String d : c.dependOns) {
					GraphNode node1 = nodeNaming.get(d);
					if (!nodes.contains(node1))
						nodes.add(node1);
					node.getDependants().add(node1);
				}
			} else {
				for (String d : c.dependOns) {
					GraphNode node1 = nodeNaming.get(d);
					if (node1.getType().equals("CM")) {
						if(!nodes.contains(node))nodes.add(node);
						node.getDependants().add(node1);
					}
				}
			}
		}

	}

}
