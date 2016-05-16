package gupta.akhil.tools.wls.deploy.runtime;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;
import gupta.akhil.tools.wls.jmxrt.jms.JMSRuntime;

public class ServerRuntime extends MBeanProxy{
	private String name;
	private List<ApplicationRuntime> applicationRuntimeBeans = new ArrayList<ApplicationRuntime>();
	private List<JDBCDataSourceRuntime> jdbcDataSourceRuntimeBeans = new ArrayList<JDBCDataSourceRuntime>();
	private JVMRuntime jvmRuntime;
	private JMSRuntime jmsRuntime;
	
	public ServerRuntime(ObjectName jmxObjectName, DomainConfig domainConfig){
		super(jmxObjectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public String getName(){
		return name;
	}
	
	public JVMRuntime getJvmRuntime() {
		if(jvmRuntime == null){
			jvmRuntime = new JVMRuntime((ObjectName)getAttribute("JVMRuntime"), getDomainConfig());
		}
		return jvmRuntime;
	}
	
	public JMSRuntime getJmsRuntime() {
		if(jmsRuntime == null){
			jmsRuntime = new JMSRuntime((ObjectName)getAttribute("JMSRuntime"), getDomainConfig());
		}
		return jmsRuntime;
	}

	
	public List<JDBCDataSourceRuntime> getJDBCDataSourceRuntimeBeans() {
		if(jdbcDataSourceRuntimeBeans.isEmpty()){
			JDBCServiceRuntime jdbcServiceRuntime = new JDBCServiceRuntime((ObjectName)getAttribute("JDBCServiceRuntime"), getDomainConfig());
			jdbcDataSourceRuntimeBeans.addAll(jdbcServiceRuntime.getJDBCDataSources(getName()));
		}
		return jdbcDataSourceRuntimeBeans;
	}
	
	public List<ApplicationRuntime> getApplicationRuntimeBeans(){
		if(applicationRuntimeBeans.isEmpty()){
			ObjectName[] objectNames = (ObjectName[])getAttribute("ApplicationRuntimes");
			for(ObjectName objectName: objectNames){
				ApplicationRuntime applicationRuntime = new ApplicationRuntime(objectName, getDomainConfig());
				if(applicationRuntime.isEar()){
					applicationRuntimeBeans.add(applicationRuntime);
				}
			}
		}
		return new ArrayList<ApplicationRuntime>(applicationRuntimeBeans);
	}
	
	public ThreadPoolRuntime getThreadPoolRuntime(){
		ObjectName objectName = (ObjectName)getAttribute("ThreadPoolRuntime");
		return new ThreadPoolRuntime(objectName, getDomainConfig());
	}
}
