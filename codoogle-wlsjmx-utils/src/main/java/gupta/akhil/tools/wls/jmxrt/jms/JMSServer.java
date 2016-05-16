package gupta.akhil.tools.wls.jmxrt.jms;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;
import gupta.akhil.tools.wls.deploy.runtime.MBeanProxy;

public class JMSServer extends MBeanProxy{
	private String name;
	private List<JMSDestination> jmsDestinations = new ArrayList<JMSDestination>();

	public JMSServer(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public String getName(){
		return name;
	}
	
	public List<JMSDestination> getJmsDestinations() {
		if(jmsDestinations.isEmpty()){
			ObjectName[] objectNames = (ObjectName[])getAttribute("Destinations");
			for(ObjectName objectName: objectNames){
				jmsDestinations.add(new JMSDestination(objectName, getDomainConfig()));
			}
		}
		return jmsDestinations;
	}
	
	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

}
