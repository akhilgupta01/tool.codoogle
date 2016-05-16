package gupta.akhil.tools.wls.jmxrt.jms;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;
import gupta.akhil.tools.wls.deploy.runtime.MBeanProxy;

public class JMSDestination extends MBeanProxy{
	private String name;
	
	public JMSDestination(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		name = (String)getAttribute("Name");
	}
	
	public long getMessagesCurrentCount() {
		return (Long)getAttribute("MessagesCurrentCount");
	}
	
	public long getMessagesPendingCount() {
		return (Long)getAttribute("MessagesPendingCount");
	}
	
	public long getConsumersCurrentCount() {
		return (Long)getAttribute("ConsumersCurrentCount");
	}
	
	public long getMessagesHighCount() {
		return (Long)getAttribute("MessagesHighCount");
	}
	

	public long getMessagesReceivedCount() {
		return (Long)getAttribute("MessagesReceivedCount");
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
