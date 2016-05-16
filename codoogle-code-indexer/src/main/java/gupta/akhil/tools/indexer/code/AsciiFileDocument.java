package gupta.akhil.tools.indexer.code;

import org.apache.lucene.document.Document;

import gupta.akhil.tools.codeanalyzer.lucene.AbstractDocument;
import gupta.akhil.tools.codeanalyzer.lucene.Indexable;
import gupta.akhil.tools.indexer.code.model.AsciiFile;

public class AsciiFileDocument extends AbstractDocument{
	
	public static final String PACKAGE_NAME = "packageName";
	public static final String CONTENTS = "contents";
	public static final String MODULE_NAME = "moduleName";
	public static final String FILE_NAME = "fileName";
	public static final String FILE_PATH = "filePath";

	public AsciiFileDocument(Document document){
		super(document);
	}

	public AsciiFileDocument(AsciiFile asciiFile){
		super(asciiFile);
	}
	
	@Override
	protected void buildDocument(Indexable indexable) {
		AsciiFile asciiFile = (AsciiFile)indexable;
		addTextField(FILE_PATH, asciiFile.getFilePath());
		addTextField(FILE_NAME, asciiFile.getFileName());
		addTextField(MODULE_NAME, asciiFile.getModuleName());
		addTextField(CONTENTS, asciiFile.getContents());
	}
	
	@Override
	protected Indexable toIndexable(Document document) {
		return new AsciiFile(document.get(MODULE_NAME), document.get(FILE_PATH), document.get(CONTENTS));
	}
	
	public AsciiFile getAsciiFile(){
		return (AsciiFile)getIndexable();
	}

	@Override
	public String getKey() {
		return getAsciiFile().getIndexKey();
	}
	
	public static String[] getFields(){
		return new String[] {FILE_NAME, MODULE_NAME, FILE_PATH, PACKAGE_NAME, CONTENTS};
	}

}
