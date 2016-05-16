package gupta.akhil.tools.wls.tda;


public class CallNode {
	private String methodDesc;
	private CallNode parent;
	private int count;
	private CallNode childNode;

	public CallNode(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
	
	public void incrementCount(){
		this.count++;
	}

	public String getMethodDesc() {
		return methodDesc;
	}

	public void setParent(CallNode parent) {
		this.parent = parent;
	}

	public CallNode getParent() {
		return parent;
	}

	public int getDepth() {
		if (getParent() == null) {
			return 0;
		}
		return getParent().getDepth() + 1;
	}

	public CallNode addMethodCall(String method) {
		CallNode childNode = new CallNode(method);
		childNode.setParent(this);
		this.childNode = childNode;
		return childNode;
	}
	
	public CallNode getChildNode(){
		return childNode;
	}
	
	public boolean hasChildNode(){
		return !(this.childNode == null);
	}

	public String getCallGraph() {
		return getCallGraph(this, 0);
	}

	public String getCallGraph(CallNode callNode, int depth) {
		StringBuffer sb = new StringBuffer();
		if (depth > 0) {
			sb.append("\n");
		}
		sb.append(makeTabs(depth)).append(callNode.getMethodDesc());
		if(callNode.hasChildNode()){
			sb.append(getCallGraph(callNode.getChildNode(), depth + 1));
		}
		return sb.toString();
	}

	private String makeTabs(int depth) {
		String tabString = "";
		for (int i = 0; i < depth; i++) {
			tabString += " ";
		}
		return tabString;
	}
}
