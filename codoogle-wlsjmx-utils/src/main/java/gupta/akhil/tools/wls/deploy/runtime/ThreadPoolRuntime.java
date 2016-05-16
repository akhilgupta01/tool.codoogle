package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class ThreadPoolRuntime extends MBeanProxy{
	private String name;

	public ThreadPoolRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public String getName(){
		return name;
	}
	
	public Integer getExecuteThreadTotalCount() {
		return (Integer)getAttribute("ExecuteThreadTotalCount");
	}
	
	public Integer getExecuteThreadIdleCount() {
		return (Integer)getAttribute("ExecuteThreadIdleCount");
	}
	
	public Integer getPendingUserRequestCount(){
		return (Integer)getAttribute("PendingUserRequestCount");
	}
	
	public Integer getStandbyThreadCount(){
		return (Integer)getAttribute("StandbyThreadCount");
	}

	public void printExecuteThreads(){
		ObjectName[] objectNames = (ObjectName[])getAttribute("ExecuteThreads");
		for(ObjectName objectName: objectNames){
			System.out.println(objectName);
			printAttributes(objectName);
		}
	}
	
	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

}
