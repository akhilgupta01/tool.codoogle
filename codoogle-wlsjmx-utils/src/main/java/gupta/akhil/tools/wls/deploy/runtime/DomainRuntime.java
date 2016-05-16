package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class DomainRuntime extends MBeanProxy{
	private String name;

	public DomainRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public String getName(){
		return name;
	}
	
	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

}
