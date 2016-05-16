package gupta.akhil.tools.codeanalyzer.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import gupta.akhil.tools.common.config.SystemConfig;

public abstract class AbstractIndexer {
	private String indexPath;
	protected IndexWriter indexWriter;
	protected boolean initialized = false;
	private int counter = 0;
	private String revision;

	public AbstractIndexer(String indexType, String indexGroup) {
		this.revision = indexGroup;
		this.indexPath = SystemConfig.getBaseIndexLocation() + '/' + indexGroup + '/' + indexType;
	}

	private void checkIfInitialized() throws IOException{
		if(!initialized){
			initialize();
		}
	}
	
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	
	public String getIndexPath(){
		return indexPath;
	}

	public String getRevision() {
		return revision;
	}

	protected synchronized void initialize() throws IOException {
		if(!initialized){
			Directory dir = FSDirectory.open(new File(getIndexPath()));
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);//, new CharArraySet(Version.LUCENE_47, StopWords.JAVA_STOP_WORDS, false));
			IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
			writerConfig.setOpenMode(getOpenMode());
			indexWriter = new IndexWriter(dir, writerConfig);
			initialized = true;
		}
	}
	
	protected final void updateDocument(AbstractDocument document) throws IOException {
		checkIfInitialized();
		document.setKeyField();
		Term term = new Term(AbstractDocument.KEY, document.getKey());
		indexWriter.updateDocument(term, document.getDocument(), new StandardAnalyzer(Version.LUCENE_47));
		registerForBlockCommit();
	}

	private void registerForBlockCommit() throws IOException {
		counter++;
		if(counter > 500){
			indexWriter.commit();
			counter = 0;
		}
	}

	public void commit() throws IOException{
		if(counter>0){
			indexWriter.commit();
			counter=0;
		}
	}
	
	public void close() throws IOException{
		indexWriter.close();
		this.initialized = false;
	}
	
	protected OpenMode getOpenMode() {
		return OpenMode.CREATE_OR_APPEND;
	}
	
}