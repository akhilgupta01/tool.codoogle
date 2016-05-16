package gupta.akhil.tools.codeanalyzer.lucene;

import org.apache.lucene.document.Document;

public interface SearchFilter {
	public boolean acceptDocument(Document document);
}
