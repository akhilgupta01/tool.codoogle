package gupta.akhil.tools.wls.deploy.runtime;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class ApplicationRuntime extends MBeanProxy{
	private String name;
	private Boolean ear;
	private String type;
	
	private List<ComponentRuntime> componentRuntimeBeans = new ArrayList<ComponentRuntime>();
	
	public ApplicationRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		ear = (Boolean)getAttribute("EAR");
		name = (String)getAttribute("Name");
		type = (String)getAttribute("Type");
	}
	
	public String getName(){
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public List<ComponentRuntime> getComponentRuntimeBeans(){
		if(componentRuntimeBeans.isEmpty()){
			ObjectName[] objectNames = (ObjectName[])getAttribute("ComponentRuntimes");
			for(ObjectName objectName: objectNames){
				ComponentRuntime componentRuntime = new ComponentRuntime(objectName, getDomainConfig());
				if(componentRuntime.isEJBModule()){
					componentRuntimeBeans.add(new EJBComponentRuntime(objectName, getDomainConfig()));
				}else if(componentRuntime.isWebModule()){
					componentRuntimeBeans.add(new WebComponentRuntime(objectName, getDomainConfig()));
				}
			}
		}
		return componentRuntimeBeans;
	}

	public List<EJBComponentRuntime> getEJBComponentRuntimeBeans(){
		List<EJBComponentRuntime> ejbComponentRuntimeBeans = new ArrayList<EJBComponentRuntime>();
		for(ComponentRuntime componentRuntime: getComponentRuntimeBeans()){
			if(componentRuntime.isEJBModule()){
				ejbComponentRuntimeBeans.add((EJBComponentRuntime)componentRuntime);
			}
		}
		return ejbComponentRuntimeBeans;
	}
	
	public List<WebComponentRuntime> getWebComponentRuntimeBeans(){
		List<WebComponentRuntime> webComponentRuntimeBeans = new ArrayList<WebComponentRuntime>();
		for(ComponentRuntime componentRuntime: getComponentRuntimeBeans()){
			if(!componentRuntime.isEJBModule()){
				webComponentRuntimeBeans.add((WebComponentRuntime)componentRuntime);
			}
		}
		return webComponentRuntimeBeans;
	}

	
	public boolean isEar() {
		return ear == null?false:ear;
	}
}
