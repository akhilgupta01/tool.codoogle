package gupta.akhil.tools.wls.deploy.runtime;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import gupta.akhil.tools.common.config.DomainConfig;

public class MBeanServerConnectionFactory {
	private static Map<String, MBeanServerConnection> CONNECTION_MAP = new HashMap<String, MBeanServerConnection>();
	
	public static MBeanServerConnection getConnection(DomainConfig domainConfig){
		MBeanServerConnection connection = CONNECTION_MAP.get(domainConfig.getAdminHost() + "_" + domainConfig.getAdminServerPort());
		if(connection == null){
			connection = createConnection(domainConfig);
		}
		return connection;
	}

	public static MBeanServerConnection getConnection(String domainConfigKey){
		MBeanServerConnection connection = CONNECTION_MAP.get(domainConfigKey);
		if(connection == null){
			throw new RuntimeException("No connection exists for key: " + domainConfigKey);
		}
		return connection;
	}
	
	private static synchronized MBeanServerConnection createConnection(DomainConfig domainConfig) {
		MBeanServerConnection connection = CONNECTION_MAP.get(domainConfig.getAdminHost() + "_" + domainConfig.getAdminServerPort());
		if(connection == null){
			String username = domainConfig.getUserName(); 
			String password = domainConfig.getPassword(); 
			System.out.println(domainConfig);
			try {
				String protocol = "t3";
				String jndiroot = "/jndi/";
				String mserver = "weblogic.management.mbeanservers.domainruntime";
				JMXServiceURL serviceURL = new JMXServiceURL(protocol, domainConfig.getAdminHost(), domainConfig.getAdminServerPort(), jndiroot + mserver);

				Hashtable<String, Object> h = new Hashtable<String, Object>();
				h.put(Context.SECURITY_PRINCIPAL, username);
				h.put(Context.SECURITY_CREDENTIALS, password);
				h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,"weblogic.management.remote");
				h.put("jmx.remote.x.request.waiting.timeout", new Long(100000));
				connection = new MBeanServerConnection(JMXConnectorFactory.connect(serviceURL, h).getMBeanServerConnection());
				CONNECTION_MAP.put(domainConfig.getAdminHost() + "_" + domainConfig.getAdminServerPort(), connection);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error making the JMX Connection");
			}
		}
		return connection;
	}
}
