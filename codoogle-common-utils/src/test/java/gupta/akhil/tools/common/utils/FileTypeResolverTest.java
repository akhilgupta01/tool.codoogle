package gupta.akhil.tools.common.utils;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import gupta.akhil.tools.common.utils.FileTypeResolver;

public class FileTypeResolverTest {
	
	@Test
	public void testFileTypes() throws IOException{
		System.out.println(System.getProperties());
		String baseDir = System.getProperty("user.dir");
		FileTypeResolver fileResolver = FileTypeResolver.getInstance();
		
		File file1 = new File(baseDir + "/" + "src/test/resources/testdata/config.props");
		Assert.assertTrue(fileResolver.isAsciiFile(file1));

		File file3 = new File(baseDir + "/" + "src/test/resources/testdata/pom.xml");
		Assert.assertTrue(fileResolver.isAsciiFile(file3));

		File file4 = new File(baseDir + "/" + "src/test/resources/testdata/activation.jar");
		Assert.assertFalse(fileResolver.isAsciiFile(file4));

		File file2 = new File(baseDir + "/" + "src/test/resources/testdata/commons-daemon.jar");
		Assert.assertFalse(fileResolver.isAsciiFile(file2));


	}
}
