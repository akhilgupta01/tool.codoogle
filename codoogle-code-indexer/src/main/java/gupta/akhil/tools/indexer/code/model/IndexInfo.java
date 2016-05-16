package gupta.akhil.tools.indexer.code.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gupta.akhil.tools.codeanalyzer.lucene.Indexable;

public class IndexInfo implements Indexable{
	private String indexName;
	private String module;
	private Date lastIndexedOn;

	
	public IndexInfo(String indexName, String moduleName, Date lastIndexedOn) {
		super();
		this.indexName = indexName;
		this.module = moduleName;
		this.lastIndexedOn = lastIndexedOn;
	}

	public IndexInfo(String indexName, String moduleName, String lastIndexedOn){
		this.indexName = indexName;
		this.module = moduleName;
		try {
			this.lastIndexedOn = new SimpleDateFormat("yyyy-MM-dd").parse(lastIndexedOn);
		} catch (ParseException e) {
			throw new RuntimeException("unable to parse date - " + lastIndexedOn);
		}
	}
	
	public String getIndexName() {
		return indexName;
	}
	
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	
	public String getModuleName() {
		return module;
	}
	
	public void setModuleName(String moduleName) {
		this.module = moduleName;
	}
	
	public Date getLastIndexedOn() {
		return lastIndexedOn;
	}
	
	public void setLastIndexedOn(Date lastIndexedOn) {
		this.lastIndexedOn = lastIndexedOn;
	}
	
	@Override
	public String getIndexKey() {
		return String.valueOf((indexName + ":" + module).hashCode());
	}

	@Override
	public String toString() {
		return "IndexInfo [indexName=" + indexName + ", moduleName=" + module + ", lastIndexedOn=" + lastIndexedOn
				+ "]";
	}
}
