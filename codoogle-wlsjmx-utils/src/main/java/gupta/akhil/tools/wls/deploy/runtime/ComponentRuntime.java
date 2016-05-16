package gupta.akhil.tools.wls.deploy.runtime;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class ComponentRuntime extends MBeanProxy{
	private String name;
	private String type;
	private String moduleId;
	private String moduleURI;
	private String contextRoot;
	private List<EJBRuntime> ejbRuntimeBeans = new ArrayList<EJBRuntime>();

	public ComponentRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		type = (String)getAttribute("Type");
		name = (String)getAttribute("Name");
		moduleId = (String)getAttribute("ModuleId");
	}
	
	public String getName(){
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getModuleId() {
		return moduleId;
	}
	
	public String getContextRoot() {
		if(contextRoot == null){
			contextRoot = (String)getAttribute("ContextRoot");
		}
		return contextRoot;
	}
	
	public boolean isWebModule(){
		return "WebAppComponentRuntime".equals(getType());
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
	
	public void printSpringRuntimeMBean(){
		System.out.println(getAttribute("SpringRuntimeMBean"));
	}
	
	public String getModuleURI() {
		if(moduleURI == null){
			moduleURI = (String)getAttribute("ModuleURI");
		}
		return moduleURI;
	}
}
