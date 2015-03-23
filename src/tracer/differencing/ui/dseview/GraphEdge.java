package tracer.differencing.ui.dseview;

public class GraphEdge {
	private String name;
	private String type;
	private GraphNode caller;
	private GraphNode callee;
	private int num;

	public String getName() {
		return name;
	}

	public GraphNode getCaller() {
		return caller;
	}

	public GraphNode getCallee() {
		return callee;
	}

	public String getType() {
		return type;
	}

	public int getNum() {
		return num;
	}

	public void increaseNum() {
		this.num++;
	}

	public GraphEdge(String name, String type, GraphNode caller,
			GraphNode callee) {
		this.name = name;
		this.type = type;
		this.caller = caller;
		this.callee = callee;
		this.num = 1;
	}

}
