package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class JDBCDataSourceRuntime extends MBeanProxy{
	private String name;
	private String serverName;

	public JDBCDataSourceRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public String getName(){
		return name;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public Integer getActiveConnectionsHighCount() {
		return (Integer)getAttribute("ActiveConnectionsHighCount");
	}
	
	public Integer getConnectionsTotalCount() {
		return (Integer)getAttribute("ConnectionsTotalCount");
	}
	
	public Integer getCurrCapacityHighCount() {
		return (Integer)getAttribute("CurrCapacityHighCount");
	}
	
	public Long getReserveRequestCount() {
		return (Long)getAttribute("ReserveRequestCount");
	}

	public Integer getActiveConnectionsCurrentCount() {
		return (Integer)getAttribute("ActiveConnectionsCurrentCount");
	}
	
	public Integer getWaitingForConnectionTotal(){
		return (Integer)getAttribute("WaitingForConnectionTotal");
	}
	
	public Integer getWaitingForConnectionCurrentCount(){
		return (Integer)getAttribute("WaitingForConnectionCurrentCount");
	}
	
	public Integer getWaitSecondsHighCount(){
		return (Integer)getAttribute("WaitSecondsHighCount");
	}
	
	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}
