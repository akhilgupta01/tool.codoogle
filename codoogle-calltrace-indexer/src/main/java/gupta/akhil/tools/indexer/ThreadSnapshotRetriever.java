package gupta.akhil.tools.indexer;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gupta.akhil.tools.common.config.TraceCollectionGroup;
import gupta.akhil.tools.wls.deploy.runtime.ServerRuntime;
import gupta.akhil.tools.wls.tda.ThreadDumpParser;
import gupta.akhil.tools.wls.tda.ThreadSnapshot;

public class ThreadSnapshotRetriever implements Runnable{

	private static final Log logger = LogFactory.getLog(ThreadSnapshotRetriever.class);

	private ArrayBlockingQueue<ThreadSnapshot> threadDumpsQueue;
	private ServerRuntimeProvider serverRuntimeProvider;
	private TraceCollectionGroup traceCollectionGroup;
	
	public ThreadSnapshotRetriever(ArrayBlockingQueue<ThreadSnapshot> threadDumpsQueue, ServerRuntimeProvider serverRuntimeProvider, TraceCollectionGroup traceCollectionGroup){
		this.threadDumpsQueue  = threadDumpsQueue;
		this.serverRuntimeProvider = serverRuntimeProvider;
		this.traceCollectionGroup = traceCollectionGroup;
	}
	
	@Override
	public void run() {
		long elapsedTime = 0;
		long startTime = System.currentTimeMillis();
		while(elapsedTime < traceCollectionGroup.getDuration()){
		for(ServerRuntime serverRuntime: serverRuntimeProvider.getServerRuntimes()){
				try{
					if(logger.isDebugEnabled()){
						logger.debug("Taking dumps on server - " + serverRuntime.getName());
					}
					for(ThreadSnapshot threadSnapshot: getThreadSnapshots(serverRuntime)){
						threadDumpsQueue.put(threadSnapshot);
					}
				}catch(Exception e){
					logger.info("Error for server " + serverRuntime.getName());
				}
			}
			elapsedTime = System.currentTimeMillis() - startTime;
			sleep(traceCollectionGroup.getInterval());
		}
	}
	
	private void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String getThreadSnapshot(ServerRuntime serverRuntime) {
		long start = System.currentTimeMillis();
		String threadDump = serverRuntime.getJvmRuntime().getThreadDump();
		long end = System.currentTimeMillis();
		if((end-start)>5000){
			logger.warn("Threaddump taking long. Removing server from the list - " + serverRuntime.getName());
			serverRuntimeProvider.removeServer(serverRuntime);
		}
		return threadDump;
	}

	private List<ThreadSnapshot> getThreadSnapshots(ServerRuntime serverRuntime) {
		return ThreadDumpParser.parse(serverRuntime.getName(), getThreadSnapshot(serverRuntime), true);
	}
}
