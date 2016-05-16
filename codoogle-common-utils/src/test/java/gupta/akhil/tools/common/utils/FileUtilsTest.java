package gupta.akhil.tools.common.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import gupta.akhil.tools.common.utils.Config;
import gupta.akhil.tools.common.utils.FileUtils;


public class FileUtilsTest {
	
	@Test
	public void shouldSkipIgnoredFiles(){
		String moduleBaseDir = System.getProperty("user.dir");
		List<String> ignoredFilePathPatterns = Config.getIgnoredFiles();
		
		List<String> files = FileUtils.listFiles(moduleBaseDir + "/" + "src/test/resources", ignoredFilePathPatterns);
		for(String fileAbsolutePath: files){
			//System.out.println("Checking - " + fileAbsolutePath);
			for(String ignoredFilePathPattern: ignoredFilePathPatterns){
				Assert.assertFalse("Failed for -" + fileAbsolutePath,fileAbsolutePath.matches(ignoredFilePathPattern));
			}
		}
	}
}
