package gupta.akhil.tools.wls.reporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gupta.akhil.tools.wls.tda.ThreadSnapshot;

public class ServerProfilingData {
	private String serverName;
	
	public ServerProfilingData(String serverName){
		this.serverName = serverName;
	}
	
	private Map<String,List<ThreadSnapshot>> snapshotMap = new HashMap<String, List<ThreadSnapshot>>();
	
	public void addSnapshots(List<ThreadSnapshot> threadSnapshots){
		for(ThreadSnapshot threadSnapshot: threadSnapshots){
			if(!threadSnapshot.getServerName().equals(serverName)){
				throw new RuntimeException("ServerName does not match with the Thread Snapshot!");
			}
			List<ThreadSnapshot> threadSnapshotList = snapshotMap.get(threadSnapshot.getThreadName());
			if(threadSnapshotList == null){
				threadSnapshotList = new ArrayList<ThreadSnapshot>();
				snapshotMap.put(threadSnapshot.getThreadName(), threadSnapshotList);
			}
			threadSnapshotList.add(threadSnapshot);
		}
	}
	
	public List<SlowThread> identifySlowThreads(long timeInMillis){
		List<SlowThread> slowThreads = new ArrayList<SlowThread>();
		for(String threadName: snapshotMap.keySet()){
			List<ThreadSnapshot> threadSnapshotList = snapshotMap.get(threadName);
			ThreadSnapshot lastThreadSnapshot = null;
			SlowThread slowThread = null;
			for(ThreadSnapshot threadSnapshot: threadSnapshotList){
				if(threadSnapshot.isSameAs(lastThreadSnapshot)){
					if(slowThread == null){
						slowThread = new SlowThread(lastThreadSnapshot.getTimeStamp(), threadSnapshot.getTimeStamp(), threadSnapshot);
						slowThreads.add(slowThread);
					}else{
						slowThread.setLastSeenTime(threadSnapshot.getTimeStamp());
					}
				}else{
					slowThread = null;
				}
				lastThreadSnapshot = threadSnapshot;
			}
		}
		
		List<SlowThread> filteredSlowThreads = new ArrayList<SlowThread>();
		for(SlowThread slowThread: slowThreads){
			if(slowThread.getExecutionTime() > timeInMillis){
				filteredSlowThreads.add(slowThread);
			}
		}
		return filteredSlowThreads;
	}
	
}
