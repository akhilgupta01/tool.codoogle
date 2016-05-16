package gupta.akhil.tools.wls.reporter;

import gupta.akhil.tools.wls.tda.ThreadSnapshot;

public class SlowThread {
	private long firstSeenTime;
	private long lastSeenTime;
	private ThreadSnapshot threadSnapshot;
	
	public SlowThread(long firstSeenTime, long lastSeenTime, ThreadSnapshot threadSnapshot){
		this.firstSeenTime = firstSeenTime;
		this.lastSeenTime = lastSeenTime;
		this.threadSnapshot = threadSnapshot;
	}
	
	public void setFirstSeenTime(long firstSeenTime) {
		this.firstSeenTime = firstSeenTime;
	}
	
	public void setLastSeenTime(long lastSeenTime) {
		this.lastSeenTime = lastSeenTime;
	}
	
	public long getFirstSeenTime() {
		return firstSeenTime;
	}
	
	public long getLastSeenTime() {
		return lastSeenTime;
	}
	
	public ThreadSnapshot getThreadSnapshot() {
		return threadSnapshot;
	}
	
	public long getExecutionTime(){
		return lastSeenTime - firstSeenTime; 
	}
	
}
