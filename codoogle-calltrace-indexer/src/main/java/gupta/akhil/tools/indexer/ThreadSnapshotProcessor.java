package gupta.akhil.tools.indexer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gupta.akhil.tools.indexer.calltrace.CallTraceCache;
import gupta.akhil.tools.indexer.calltrace.CallTraceIndexer;
import gupta.akhil.tools.indexer.calltrace.CallTraceSearcher;
import gupta.akhil.tools.indexer.calltrace.MergedCallGraph;
import gupta.akhil.tools.indexer.calltrace.model.CallTrace;
import gupta.akhil.tools.wls.tda.ThreadSnapshot;

public class ThreadSnapshotProcessor implements Runnable{
	
	private static final Log logger = LogFactory.getLog(ThreadSnapshotProcessor.class);

	private ArrayBlockingQueue<ThreadSnapshot> threadDumpsQueue;
	private String indexGroup;
	
	public ThreadSnapshotProcessor(ArrayBlockingQueue<ThreadSnapshot> threadDumpsQueue, String indexGroup){
		this.threadDumpsQueue  = threadDumpsQueue;
		this.indexGroup = indexGroup;
	}

	@Override
	public void run() {
		CallTraceCache callTraceCache = new CallTraceCache();
		ThreadSnapshot threadSnapshot = null;
		while(true){
			try{
				threadSnapshot = threadDumpsQueue.take();
				if(logger.isTraceEnabled()){
					logger.trace("Received snapshot = " + threadSnapshot);
				}
				String serverCluster = threadSnapshot.getServerName().substring(0, threadSnapshot.getServerName().length() - 2);
				String entryPoint = threadSnapshot.getEntryPoint();
				String callType = threadSnapshot.getCallType();
				CallTrace callTrace = callTraceCache.get(serverCluster, entryPoint);
				if(callTrace == null){
					callTrace = getCallTraceFromIndex(serverCluster, entryPoint, indexGroup);
				}
				if(callTrace == null){
					callTrace = new CallTrace(serverCluster, entryPoint, callType, new MergedCallGraph(threadSnapshot));
				}else{
					callTrace.getMergedCallGraph().merge(threadSnapshot.getCallNode());
				}
				callTrace.incrementObservationCount();
				callTrace.setLastObservationTime(new Date());
				callTraceCache.put(callTrace);
				CallTraceIndexer.getInstance(indexGroup).addToIndex(callTrace);
			}catch(Exception e){
				logger.error("Error while indexing call trace. \n indexGroup = " + indexGroup + "] \n Error Message = " + e.getMessage());
			}
		}

	}
	
	private static CallTrace getCallTraceFromIndex(String serverCluster, String entryPoint, String indexGroup) {
		if(logger.isTraceEnabled()){
			logger.trace("Checking in index [serverCluster=" + serverCluster + ", entryPoint=" + entryPoint + "]");
		}
		List<CallTrace> callTraces = CallTraceSearcher.getInstance(indexGroup).findCallTrace(entryPoint);
		CallTrace matching = null;
		for(CallTrace callTrace: callTraces){
			if(callTrace.getServerCluster().equals(serverCluster)){
				matching = callTrace;
				break;
			}
		}
		logger.debug("Returning from Index - " + matching);
		return matching;
	}

}
