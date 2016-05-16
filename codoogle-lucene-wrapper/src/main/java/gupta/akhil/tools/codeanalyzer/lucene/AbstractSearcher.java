package gupta.akhil.tools.codeanalyzer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import gupta.akhil.tools.common.config.SystemConfig;

public abstract class AbstractSearcher {
	private static final int MAX_COUNT_LIMIT = 1000;
	private IndexReader reader;
	private IndexSearcher searcher;
	private boolean initialized = false;
	private String indexPath;
	private int searchCountLimit = MAX_COUNT_LIMIT;

	protected AbstractSearcher(String indexFolder, String indexGroup) {
		this.indexPath = SystemConfig.getBaseIndexLocation() + '/' + indexGroup + '/' + indexFolder;
	}
	
	
	private void checkIfInitialized() throws IOException{
		if(!initialized){
			initialize();
		}
	}
	private synchronized void initialize() throws IOException{
		if(!initialized){
			if(reader!=null){
				reader.close();
			}
			reader = DirectoryReader.open(FSDirectory.open(new File(getIndexPath())));
			searcher = new IndexSearcher(reader);
			initialized = true;
		}
	}

	protected List<Document> search(String queryExpression) throws IOException, ParseException{
		return search(queryExpression, null);
	}

	protected List<Document> search(String queryExpression, String[] fields) throws IOException, ParseException{
		QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_47, fields, new StandardAnalyzer(Version.LUCENE_47));
		queryParser.setAllowLeadingWildcard(true);
		queryParser.setLowercaseExpandedTerms(true);
		Query query = queryParser.parse(queryExpression);
		return search(query, MAX_COUNT_LIMIT);
	}
	
	
	protected Document getDocumentByKey(String key) throws IOException{
		Query query = new TermQuery(new Term(AbstractDocument.KEY, key));
		List<Document> searchResults = search(query, MAX_COUNT_LIMIT);
		assert(searchResults.size() < 2);
		if(!searchResults.isEmpty() && searchResults.size() == 1){
			return searchResults.get(0);
		}
		return null;
	}

	private List<Document> search(Query query, int maxCount, SearchFilter searchFilter) throws IOException{
		checkIfInitialized();
		List<Document> searchResults = new ArrayList<Document>();
		TopDocs topDocs = null;
		boolean hasMoreRecords = true;
		ScoreDoc lastVisitedScoreDoc = null;
		while(searchResults.size() < maxCount && hasMoreRecords){
			if(topDocs == null){
				topDocs = searcher.search(query, maxCount);
			}else{
				topDocs = searcher.searchAfter(lastVisitedScoreDoc, query, maxCount);
			}
			if(topDocs.totalHits < maxCount){
				hasMoreRecords = false;
			}
			for(ScoreDoc scoreDoc: topDocs.scoreDocs){
				lastVisitedScoreDoc = scoreDoc;
				Document document = searcher.doc(scoreDoc.doc);
				if(searchFilter==null || searchFilter.acceptDocument(document)){
					searchResults.add(document);
				}
			}
		}
		return searchResults;
	}

	private List<Document> search(Query query, int maxCount) throws IOException{
		return search(query, maxCount, null);
	}
	
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	
	private String getIndexPath(){
		return indexPath;
	}
	
	public void reOpen(){
		this.initialized = false;
	}
	
	public void close() throws IOException{
		reader.close();
	}
	
	public int getSearchCountLimit() {
		return searchCountLimit;
	}
	
	public void setSearchCountLimit(int searchCountLimit) {
		this.searchCountLimit = searchCountLimit;
	}
	
	protected abstract String[] getFieldNames();

}
