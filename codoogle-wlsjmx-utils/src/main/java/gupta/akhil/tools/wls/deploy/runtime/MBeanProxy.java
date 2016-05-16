package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public abstract class MBeanProxy {
	private ObjectName objectName;
	private DomainConfig domainConfig;
	
	protected MBeanProxy(String objectName, DomainConfig domainConfig) {
		try {
			this.objectName = new ObjectName(objectName);
			this.domainConfig = domainConfig;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to create ObjectName");
		}
	}
	
	public DomainConfig getDomainConfig(){
		return domainConfig;
	}
	
	protected MBeanProxy(ObjectName objectName, DomainConfig domainConfig) {
		this.objectName = objectName;
		this.domainConfig = domainConfig;
	}
	
	private MBeanServerConnection getConnection(){
		return MBeanServerConnectionFactory.getConnection(this.domainConfig);
	}
	
	public Object getAttribute(String attribute){
		return getConnection().getAttribute(objectName, attribute);
	}
	
	public void printAttributes() {
		System.out.println("-----------------------------------------------------");
		System.out.println("| Attributes of Object : " + objectName);
		System.out.println("-----------------------------------------------------");
		MBeanInfo mbeanInfo = getConnection().getMBeanInfo(objectName);
		MBeanAttributeInfo[] mbeanAttributeInfo = mbeanInfo.getAttributes();
		for(MBeanAttributeInfo attr: mbeanAttributeInfo){
			System.out.println("|" + attr.getName());
		}
		System.out.println("-----------------------------------------------------");
	}
	
	public void printAttributes(ObjectName objectName) {
		System.out.println("-----------------------------------------------------");
		System.out.println("| Attributes of Object : " + objectName);
		System.out.println("-----------------------------------------------------");
		MBeanInfo mbeanInfo = getConnection().getMBeanInfo(objectName);
		MBeanAttributeInfo[] mbeanAttributeInfo = mbeanInfo.getAttributes();
		for(MBeanAttributeInfo attr: mbeanAttributeInfo){
			System.out.println("|" + attr.getName());
		}
		System.out.println("-----------------------------------------------------");
	}
	
	protected String getTabs(int level){
		StringBuffer tabs = new StringBuffer();
		for(int i=0; i<level; i++){
			tabs.append("\t");
		}
		return tabs.toString();
	}
}
