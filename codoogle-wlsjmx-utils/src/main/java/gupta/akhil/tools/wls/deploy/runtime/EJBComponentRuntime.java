package gupta.akhil.tools.wls.deploy.runtime;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class EJBComponentRuntime extends ComponentRuntime{
	private String type;
	private List<EJBRuntime> ejbRuntimeBeans = new ArrayList<EJBRuntime>();

	public EJBComponentRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		type = (String)getAttribute("Type");
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isEJBModule(){
		return "EJBComponentRuntime".equals(getType());
	}
	
	public void printEJBComponent(){
		printAttributes(((ObjectName[])getAttribute("EJBRuntimes"))[0]);
	}
	
	public List<EJBRuntime> getEJBRuntimeBeans(){
		if(ejbRuntimeBeans.isEmpty()){
			ObjectName[] objectNames = (ObjectName[])getAttribute("EJBRuntimes");
			for(ObjectName objectName: objectNames){
				ejbRuntimeBeans.add(new EJBRuntime(objectName, getDomainConfig()));
			}
		}
		return ejbRuntimeBeans;
	}
	
	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		sb.append(tabString).append("<EJBComponent name=\"" + getName() + "\">");
		for(EJBRuntime ejbRuntime: getEJBRuntimeBeans()){
			sb.append("\n").append(ejbRuntime.toFormattedString(level + 1));
		}
		sb.append("\n").append(tabString).append("</EJBComponent>");
		return sb.toString();
	}
}
