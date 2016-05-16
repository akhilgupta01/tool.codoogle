package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class MBeanProxyObjectFactory {
	private DomainConfig domainConfig;
	
	public static MBeanProxyObjectFactory getInstance(MBeanProxy mbeanProxy){
		return new MBeanProxyObjectFactory(mbeanProxy.getDomainConfig());
	}
	public MBeanProxyObjectFactory(DomainConfig domainConfig){
		this.domainConfig = domainConfig;
	}
	public ComponentRuntime newComponentRuntime(ObjectName objectName){
		return new ComponentRuntime(objectName, domainConfig);
	}
}
