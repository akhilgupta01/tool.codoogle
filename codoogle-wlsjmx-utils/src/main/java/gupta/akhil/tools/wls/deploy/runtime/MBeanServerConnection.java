package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.MBeanInfo;
import javax.management.ObjectName;

public class MBeanServerConnection {
	private javax.management.MBeanServerConnection connection;
	
	public MBeanServerConnection(javax.management.MBeanServerConnection connection) {
		this.connection = connection;
	}
	
	public Object getAttribute(ObjectName name, String attribute){
		Object object = null;
		try{
			object = connection.getAttribute(name, attribute);
		}catch(Exception e){
			System.out.println(e.getMessage());
			//throw new RuntimeException("Unable to read attribute - " + attribute);
		}
		return object;
	}

	MBeanInfo getMBeanInfo(ObjectName objectName) {
		MBeanInfo mbeanInfo = null;
		try{
			mbeanInfo = connection.getMBeanInfo(objectName);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Unable to getMBeanInfo");
		}
		return mbeanInfo;
	}
}
