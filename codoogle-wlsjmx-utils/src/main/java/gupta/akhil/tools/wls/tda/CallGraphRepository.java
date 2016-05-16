package gupta.akhil.tools.wls.tda;

import java.util.HashMap;
import java.util.Map;

public class CallGraphRepository {
	private static Map<String, CallNode> CALL_GRAPH_MAP = new HashMap<String, CallNode>();
	
	public static CallNode getGraph(String entryMethod){
		return CALL_GRAPH_MAP.get(entryMethod);
	}
	
	public static void addGraph(String entryMethod, CallNode callGraph){
		CALL_GRAPH_MAP.put(entryMethod, callGraph);
	}
	
	public static void printCallGraphs(){
		for(CallNode callNode: CALL_GRAPH_MAP.values()){
			System.out.println(callNode.getCallGraph());
			System.out.println();
			System.out.println("--------------------------------------------------------------------------------");
		}
	}
}
