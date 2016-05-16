package gupta.akhil.tools.common.config;

import java.util.ArrayList;
import java.util.List;

public class ThreadDumpConfig {
	private List<String> allowedPatterns = new ArrayList<String>();
	private List<String> excludedPatterns = new ArrayList<String>();
	private List<String> excludedEntryPointPatterns = new ArrayList<String>();

	public void addAllowedPattern(String allowedPattern){
		allowedPatterns.add(allowedPattern);
	}
	
	public void addExcludedPattern(String excludedPattern){
		excludedPatterns.add(excludedPattern);
	}
	
	public void addExcludedEntryPointPattern(String excludedEntryPointPattern){
		excludedEntryPointPatterns.add(excludedEntryPointPattern); 
	}
	
	public List<String> getAllowedPatterns() {
		return new ArrayList<String>(allowedPatterns);
	}
	
	public List<String> getExcludedPatterns() {
		return new ArrayList<String>(excludedPatterns);
	}
	
	public List<String> getExcludedEntryPointPatterns() {
		return new ArrayList<String>(excludedEntryPointPatterns);
	}
}
