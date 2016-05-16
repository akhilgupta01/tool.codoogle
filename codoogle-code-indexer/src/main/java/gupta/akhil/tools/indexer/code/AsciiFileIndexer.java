package gupta.akhil.tools.indexer.code;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gupta.akhil.tools.codeanalyzer.lucene.AbstractIndexer;
import gupta.akhil.tools.common.utils.AsciiFileReader;
import gupta.akhil.tools.common.utils.FileTypeResolver;
import gupta.akhil.tools.common.utils.FileUtils;
import gupta.akhil.tools.indexer.code.model.AsciiFile;
import gupta.akhil.tools.indexer.code.model.AsciiFileFragment;
import gupta.akhil.tools.indexer.code.model.IndexInfo;

public class AsciiFileIndexer extends AbstractIndexer {
	private static final Log logger = LogFactory.getLog(AsciiFileIndexer.class);

	private static Map<String, AsciiFileIndexer> INSTANCE_CACHE = new HashMap<String, AsciiFileIndexer>();

	private AsciiFileIndexer(String indexGroup){
		super("AsciiFileIndex", indexGroup);
	}
	
	public static AsciiFileIndexer getInstance(String indexGroup){
		if(INSTANCE_CACHE.get(indexGroup) == null){
			createInstance(indexGroup);
		}
		return INSTANCE_CACHE.get(indexGroup);
	}
	
	private synchronized static void createInstance(String indexGroup){
		if(INSTANCE_CACHE.get(indexGroup) == null){
			INSTANCE_CACHE.put(indexGroup, new AsciiFileIndexer(indexGroup));
		}
	}
	
	/**
	 * Indexes all the ASCII files in a given list of JarFiles.
	 * @param jarFiles
	 * @throws IOException
	 */
	public void indexFiles(Collection<String> archiveFilePaths) throws IOException {
		for(String archiveFilePath: archiveFilePaths){
			try {
				JarFile jarFile = new JarFile(archiveFilePath);
				indexFilesInJar(jarFile);
			} catch (IOException e) {
				logger.warn("Unable to index ascii files for archive - " + archiveFilePath, e);
			}
		}
	}

	/**
	 * Indexes all the known ASCII files in a given JarFile
	 * @param jarFile - JarFile from which the files have to be indexed.
	 * @throws IOException
	 */
	public void indexFilesInJar(JarFile jarFile) throws IOException {
		logger.debug("Indexing artifact - " + jarFile.getName());
		String moduleName = getJarFileName(jarFile);
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String filePath = FileUtils.toUnixFilePath(entry.getName());
			logger.debug("ASCII file found - " + filePath) ;
			InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
			indexFile(moduleName, filePath, stream);
			stream.close();
		}
		commit();
	}
	
	/**
	 * Indexes all the known ASCII files in a given folder
	 * @throws IOException
	 */
	public void indexFilesInFolder(String folderPath, List<String> ignoredFiles) throws IOException {
		logger.debug("Indexing files in folder - " + folderPath);
		String modulePath = FileUtils.toUnixFilePath(folderPath);
		String moduleName = FileUtils.resolveFileName(modulePath);
		FileTypeResolver fileTypeResolver = FileTypeResolver.getInstance();
		for(String absoluteFilePath: FileUtils.listFiles(modulePath, ignoredFiles)){
			File file = new File(absoluteFilePath);
			if(fileTypeResolver.isAsciiFile(file)){
				String relativeFilePath = absoluteFilePath.substring(folderPath.length());
				logger.debug("ASCII file found - " + relativeFilePath) ;
				InputStream stream = new BufferedInputStream(new FileInputStream(absoluteFilePath), 1024);
				indexFile(moduleName, relativeFilePath, stream);
				stream.close();
			}
		}
		commit();
	}


	/**
	 * Indexes an ASCII file, given its modulePath and filePath relative to the module path.
	 * @param modulePath - Absolute path of the module
	 * @param relativeFilePath - relative path of the file, w.r.t the module
	 * @throws IOException
	 */
	public void indexFile(String modulePath, String relativeFilePath) throws IOException {
		String moduleName = new File(modulePath).getName();
		String absoluteFilePath = FileUtils.toUnixFilePath(modulePath + '/' + relativeFilePath);
		StringBuffer fileContents = getFileContents(new AsciiFileReader(absoluteFilePath));
		AsciiFile asciiFile = new AsciiFile(moduleName, relativeFilePath, fileContents.toString());
		addToIndex(asciiFile);
		commit();
	}
	
	public void updateIndexInfo(IndexInfo indexInfo) throws IOException{
		updateDocument(new IndexInfoDocument(indexInfo));
		commit();
	}

	private void indexFile(String moduleName, String filePath, InputStream stream) throws IOException {
		StringBuffer fileContents = getFileContents(new AsciiFileReader(stream));
		AsciiFile asciiFile = new AsciiFile(moduleName, filePath, fileContents.toString());
		addToIndex(asciiFile);
	}

	private void addToIndex(AsciiFile asciiFile) throws IOException{
		logger.debug("Contents Length = " + asciiFile.getContents().length());
		updateDocument(new AsciiFileDocument(asciiFile));
//		for(AsciiFileFragment asciiFileFragment: asciiFile.getContentFragments()){
//			updateDocument(new AsciiFileFragmentDocument(asciiFileFragment));
//		}
	}

	private StringBuffer getFileContents(AsciiFileReader asciiFileReader) {
		StringBuffer fileContents = new StringBuffer();
		String line = null;
		while((line = asciiFileReader.getNextLine()) != null){
			fileContents.append(line).append("\n");
		}
		return fileContents;
	}

	private String getJarFileName(JarFile jarFile) {
		String jarName = jarFile.getName();
		return FileUtils.resolveFileName(jarName);
	}
}
