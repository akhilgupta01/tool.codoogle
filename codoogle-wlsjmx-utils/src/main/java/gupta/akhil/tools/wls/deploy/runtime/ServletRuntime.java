package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class ServletRuntime extends MBeanProxy{
	private String name;
	
	public ServletRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
	}
	
	public String getName(){
		if(name == null){
			name = (String)getAttribute("Name");
		}
		return name;
	}
	
	public int getInvocationCount(){
		return (Integer)getAttribute("InvocationTotalCount");
	}
}
