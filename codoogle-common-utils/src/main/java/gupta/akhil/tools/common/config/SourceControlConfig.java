package gupta.akhil.tools.common.config;

public abstract class SourceControlConfig {
	private String id;
	private String user;
	private String password;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "SourceControlConfig [id=" + id + ", user=" + user + ", password=" + password + "]";
	}
}
