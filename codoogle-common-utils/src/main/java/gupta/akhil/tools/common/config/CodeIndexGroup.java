package gupta.akhil.tools.common.config;

import java.util.ArrayList;
import java.util.List;

public class CodeIndexGroup {
	private String indexGroup;
	private String checkoutLocation;
	private String ignoredFiles;
	private List<ModuleSet> moduleConfigurations = new ArrayList<ModuleSet>();
	
	public String getIndexGroup() {
		return indexGroup;
	}
	public void setIndexGroup(String indexGroup) {
		this.indexGroup = indexGroup;
	}
	public String getCheckoutLocation() {
		return checkoutLocation;
	}
	public void setCheckoutLocation(String checkoutLocation) {
		this.checkoutLocation = checkoutLocation;
	}
	public List<String> getIgnoredFiles() {
		List<String> indexedFileExtensionsList = new ArrayList<String>();
		for(String extn: ignoredFiles.split(",")){
			indexedFileExtensionsList.add(extn.trim());
		}
		return indexedFileExtensionsList;
	}
	
	public void setIgnoredFiles(String ignoredFiles) {
		this.ignoredFiles = ignoredFiles;
	}

	public List<ModuleSet> getModuleConfigurations() {
		return moduleConfigurations;
	}
	
	public void setModuleConfigurations(List<ModuleSet> moduleConfigurations) {
		this.moduleConfigurations.clear();
		this.moduleConfigurations.addAll(moduleConfigurations);
	}
	
	@Override
	public String toString() {
		return "CodeIndexGroup [indexGroup=" + indexGroup + ", checkoutLocation=" + checkoutLocation + ", ignoredFiles="
				+ ignoredFiles + ", moduleConfigurations=" + moduleConfigurations + "]";
	}
	
}
