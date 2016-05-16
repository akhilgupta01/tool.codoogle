package gupta.akhil.tools.common.config;

public class CVSConfig extends SourceControlConfig{
	
	private String hostName;
	
	private String path;
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getRoot(){
		return ":pserver:" + getUser() + "@" + getHostName() + ":" + getPath();
	}

	@Override
	public String toString() {
		return "CVSConfig [hostName=" + hostName + ", path=" + path + ", getId()=" + getId() + ", getUser()="
				+ getUser() + ", getPassword()=" + getPassword() + "]";
	}

	
}
