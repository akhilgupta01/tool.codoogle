package gupta.akhil.tools.wls.tda;

import java.util.Stack;

import gupta.akhil.tools.common.config.SystemConfig;

public class ThreadSnapshotBuilder {
	static ThreadSnapshot buildThreadSnapshot(String serverName, Stack<String> callStack) {
		String threadName = callStack.remove(0);
		String rootMethod = callStack.pop();
		CallNode currentNode = new CallNode(rootMethod);
		ThreadSnapshot threadSnapshot = new ThreadSnapshot(serverName, threadName);
		threadSnapshot.setCallNode(currentNode);
		while(!callStack.isEmpty()){
			currentNode = currentNode.addMethodCall(callStack.pop());
		}
		threadSnapshot.setEntryPoint(resolveEntryMethod(threadSnapshot.getCallNode()));
		threadSnapshot.setCallType(resolveCallType(threadSnapshot.getCallNode()));
		threadSnapshot.setTimeStamp(System.currentTimeMillis());
		return threadSnapshot;
	}
	
	private static String resolveEntryMethod(CallNode callNode){
		String entryMethod = callNode.getMethodDesc();
		if(isIgnorable(entryMethod)){
			if(callNode.hasChildNode()){
				entryMethod = resolveEntryMethod(callNode.getChildNode());
			}else{
				entryMethod = null;
			}
		}
		return entryMethod;
	}
	
	private static String resolveCallType(CallNode callNode) {
		String callStackLine = callNode.getMethodDesc();
		String callType = "";
		if(callStackLine.contains("doFilter")){
			callType = "Web Page";
		}else if(callStackLine.contains("onMessage")){
			callType = "JMS Message";
		}else if(callStackLine.contains("weblogic.wsee.jaxws.HttpServletAdapter")){
			callType = "Web Service";
		}else if(callStackLine.contains("weblogic.webservice.server.servlet.WebServiceServlet")){
			callType = "Web Service";
		}else if(callStackLine.contains("weblogic.wsee.server.servlet.SoapProcessor")){
			callType = "Web Service";
		}
		else{
			if(callNode.hasChildNode()){
				callType = resolveCallType(callNode.getChildNode());
			}
		}
		return callType;
	}

	private static boolean isIgnorable(String callStackLine){
		boolean isIgnorable = false;
		for(String matchEx: SystemConfig.getThreadDumpConfig().getExcludedEntryPointPatterns()){
			if(callStackLine.matches(matchEx)){
				isIgnorable = true;
				break;
			}
		}
		return isIgnorable;
	}
}
