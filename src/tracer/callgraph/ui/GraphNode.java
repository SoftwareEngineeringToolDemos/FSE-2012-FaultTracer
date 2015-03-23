package tracer.callgraph.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

public class GraphNode {
	private String name;
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public IJavaElement getElem() {
		return elem;
	}

	public List<GraphNode> getCallees() {
		return callees;
	}
	public void setCallees(List<GraphNode> callees){
		this.callees=callees;
	}

	private String type;
	private IJavaElement elem;
	private List <GraphNode> callees;

	public GraphNode(String name, String type, IJavaElement elem) {
		this.name = name;
		this.elem = elem;
		this.type=type;
		this.callees=new ArrayList<GraphNode>();
	}
	

}
