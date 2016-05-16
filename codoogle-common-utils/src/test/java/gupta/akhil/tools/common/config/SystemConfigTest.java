package gupta.akhil.tools.common.config;

import org.junit.Test;

import gupta.akhil.tools.common.config.SystemConfig;
import junit.framework.Assert;

public class SystemConfigTest {

	@Test
	public void testLoadConfig(){
		Assert.assertEquals("C:/temp", SystemConfig.getBaseIndexLocation());
		System.out.println(SystemConfig.getSourceControlConfigurations());
		System.out.println(SystemConfig.getCodeIndexGroups());
	}
}
