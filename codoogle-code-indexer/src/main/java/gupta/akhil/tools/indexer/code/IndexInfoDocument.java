package gupta.akhil.tools.indexer.code;

import java.text.SimpleDateFormat;

import org.apache.lucene.document.Document;

import gupta.akhil.tools.codeanalyzer.lucene.AbstractDocument;
import gupta.akhil.tools.codeanalyzer.lucene.Indexable;
import gupta.akhil.tools.indexer.code.model.IndexInfo;

public class IndexInfoDocument extends AbstractDocument{
	
	public static final String LAST_INDEX_DATE = "lastIndexDate";
	public static final String INDEX_NAME = "indexName";
	public static final String MODULE_NAME = "module";

	public IndexInfoDocument(Document document){
		super(document);
	}

	public IndexInfoDocument(IndexInfo indexInfo){
		super(indexInfo);
	}
	
	@Override
	protected void buildDocument(Indexable indexable) {
		IndexInfo indexInfo = (IndexInfo)indexable;
		addTextField(INDEX_NAME, indexInfo.getIndexName());
		addTextField(MODULE_NAME, indexInfo.getModuleName());
		addTextField(LAST_INDEX_DATE, new SimpleDateFormat("yyyy-MM-dd").format(indexInfo.getLastIndexedOn()));
	}
	
	@Override
	protected Indexable toIndexable(Document document) {
		return new IndexInfo(document.get(INDEX_NAME), document.get(MODULE_NAME), document.get(LAST_INDEX_DATE));
	}
	
	public IndexInfo getIndexInfo(){
		return (IndexInfo)getIndexable();
	}

	@Override
	public String getKey() {
		return getIndexInfo().getIndexKey();
	}
	
	@Override
	public String toString() {
		return getDocument().toString();
	}
}
