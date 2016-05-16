package gupta.akhil.tools.wls.deploy.runtime;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class JDBCServiceRuntime extends MBeanProxy{
	private String name;

	public JDBCServiceRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public String getName(){
		return name;
	}
	
	public List<JDBCDataSourceRuntime> getJDBCDataSources(){
		return getJDBCDataSources("");
	}

	public List<JDBCDataSourceRuntime> getJDBCDataSources(String serverName){
		List<JDBCDataSourceRuntime> dataSourceRuntimeBeans = new ArrayList<JDBCDataSourceRuntime>();
		ObjectName[] objectNames = (ObjectName[])getAttribute("JDBCDataSourceRuntimeMBeans");
		for(ObjectName objectName: objectNames){
			JDBCDataSourceRuntime jdbcDataSource = new JDBCDataSourceRuntime(objectName, getDomainConfig());
			jdbcDataSource.setServerName(serverName);
			dataSourceRuntimeBeans.add(jdbcDataSource);
		}
		return dataSourceRuntimeBeans;
	}

	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

}
