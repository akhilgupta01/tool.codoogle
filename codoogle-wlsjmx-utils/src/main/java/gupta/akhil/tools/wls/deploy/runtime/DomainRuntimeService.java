package gupta.akhil.tools.wls.deploy.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class DomainRuntimeService extends MBeanProxy{
	private List<ServerRuntime> serverRuntimeBeans = new ArrayList<ServerRuntime>();
	private DomainRuntime domainRuntime;
	private DomainConfig domainConfig;
	private static final Map<DomainConfig, DomainRuntimeService> DOMAIN_RUNTIME_MAP = new HashMap<DomainConfig, DomainRuntimeService>();
	
	private DomainRuntimeService(DomainConfig domainConfig){
		super("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean", domainConfig);
		this.domainConfig = domainConfig;
	}
	
	public static DomainRuntimeService getInstance(DomainConfig domainConfig){
		DomainRuntimeService domainRuntimeService = DOMAIN_RUNTIME_MAP.get(domainConfig);
		if(domainRuntimeService == null){
			domainRuntimeService = new DomainRuntimeService(domainConfig);
			DOMAIN_RUNTIME_MAP.put(domainConfig, domainRuntimeService);
		}
		return domainRuntimeService;
	}
	
	public DomainRuntime getDomainRuntime() {
		if(domainRuntime == null){
			domainRuntime = new DomainRuntime((ObjectName)getAttribute("DomainRuntime"), getDomainConfig());
		}
		return domainRuntime;
	}
	
	public List<ServerRuntime> getServerRuntimes(boolean forceReload){
		if(serverRuntimeBeans.isEmpty() || forceReload){
			serverRuntimeBeans.clear();
			ObjectName[] serverRT = (ObjectName[])getAttribute("ServerRuntimes");
			for(ObjectName objectName: serverRT){
				serverRuntimeBeans.add(new ServerRuntime(objectName, getDomainConfig()));
			}
		}
		return serverRuntimeBeans;
	}
	
	public List<ServerRuntime> getServerRuntimes(){
		return getServerRuntimes(false);
	}
}
