package gupta.akhil.tools.indexer.calltrace;

import java.util.HashMap;
import java.util.Map;

import gupta.akhil.tools.indexer.calltrace.model.CallTrace;

public class CallTraceCache {
	private static final Map<String, CallTrace> THREAD_MAP = new HashMap<String, CallTrace>();

	public CallTraceCache(){
		
	}
	
	public CallTrace get(String serverCluster, String entryMethod){
		return THREAD_MAP.get(getKey(serverCluster, entryMethod));
	}
	
	private String getKey(String serverCluster, String entryMethod) {
		return serverCluster + "_" + entryMethod;
	}

	public void put(CallTrace callTrace){
		String key = getKey(callTrace.getServerCluster(), callTrace.getEntryPoint());
		THREAD_MAP.put(key, callTrace);
	}
	
}
