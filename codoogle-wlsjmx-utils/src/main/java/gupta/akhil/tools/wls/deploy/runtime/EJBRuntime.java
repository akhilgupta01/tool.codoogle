package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class EJBRuntime extends MBeanProxy{
	private String name;
	private String ejbName;
	private TransactionRuntime txRuntime;
	
	public EJBRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
	}
	
	public String getName(){
		if(name == null){
			name = (String)getAttribute("Name");
		}
		return name;
	}
	
	public String getEjbName() {
		if(ejbName == null){
			ejbName = (String)getAttribute("EJBName");
		}
		return ejbName;
	}
	
	public TransactionRuntime getTransactionRuntime(){
		if(txRuntime == null){
			txRuntime = new TransactionRuntime((ObjectName)getAttribute("TransactionRuntime"), getDomainConfig());
		}
		return txRuntime;
	}
	
	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		sb.append(tabString).append("<EJB name=\"" + getName() + "\"/>");
		return sb.toString();
	}
}
