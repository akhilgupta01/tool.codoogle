package gupta.akhil.tools.indexer.calltrace;

import gupta.akhil.tools.indexer.calltrace.MergedCallGraph;
import gupta.akhil.tools.wls.tda.MultiCallNode;

public class HTMLPrettyPrinter {
	public static void main(String[] args) {
		MergedCallGraph mergedCallGraph = MergedCallGraph.parse("call1\n call2_WLStub\n  call3\n call4");
		System.out.println(mergedCallGraph.getCallGraph());
		System.out.println(HTMLPrettyPrinter.getPrettyHTMLTree(mergedCallGraph));
	}
	public static String getPrettyHTMLTree(MergedCallGraph mergedCallGraph){
		MultiCallNode startNode = mergedCallGraph.getStartNode();
		return getCallGraph(startNode, 0);
	}
	
	private static String getCallGraph(MultiCallNode callNode, int depth) {
		StringBuffer sb = new StringBuffer();
		if (depth == 0) {
			sb.append("<ul>");
		}
		sb.append("<li class=\"expanded\">");
		sb.append(getPrettyStartTag(callNode));
		sb.append(callNode.getMethodDesc());
		sb.append(getPrettyEndTag(callNode));
		if(callNode.hasChildNodes()){
			sb.append("<ul>");
			for (MultiCallNode childNode : callNode.getCalledMethods()) {
				sb.append(getCallGraph(childNode, depth + 1));
			}
			sb.append("</ul>");
		}
		sb.append("</li>");
		if (depth == 0) {
			sb.append("</ul>");
		}
		return sb.toString();
	}
	
	private static String getPrettyEndTag(MultiCallNode callNode) {
		String tag = "";
		String lastCall = callNode.getMethodDesc();
		if(lastCall.contains("WLStub") || lastCall.contains("EOImpl") || lastCall.contains("WLSkel")){
			tag = "</b>";
		}else if(invokesWS(callNode)){
			tag = "</b>";
		}
		return tag;
	}

	private static String getPrettyStartTag(MultiCallNode callNode) {
		String tag = "";
		String lastCall = callNode.getMethodDesc();
		if(lastCall.contains("WLStub") || lastCall.contains("EOImpl") || lastCall.contains("WLSkel")){
			tag = "<b>";
		}else if(invokesWS(callNode)){
			tag = "<b>";
		}
		return tag;
	}

	private static boolean invokesWS(MultiCallNode callNode) {
		boolean invokesWS = false;
		for(MultiCallNode multiNode: callNode.getChildNodes()){
			if(multiNode.getMethodDesc().contains("SEIStub.invoke")){
				invokesWS = true;
			}
		}
		return invokesWS;
	}
}
