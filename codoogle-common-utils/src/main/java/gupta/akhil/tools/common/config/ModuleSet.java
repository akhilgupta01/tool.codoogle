package gupta.akhil.tools.common.config;

import java.util.ArrayList;
import java.util.List;

import gupta.akhil.tools.common.utils.AsciiFileReader;

public class ModuleSet {
	private String file;
	private String scmId;
	private SourceControlConfig sourceControlConfig;
	private String scmBranch;
	private List<String> moduleList = new ArrayList<String>();
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getScmId() {
		return scmId;
	}
	public void setScmId(String scmId) {
		this.scmId = scmId;
	}
	public String getScmBranch() {
		return scmBranch;
	}
	public void setScmBranch(String scmBranch) {
		this.scmBranch = scmBranch;
	}
	
	public SourceControlConfig getSourceControlConfig() {
		return sourceControlConfig;
	}
	public void setSourceControlConfig(SourceControlConfig sourceControlConfig) {
		this.sourceControlConfig = sourceControlConfig;
	}
	public void loadModulesList(){
		AsciiFileReader fileReader = new AsciiFileReader(this.getFile());
		String line = null;
		while((line = fileReader.getNextLine()) != null){
			moduleList.add(line.trim());
		}
	}
	
	public List<String> getModuleList() {
		return moduleList;
	}
}
