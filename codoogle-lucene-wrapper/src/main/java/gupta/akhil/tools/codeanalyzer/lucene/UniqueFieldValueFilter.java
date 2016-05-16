package gupta.akhil.tools.codeanalyzer.lucene;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

public class UniqueFieldValueFilter implements SearchFilter {
	
	private String fieldName;
	
	private List<Object> recordedValues = new ArrayList<Object>();
	
	public UniqueFieldValueFilter(String fieldName){
		this.fieldName = fieldName;
	}
	
	@Override
	public boolean acceptDocument(Document document) {
		String fieldValue = document.get(fieldName);
		if(recordedValues.contains(fieldValue)){
			return false;
		}
		recordedValues.add(fieldValue);
		return true;
	}
}
