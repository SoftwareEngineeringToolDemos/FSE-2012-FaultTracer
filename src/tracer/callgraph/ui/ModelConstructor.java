package tracer.callgraph.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ModelConstructor {
	public static Logger logger = Logger.getLogger(ModelConstructor.class);
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

	public ModelConstructor(String chosenTest, boolean before) {
		nodes = new ArrayList<GraphNode>();
		edges = new ArrayList<GraphEdge>();
		nodeNaming = new HashMap<String, GraphNode>();
		edgeNaming = new HashMap<String, GraphEdge>();
		
		ConstructUtils.initialize(chosenTest, before);
		for (String content : ConstructUtils.call_relation) {
			String callerName = getCaller(content);
			if (callerName.contains("StartFakeCaller"))
				continue;
			GraphNode caller = nodeNaming.get(callerName);
			if (caller == null) {
				caller = new GraphNode(callerName, "Method", null);
				nodeNaming.put(callerName, caller);
				nodes.add(caller);
			}
			String calleeName = getCallee(content);
			GraphNode callee = nodeNaming.get(calleeName);
			if (callee == null) {
				if (content.startsWith("<FW>") || content.startsWith("FR"))
					callee = new GraphNode(calleeName, "Field", null);
				else
					callee = new GraphNode(calleeName, "Method", null);
				nodeNaming.put(calleeName, callee);
				nodes.add(callee);
			}
			logger.debug(">>>>" + callerName + "->" + calleeName);
			String edgeName = getRelation(content);
			GraphEdge edge = edgeNaming.get(edgeName);
			if (edge != null) {
				edges.remove(edge);
				edge.increaseNum();
				edges.add(edge);
			} else {
				if (content.startsWith("<FW>"))
					edge = new GraphEdge(edgeName, "FW", caller, callee);// "FW"
				else if (content.startsWith("<FR>"))
					edge = new GraphEdge(edgeName, "FR", caller, callee);// "FR"
				else
					edge = new GraphEdge(edgeName, "MC", caller, callee);// "MC"
				edgeNaming.put(edgeName, edge);
				edges.add(edge);
				caller.getCallees().add(callee);
			}
		}

	}

	public static String getCaller(String line) {
		return line.substring(line.indexOf(">") + 1).split("-")[0];
	}

	public static String getCallee(String line) {
		return line.substring(line.indexOf(">") + 1).split("-")[1];
	}

	public static String getRelation(String line) {
		return line.substring(line.indexOf(">") + 1);
	}

}
