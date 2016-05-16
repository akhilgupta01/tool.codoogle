package gupta.akhil.tools.indexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gupta.akhil.tools.common.config.DomainConfig;
import gupta.akhil.tools.wls.deploy.runtime.DomainRuntimeService;
import gupta.akhil.tools.wls.deploy.runtime.ServerRuntime;

public class ServerRuntimeProvider {
	private static final Log logger = LogFactory.getLog(ServerRuntimeProvider.class);

	private List<DomainConfig> domainConfigurations;
	
	private boolean timeToRefreshServers = true;

	private Timer serverRefreshTimer = new Timer();
	
	private List<ServerRuntime> serverRuntimes = new ArrayList<ServerRuntime>();

	private final TimerTask serverRefreshTask = new TimerTask(){
		@Override
		public void run() {
			timeToRefreshServers = true;
		}
	};
	
	public ServerRuntimeProvider(List<DomainConfig> domainConfigurations){
		this.domainConfigurations = new ArrayList<DomainConfig>(domainConfigurations); 
		serverRefreshTimer.schedule(serverRefreshTask, 1*60*60*1000, 1*60*60*1000);
	}
	
	public void removeServer(ServerRuntime serverRuntime){
		serverRuntimes.remove(serverRuntime);
	}

	public List<ServerRuntime> getServerRuntimes(){
		if(timeToRefreshServers){
			serverRuntimes = new ArrayList<ServerRuntime>();
			for(DomainConfig domainConfig: domainConfigurations){
				serverRuntimes.addAll(findServerRuntimes(domainConfig));
			}
			timeToRefreshServers = false;
			logger.info("Total servers - " + serverRuntimes.size());
		}
		return new ArrayList<ServerRuntime>(serverRuntimes);
	}
	
	protected static List<ServerRuntime> findServerRuntimes(DomainConfig domainConfig){
		List<ServerRuntime> serverRuntimes = DomainRuntimeService.getInstance(domainConfig).getServerRuntimes();
		List<ServerRuntime> matchingServerRuntimes = new ArrayList<ServerRuntime>();
		for(ServerRuntime serverRuntime: serverRuntimes){
			if(serverRuntime.getName().matches(domainConfig.getServerNamePattern())){
				matchingServerRuntimes.add(serverRuntime);
			}
		}
		return matchingServerRuntimes;
	}
}
