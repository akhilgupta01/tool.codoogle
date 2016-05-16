package gupta.akhil.tools.common.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fullgc.codoogle.vcsConfig.CVSType;
import com.fullgc.codoogle.vcsConfig.CodeIndexConfigType;
import com.fullgc.codoogle.vcsConfig.CodoogleConfigDocument;
import com.fullgc.codoogle.vcsConfig.CodoogleConfigurationType;
import com.fullgc.codoogle.vcsConfig.DomainConfigType;
import com.fullgc.codoogle.vcsConfig.ModuleListType;
import com.fullgc.codoogle.vcsConfig.SourceControlSystemType;
import com.fullgc.codoogle.vcsConfig.ThreadDumpConfigType;
import com.fullgc.codoogle.vcsConfig.TraceCollectionGroupType;

public class SystemConfig {
	private String baseIndexLocation;
	private List<TraceCollectionGroup> traceCollectionGroups = new ArrayList<TraceCollectionGroup>();
	private List<CodeIndexGroup> codeIndexGroups = new ArrayList<CodeIndexGroup>();
	private List<SourceControlConfig> sourceControlConfigurations = new ArrayList<SourceControlConfig>();
	private ThreadDumpConfig threadDumpConfig;
	
	private static SystemConfig instance = null;
	
	public static String getBaseIndexLocation() {
		return getConfig().baseIndexLocation;
	}

	public static List<TraceCollectionGroup> getTraceCollectionGroups(){
		return new ArrayList<TraceCollectionGroup>(getConfig().traceCollectionGroups);
	}
	
	public static List<CodeIndexGroup> getCodeIndexGroups(){
		return new ArrayList<CodeIndexGroup>(getConfig().codeIndexGroups);
	}
	
	public static Set<String> getCodeIndexGroupNames(){
		Set<String> codeIndexGroupNames = new HashSet<String>();
		for(CodeIndexGroup codeIndexGroup: getConfig().codeIndexGroups){
			codeIndexGroupNames.add(codeIndexGroup.getIndexGroup());
		}
		return codeIndexGroupNames;
	}

	public static Set<String> getCallTraceIndexGroupNames(){
		Set<String> callTraceIndexGroupNames = new HashSet<String>();
		for(TraceCollectionGroup traceCollectionGroup: getConfig().traceCollectionGroups){
			callTraceIndexGroupNames.add(traceCollectionGroup.getIndexGroup());
		}
		return callTraceIndexGroupNames;
	}

	private void setBaseIndexLocation(String baseIndexLocation) {
		this.baseIndexLocation = baseIndexLocation;
	}
	
	private void setTraceCollectionGroups(List<TraceCollectionGroup> traceCollectionGroups) {
		this.traceCollectionGroups = traceCollectionGroups;
	}
	
	public void setCodeIndexGroups(List<CodeIndexGroup> codeIndexGroups) {
		this.codeIndexGroups = codeIndexGroups;
	}
	
	private void setThreadDumpConfig(ThreadDumpConfig threadDumpConfig) {
		this.threadDumpConfig = threadDumpConfig;
	}
	
	public static ThreadDumpConfig getThreadDumpConfig() {
		return getConfig().threadDumpConfig;
	}

	private static SystemConfig getConfig(){
		if(instance == null){
			loadConfiguration();
		}
		return instance;
	}
	
	private synchronized static void loadConfiguration() {
		if(instance == null){
			try{
				InputStream is = SystemConfig.class.getClassLoader().getResourceAsStream("codoogle-config.xml");
				CodoogleConfigDocument codoogleConfigDoc = CodoogleConfigDocument.Factory.parse(is);
				instance = buildSystemConfig(codoogleConfigDoc.getCodoogleConfig());
			}catch(Exception e){
				throw new RuntimeException("Error loading codoogle-config.xml", e);
			}
		}
	}
	
	private static SystemConfig buildSystemConfig(CodoogleConfigurationType codoogleConfig) {
		SystemConfig SystemConfig = new SystemConfig();
		SystemConfig.setBaseIndexLocation(codoogleConfig.getBaseIndexLocation());
		SystemConfig.setTraceCollectionGroups(buildTraceCollectionGroups(codoogleConfig.getTraceCollectionGroupsArray()));
		List<SourceControlConfig> sourceControlConfigurations = buildSourceControlConfigs(codoogleConfig.getSourceControlInfoArray());
		SystemConfig.setSourceControlConfigurations(sourceControlConfigurations);
		SystemConfig.setCodeIndexGroups(buildCodeIndexGroups(codoogleConfig.getCodeIndexGroupsArray(), sourceControlConfigurations));
		SystemConfig.setThreadDumpConfig(buildThreadDumpConfig(codoogleConfig.getThreadDumpConfig()));
		return SystemConfig;
	}
	
	public static List<SourceControlConfig> getSourceControlConfigurations() {
		return getConfig().sourceControlConfigurations;
	}

	public void setSourceControlConfigurations(List<SourceControlConfig> sourceControlConfigurations) {
		this.sourceControlConfigurations = sourceControlConfigurations;
	}

	private static List<SourceControlConfig> buildSourceControlConfigs(SourceControlSystemType[] sourceControlInfoArray) {
		List<SourceControlConfig> sourceControlConfigs = new ArrayList<SourceControlConfig>();
		for(SourceControlSystemType sourceControlSystem: sourceControlInfoArray){
			if(sourceControlSystem instanceof CVSType){
				CVSConfig cvsConfig = new CVSConfig();
				cvsConfig.setId(sourceControlSystem.getId());
				cvsConfig.setUser(sourceControlSystem.getUser());
				cvsConfig.setPassword(sourceControlSystem.getPassword());
				cvsConfig.setHostName(((CVSType)sourceControlSystem).getHostName());
				cvsConfig.setPath(((CVSType)sourceControlSystem).getPath());
				sourceControlConfigs.add(cvsConfig);
			}
		}
		return sourceControlConfigs;
	}

	private static ThreadDumpConfig buildThreadDumpConfig(ThreadDumpConfigType threadDumpConfigType) {
		ThreadDumpConfig threadDumpConfig = new ThreadDumpConfig();
		for(String includePattern: threadDumpConfigType.getIncludedPatterns().getIncludeArray()){
			threadDumpConfig.addAllowedPattern(includePattern);
		}
		for(String excludePattern: threadDumpConfigType.getExcludedPatterns().getExcludeArray()){
			threadDumpConfig.addExcludedPattern(excludePattern);
		}
		for(String excludedEntryPointPattern: threadDumpConfigType.getExcludedEntryPointPatterns().getExcludedEntryPointPatternArray()){
			threadDumpConfig.addExcludedEntryPointPattern(excludedEntryPointPattern);
		}
		return threadDumpConfig;
	}

	private static List<CodeIndexGroup> buildCodeIndexGroups(CodeIndexConfigType[] codeIndexGroupsArray, List<SourceControlConfig> sourceControlConfigurations) {
		List<CodeIndexGroup> codeIndexGroups = new ArrayList<CodeIndexGroup>();
		for(CodeIndexConfigType codeIndexGroupConfigType: codeIndexGroupsArray){
			CodeIndexGroup codeIndexGroup = new CodeIndexGroup();
			codeIndexGroup.setIndexGroup(codeIndexGroupConfigType.getIndexGroup());
			codeIndexGroup.setCheckoutLocation(codeIndexGroupConfigType.getCheckoutLocation());
			codeIndexGroup.setIgnoredFiles(codeIndexGroupConfigType.getIgnoredFiles());
			codeIndexGroup.setModuleConfigurations(buildModuleList(codeIndexGroupConfigType.getModulesListArray(), sourceControlConfigurations));
			codeIndexGroups.add(codeIndexGroup);
		}
		return codeIndexGroups;
	}

	private static List<ModuleSet> buildModuleList(ModuleListType[] moduleListArray, List<SourceControlConfig> sourceControlConfigurations) {
		List<ModuleSet> moduleConfigurations = new ArrayList<ModuleSet>();
		for(ModuleListType moduleList: moduleListArray){
			ModuleSet moduleSet = new ModuleSet();
			moduleSet.setFile(moduleList.getFile());
			moduleSet.setScmId(moduleList.getSourceControlId());
			moduleSet.setScmBranch(moduleList.getBranch());
			moduleConfigurations.add(moduleSet);
			for(SourceControlConfig sourceControlConfig: sourceControlConfigurations){
				if(sourceControlConfig.getId().equals(moduleSet.getScmId())){
					moduleSet.setSourceControlConfig(sourceControlConfig);
				}
			}
			if(moduleSet.getSourceControlConfig() == null){
				throw new RuntimeException("No matching source control configuration found for SCM id -> " + moduleSet.getScmId());
			}
			moduleSet.loadModulesList();
		}
		return moduleConfigurations;
	}

	private static List<TraceCollectionGroup> buildTraceCollectionGroups(TraceCollectionGroupType[] traceCollectionGroupArray) {
		List<TraceCollectionGroup> traceCollectionGroups = new ArrayList<TraceCollectionGroup>();
		for(TraceCollectionGroupType traceCollectionGroupType: traceCollectionGroupArray){
			TraceCollectionGroup traceCollectionGroup = new TraceCollectionGroup();
			traceCollectionGroup.setId(traceCollectionGroupType.getId());
			traceCollectionGroup.setIndexGroup(traceCollectionGroupType.getIndexGroup());
			traceCollectionGroup.setInterval(traceCollectionGroupType.getThreadDumpInterval());
			traceCollectionGroup.setDuration(traceCollectionGroupType.getTotalDuration());
			traceCollectionGroup.setDomainConfigurations(buildDomainConfigurations(traceCollectionGroupType.getDomainConfigArray()));
			traceCollectionGroups.add(traceCollectionGroup);
		}
		return traceCollectionGroups;
	}

	private static List<DomainConfig> buildDomainConfigurations(DomainConfigType[] domainConfigArray) {
		List<DomainConfig> domainConfigurations = new ArrayList<DomainConfig>();
		for(DomainConfigType domainConfigType: domainConfigArray){
			DomainConfig domainConfig = new DomainConfig();
			domainConfig.setDomainName(domainConfigType.getDomainName());
			domainConfig.setAdminHost(domainConfigType.getAdminHostName());
			domainConfig.setAdminServerPort(domainConfigType.getAdminPort());
			domainConfig.setUserName(domainConfigType.getUserName());
			domainConfig.setPassword(domainConfigType.getPassword());
			domainConfig.setServerNamePattern(domainConfigType.getServerNamePattern());
			domainConfigurations.add(domainConfig);
		}
		return domainConfigurations;
	}
}
