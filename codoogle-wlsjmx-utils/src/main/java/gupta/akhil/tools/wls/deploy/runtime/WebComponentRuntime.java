package gupta.akhil.tools.wls.deploy.runtime;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class WebComponentRuntime extends ComponentRuntime{
	private String moduleId;
	private String moduleURI;
	private String contextRoot;
	private List<ServletRuntime> servletRuntimes = new ArrayList<ServletRuntime>();

	public WebComponentRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
		moduleId = (String)getAttribute("ModuleId");
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
	
	public String getModuleURI() {
		if(moduleURI == null){
			moduleURI = (String)getAttribute("ModuleURI");
		}
		return moduleURI;
	}
	
	public List<ServletRuntime> getServletRuntimes(){
		if(servletRuntimes.isEmpty()){
			ObjectName[] objectNames = (ObjectName[])getAttribute("Servlets");
			for(ObjectName objectName: objectNames){
				servletRuntimes.add(new ServletRuntime(objectName, getDomainConfig()));
			}
		}
		return servletRuntimes;
	}

	public String toFormattedString(int level) {
		String tabString = getTabs(level);
		StringBuffer sb = new StringBuffer();
		sb.append(tabString).append("<WebComponent name=\"" + getModuleURI() + "\"" + " contextRoot=\"" + getContextRoot() + "\">");
		sb.append("\n").append(tabString).append("</WebComponent>");
		return sb.toString();
	}

}
