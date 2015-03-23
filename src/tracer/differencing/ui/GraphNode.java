package tracer.differencing.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;

import tracer.differencing.core.data.Constants;
import tracer.utils.GetNames;

public class GraphNode implements Constants{
	private String name;
	public String oldContent;
	public String newContent;
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public IJavaElement getElem() {
		return elem;
	}
	public IJavaElement getOldElem() {
		return oldElem;
	}

	public List<GraphNode> getDependants() {
		return dependants;
	}
	public void setDependants(List<GraphNode> dependants){
		this.dependants=dependants;
	}

	private String type;
	private IJavaElement elem;
	private IJavaElement oldElem;
	private List <GraphNode> dependants;

	public GraphNode(String name, String type, IJavaElement elem) {
		this.name = name;
		this.elem = elem;
		this.type=type;
		this.dependants=new ArrayList<GraphNode>();
	}
	public GraphNode(String name, String type,IJavaElement elem1, IJavaElement elem2,String oldContent, String newContent) {
		this.name = name;
		this.oldElem=elem1;
		this.elem = elem2;
		this.oldContent=oldContent;
		this.newContent=newContent;
		this.type=type;
		this.dependants=new ArrayList<GraphNode>();
	}
	@Override
	public String toString() {
		return name;
	}

}
