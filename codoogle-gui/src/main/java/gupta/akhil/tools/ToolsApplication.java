package gupta.akhil.tools;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class ToolsApplication extends ResourceConfig {
	public ToolsApplication() {
		packages("gupta.akhil.tools.indexer.calltrace", "gupta.akhil.tools.indexer.code");
	}
}
