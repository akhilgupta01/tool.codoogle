package gupta.akhil.tools.codeanalyzer.lucene.ascii;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.jar.JarFile;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import gupta.akhil.tools.TestDataUtils;
import gupta.akhil.tools.indexer.code.AsciiFileIndexer;
import gupta.akhil.tools.indexer.code.AsciiFileSearcher;
import gupta.akhil.tools.indexer.code.model.AsciiFile;

public class AsciiFileIndexerTest {

	@BeforeClass
	public static void setUp()  throws Exception{
		String modulePath = TestDataUtils.PROJECT_LOCATION;
		String samplePOMXml = TestDataUtils.TEST_RESOURCES_DIR + '/' + TestDataUtils.SAMPLE_XML_FILE; 
		String veryLargeJavaFile = TestDataUtils.TEST_RESOURCES_DIR + '/' + TestDataUtils.VERY_LARGE_JAVA_FILE; 
		String testJarPath = TestDataUtils.TEST_RESOURCES_DIR + '/' + TestDataUtils.TEST_JAR_NAME;
		
		JarFile jarFile = new JarFile(new File(modulePath + '/' + testJarPath));
		
		AsciiFileIndexer.getInstance("codeIndexGroup1").indexFile(modulePath, samplePOMXml);
		AsciiFileIndexer.getInstance("codeIndexGroup2").indexFile(modulePath, veryLargeJavaFile);
	}
	
	@Test
	public void shouldSearchFileByQueryExpression() throws Exception{
		checkSearchExpression("codeIndexGroup1","fileName:samp*.xml && moduleName:codoogle", 1);
		checkSearchExpression("codeIndexGroup1","fileName:Samp*.xml", 1);
		checkSearchExpression("codeIndexGroup1","SamplePOM.xml", 1);
		checkSearchExpression("codeIndexGroup2","getOptionProducts", 1);
		checkSearchExpression("codeIndexGroup1","contents:dependency", 1);
		checkSearchExpression("codeIndexGroup1","contents:depende*", 1);
		checkSearchExpression("codeIndexGroup1","fileName:Simp*.xml && contents:depende*", 0);
		checkSearchExpression("codeIndexGroup1","fileName:Simp*.xml || contents:depende*", 1);
		checkSearchExpression("codeIndexGroup2","contents:\"Inside getOptionProducts\"", 1);
	}
	
	private void checkSearchExpression(String indexGroup, String searchExpression, int expectedSearchCount) throws Exception{
		List<AsciiFile> searchedFiles = AsciiFileSearcher.getInstance(indexGroup).searchFiles(searchExpression);
		Assert.assertEquals(expectedSearchCount, searchedFiles.size());
	}

	@Test
	public void shouldSearchFileByExactName() throws Exception{
		List<AsciiFile> searchedFiles = AsciiFileSearcher.getInstance("codeIndexGroup1").searchFilesByName(TestDataUtils.SAMPLE_XML_FILE);
		Assert.assertEquals(1, searchedFiles.size());
	}
	
	@Test
	public void shouldSearchFileByAnyCaseName() throws Exception{
		List<AsciiFile> searchedFiles = AsciiFileSearcher.getInstance("codeIndexGroup1").searchFilesByName("samplepoM.xml");
		Assert.assertEquals(1, searchedFiles.size());
	}

	@Test
	public void shouldSearchFileByWildCardName() throws Exception{
		List<AsciiFile> searchedFiles = AsciiFileSearcher.getInstance("codeIndexGroup1").searchFilesByName("samplePo*.xml");
		Assert.assertEquals(1, searchedFiles.size());
	}

	
	@Test
	public void shouldSearchFileByContent() throws Exception{
		List<AsciiFile> searchedFiles = AsciiFileSearcher.getInstance("codeIndexGroup1").searchFilesByContent("<dependencies>");
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(AsciiFile.class, new JsonSerializer<AsciiFile>() {
			@Override
			public JsonElement serialize(AsciiFile asciiFile, Type arg1, JsonSerializationContext arg2) {
				final JsonObject jsonObject = new JsonObject();
		        jsonObject.addProperty("filePath", asciiFile.getFilePath());
		        jsonObject.addProperty("fileName", asciiFile.getFileName());
		        jsonObject.addProperty("moduleName", asciiFile.getModuleName());
		        return jsonObject;
			}
		});
		final Gson gson = gsonBuilder.create();
		final String json = gson.toJson(searchedFiles);
		System.out.println(json);
		Assert.assertEquals(1, searchedFiles.size());
	}

	@Test
	public void shouldIndexAVeryLargeFile() throws Exception{
		List<AsciiFile> searchedFiles = AsciiFileSearcher.getInstance("codeIndexGroup2").searchFilesByContent("getOptionProducts");
		System.out.println(searchedFiles);
		Assert.assertEquals(1, searchedFiles.size());
		Assert.assertEquals("VeryLargeJavaFile.java", searchedFiles.get(0).getFileName());
	}
}
