package gupta.akhil.tools.wls.jmxrt.jms;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;
import gupta.akhil.tools.wls.deploy.runtime.MBeanProxy;

public class JMSRuntime extends MBeanProxy{
	private String name;
	private List<JMSServer> jmsServers = new ArrayList<JMSServer>();

	public JMSRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public List<JMSServer> getJmsServers() {
		if(jmsServers.isEmpty()){
			ObjectName[] objectNames = (ObjectName[])getAttribute("JMSServers");
			for(ObjectName objectName: objectNames){
				jmsServers.add(new JMSServer(objectName, getDomainConfig()));
			}
		}
		return jmsServers;
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
