package gupta.akhil.tools.common.config;

public class DomainConfig {
	private String adminHost;
	private int adminServerPort;
	private String userName;
	private String password;
	private String domainName;
	private String serverNamePattern;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAdminHost() {
		return adminHost;
	}
	public void setAdminHost(String adminHost) {
		this.adminHost = adminHost;
	}
	public int getAdminServerPort() {
		return adminServerPort;
	}
	public void setAdminServerPort(int adminServerPort) {
		this.adminServerPort = adminServerPort;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getServerNamePattern() {
		return serverNamePattern;
	}
	public void setServerNamePattern(String serverNamePattern) {
		this.serverNamePattern = serverNamePattern;
	}
	@Override
	public String toString() {
		return "DomainConfig [adminHost=" + adminHost + ", adminServerPort=" + adminServerPort + ", userName="
				+ userName + ", password=" + password + ", domainName=" + domainName + ", serverNamePattern="
				+ serverNamePattern + "]";
	}
	
	
}
