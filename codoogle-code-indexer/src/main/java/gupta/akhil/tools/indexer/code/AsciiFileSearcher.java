package gupta.akhil.tools.indexer.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import gupta.akhil.tools.codeanalyzer.lucene.AbstractSearcher;
import gupta.akhil.tools.indexer.code.model.AsciiFile;
import gupta.akhil.tools.indexer.code.model.IndexInfo;

public class AsciiFileSearcher extends AbstractSearcher{
	private static Map<String, AsciiFileSearcher> INSTANCE_CACHE = new HashMap<String, AsciiFileSearcher>();

	private AsciiFileSearcher(String revision){
		super("AsciiFileIndex", revision);
	}
	
	public static AsciiFileSearcher getInstance(String revision){
		if(INSTANCE_CACHE.get(revision) == null){
			createInstance(revision);
		}
		return INSTANCE_CACHE.get(revision);
	}
	
	private synchronized static void createInstance(String revision){
		if(INSTANCE_CACHE.get(revision) == null){
			INSTANCE_CACHE.put(revision, new AsciiFileSearcher(revision));
		}
	}

	public IndexInfo getIndexInfo(String indexGroup, String module) throws IOException, ParseException{
		IndexInfo indexInfo = null;
		List<Document> documents = search("indexName:" + indexGroup + " && module:" + module);
		System.out.println(documents);
		if(!documents.isEmpty()){
			indexInfo = new IndexInfoDocument(documents.get(0)).getIndexInfo();
		}
		return indexInfo;
	}
	
	public List<AsciiFile> searchFilesByName(String fileName) throws IOException, ParseException{
		return convert(search(AsciiFileDocument.FILE_NAME + ":" + fileName));
	}

	public List<AsciiFile> searchFilesByContent(String content) throws IOException, ParseException{
		return convert(search(AsciiFileDocument.CONTENTS + ":" + content));
	}

	public List<AsciiFile> searchFiles(String queryExpression) throws IOException, ParseException{
		return convert(search(queryExpression, AsciiFileDocument.getFields()));
	}

	public AsciiFile getFile(String id) throws IOException{
		Document document = getDocumentByKey(id);
		AsciiFile asciiFile = null;
		if(document != null){
			asciiFile = new AsciiFileDocument(document).getAsciiFile();
		}
		return asciiFile;
	}

	private List<AsciiFile> convert(List<Document> searchedDocuments) throws IOException {
		List<AsciiFile> searchResults = new ArrayList<AsciiFile>();
		for(Document document: searchedDocuments){
			AsciiFile asciiFile = new AsciiFileDocument(document).getAsciiFile();
			searchResults.add(asciiFile);
		}
		return searchResults;
	}

	@Override
	protected String[] getFieldNames() {
		return new String[]{AsciiFileDocument.FILE_NAME, AsciiFileDocument.FILE_PATH, AsciiFileDocument.MODULE_NAME};
	}

}
