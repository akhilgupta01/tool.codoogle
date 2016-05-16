package gupta.akhil.tools.indexer.code.model;

import gupta.akhil.tools.codeanalyzer.lucene.Indexable;
import gupta.akhil.tools.common.utils.FileUtils;


public class AsciiFile implements Indexable{
	private String filePath;
	private String fileName;
	private String moduleName;
	private String contents;

	public AsciiFile(String moduleName, String filePath, String contents){
		String canonicalFilePath = FileUtils.toUnixFilePath(filePath); 
		this.fileName = FileUtils.resolveFileName(canonicalFilePath);
		this.moduleName = moduleName;
		this.filePath = canonicalFilePath;
		this.contents = contents;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getContents() {
		return contents;
	}
	
	@Override
	public String getIndexKey() {
		return (moduleName + "::" + filePath).hashCode() + "";
	}
	
	@Override
	public String toString() {
		return "AsciiFile [filePath=" + filePath + ", fileName=" + fileName
				+ ", moduleName=" + moduleName + "]";
	}
	
	
}
