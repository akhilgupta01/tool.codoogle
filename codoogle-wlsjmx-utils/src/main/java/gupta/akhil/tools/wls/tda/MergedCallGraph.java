package gupta.akhil.tools.wls.tda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

public class MergedCallGraph {
	private MultiCallNode startNode;
	
	public MergedCallGraph(ThreadSnapshot threadSnapshot){
		CallNode callNode = threadSnapshot.getCallNode();
		startNode = new MultiCallNode(callNode.getMethodDesc());
		merge(callNode);
	}
	
	private MergedCallGraph(MultiCallNode mergedCallNode){
		startNode = mergedCallNode;
	}
	
	MultiCallNode getStartNode() {
		return startNode;
	}
	
	public void merge(CallNode callNode){
		MultiCallNode meultiCallNode = startNode;
		while(callNode.hasChildNode()){
			callNode = callNode.getChildNode();
			String methodCall = callNode.getMethodDesc();
			if(meultiCallNode.getChildNode(methodCall) == null){
				meultiCallNode = meultiCallNode.addMethodCall(methodCall);
			}else{
				meultiCallNode = meultiCallNode.getChildNode(methodCall);
			}
		}
	}
	
	public Set<String> getRemoteEJBCalls(){
		return startNode.getRemoteCalls();
	}

	public Set<String> getWebServiceInvocationPoints(){
		return startNode.getWebServiceInvocationPoints();
	}

	public String getCallGraph() {
		return MultiCallNode.getCallGraph(startNode, 0);
	}


	public static MergedCallGraph parse(String callGraph) {
		BufferedReader br = new BufferedReader(new StringReader(callGraph));
		String currentLine = null;
		MultiCallNode startNode = null;
		MultiCallNode currentNode = null;
		int depthOffset = 0;
		try {
			while((currentLine = br.readLine())!=null){
				if(!currentLine.trim().isEmpty()){
					if(currentNode == null){
						startNode = new MultiCallNode(currentLine.trim());
						currentNode = startNode;
						depthOffset = getLeadingSpaceCount(currentLine);
					}else{
						while(currentNode.getDepth() >= (getLeadingSpaceCount(currentLine) - depthOffset)){
							currentNode = currentNode.getParent();
						}
						currentNode = currentNode.addMethodCall(currentLine.trim());
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to parse..", e);
		}
		return new MergedCallGraph(startNode);
	}

	private static int getLeadingSpaceCount(String line) {
		String trimmedLine = line.trim();
		return line.indexOf(trimmedLine);
	}
}
