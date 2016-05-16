package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class JVMRuntime extends MBeanProxy{
	private String name;
	private int HeapFreePercent;
	
	public JVMRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public String getName(){
		return name;
	}
	
	public Long getHeapSizeCurrent() {
		return (Long)getAttribute("HeapSizeCurrent");
	}
	
	public Long getHeapSizeMax() {
		return (Long)getAttribute("HeapSizeMax");
	}
	
	public String getThreadDump(){
		return (String)getAttribute("ThreadStackDump");
	}
	
	public int getHeapFreePercent() {
		return (Integer)getAttribute("HeapFreePercent");
	}
	
	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

}
