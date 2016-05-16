package gupta.akhil.tools.indexer.code.model;

import gupta.akhil.tools.codeanalyzer.lucene.Indexable;


public class AsciiFileFragment implements Indexable{
	private String parentAsciiFileKey;
	private String contents;
	private int fragmentNumber;
	
	public AsciiFileFragment(String parentAsciiFileKey, int fragmentNumber, String contents){
		this.parentAsciiFileKey = parentAsciiFileKey;
		this.fragmentNumber = fragmentNumber;
		this.contents = contents;
	}

	public String getParentAsciiFileKey() {
		return parentAsciiFileKey;
	}

	public String getContents() {
		return contents;
	}

	public int getFragmentNumber() {
		return fragmentNumber;
	}
	
	@Override
	public String getIndexKey() {
		return parentAsciiFileKey + "_" + fragmentNumber;
	}
}
