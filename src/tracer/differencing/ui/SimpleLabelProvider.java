package tracer.differencing.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;

public class SimpleLabelProvider  extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof GraphNode){
			GraphNode myNode = (GraphNode) element;
			return myNode.getName();
		}
		if (element instanceof GraphEdge){
			GraphEdge myConnection = (GraphEdge) element;
			return myConnection.getName();
		}
		
		if (element instanceof EntityConnectionData){
			return "";
		}
		throw new RuntimeException("Wrong type: " + element.getClass().toString() );
	}
}
