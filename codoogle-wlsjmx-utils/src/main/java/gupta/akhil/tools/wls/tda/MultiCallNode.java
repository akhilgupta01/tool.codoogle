package gupta.akhil.tools.wls.tda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiCallNode {
	private String methodDesc;
	private MultiCallNode parent;
	private List<MultiCallNode> childNodes = new ArrayList<MultiCallNode>();

	public MultiCallNode(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	
	public String getMethodDesc() {
		return methodDesc;
	}

	public void setParent(MultiCallNode parent) {
		this.parent = parent;
	}

	public MultiCallNode getParent() {
		return parent;
	}

	public List<MultiCallNode> getCalledMethods() {
		return childNodes;
	}

	public int getDepth() {
		if (getParent() == null) {
			return 0;
		}
		return getParent().getDepth() + 1;
	}


	public MultiCallNode addMethodCall(String method) {
		MultiCallNode childNode = new MultiCallNode(method);
		childNode.setParent(this);
		this.childNodes.add(childNode);
		return childNode;
	}
	
	public MultiCallNode getChildNode(String method){
		MultiCallNode result = null;
		for(MultiCallNode childNode: childNodes){
			if(childNode.getMethodDesc().equals(method)){
				result = childNode;
				break;
			}
		}
		return result;
	}

	public Set<String> getWebServiceInvocationPoints() {
		Set<String> remoteCalls = new HashSet<String>();
		if(!hasChildNodes()){
			String lastCall = getMethodDesc();
			if(lastCall.contains("SEIStub.invoke")){
				lastCall = getParent().getMethodDesc();
				remoteCalls.add(lastCall);
			}
		}else{
			for(MultiCallNode childNode: getChildNodes()){
				remoteCalls.addAll(childNode.getWebServiceInvocationPoints());
			}
		}
		return remoteCalls;
	}

	public Set<String> getRemoteCalls(){
		Set<String> remoteCalls = new HashSet<String>();
		if(!hasChildNodes()){
			String lastCall = getMethodDesc();
			if(lastCall.contains("_") && !lastCall.contains("LProduct_ext")){
				remoteCalls.add(lastCall);
			}
		}else{
			for(MultiCallNode childNode: getChildNodes()){
				remoteCalls.addAll(childNode.getRemoteCalls());
			}
		}
		return remoteCalls;
	}
	
	public List<MultiCallNode> getChildNodes() {
		return childNodes;
	}
	
	public boolean hasChildNodes(){
		return !this.childNodes.isEmpty();
	}


	public static String getCallGraph(MultiCallNode callNode, int depth) {
		StringBuffer sb = new StringBuffer();
		if (depth > 0) {
			sb.append("\n");
		}
		sb.append(makeTabs(depth)).append(callNode.getMethodDesc());
		for (MultiCallNode childNode : callNode.getCalledMethods()) {
			sb.append(getCallGraph(childNode, depth + 1));
		}
		return sb.toString();
	}

	private static String makeTabs(int depth) {
		String tabString = "";
		for (int i = 0; i < depth; i++) {
			tabString += " ";
		}
		return tabString;
	}
}
