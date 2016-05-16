package gupta.akhil.tools.wls.tda;


public class ThreadSnapshot {
	private String serverName;
	private String threadName;
	private String callType;
	private String entryPoint;
	private CallNode callNode;
	private long timeStamp;
	
	ThreadSnapshot(String serverName, String threadName){
		this.serverName = serverName;
		this.threadName = threadName;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}
	
	public String getEntryPoint() {
		return entryPoint;
	}
	
	void setCallNode(CallNode callNode) {
		this.callNode = callNode;
	}
	
	void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	public CallNode getCallNode() {
		return callNode;
	}
	
	public String getThreadName() {
		return threadName;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public String getCallType() {
		return callType;
	}
	
	void setCallType(String callType) {
		this.callType = callType;
	}
	
	public boolean isSameAs(ThreadSnapshot other){
		if(other == null){
			return false;
		}
		return other.getCallNode().getCallGraph().equals(getCallNode().getCallGraph());
	}

	public String getLastPoint() {
		CallNode callNode = getCallNode();
		while(callNode.hasChildNode()){
			callNode = callNode.getChildNode();
		}
		return callNode.getMethodDesc();
	}

	public CallNode getLastCallNode() {
		CallNode callNode = getCallNode();
		while(callNode.hasChildNode()){
			callNode = callNode.getChildNode();
		}
		return callNode;
	}

	@Override
	public String toString() {
		return "ThreadSnapshot [serverName=" + serverName + ", callType="
				+ callType + ", entryPoint=" + entryPoint + "\n"
				+ callNode.getCallGraph() + "]";
	}
	
}
