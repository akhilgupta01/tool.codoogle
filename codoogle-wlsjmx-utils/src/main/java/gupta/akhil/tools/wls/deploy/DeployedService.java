package gupta.akhil.tools.wls.deploy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeployedService {
	private String ejbName;
	//private String servletName;
	private String contextRoot;
	private String moduleName;
	private String applicationName;
	private String serverName;
	//private String id;
	
	public String getEjbName() {
		return ejbName;
	}
	public void setEjbName(String ejbName) {
		this.ejbName = ejbName;
	}
//	public String getServletName() {
//		return servletName;
//	}
//	public void setServletName(String servletName) {
//		this.servletName = servletName;
//	}
	public String getContextRoot() {
		return contextRoot;
	}
	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public String toJson(){
		//this.id = (this.applicationName + "_" + this.moduleName + "_" + this.serverName + "_" + (this.servletName == null? this.ejbName: this.servletName)).hashCode() + "";   
		GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}
}
