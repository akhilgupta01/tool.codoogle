package gupta.akhil.tools.indexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gupta.akhil.tools.common.config.SystemConfig;
import gupta.akhil.tools.common.config.TraceCollectionGroup;
import gupta.akhil.tools.wls.tda.ThreadSnapshot;

public class CallTraceCollector {
	public static void main(String[] args) throws Exception{
		Map<String, ArrayBlockingQueue<ThreadSnapshot>> threadSnapshotQueueMap = new HashMap<String,ArrayBlockingQueue<ThreadSnapshot>>();
		List<TraceCollectionGroup> traceCollectionGroups = SystemConfig.getTraceCollectionGroups();
		ExecutorService executorService = Executors.newFixedThreadPool(traceCollectionGroups.size() * 2); 
		for(TraceCollectionGroup traceCollectionGroup : traceCollectionGroups){
			ArrayBlockingQueue<ThreadSnapshot> threadDumpsQueue = threadSnapshotQueueMap.get(traceCollectionGroup.getIndexGroup());
			if(threadDumpsQueue == null){
				threadDumpsQueue = new ArrayBlockingQueue<ThreadSnapshot>(5000);
			}

			ServerRuntimeProvider serverRuntimeProvider = new ServerRuntimeProvider(traceCollectionGroup.getDomainConfigurations());
			executorService.execute(new ThreadSnapshotRetriever(threadDumpsQueue, serverRuntimeProvider, traceCollectionGroup));
			executorService.execute(new ThreadSnapshotProcessor(threadDumpsQueue, traceCollectionGroup.getIndexGroup()));
		}
		executorService.awaitTermination(24, TimeUnit.HOURS);
	}
}