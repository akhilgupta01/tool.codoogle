package gupta.akhil.tools.codeanalyzer.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public abstract class AbstractDocument {
	protected static final String KEY = "key";

	private Document document;

	private Indexable indexable;
	
	protected AbstractDocument(Document document){
		this.document = document;
		this.indexable = toIndexable(document);
	}

	protected AbstractDocument(Indexable indexable){
		this.indexable = indexable;
		this.document = new Document();
		buildDocument(indexable);
	}

	protected void addStringField(String tag, String value){
		if(value != null){
			document.add(new StringField(tag, value, Field.Store.YES));
		}
	}

	protected void addIntegerField(String tag, Integer value){
		if(value != null){
			document.add(new IntField(tag, value, Field.Store.YES));
		}
	}

	protected void addTextField(String tag, String value){
		if(value != null){
			document.add(new TextField(tag, value, Store.YES));
		}
	}

	public Document getDocument(){
		return document;
	}
	
	protected Indexable getIndexable(){
		return indexable;
	}

	protected void setKeyField(){
		if(document.get(KEY) == null){
			document.add(new StringField(KEY, getKey(), Field.Store.NO));
		}
	}
	
	public abstract String getKey();

	protected abstract Indexable toIndexable(Document document);
	
	protected abstract void buildDocument(Indexable indexable);

}
