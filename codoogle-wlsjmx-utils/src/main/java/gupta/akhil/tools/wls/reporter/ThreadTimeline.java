package gupta.akhil.tools.wls.reporter;

import java.util.ArrayList;
import java.util.List;

import gupta.akhil.tools.wls.tda.ThreadSnapshot;

public class ThreadTimeline {
	private String threadName;
	private long slowMethodDuration = 2000;
	
	private List<ThreadSnapshot> unprocessedSnapshots = new ArrayList<ThreadSnapshot>();
	private List<SlowThread> slowThreads = new ArrayList<SlowThread>();
	
	public ThreadTimeline(String threadName){
		this.threadName = threadName;
	}
	
	public void setSlowMethodDuration(long slowMethodDuration) {
		this.slowMethodDuration = slowMethodDuration;
	}
	
	public String getThreadName() {
		return threadName;
	}
	
	public void addSnapshot(ThreadSnapshot threadSnapshot){
		unprocessedSnapshots.add(threadSnapshot);
		detectSlowThreads();
	}
	
	public List<SlowThread> getSlowThreads() {
		return new ArrayList<SlowThread>(slowThreads);
	}
	
	public void purgeData(){
		this.slowThreads.clear();
	}

	public void detectSlowThreads(){
		List<ThreadSnapshot> visitedSnapshots = new ArrayList<ThreadSnapshot>();
		List<ThreadSnapshot> purgedSnapshots = new ArrayList<ThreadSnapshot>();
		ThreadSnapshot lastSnapshot = null;
		SlowThread subject = null;
		for(ThreadSnapshot threadSnapshot: unprocessedSnapshots){
			if(lastSnapshot == null){ //First snapshot, nothing to compare yet
				subject = new SlowThread(threadSnapshot.getTimeStamp(), threadSnapshot.getTimeStamp(), threadSnapshot);
			}else{ //we have snapshots to compare
				if(threadSnapshot.isSameAs(lastSnapshot)){
					subject.setLastSeenTime(threadSnapshot.getTimeStamp());
				}else{
					if(subject.getExecutionTime() > slowMethodDuration){
						slowThreads.add(subject);
					}
					subject = new SlowThread(threadSnapshot.getTimeStamp(), threadSnapshot.getTimeStamp(), threadSnapshot);
					purgedSnapshots.addAll(visitedSnapshots);
					visitedSnapshots.clear();
				}
			}
			lastSnapshot = threadSnapshot;
			visitedSnapshots.add(threadSnapshot);
		}
		unprocessedSnapshots.removeAll(purgedSnapshots);
	}
}
