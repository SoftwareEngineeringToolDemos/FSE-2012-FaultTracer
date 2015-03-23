package tracer.differencing.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tracer.differencing.core.data.AtomicChange;
import tracer.differencing.core.data.GlobalData;
import tracer.ui.DifferencingPopupAction;

public class ModelConstructor {
	private List<GraphNode> nodes;
	private List<GraphEdge> edges;
	private HashMap<String, GraphNode> nodeNaming;
	private HashMap<String, GraphEdge> edgeNaming;
	boolean filter = false;
	String path = DifferencingPopupAction.dir
			+ "ptest_tarantula_ranking12".replace("/", "\\");

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

		Set<String> focus = null;
		try {
			// focus = readAffectingChanges(path);
			focus = new HashSet<String>();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (AtomicChange c : worklist) {
			GraphNode node = null;
			if (c.getType().equals("CM")) {
				node = new GraphNode(c.toString(), c.getType(),
						c.getOldJavaElement(), c.getJavaElement(),
						c.oldContent, c.newContent);
			} else if (!c.getType().equals("LC") && !c.getType().equals("LCF")) {
				node = new GraphNode(c.toString(), c.getType(),
						c.getJavaElement());
			} else {
				node = new GraphNode(c.toString(), c.getType(), null);
			}
			nodeNaming.put(c.toString(), node);
			if (!filter || focus.contains(c.toString()))
				nodes.add(node);
		}
		for (AtomicChange c : worklist) {
			if (filter && !focus.contains(c.toString()))
				continue;
			GraphNode node = nodeNaming.get(c.toString());
			if (node == null)
				continue;
			for (String d : c.dependOns) {

				GraphNode node1 = nodeNaming.get(d);
				if (!nodes.contains(node1))
					nodes.add(node1);
				node.getDependants().add(node1);
			}
		}
	}

	public Set<String> readAffectingChanges(String path) throws IOException {
		Set<String> res = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while (line != null) {
			if (!line.startsWith("<TEST>"))
				res.add(line.split(": ")[0]);
			line = reader.readLine();
		}
		reader.close();
		return res;
	}

}
