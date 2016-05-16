package gupta.akhil.tools;

import org.junit.Ignore;

@Ignore
public class TestDataUtils {
	public static final String TEST_MODULE1_NAME = "testASCIIFile.xml";
	public static final String TEST_MODULE1_PATH = System.getProperty("user.dir") + '/' 
													+ "src" + '/' 
													+ "test" + '/' 
													+ "resources" + '/' + TEST_MODULE1_NAME;
	

	public static final String PROJECT_LOCATION = System.getProperty("user.dir");
	
	public static final String TEST_RESOURCES_DIR = "src\\test\\resources";
	
	public static final String SAMPLE_XML_FILE = "SamplePOM.xml";
	public static final String VERY_LARGE_JAVA_FILE = "VeryLargeJavaFile.java";
	
	public static final String TEST_JAR_NAME = "testModule1.jar";

	public static String getTestResourcesPath(){
		return "C:\\Akhil\\code\\branch_dm_14_30\\tool.module-dependency";
	}
}
