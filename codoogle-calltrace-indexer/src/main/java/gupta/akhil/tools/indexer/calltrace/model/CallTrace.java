package gupta.akhil.tools.indexer.calltrace.model;

import java.util.Date;

import gupta.akhil.tools.codeanalyzer.lucene.Indexable;
import gupta.akhil.tools.indexer.calltrace.MergedCallGraph;

public class CallTrace implements Indexable{
	private String serverCluster;
	private String entryPoint;
	private String callType;
	private long snapshotCount;
	private Date lastObservationTime;
	
	private MergedCallGraph mergedCallGraph;
	
	public CallTrace(String serverCluster, String entryPoint, String callType, MergedCallGraph mergedCallGraph) {
		this.serverCluster = serverCluster;
		this.entryPoint = entryPoint;
		this.callType = callType;
		this.mergedCallGraph = mergedCallGraph;
	}
	
	@Override
	public String getIndexKey() {
		return (serverCluster + "_" + entryPoint).hashCode() + "";
	}
	
	public String getEntryPoint() {
		return entryPoint;
	}
	
	public MergedCallGraph getMergedCallGraph() {
		return mergedCallGraph;
	}
	
	public String getServerCluster() {
		return serverCluster;
	}
	
	public String getCallType() {
		return callType==null?"":callType;
	}
	
	public long getSnapshotCount() {
		return snapshotCount;
	}
	
	public void setSnapshotCount(long snapshotCount) {
		this.snapshotCount = snapshotCount;
	}
	
	public Date getLastObservationTime() {
		return lastObservationTime;
	}
	
	public void setLastObservationTime(Date lastObservationTime) {
		this.lastObservationTime = lastObservationTime;
	}
	
	public void incrementObservationCount(){
		snapshotCount = snapshotCount + 1;
	}

	@Override
	public String toString() {
		return "CallTrace [serverCluster=" + serverCluster + ", entryPoint="
				+ entryPoint + ", callType=" + callType + ", snapshotCount="
				+ snapshotCount + ", lastObservationTime="
				+ lastObservationTime + "]";
	}
	
	
}
